package com.inplanesight.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.inplanesight.BuildConfig;
import com.inplanesight.ui.find.FindFragment;

import org.json.JSONException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GooglePlacesAPI {

    // Initialize the SDK
    public void initialize(Context c, String apiKey) {
        Places.initialize(c, apiKey);
    }

    public void getPhotoBitmapFromPlace(Context c, String placeId, FindFragment findFragment) throws InterruptedException {
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(c);

        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);

        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);

        PhotoParams myParams = new PhotoParams(placesClient, placeRequest, findFragment);

        getBitmapAsyncTask task = new getBitmapAsyncTask();
        task.execute(myParams);
    }

    static class PhotoParams {
        PlacesClient placesClient;
        FetchPlaceRequest fetchPlaceRequest;
        FindFragment findFragment;

        public PhotoParams(PlacesClient placesClient, FetchPlaceRequest fetchPlaceRequest, FindFragment findFragment) {
            this.placesClient = placesClient;
            this.fetchPlaceRequest = fetchPlaceRequest;
            this.findFragment = findFragment;
        }
    }

    class getBitmapAsyncTask extends AsyncTask<PhotoParams, Void, Void> {

        @Override
        protected Void doInBackground(PhotoParams... params) {
            PlacesClient placesClient = params[0].placesClient;
            FetchPlaceRequest placeRequest = params[0].fetchPlaceRequest;
            FindFragment findFragment = params[0].findFragment;

            placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                final Place place = response.getPlace();
                // Get the photo metadata.
                final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                if (metadata == null || metadata.isEmpty()) {
                    // TODO: I Replaced TAG with "" for now, wasn't finding symbol
                    Log.w("", "No photo metadata.");
                    return;
                }
                final PhotoMetadata photoMetadata = metadata.get(0);

                // Get the attribution text.
                final String attributions = photoMetadata.getAttributions();

                // Create a FetchPhotoRequest.
                final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(1600) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build();

                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    findFragment.setImage(fetchPhotoResponse.getBitmap());
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        final ApiException apiException = (ApiException) exception;
                        // TODO: I Replaced TAG with "" for now, wasn't finding symbol
                        Log.e("", "Place not found: " + exception.getMessage());
                        final int statusCode = apiException.getStatusCode();
                        // TODO: Handle error with given status code.
                    }
                });
            });
            return null;
        }

    }

    @SuppressLint("MissingPermission")
    public void getNearbyPlaces(String lat, String lng, FindFragment findFragment) throws IOException {
        GetNearbyPlacesAsyncTask task = new GetNearbyPlacesAsyncTask();
        PlacesParams placesParams = new PlacesParams(lat, lng, findFragment);
        task.execute(placesParams);
    }

    static class PlacesParams {
        String lat;
        String lng;
        FindFragment findFragment;

        public PlacesParams(String lat, String lng, FindFragment findFragment) {
            this.lat = lat;
            this.lng = lng;
            this.findFragment = findFragment;
        }
    }


    static class GetNearbyPlacesAsyncTask extends AsyncTask<PlacesParams, Void, Void> {

        @Override
        protected Void doInBackground(PlacesParams... params) {
            String lat = params[0].lat;
            String lng = params[0].lng;
            FindFragment findFragment = params[0].findFragment;
            String apiKey = BuildConfig.MAPS_API_KEY;

            String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                    + lat + "%2C" + lng + "&radius=2500&key=" + apiKey;
//                    + "&pagetoken=AW30NDzeKHghL3BxoMKgRvYU0pi3ev3JHuPanKgTfadT98cO9Fj1wXdWFHnw6D4DdXxnTwmWelE5TmT5PH0kK6wiSgAdJx1QrN27BpXJKlISNCNnOE_CW6Dyxd1i6RkOwLhmMLnO2WFZUSjv6YzexSrdemKWK4Rl7V_ssec1PLhUo7ldZygSfMTPYawBNsdCwHiJtuExaxq4X6sMdtvELAIrIRuFtujtceOWDZvNZbb5OsdUojelJPXfXfCE73uBbhyYb-LFqU9omtPNot4YquuE5RbPMHohRWFmf72zNEyK-eqaT_oklN0DaTeIgGNV4QGdLRRnqETPSaDG41vdXZJnfCOudCrUNZI8X2JmPm-mxpZ8jF_Qr377SCVPa5c8jEdFb9bHpnbgj7c1O9Azr9q8XaeJXL_zbQ48-dBO54Utjg6P";

            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Request request = new Request.Builder()
                    .url(urlString)
                    .addHeader("Accept", "application/json")
                    .method("GET", null)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                findFragment.testResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

