package com.inplanesight.ui.find;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inplanesight.BuildConfig;
import com.inplanesight.R;
import com.inplanesight.api.GooglePlacesAPI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GooglePlacesAPI googlePlaceAPI;

    public FindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setImage(Bitmap bitmap) {
        ImageView places = (ImageView) getActivity().findViewById(R.id.hunt_photo);
        places.setImageBitmap(bitmap);
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

    }
}