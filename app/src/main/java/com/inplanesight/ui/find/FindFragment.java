package com.inplanesight.ui.find;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.inplanesight.R;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.LocationViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;
import com.inplanesight.models.Game;
import com.inplanesight.models.Hunt;
import com.inplanesight.ui.common.FlightInfoRecyclerViewAdapter;

import java.util.ArrayList;

public class FindFragment extends Fragment {
    ArrayList<ViewPagerItem> viewPagerItems;
    boolean popupOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = requireView().findViewById(R.id.findImgCarousel);
//        FindRecyclerViewAdapter emptyAdapter = new FindRecyclerViewAdapter(getContext(), new Hunt[1]);
//        viewPager.setAdapter(emptyAdapter);
        viewPagerItems = new ArrayList<>();
        Button[] carouselBtns = {requireActivity().findViewById(R.id.findImgBtn1),
                requireActivity().findViewById(R.id.findImgBtn2),
                requireActivity().findViewById(R.id.findImgBtn3)};
        Button foundBtn = requireView().findViewById(R.id.foundHuntBtn);

        LocationViewModel locationService = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        StateViewModel state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
        if (state.getUser() != null) {
            gameViewModel.getGame().getValue().setUser(state.getUser());
        };

        gameViewModel.getGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                if (viewPagerItems.isEmpty()) {
                    ArrayList<String> imageRefs = new ArrayList<>();
                    for (Hunt hunt : game.getScavengerHunt()) {
                        imageRefs.add(hunt.getImageRef());
                    }

                    for (int i = 0; i < imageRefs.size(); i++) {
                        int finalI = i;
                        FirebaseAPI.downloadPhotoFromStorage(imageRefs.get(i), bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ViewPagerItem viewPagerItem = new ViewPagerItem(bitmap);
                            viewPagerItems.add(viewPagerItem);
                            if (viewPagerItems.size() == imageRefs.size()) {
                                MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewPagerItems);
                                viewPager.setAdapter(myPagerAdapter);
                            }
                        });
                    }
                }

                boolean endHunt = true;
                ArrayList<Hunt> hunts = game.getScavengerHunt();
                for (int i = 0; i < hunts.size(); i++) {
                    if (hunts.get(i).getTimestampFound() != null) {
                        carouselBtns[i].setBackgroundColor(getResources().getColor(R.color.marine_blue));
                    } else {
                        endHunt = false;
                        carouselBtns[i].setBackgroundColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_grey));
                    }
                }
                if (endHunt && hunts.size() > 0 && !popupOpen) {
                    onEndHunt(game.getScore());
                }
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                gameViewModel.getGame().observe(getViewLifecycleOwner(), game -> {
                    if (game != null) {
                        Hunt currHunt = game.getHuntAt(position);
                        if (currHunt.getTimestampFound() != null) {
                            foundBtn.setText(getResources().getText(R.string.hunt_btn_disabled));
                            foundBtn.setBackgroundColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_grey));
                            foundBtn.setEnabled(false);
                        } else {
                            foundBtn.setText(getResources().getText(R.string.hunt_btn_enabled));
                            foundBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                            foundBtn.setEnabled(true);
                        }
                    }
                });
            }
        });

        foundBtn.setOnClickListener(v -> {
            locationService.storeLocation();
            locationService.getCoordinates().observe(getViewLifecycleOwner(), loc -> {
                int res = gameViewModel.foundLocation(loc, viewPager.getCurrentItem());
                String message;
                switch (res) {
                    case 0:
                        message = "Wrong place :( -1pts";
                        break;
                    case 1:
                        message = "Found location +10pts!";
                        break;
                    case 2:
                        message = "Location found!";
                        break;
                    default:
                        message = "Hunt complete!";
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void onEndHunt(double score) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.view_end_hunt_popup, null);
        popupOpen = true;

        TextView scoreView = popup.findViewById(R.id.endHuntScore);
        String scoreString = ((int) score) + " points";
        scoreView.setText(scoreString);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popup, width, height, true);
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

        popup.setOnTouchListener((v, e) -> {
            v.performClick();
            popupWindow.dismiss();
            popupOpen = false;
            return true;
        });

        Button redirectBtn = popup.findViewById(R.id.endHuntBtn);
        redirectBtn.setOnClickListener((e) -> {
            popupWindow.dismiss();
            popupOpen = false;
            Navigation.findNavController(requireView()).navigate(R.id.action_end_hunt);
        });
    }
}