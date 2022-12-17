package com.inplanesight.ui.flight_info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;
import com.inplanesight.data.FlightSearchViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Flight;
import com.inplanesight.ui.common.FlightInfoRecyclerViewAdapter;

public class FlightInfoFragment extends Fragment {
    public int counter = 60;

    public FlightInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView flightList = requireActivity().findViewById(R.id.searchFlightsRecyclerView);

        StateViewModel state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
        Flight flight = state.getFlight();

        TextView airline = view.findViewById(R.id.flightInfoAirline);
        TextView flightNumber = view.findViewById(R.id.flightInfoFlightNumVal);
        LinearLayout terminalGroup = view.findViewById(R.id.flightInfoTerminal);
        TextView terminal = view.findViewById(R.id.flightInfoTerminalVal);
        LinearLayout gateGroup = view.findViewById(R.id.flightInfoGate);
        TextView gate = view.findViewById(R.id.flightInfoGateVal);
        TextView destination = view.findViewById(R.id.flightInfoDestinationVal);
        TextView departureTime = view.findViewById(R.id.flightInfoDepartureTimeVal);
        TextView status = view.findViewById(R.id.flightInfoStatusVal);

        airline.setText(flight.getAirline());
        flightNumber.setText(flight.getNumber());
        destination.setText(flight.getDestination());

        if (!flight.getGate().equals("")) {
            gateGroup.setVisibility(View.VISIBLE);
            gate.setText(flight.getGate());
        } else {
            gate.setVisibility(View.GONE);
        }

        if (!flight.getTerminal().equals("")) {
            terminalGroup.setVisibility(View.VISIBLE);
            terminal.setText(flight.getGate());
        } else {
            terminalGroup.setVisibility(View.GONE);
        }

        terminal.setText(flight.getTerminal());
        departureTime.setText(flight.getTimeAsString());
        status.setText(flight.getStatus());
    }
}