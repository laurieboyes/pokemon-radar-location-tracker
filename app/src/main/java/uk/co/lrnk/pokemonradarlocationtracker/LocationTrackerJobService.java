package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class LocationTrackerJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("Hello from the job handler thing!");

            RequestQueue queue = Volley.newRequestQueue(LocationTrackerJobService.this);
//        String url = "https://mon-radar.herokuapp.com/api/updateLocation/";
            String url = "https://api.myjson.com/bins/fpe57";

            try {
                JSONObject body = new JSONObject("{\"uuid\":\"48abbf1a-e0dd-4a51-850d-61250ef67aeb\",\"location\":{\"lat\":\"1.123\",\"lng\":\"2.345\"}, \"updatedAt\":\"" + new Date().toString() + "\"}");

                // Remember for real URL method must be POST
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("Response: " + response);
                                Toast.makeText(getApplicationContext(), "Sent location to Pokémon Radar 📍", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Response error: " + error);
                    }
                });
                queue.add(request);
            } catch (JSONException e) {
                System.out.println("Error parsing JSON body: " + e);
            }

            jobFinished((JobParameters) msg.obj, false);
            return true;
        }

    });

}
