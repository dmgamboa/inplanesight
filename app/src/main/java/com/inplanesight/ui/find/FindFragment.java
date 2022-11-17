package com.inplanesight.ui.find;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.BuildConfig;
import com.inplanesight.R;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.models.Airport;

public class FindFragment extends Fragment {
    private String mParam1;
    private String mParam2;

    Airport selectedAirport;

    private GooglePlacesAPI googlePlaceAPI;

    public FindFragment() {}

    public void setImage(Bitmap bitmap) {
        ImageView places = (ImageView) getActivity().findViewById(R.id.hunt_photo);
        places.setImageBitmap(bitmap);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedAirport = FindFragmentArgs.fromBundle(getArguments()).getSelectedAirport();
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        googlePlaceAPI = new GooglePlacesAPI();

        // Place ID for YVR Airport
        //TODO : get this as an arg from a bundle passed from search fragment
        final String placeId = "ChIJm6MnhjQLhlQRhIA0hqzMaLo";

        ImageView places = (ImageView) view.findViewById(R.id.hunt_photo);

        // Initialize the Places SDK
        googlePlaceAPI.initialize(places.getContext(), BuildConfig.MAPS_API_KEY);

         //get photo bitmap:
        try {
            googlePlaceAPI.getPhotoBitmapFromPlace(places.getContext(), placeId, this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Button startBtn = getActivity().findViewById(R.id.start_hunt);
        startBtn.setOnClickListener((e) -> {
            FindFragmentDirections.ActionFindFragmentToHuntFragment action
                    = FindFragmentDirections.actionFindFragmentToHuntFragment(selectedAirport);
            Navigation.findNavController(view).navigate(action);
        });
    }
}