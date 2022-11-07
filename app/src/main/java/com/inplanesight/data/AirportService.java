package com.inplanesight.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.inplanesight.R;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Coordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AirportService extends Service {
    final static int NAME_INDEX = 1;
    final static int CITY_INDEX = 2;
    final static int COUNTRY_INDEX = 3;
    final static int CODE_INDEX = 5;
    final static int LAT_INDEX = 6;
    final static int LONG_INDEX = 7;

    ArrayList<Airport> airports;
    Context context;

    public AirportService(Context context) {
        this.context = context;
        try {
            airports = this.fromRaw();
        } catch (IOException e) {
            Log.d("In Plane Sight", e.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ArrayList<String> getSpinnerOpts() {
        ArrayList<String> opts = new ArrayList<>();
        for (Airport a : airports) {
            opts.add(a.toString());
        }
        return opts;
    }

    public Airport getAirport(int index) {
        return airports.get(index);
    }

    public Airport getClosestAirport(Coordinates userLocation) {
        return airports.stream().reduce(airports.get(0), (acc, curr) -> {
            return Coordinates.getDistance(userLocation, acc.getCoordinates())
                    > Coordinates.getDistance(userLocation, curr.getCoordinates()) ? curr : acc;
        });
    }

    public int getClosestAirportIndex(Coordinates userLocation) {
        return airports.indexOf(getClosestAirport(userLocation));
    }

    public ArrayList<Airport> fromRaw() throws IOException {
        ArrayList<Airport> airports = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.airports);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = br.readLine()) != null) {
            String[] input = line.split(",");
            Coordinates location = new Coordinates(Double.parseDouble(input[LAT_INDEX]), Double.parseDouble(input[LONG_INDEX]));
            String name = input[NAME_INDEX].replace("\"", "");
            String city = input[CITY_INDEX].replace("\"", "");
            String country = input[COUNTRY_INDEX].replace("\"", "");
            String code = input[CODE_INDEX].replace("\"", "");
            Airport toAdd = new Airport(name, city, country, code, location);
            airports.add(toAdd);
        }

        br.close();
        is.close();
        return airports;
    }

}