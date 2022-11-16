package com.inplanesight.ui.find;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.inplanesight.BuildConfig;
import com.inplanesight.R;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.LocationViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Hunt;
import com.inplanesight.ui.check_in.SearchFragmentArgs;

import org.checkerframework.checker.units.qual.A;

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
    ArrayList<String> imageUrls = new ArrayList<>();

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
        selectedAirport = HuntFragmentArgs.fromBundle(getArguments()).getSelectedAirport();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hunt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameViewModel = new GameViewModel(selectedAirport.getCode());
        locationService = new LocationViewModel(getActivity());
        googlePlaceAPI = new GooglePlacesAPI();

        // Place ID for YVR Airport
        //TODO : get this as an arg from a bundle passed from search fragment
        final String placeId = "ChIJm6MnhjQLhlQRhIA0hqzMaLo";

        ImageView places = (ImageView) view.findViewById(R.id.hunt_photo);

        // Initialize the Places SDK
        googlePlaceAPI.initialize(places.getContext(), BuildConfig.MAPS_API_KEY);

        //get photo bitmap:
//        try {
//            googlePlaceAPI.getPhotoBitmapFromPlace(places.getContext(), placeId, this);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        gameViewModel.checkDatabase();

        viewPager2 = getView().findViewById(R.id.vPager);
        for (Hunt hunt: gameViewModel.getGame().getScavengerHunt()) {
            imageUrls.add(hunt.getImageUrl());
        }
        viewPagerItems = new ArrayList<>();

        for(int i=0; i<imageUrls.size(); i++) {
            ViewPagerItem viewPagerItem = new ViewPagerItem(imageUrls.get(i));
            viewPagerItems.add(viewPagerItem);
        }

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(viewPagerItems);
        viewPager2.setAdapter(myPagerAdapter);

        Button foundBtn = getActivity().findViewById(R.id.btnFoundLocation);
        foundBtn.setOnClickListener((e) -> {
            locationService.storeLocation();
            Coordinates currLocation = locationService.getCoordinates();
            gameViewModel.foundLocation(currLocation, viewPager2.getCurrentItem());
        });

        Button endBtn = getActivity().findViewById(R.id.btnEndHunt);
        endBtn.setOnClickListener((e) -> {
            gameViewModel.endHunt();
        });
    }
}