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

import com.inplanesight.BuildConfig;
import com.inplanesight.R;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.LocationViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Hunt;


import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HuntFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HuntFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GooglePlacesAPI googlePlaceAPI;

    GameViewModel gameViewModel;
    LocationViewModel locationService;
    Airport selectedAirport;
    ArrayList<String> imageRefs;
    FirebaseAPI firebaseAPI;

    ViewPager2 viewPager2;
    ArrayList<ViewPagerItem> viewPagerItems;

    public HuntFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HuntFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HuntFragment newInstance(String param1, String param2) {
        HuntFragment fragment = new HuntFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hunt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        googlePlaceAPI = new GooglePlacesAPI();
        imageRefs = new ArrayList<>();
        firebaseAPI = new FirebaseAPI();
        viewPager2 = getView().findViewById(R.id.vPager);

        // Initialize the Places SDK
        googlePlaceAPI.initialize(this.getContext(), BuildConfig.MAPS_API_KEY);

        gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        try {
            gameViewModel.startHunt(selectedAirport.getCode(), selectedAirport.getCoordinates());
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewPagerItems = new ArrayList<>();
        gameViewModel.getGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                if (viewPagerItems.isEmpty()) {
                    for (Hunt hunt: game.getScavengerHunt()) {
                        imageRefs.add(hunt.getImageRef());
                    }

                    for(int i=0; i<imageRefs.size(); i++) {
                        int finalI = i;
                        FirebaseAPI.downloadPhotoFromStorage(imageRefs.get(i), bytes -> {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ViewPagerItem viewPagerItem = new ViewPagerItem(bitmap);
                            viewPagerItems.add(viewPagerItem);
                            if (finalI == imageRefs.size() - 1) {
                                MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewPagerItems);
                                viewPager2.setAdapter(myPagerAdapter);
                            }
                        });
                    }
                }
            }
        });

//        Button foundBtn = getActivity().findViewById(R.id.btnFoundLocation);
//        foundBtn.setOnClickListener((e) -> {
//            locationService.storeLocation();
//            locationService.getCoordinates().observe(getViewLifecycleOwner(), loc -> {
//                boolean res = gameViewModel.foundLocation(loc, viewPager2.getCurrentItem());
//                String message = res ? "Found location +1000pts!" : "Wrong place :( -100pts";
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//            });
//        });

        Button endBtn = getActivity().findViewById(R.id.btnEndHunt);
        endBtn.setOnClickListener((e) -> {
            int score = gameViewModel.endHunt();
            Toast.makeText(getContext(), score + "pts!", Toast.LENGTH_SHORT).show();
        });
    }
}