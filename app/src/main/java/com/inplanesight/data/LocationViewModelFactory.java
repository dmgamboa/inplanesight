package com.inplanesight.data;

import android.content.Context;
import android.location.LocationManager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.inplanesight.models.Coordinates;

public class LocationViewModelFactory implements ViewModelProvider.Factory {
    Context context;

    public LocationViewModelFactory(Context context) {
        this.context = context;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new LocationViewModel(context);
    }


}
