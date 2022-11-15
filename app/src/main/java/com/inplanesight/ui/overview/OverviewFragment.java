package com.inplanesight.ui.overview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.inplanesight.R;
import com.inplanesight.ui.common.NavlessFragment;

public class OverviewFragment extends Fragment {
    public OverviewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_nav);
        bottomNav.setVisibility(View.GONE);
        Button btnGetStarted = view.findViewById(R.id.overviewBtnCTA);
        btnGetStarted.setOnClickListener((e) -> {
            Navigation.findNavController(view).navigate(R.id.action_overviewFragment_to_checkInFragment);
        });
    }
}