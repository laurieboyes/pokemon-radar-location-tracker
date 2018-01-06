package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final int jobId = 890709;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void turnOnLocationTracking(View view) {

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

    public void turnOffLocationTracking(View view) {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancel(jobId);
        Toast.makeText(getApplicationContext(), "Turning off location tracking", Toast.LENGTH_LONG).show();
    }
}
