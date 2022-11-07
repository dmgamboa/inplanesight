package com.inplanesight.ui.check_in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.inplanesight.R;
import com.inplanesight.data.AirportService;
import com.inplanesight.data.LocationService;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Coordinates;
import com.inplanesight.ui.common.NavlessFragment;

import java.util.ArrayList;

public class CheckInFragment extends NavlessFragment implements AdapterView.OnItemSelectedListener {
    LocationService locationService;
    AirportService airportService;
    ArrayList<String> airportOpts;
    Airport selectedAirport;

    public CheckInFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationService = new LocationService(getActivity());
        airportService = new AirportService(getActivity());

        Button btnSelectAirport = getActivity().findViewById(R.id.checkInBtnSelectAirport);
        btnSelectAirport.setOnClickListener(this::selectAirport);

        Button btnSuggestAirport = getActivity().findViewById(R.id.checkInBtnSuggestAirport);
        btnSuggestAirport.setOnClickListener(this::suggestAirport);

        Spinner airportSpinner = getActivity().findViewById(R.id.checkInAirportSpinner);
        airportOpts = airportService.getSpinnerOpts();
        ArrayAdapter<String> data = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, airportOpts);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        airportSpinner.setAdapter(data);
        airportSpinner.setOnItemSelectedListener(this);

        locationService.storeLocation();
    }

    public void selectAirport(View view) {
        if (selectedAirport != null) {
            CheckInFragmentDirections.ActionAirportSelected action
                    = CheckInFragmentDirections.actionAirportSelected(selectedAirport);
            Navigation.findNavController(view).navigate(action);
        }
    }

    public void suggestAirport(View view) {
        locationService.storeLocation();
        Coordinates userLocation = locationService.getCoordinates();
        int closestAirportIndex = airportService.getClosestAirportIndex(userLocation);
        Spinner airportSpinner = getActivity().findViewById(R.id.checkInAirportSpinner);
        airportSpinner.setSelection(closestAirportIndex);
        selectedAirport = airportService.getAirport(closestAirportIndex);

        Toast.makeText(getActivity(), R.string.check_in_suggest_airport_toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.selectedAirport = airportService.getAirport(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}