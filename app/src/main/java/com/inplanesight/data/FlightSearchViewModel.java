package com.inplanesight.data;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.inplanesight.BuildConfig;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlightSearchViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Flight>> flights;

    public FlightSearchViewModel() {

    }

    public LiveData<ArrayList<Flight>> getFlights(Airport airport) {
        flights = new MutableLiveData<ArrayList<Flight>>();
        loadFlights(airport);
        return flights;
    }

    private void loadFlights(Airport airport) {
        OkHttpClient client = new OkHttpClient();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm");
        LocalDateTime currentTime = LocalDateTime.now();
        String fromTime = formatter.format(currentTime);
        String toTime = formatter.format(currentTime.plusHours(6));

        String url = "https://" + BuildConfig.AERODATABOX_API_HOST + "/flights/airports/icao/"
                + airport.getCode() + "/" + fromTime + "/" + toTime
                + "?withLeg=true&direction=Departure&withCancelled=true&withCodeshared=false&withCargo=false&withPrivate=false&withLocation=false";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", BuildConfig.AERODATABOX_API_KEY)
                .addHeader("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("In Plane Sight", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    ArrayList<Flight> res = new ArrayList<Flight>();
                    String resAsString = response.body().string();
                    JSONObject data = new JSONObject(resAsString);
                    JSONArray departures = data.getJSONArray("departures");
                    for (int i = 0; i < departures.length(); i++) {
                        JSONObject flight = departures.getJSONObject(i);
                        JSONObject departureInformation = flight.getJSONObject("departure");
                        JSONObject arrivalAirport = flight.getJSONObject("arrival").getJSONObject("airport");

                        String airline = flight.getJSONObject("airline").getString("name");
                        String number = flight.getString("number");
                        String status = flight.getString("status");
                        String code = arrivalAirport.has("iata") ? " (" +arrivalAirport.getString("iata") + ")" : "";
                        String destination = arrivalAirport.getString("name") + " (" + arrivalAirport.getString("iata") + ")";
                        String terminal = departureInformation.has("terminal") ? departureInformation.getString("terminal") : "";
                        String gate = departureInformation.has("gate") ? departureInformation.getString("gate") : "";

                        String departureTime = departureInformation.getString("scheduledTimeLocal").replace(" ", "T");
                        ZonedDateTime departureTimeZoned = ZonedDateTime.parse(departureTime);
                        LocalDateTime departureTimeLocal = departureTimeZoned.toLocalDateTime();

                        res.add(new Flight(airline, number, status, destination, terminal, gate, departureTimeLocal));
                    }

                    flights.postValue(res);
                } catch (Exception e) {
                    Log.d("In Plane Sight", e.getMessage());
                }
            }
        });
    }

}