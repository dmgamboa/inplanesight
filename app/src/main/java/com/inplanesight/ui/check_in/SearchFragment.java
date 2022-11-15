package com.inplanesight.ui.check_in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;
import com.inplanesight.data.AirportViewModel;
import com.inplanesight.data.FlightSearchViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;
import com.inplanesight.ui.common.FlightInfoRecyclerViewAdapter;
import com.inplanesight.ui.common.NavlessFragment;

import java.util.ArrayList;
import java.util.Arrays;

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

        RecyclerView flightList = getActivity().findViewById(R.id.searchFlightsRecyclerView);

        FlightSearchViewModel flightSearch = new ViewModelProvider(this).get(FlightSearchViewModel.class);
        flightSearch.getFlights(selectedAirport).observe(getViewLifecycleOwner(), models -> {
            if (models != null) {
                Flight[] flights = new Flight[models.size()];
                flights = models.toArray(flights);
                FlightInfoRecyclerViewAdapter adapter = new FlightInfoRecyclerViewAdapter(getContext(), flights);
                flightList.setLayoutManager(new LinearLayoutManager(getContext()));
                flightList.setAdapter(adapter);
            }
        });

        TextView airportTxt = getActivity().findViewById(R.id.searchFlightsAirportTxt);
        airportTxt.setText(selectedAirport.toString());

        Button confirmBtn = getActivity().findViewById(R.id.searchFlightsBtnSelectFlight);
        confirmBtn.setOnClickListener((e) -> { Navigation.findNavController(view).navigate(R.id.action_flight_selected);});

        Button backBtn = getActivity().findViewById(R.id.searchFlightsBtnBack);
        backBtn.setOnClickListener((e) -> { Navigation.findNavController(view).popBackStack(); });
    }

    @Override
    public void onDestroyView() {
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.VISIBLE);

        super.onDestroyView();
    }
}