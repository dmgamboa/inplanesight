package com.inplanesight.data;

import androidx.lifecycle.ViewModel;

import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;
import com.inplanesight.models.Users;


public class StateViewModel extends ViewModel {
    private Airport airport;
    private Flight flight;
    private Users user;

    public StateViewModel() {}

    public void setFlight(Flight f) {
        flight = f;
    }

    public void setAirport(Airport a) {
        airport = a;
    }

    public Flight getFlight() { return flight; }

    public Airport getAirport() { return airport; }

    public Users getUser() { return user; }

    public void setUser(Users user) { this.user = user; }
}
