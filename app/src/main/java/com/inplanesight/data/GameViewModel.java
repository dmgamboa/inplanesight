package com.inplanesight.data;

import static java.lang.Math.abs;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Game;
import com.inplanesight.models.Hunt;
import com.inplanesight.models.Leaderboard;
import com.inplanesight.models.Users;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class GameViewModel extends ViewModel {

    private MutableLiveData<Game> game = new MutableLiveData<>();
    private String airportCode;
    private Coordinates coordinates;
    private ArrayList<JSONObject> huntList = new ArrayList<>();
    private ArrayList<Hunt> huntDataList = new ArrayList<>();
    private MutableLiveData<ArrayList<Hunt>> hunt = new MutableLiveData<>();
    private GooglePlacesAPI places = new GooglePlacesAPI(huntDataList);
    boolean postedScore = false;

    public GameViewModel() {}

    public GameViewModel(String airportCode, Coordinates coordinates) throws IOException {
        this.airportCode = airportCode;
        this.coordinates = coordinates;
        game.setValue(new Game(airportCode));
    }

    public MutableLiveData<Game> getGame() {
        return game;
    }

    // Query firebase based on location (airport) to check if location has available hunt if not query google places
    public void checkDatabase () throws IOException {
        FirebaseAPI.readFromFirebase(airportCode, "hunt", task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("Success", document.getId() + " => " + document.getData());
                    huntList.add(new JSONObject(document.getData()));
                }
            } else {
                Log.w("Failure", "Error getting documents.", task.getException());
            }
            try {
                if (huntList.size() < 1) {
                    places.getNearbyPlaces(coordinates.getLatitude().toString(), coordinates.getLongitude().toString(), airportCode, game);
                } else {
                    for (JSONObject hunt : huntList) {
                        Game updatedGame = game.getValue();
                        updatedGame.addHunt(new Hunt(hunt));
                        game.postValue(updatedGame);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Check Coordinates object with LocationService and update score
    public int foundLocation (Coordinates userLoc, int index) {
        double threshold = 11000;
        Coordinates placeLoc = game.getValue().getHuntAt(index).getCoordinates();
        Game updatedGame = game.getValue();
        int foundLocation = 0;
        if (updatedGame.hasEnded() && !postedScore) {
            postedScore = true;
            foundLocation = endHunt();
            Users user = game.getValue().getUser();
            String username = null;
            if (user != null) {
                username = user.getNickname();
            }
            Leaderboard entry = new Leaderboard(username, foundLocation, airportCode);
            FirebaseAPI.writeToFirebase(entry, "leaderboards");
        } else if (Math.abs(Coordinates.getDistance(userLoc, placeLoc)) <= threshold) {
            if (updatedGame.getScavengerHunt().get(index).getTimestampFound() == null) {
                foundLocation = 1;
                updatedGame.getScavengerHunt().get(index).setTimestampFound(new Date(0));
                updatedGame.setScore(10);
            } else {
                foundLocation = 2;
            }
        } else {
            updatedGame.setScore(-1);
        }

        game.setValue(updatedGame);
        return foundLocation;
    }

    // Start hunt
    public void startHunt (String airportCode, Coordinates coordinates) throws IOException {
        this.airportCode = airportCode;
        this.coordinates = coordinates;
        game.setValue(new Game(airportCode));
        game.getValue().setStartingTimestamp(new Date(0));
        checkDatabase();
    }

    // End hunt
    public int endHunt () {
        for (Hunt item: game.getValue().getScavengerHunt()) {
            if (item.getTimestampFound() != null) {
                game.getValue().setScore((int) (item.getTimestampFound().getTime() - game.getValue().getStartingTimestamp().getTime()));
            }
        }
        return game.getValue().getScore();
        // display score and send to leaderboards
    }
}
