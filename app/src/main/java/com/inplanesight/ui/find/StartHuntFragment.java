package com.inplanesight.ui.find;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.BuildConfig;
import com.inplanesight.R;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Airport;

import java.io.IOException;

public class StartHuntFragment extends Fragment {

    public StartHuntFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_hunt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GooglePlacesAPI googlePlacesAPI = new GooglePlacesAPI();
        googlePlacesAPI.initialize(this.getContext(), BuildConfig.MAPS_API_KEY);

        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_startHuntFragment_to_findFragment);
            }
        });

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.VISIBLE);

        Button startHuntBtn = requireActivity().findViewById(R.id.startHuntBtn);
        startHuntBtn.setOnClickListener(v -> {
            StateViewModel state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
            Airport selectedAirport = state.getAirport();
            if (state.getUser() == null) {
                openSignInPopup(gameViewModel, selectedAirport);
            } else {
                try {
                    gameViewModel.startHunt(selectedAirport.getCode(), selectedAirport.getCoordinates());
                } catch (IOException e) {
                    Log.d("In Plane Sight", e.getMessage());
                }
            }
        });
    }

    public void openSignInPopup(GameViewModel gameViewModel, Airport selectedAirport) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.view_sign_in_popup, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popup, width, height, true);
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

        popup.setOnTouchListener((v, e) -> {
            v.performClick();
            popupWindow.dismiss();
            return true;
        });

        Button toSignInBtn = popup.findViewById(R.id.signInPopupBtnSignin);
        toSignInBtn.setOnClickListener(e -> {
            popupWindow.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_startHuntFragment_to_accountFragment);
        });

        Button toStartHuntBtn = popup.findViewById(R.id.signInPopupBtnStartHunt);
        toStartHuntBtn.setOnClickListener(v -> {
            popupWindow.dismiss();
            try {
                gameViewModel.startHunt(selectedAirport.getCode(), selectedAirport.getCoordinates());
            } catch (IOException e) {
                Log.d("In Plane Sight", e.getMessage());
            }
        });
    }
}