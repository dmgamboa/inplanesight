package com.inplanesight.data;

import static java.lang.Math.abs;

import androidx.lifecycle.ViewModel;

import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.Game;
import com.inplanesight.models.Hunt;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class GameViewModel extends ViewModel {

    private Game game;

    private String airportCode;

    private ArrayList<JSONObject> huntList = new ArrayList<>();

    public GameViewModel(String myLocationCode) {
        airportCode = myLocationCode;
        game = new Game(airportCode);
    }

    public Game getGame() {
        return game;
    }

    // Query firebase based on location (airport) to check if location has available hunt if not query google places
    public void checkDatabase () {
        FirebaseAPI.readFromFirebase(airportCode, "hunt", huntList);
        if (huntList.size() < 1) {
//            FirebaseAPI.writeToFirebase(new Hunt(GooglePlacesAPI.getPlaces(airportCode)), "hunt");
//            game.getScavengerHunt().add(new Hunt(GooglePlacesAPI.getPlaces(airportCode)));
        } else {
            for (JSONObject hunt : huntList) {
                game.getScavengerHunt().add(new Hunt(hunt));
            }
        }
    }

    // Check Coordinates object with LocationService and update score
    public void foundLocation (Coordinates location, int index) {
        double threshold = 1.0;
        if (abs(location.getLatitude() - game.getScavengerHunt().get(index).getCoordinates().getLatitude()) < threshold &&
                abs(location.getLongitude() - game.getScavengerHunt().get(index).getCoordinates().getLongitude()) < threshold) {
            game.getScavengerHunt().get(index).setTimestampFound(new Date(0));
        } else {
            game.setScore(-100);
        }
    }

    // Start hunt
    public void startHunt () {
        game.setStartingTimestamp(new Date(0));
    }

    // End hunt
    public void endHunt () {
        for (Hunt item: game.getScavengerHunt()) {
            game.setScore((int) (item.getTimestampFound().getTime() - game.getStartingTimestamp().getTime()));
        }
        // display score and send to leaderboards
    }
}
