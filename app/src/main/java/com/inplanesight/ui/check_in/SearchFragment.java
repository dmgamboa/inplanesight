package com.inplanesight.ui.check_in;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inplanesight.R;
import com.inplanesight.data.FlightSearchViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Flight;
import com.inplanesight.ui.common.FlightInfoRecyclerViewAdapter;

public class SearchFragment extends Fragment {

    StateViewModel state;

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);

        RecyclerView flightList = requireActivity().findViewById(R.id.searchFlightsRecyclerView);
        flightList.setLayoutManager(new LinearLayoutManager(getContext()));
        FlightInfoRecyclerViewAdapter emptyAdapter = new FlightInfoRecyclerViewAdapter(getContext());
        flightList.setAdapter(emptyAdapter);


        FlightSearchViewModel flightSearch = new ViewModelProvider(this).get(FlightSearchViewModel.class);
        flightSearch.getFlights(state.getAirport()).observe(getViewLifecycleOwner(), models -> {
            if (models != null) {
                Flight[] flights = new Flight[models.size()];
                flights = models.toArray(flights);
                FlightInfoRecyclerViewAdapter adapter = new FlightInfoRecyclerViewAdapter(getContext(), flights, this::onFlightClicked);
                flightList.setAdapter(adapter);
            }
        });

        TextView airportTxt = requireActivity().findViewById(R.id.searchFlightsAirportTxt);
        airportTxt.setText(state.getAirport().toString());

        Button backBtn = requireActivity().findViewById(R.id.searchFlightsBtnBack);
        backBtn.setOnClickListener((e) -> Navigation.findNavController(view).popBackStack() );
    }

    public void onFlightClicked(Flight flight) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.view_flight_info_confirm, null);

        TextView flightNum = popup.findViewById(R.id.searchFlightPopupFlightNum);
        TextView destination = popup.findViewById(R.id.searchFlightPopupDestination);
        TextView departureTime = popup.findViewById(R.id.searchFlightPopupDepartureTime);
        Button selectBtn = popup.findViewById(R.id.searchFlightsBtnSelectFlight);

        flightNum.setText(flight.getNumber());
        destination.setText(flight.getDestination());
        departureTime.setText(flight.getTimeAsString());

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popup, width, height, true);
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

        popup.setOnTouchListener((v, e) -> {
            v.performClick();
            popupWindow.dismiss();
            return true;
        });

        selectBtn.setOnClickListener((e) -> {
            state.setFlight(flight);
            popupWindow.dismiss();
            Navigation.findNavController(requireView()).navigate(R.id.action_searchFragment_to_startHuntFragment);
        });
    }
}