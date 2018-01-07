package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

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

    private void sendUpdateRequest(Location location) {
        RequestQueue queue = Volley.newRequestQueue(LocationTrackerJobService.this);
//        String url = "https://mon-radar.herokuapp.com/api/updateLocation/";
        String url = "https://api.myjson.com/bins/fpe57";

        try {

            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
            String uuid = settings.getString("uuid", "");
            if(!uuid.isEmpty()) {
                JSONObject body = new JSONObject("{\"uuid\":\"" + uuid + "\",\"location\":{\"lat\":\"" + location.getLatitude() + "\",\"lng\":\"" + location.getLongitude() + "\"}, \"updatedAt\":\"" + new Date().toString() + "\"}");

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
            } else {
                System.out.println("No uuid saved");
                Toast.makeText(getApplicationContext(), "No UUID saved 🤔", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            System.out.println("Error parsing JSON body: " + e);
        }
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            System.out.println("Hello from the job handler thing!");

            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(LocationTrackerJobService.this);
            try {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    System.out.println("sick, got location " + location);

                                    SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);

                                    // weird shit for working out prev distances
                                    float prevLat = settings.getFloat("lat", 0);
                                    float prevLng = settings.getFloat("lng", 0);
                                    float[] results = new float[3];
                                    Location.distanceBetween(prevLat, prevLng, location.getLatitude(), location.getLongitude(), results);
                                    float distance = results[0];

                                    System.out.println("distance from last move was " + distance + " metres");

                                    if(distance > 50) {
                                        sendUpdateRequest(location);

                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putFloat("lat", (float) location.getLatitude());
                                        editor.putFloat("lng", (float) location.getLongitude());
                                        editor.apply();
                                    } else {
                                        System.out.println("Not moved much since last time, don't bother updating");
                                    }
                                } else {
                                    System.out.println("location was null for some reason???");
                                }
                            }
                        });
            } catch (SecurityException e) {
                System.out.println("Huh, SecurityException" + e);
                Toast.makeText(getApplicationContext(), "SecurityException trying to get location", Toast.LENGTH_LONG).show();
            }
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }

    });

}
