package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void turnOnLocationTracking(View view) {

        // one minute is the minimum
        final int PERIOD = 60 * 1000;

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getTrackerIntent();

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + PERIOD, PERIOD, alarmIntent);

        Toast.makeText(getApplicationContext(), "Sending location roughly every " + PERIOD + "ms", Toast.LENGTH_LONG).show();
    }

    public void turnOffLocationTracking(View view) {

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getTrackerIntent();
        alarmManager.cancel(alarmIntent);
        Toast.makeText(getApplicationContext(), "Turning off location tracking", Toast.LENGTH_LONG).show();
    }

    private PendingIntent getTrackerIntent() {
        Intent intent = new Intent(this, LocationTrackerService.class);
        return PendingIntent.getService(this, 0, intent, 0);
    }
}
