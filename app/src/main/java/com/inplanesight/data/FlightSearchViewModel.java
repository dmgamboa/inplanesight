package com.inplanesight.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FlightSearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Flight>> flights;

    public FlightSearchViewModel() {

    }

    public LiveData<ArrayList<Flight>> getFlights(Airport airport) {
        flights = new MutableLiveData<ArrayList<Flight>>();
        return flights;
    }

    private void loadFlights() {
        ArrayList<Flight> loadedFlights = new ArrayList<>();
        loadedFlights.add(new Flight("ABC123", "Burnaby / BBY", "Vancouver / YVR", "2", "32F", LocalDateTime.now()));
        loadedFlights.add(new Flight("DEF123", "White Rock / WRO", "Richmond / PHO", "9", "3R", LocalDateTime.now()));
        flights.setValue(loadedFlights);
        flights.postValue(loadedFlights);
    }
}