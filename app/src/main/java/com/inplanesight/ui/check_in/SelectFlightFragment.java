package com.inplanesight.ui.check_in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.inplanesight.R;
import com.inplanesight.ui.common.NavlessFragment;

public class CheckInFragment extends NavlessFragment {
    public CheckInFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button backBtn = this.getView().findViewById(R.id.btnBack);
        Button confirmBtn = this.getView().findViewById(R.id.btnConfirm);
        backBtn.setOnClickListener((e) -> { Navigation.findNavController(view).popBackStack(); });
        confirmBtn.setOnClickListener((e) -> { Navigation.findNavController(view).navigate(R.id.action_checkInFragment_to_findFragment2);});
    }
}