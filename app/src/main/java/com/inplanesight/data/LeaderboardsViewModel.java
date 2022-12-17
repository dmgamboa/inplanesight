package com.inplanesight.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.models.Flight;
import com.inplanesight.models.Game;
import com.inplanesight.models.Hunt;
import com.inplanesight.models.Leaderboard;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class LeaderboardsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Leaderboard>> leaderboards;
    private ArrayList<JSONObject> leaderboardList = new ArrayList<>();

    public LeaderboardsViewModel() {}

    public LiveData<ArrayList<Leaderboard>> getLeaderboards(String airportCode) {
        leaderboards = new MutableLiveData<ArrayList<Leaderboard>>();
        loadLeaderboards(airportCode);
        return leaderboards;
    }

    private void loadLeaderboards(String airportCode) {
        FirebaseAPI.readFromFirebaseLeaderboards(airportCode, "leaderboards", task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("Success", document.getId() + " => " + document.getData());
                    leaderboardList.add(new JSONObject(document.getData()));
                }
            } else {
                Log.w("Failure", "Error getting documents.", task.getException());
            }
            ArrayList<Leaderboard> updatedLeaderboards = new ArrayList<>();
            for (JSONObject entry : leaderboardList) {
                String username = null;
                try {
                    username = entry.getString("user");
                } catch (Exception e){

                }
                if (username != null && !username.equals("null")) {
                    updatedLeaderboards.add(new Leaderboard(entry));
                }
            }
            leaderboards.postValue(updatedLeaderboards);
        });
    }
}
