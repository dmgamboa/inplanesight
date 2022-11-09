package com.inplanesight.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.inplanesight.ui.find.FindFragment;

import java.util.Collections;
import java.util.List;

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

        MyAsyncTask task = new MyAsyncTask();
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

    class MyAsyncTask extends AsyncTask<PhotoParams, Void, Void> {

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

}

/*
 *
 */
