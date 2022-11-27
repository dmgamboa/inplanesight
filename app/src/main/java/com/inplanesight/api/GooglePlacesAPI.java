package com.inplanesight.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.inplanesight.BuildConfig;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Game;
import com.inplanesight.models.Hunt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GooglePlacesAPI {

    private ArrayList<Hunt> huntList;

    public static PlacesClient placesClient;

    public FirebaseAPI firebaseAPI = new FirebaseAPI();

    public GooglePlacesAPI() {}

    public GooglePlacesAPI(ArrayList<Hunt> huntList) { this.huntList = huntList; }

    // Initialize the SDK
    public void initialize(Context c, String apiKey) {
        Places.initialize(c, apiKey);
        placesClient = Places.createClient(c);
    }

    MutableLiveData<Game> game;
    @SuppressLint("MissingPermission")
    public void getNearbyPlaces(String lat, String lng, String airportCode, MutableLiveData<Game> game) throws IOException {
        GetNearbyPlacesAsyncTask task = new GetNearbyPlacesAsyncTask();
        this.game =game;
        PlacesParams placesParams = new PlacesParams(lat, lng, airportCode);
        task.execute(placesParams);
    }

    static class PlacesParams {
        String lat;
        String lng;
        String airportCode;

        public PlacesParams(String lat, String lng, String airportCode) {
            this.lat = lat;
            this.lng = lng;
            this.airportCode = airportCode;
        }
    }


    class GetNearbyPlacesAsyncTask extends AsyncTask<PlacesParams, Void, Void> {

        @Override
        protected Void doInBackground(PlacesParams... params) {
            String lat = params[0].lat;
            String lng = params[0].lng;
            String airportCode = params[0].airportCode;
            String apiKey = BuildConfig.MAPS_API_KEY;

            String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + lat + "%2C" + lng + "&radius=2500&key=" + apiKey;

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(urlString)
                    .addHeader("Accept", "application/json")
                    .method("GET", null)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String resAsString = response.body().string();
                JSONObject data = new JSONObject(resAsString);
                JSONArray results = data.getJSONArray("results");
                for (int i = 0; i < 3; i++) {
                    JSONObject huntObject = results.getJSONObject(i);
                    String name = huntObject.getString("name");
                    String placeID = huntObject.getString("place_id");

                    JSONArray photos = huntObject.getJSONArray("photos");
                    String imageRef = name.replaceAll(" ", "_") + ".jpg";
                    String photoMetaRef = photos.getJSONObject(0).getString("photo_reference");
                    JSONObject location = huntObject.getJSONObject("geometry");
                    JSONObject coordinates = location.getJSONObject("location");
                    Double latitude = coordinates.getDouble("lat");
                    Double longitude = coordinates.getDouble("lng");

                    huntList.add(new Hunt(name, placeID, imageRef, new Coordinates(latitude, longitude), airportCode));
                    FirebaseAPI.writeToFirebase(huntList.get(i), "hunt");
                    Game updatedGame = game.getValue();
                    updatedGame.addHunt(huntList.get(i));
                    game.postValue(updatedGame);

                    final PhotoMetadata photoMetadata = PhotoMetadata.builder(photoMetaRef).build();

                    final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(500) // Optional.
                            .setMaxHeight(300) // Optional.
                            .build();

                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap bitmap = fetchPhotoResponse.getBitmap();
                        firebaseAPI.uploadPhotoBitmapFromPlace(bitmap, imageRef);
                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            final ApiException apiException = (ApiException) exception;
                            Log.e("", "Place not found: " + exception.getMessage());
                            final int statusCode = apiException.getStatusCode();
                            // TODO: Handle error with given status code.
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

