package com.inplanesight.ui.find;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.inplanesight.R;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.LocationViewModel;
import com.inplanesight.models.Hunt;

import java.util.ArrayList;

public class FindFragment extends Fragment {
    ArrayList<ViewPagerItem> viewPagerItems;

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
        viewPagerItems = new ArrayList<>();
        Button[] carouselBtns = {requireActivity().findViewById(R.id.findImgBtn1),
                requireActivity().findViewById(R.id.findImgBtn2),
                requireActivity().findViewById(R.id.findImgBtn3)};
        Button foundBtn = requireView().findViewById(R.id.foundHuntBtn);

        LocationViewModel locationService = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
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

                ArrayList<Hunt> hunts = game.getScavengerHunt();
                for (int i = 0; i < hunts.size(); i++) {
                    if (hunts.get(i).getTimestampFound() != null) {
                        carouselBtns[i].setBackgroundColor(getResources().getColor(R.color.marine_blue));
                        foundBtn.setText(getResources().getText(R.string.hunt_btn_enabled));
                        foundBtn.setEnabled(false);
                    } else {
                        carouselBtns[i].setBackgroundColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_grey));
                        foundBtn.setText(getResources().getText(R.string.hunt_btn_disabled));
                        foundBtn.setEnabled(false);
                    }
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
                        message = "Wrong place :( -100pts";
                        break;
                    case 1:
                        message = "Found location +1000pts!";
                        break;
                    case 2:
                        message = "Location found!";
                        break;
                    default:
                        message = "You scored " + res + "pts total!";
                }
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            });
        });
    }
}