package uk.co.lrnk.pokemonradarlocationtracker;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

//    public static final String PREFS_NAME = "PokemonRadarLocationTracker";

    final int jobId = 890709;
    final int permissionRequestId = 3123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton toggle = findViewById(R.id.toggleButton);
        if(isTrackingOn()) {
            toggle.setChecked(true);
        }
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnTracking();
                } else {
                    turnOffTracking();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionRequestId: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scheduleJob();
                } else {
                    Toast.makeText(getApplicationContext(), "Do try again if you change your mind️", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isTrackingOn() {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = mJobScheduler.getPendingJob(jobId);
        return jobInfo != null;
    }

    private void scheduleJob() {
        final int PERIOD = 15 * 60 * 1000;

        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(jobId,
                new ComponentName(getPackageName(),
                        LocationTrackerJobService.class.getName()));

        builder.setPeriodic(PERIOD);
        int scheduleResult = mJobScheduler.schedule(builder.build());
        if (scheduleResult == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(getApplicationContext(), "Sending location roughly every " + (PERIOD / (60 * 1000)) + " minutes", Toast.LENGTH_LONG).show();

//            // We need an Editor object to make preference changes.
//            // All objects are from android.context.Context
//            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putBoolean("trackingOn", true);
//
//            // Commit the edits!
//            editor.commit();

        } else {
            Toast.makeText(getApplicationContext(), "Error scheduling job 🤷‍♀️", Toast.LENGTH_LONG).show();
        }
    }


    private void turnOnTracking() {

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    permissionRequestId);
        } else {
            scheduleJob();
        }
    }

    private void turnOffTracking() {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancel(jobId);
        Toast.makeText(getApplicationContext(), "Turning off location tracking", Toast.LENGTH_LONG).show();
    }
}
