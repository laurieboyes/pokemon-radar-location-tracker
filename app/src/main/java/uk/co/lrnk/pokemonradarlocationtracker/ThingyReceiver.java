package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class ThingyReceiver extends BroadcastReceiver {
    private static final int PERIOD = 60 * 1000;

    @Override
    public void onReceive(Context ctxt, Intent i) {
        scheduleAlarms(ctxt);
    }

    static void scheduleAlarms(Context ctxt) {
        AlarmManager mgr =
                (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, LocationTrackerService.class);
        PendingIntent pi = PendingIntent.getService(ctxt, 0, i, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);
    }
}
