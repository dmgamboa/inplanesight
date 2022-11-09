package com.inplanesight.api;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.inplanesight.BuildConfig;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AeroDataBoxAPI extends AsyncTask {
    final static String host = "aerodatabox.p.rapidapi.com";

    private OkHttpClient client = new OkHttpClient();
    private String airportCode;
    private String origin;

    public AeroDataBoxAPI(Airport airport, Flight flights) {
         airportCode = airport.getCode();
         origin = airport.getName() + " / " + airport.getCode();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm");
        LocalDateTime currentTime = LocalDateTime.now();
        String fromTime = formatter.format(currentTime);
        String toTime = formatter.format(currentTime.plusHours(3));

        Request request = new Request.Builder()
                .url(host + "/flights/airports/icao/" + airportCode + "/" + fromTime + "/" + toTime
                        + "?withLeg=true&direction=Departure&withCancelled=true&withCodeshared=false&withCargo=false&withPrivate=false&withLocation=false")
                .get()
                .addHeader("X-RapidAPI-Key", BuildConfig.AERODATABOX_API_KEY)
                .addHeader("X-RapidAPI-Host", host)
                .build();

       client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("In Plane Sight", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    ArrayList<Flight> flights = new ArrayList<Flight>();

                    Log.d("In Plane Sight", response.body().string());
                    JSONObject data = new JSONObject(response.body().string()).getJSONObject("data");
                    JSONArray departures = data.getJSONArray("departures");
                    for (int i = 0; i < departures.length(); i++) {
                        JSONObject flight = departures.getJSONObject(i);
                        JSONObject departureInformation = flight.getJSONObject("departure");
                        JSONObject arrivalInformation = flight.getJSONObject("arrival");

                        String number = arrivalInformation.getString("number");
                        String status = arrivalInformation.getString("status");
                        /** TODO: Fix this */
                        String destination = "Test Airport / CODE";
                        String terminal = departureInformation.getString("terminal");
                        String gate = departureInformation.getString("gate");

                        /** TODO: Fix time */
                        flights.add(new Flight(number, status, origin, destination, terminal, gate, LocalDateTime.now()));
                    }
                } catch (Exception e) {
                    Log.d("In Plane Sight", e.getMessage());
                }
            }
        });
        return null;
    }
}
