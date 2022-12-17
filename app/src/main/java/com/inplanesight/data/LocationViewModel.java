package com.inplanesight.data;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.inplanesight.models.Coordinates;


public class LocationViewModel extends ViewModel {
    final public static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    FusedLocationProviderClient locationProvider;
    LocationManager locationManager;
    MutableLiveData<Coordinates> location = new MutableLiveData<>();
    Context context;

    public LocationViewModel(Context context) {
        this.context = context;
        locationProvider = LocationServices.getFusedLocationProviderClient(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        storeLocation();
    }

    @SuppressLint("MissingPermission")
    public void storeLocation() {
        if (hasLocationPermission() && isLocationEnabled()) {
            locationManager.requestLocationUpdates("gps", 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location loc) {
                    location.postValue(new Coordinates(loc.getLatitude(), loc.getLongitude()));
                }
            });
        }
    }

    public Coordinates getCoordinates() {
        return location.getValue();
    }

    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void requestEnableLocation() {
        if (!isLocationEnabled()) {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public boolean hasLocationPermission() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (! (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }
}