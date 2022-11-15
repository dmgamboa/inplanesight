package com.inplanesight.data;

import static java.lang.Math.abs;

import androidx.lifecycle.ViewModel;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.inplanesight.api.FirebaseAPI;
import com.inplanesight.api.GooglePlacesAPI;
import com.inplanesight.models.Coordinates;
import com.inplanesight.models.GameModel;
import com.inplanesight.models.HuntObject;

import java.util.Date;

public class GameViewModel extends ViewModel {

    private GameModel game;

    private Coordinates airport;

    public GameViewModel(Coordinates myLocation) {
        airport = myLocation;
        game = new GameModel(airport);
    }

    // Query firebase based on location (airport) to check if location has available hunt if not query google places
    public void checkDatabase () {
        String hunt = FirebaseAPI.queryFirebaseForHunt(airport);
        if (hunt.equals("")) {
            //game.addHuntObject(GooglePlacesAPI.getHuntObject(airport));
        } else {
            //game.addHuntObject(hunt);
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
        for (HuntObject item: game.getScavengerHunt()) {
            game.setScore((int) (item.getTimestampFound().getTime() - game.getStartingTimestamp().getTime()));
        }
        // display score and send to leaderboards
    }
}
