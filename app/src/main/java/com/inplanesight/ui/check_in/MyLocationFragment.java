package com.inplanesight.ui.check_in;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.inplanesight.R;
import com.inplanesight.ui.common.NavlessFragment;

public class MyLocationFragment extends NavlessFragment {

    TextView locationText;
    Button backBtn, confirmBtn, getLocationBtn;
    FusedLocationProviderClient client;

    public MyLocationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = this.getView().findViewById(R.id.btnBack);
        confirmBtn = this.getView().findViewById(R.id.btnConfirm);
        client = LocationServices.getFusedLocationProviderClient(view.getContext());
        getLocationBtn = this.getView().findViewById(R.id.btnUseMyLocation);
        locationText = this.getView().findViewById(R.id.tvOutputLocation);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    //when permission is granted call method
                    getCurrentLocation();
                } else {
                    //When permission is not granted
                    //Request permission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });

        backBtn.setOnClickListener((e) -> { Navigation.findNavController(view).popBackStack(); });
        confirmBtn.setOnClickListener((e) -> { Navigation.findNavController(view).navigate(R.id.action_myLocationFragment_to_checkInFragment);});
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            //when permission are granted
            //Call method
            getCurrentLocation();
        } else {
            //when permission are denied
            //Display Toast
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enabled
            //Get last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        locationText.setText("Latitude: " + String.valueOf(location.getLatitude())
                                + " Longitude : " + String.valueOf(location.getLongitude()));
                    }
                    else {
                        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                                .setPriority(LocationRequest.QUALITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                //init location
                                Location location1 = locationResult.getLastLocation();
                                //set text
                                locationText.setText("Latitude: " + String.valueOf(location.getLatitude())
                                        + " Longitude : " + String.valueOf(location.getLongitude()));
                            }
                        };
                        //Request location updates
                        client.requestLocationUpdates(locationRequest
                        ,locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //When location service is not enabled
            //Open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}