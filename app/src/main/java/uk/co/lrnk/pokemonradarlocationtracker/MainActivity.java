package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void turnOnLocationTracking(View view) {

        // one minute is the minimum
        final int PERIOD = 60 * 1000;

        AlarmManager alarmManager =
                (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, LocationTrackerService.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, i, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + PERIOD, PERIOD, alarmIntent);
    }

    public void turnOfLocationTracking(View view) {
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }
}
