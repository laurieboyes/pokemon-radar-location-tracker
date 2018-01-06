package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class LocationTrackerService extends IntentService {

    Handler mMainThreadHandler = null;

    public LocationTrackerService() {
        super("location tracker service");
        mMainThreadHandler = new Handler();

    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        System.out.println("Hello from this intent thingy 👋");
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Sending location to Pokémon Radar 📍", Toast.LENGTH_LONG).show();
            }
        });

    }
}
