package com.inplanesight.data;

import static java.lang.Math.abs;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Flight;
import com.inplanesight.models.Game;
import com.inplanesight.models.Hunt;

import org.checkerframework.checker.units.qual.A;
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

    public interface ProcessHunts {
        public void f() throws IOException;
    }

    public GameViewModel() {}

    public GameViewModel(String airportCode, Coordinates coordinates) throws IOException {
        this.airportCode = airportCode;
        this.coordinates = coordinates;
        game.setValue(new Game(airportCode));
    }

    public MutableLiveData<Game> getGame() {
        return game;
    }

    public void f() throws IOException {
        if (huntList.size() < 1) {
            places.getNearbyPlaces(coordinates.getLatitude().toString(), coordinates.getLongitude().toString(), airportCode, game);
        } else {
            for (JSONObject hunt : huntList) {
                Game updatedGame = game.getValue();
                updatedGame.addHunt(new Hunt(hunt));
                game.postValue(updatedGame);
            }
        }
    }

    // Query firebase based on location (airport) to check if location has available hunt if not query google places
    public void checkDatabase () throws IOException {
        FirebaseAPI.readFromFirebase(airportCode, "hunt", huntList, this::f);
    }

    // Check Coordinates object with LocationService and update score
    public boolean foundLocation (Coordinates userLoc, int index) {
        double threshold = 1000000;
        Coordinates placeLoc = game.getValue().getHuntAt(index).getCoordinates();
        Game updatedGame = game.getValue();
        boolean foundLocation = false;
        if (Math.abs(Coordinates.getDistance(userLoc, placeLoc)) <= threshold) {
            if (updatedGame.getScavengerHunt().get(index).getTimestampFound() == null) {
                foundLocation = true;
                updatedGame.getScavengerHunt().get(index).setTimestampFound(new Date(0));
                updatedGame.setScore(1000);
            }
        } else {
            updatedGame.setScore(-100);
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
                game.getValue().setScore(1000 - (int) (item.getTimestampFound().getTime() - game.getValue().getStartingTimestamp().getTime()));
            }
        }
        return game.getValue().getScore();
        // display score and send to leaderboards
    }
}
