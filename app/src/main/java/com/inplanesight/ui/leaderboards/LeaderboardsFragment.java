package com.inplanesight.ui.leaderboards;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.inplanesight.R;
import com.inplanesight.data.FlightSearchViewModel;
import com.inplanesight.data.LeaderboardsViewModel;
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Flight;
import com.inplanesight.models.Leaderboard;
import com.inplanesight.ui.common.FlightInfoRecyclerViewAdapter;

public class LeaderboardsFragment extends Fragment {

    public LeaderboardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StateViewModel state = new ViewModelProvider(requireActivity()).get(StateViewModel.class);
        Airport selectedAirport = state.getAirport();

        TextView airportHeader = requireActivity().findViewById(R.id.leaderboardsAirport);
        airportHeader.setText(selectedAirport.getCode());

        RecyclerView recyclerView = requireView().findViewById(R.id.leaderBoardsRecyclerView);
        LeaderboardRecyclerViewAdapter adapter = new LeaderboardRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LeaderboardsViewModel leaderboards = new ViewModelProvider(this).get(LeaderboardsViewModel.class);
        leaderboards.getLeaderboards(state.getAirport().getCode()).observe(getViewLifecycleOwner(), entries -> {
            if (entries != null) {
                Leaderboard[] leaderboard = new Leaderboard[entries.size()];
                leaderboard = entries.toArray(leaderboard);
                LeaderboardRecyclerViewAdapter updatedAdapter = new LeaderboardRecyclerViewAdapter(getContext(), leaderboard);
                recyclerView.setAdapter(updatedAdapter);
            }
        });
    }


}