package uk.co.lrnk.pokemonradarlocationtracker;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int jobId = 890709;
    final int permissionRequestId = 3123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        } else {
            Toast.makeText(getApplicationContext(), "Error scheduling job 🤷‍♀️", Toast.LENGTH_LONG).show();
        }
    }


    public void turnOnLocationTracking(View view) {

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

    public void turnOffLocationTracking(View view) {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancel(jobId);
        Toast.makeText(getApplicationContext(), "Turning off location tracking", Toast.LENGTH_LONG).show();
    }
}
