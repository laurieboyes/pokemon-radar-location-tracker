package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

public class LocationTrackerService extends IntentService {

    public LocationTrackerService () {
        super("location tracker service");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        System.out.println("Hello from this intent thingy 👋");
        System.out.println(SystemClock.elapsedRealtime());
    }
}
