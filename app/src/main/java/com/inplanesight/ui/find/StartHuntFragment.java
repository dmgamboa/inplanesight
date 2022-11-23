package com.inplanesight.ui.find;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inplanesight.R;
import com.inplanesight.data.StateViewModel;

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

        /** TODO: Check GameViewModel if Hunt has already started; redirect to next fragment accordingly */




        Button startHuntBtn = requireActivity().findViewById(R.id.startHuntBtn);
        startHuntBtn.setOnClickListener(v -> {
            StateViewModel state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
            String airport = state.getAirport().getCode();
            Navigation.findNavController(requireView()).navigate(R.id.action_startHuntFragment_to_findFragment2);
            /** TODO: Call startHunt() in appropriate View Model */

        });
    }
}