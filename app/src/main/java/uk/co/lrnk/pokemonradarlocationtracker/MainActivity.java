package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void turnOnLocationTracking(View view) {

        ThingyReceiver.scheduleAlarms(this);

        //        System.out.println("Woohoo! I can still Java!");
//
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        Intent trackerIntent = new Intent(this, ThingyReceiver.class);
//        alarmIntent = PendingIntent.getBroadcast(this, 0,
//                trackerIntent, 0);
//
//        // Hopefully your alarm will have a lower frequency than this!
////        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
////                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
////                AlarmManager.INTERVAL_HOUR, alarmIntent);
//
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + 1000,
//                1000, alarmIntent);


    }

    public void turnOfLocationTracking(View view) {
        if(alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }
}
