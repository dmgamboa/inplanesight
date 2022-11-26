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
import com.inplanesight.data.StateViewModel;
import com.inplanesight.models.Airport;
import com.inplanesight.models.Leaderboard;
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

        Leaderboard[] test = {
                new Leaderboard("Donna", 2142), new Leaderboard("Andy", 1234), new Leaderboard("Dustin", 2), new Leaderboard("Brendan", 1)
        };

        RecyclerView recyclerView = requireView().findViewById(R.id.leaderBoardsRecyclerView);
        LeaderboardRecyclerViewAdapter adapter = new LeaderboardRecyclerViewAdapter(getContext(), test);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}