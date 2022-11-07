package com.inplanesight.ui.check_in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.inplanesight.R;
import com.inplanesight.models.Airport;
import com.inplanesight.ui.common.NavlessFragment;

public class SearchFragment extends NavlessFragment {

    Airport selectedAirport;

    public SearchFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedAirport = SearchFragmentArgs.fromBundle(getArguments()).getSelectedAirport();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView airportTxt = getActivity().findViewById(R.id.searchFlightsAirportTxt);
        airportTxt.setText(selectedAirport.toString());

        Button confirmBtn = getActivity().findViewById(R.id.searchFlightsBtnSelectFlight);
        confirmBtn.setOnClickListener((e) -> { Navigation.findNavController(view).navigate(R.id.action_flight_selected);});

        Button backBtn = getActivity().findViewById(R.id.searchFlightsBtnBack);
        backBtn.setOnClickListener((e) -> { Navigation.findNavController(view).popBackStack(); });
    }
}