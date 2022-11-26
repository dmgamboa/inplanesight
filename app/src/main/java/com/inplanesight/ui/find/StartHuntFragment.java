package com.inplanesight.ui.find;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;
import com.inplanesight.data.GameViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Coordinates;

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

        GameViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
        gameViewModel.getGame().observe(getViewLifecycleOwner(), game -> {
            if (game != null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_startHuntFragment_to_findFragment2);
            }
        });

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.VISIBLE);

        Button startHuntBtn = requireActivity().findViewById(R.id.startHuntBtn);
        startHuntBtn.setOnClickListener(v -> {
            StateViewModel state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
            Airport selectedAirport = state.getAirport();

            try {
                gameViewModel.startHunt("CYVR", new Coordinates(49.193901062,-123.183998108));

//                gameViewModel.startHunt(selectedAirport.getCode(), selectedAirport.getCoordinates());
            } catch (IOException e) {
                Log.d("In Plane Sight", e.getMessage());
            }
        });
    }
}