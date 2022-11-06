package com.inplanesight.data;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    final public static int LATITUDE_INDEX = 0;
    final public static int LONGITUDE_INDEX = 1;
    final public static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    FusedLocationProviderClient locationProvider;
    LocationManager locationManager;
    Double[] location = new Double[2];
    Context context;

    public LocationService(Context context) {
        this.context = context;
        locationProvider = LocationServices.getFusedLocationProviderClient(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void storeLocation() {
        if (hasLocationPermission() && isLocationEnabled()) {
            locationProvider.getLastLocation().addOnCompleteListener(task -> {
                Location res = task.getResult();
                Double latitude;
                Double longitude;

                if (location != null) {
                    location[LATITUDE_INDEX] = res.getLatitude();
                    location[LONGITUDE_INDEX] = res.getLongitude();
                }
            });
        }
    }

    public Double[] getLocation() {
        return location;
    }

    public boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void requestEnableLocation() {
        if (!isLocationEnabled()) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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