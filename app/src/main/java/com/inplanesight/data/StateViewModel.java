package com.inplanesight.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;


public class StateViewModel extends ViewModel {
    private Airport airport;
    private Flight flight;

    public StateViewModel() {}

    public void setFlight(Flight f) {
        flight = f;
    }

    public void setAirport(Airport a) {
        airport = a;
    }

    public Flight getFlight() { return flight; }

    public Airport getAirport() { return airport; }
}
