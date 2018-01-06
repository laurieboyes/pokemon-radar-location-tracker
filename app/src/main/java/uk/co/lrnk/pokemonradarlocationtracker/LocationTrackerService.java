package uk.co.lrnk.pokemonradarlocationtracker;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
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

public class LocationTrackerService extends IntentService {

    Handler mMainThreadHandler = null;

    public LocationTrackerService() {
        super("location tracker service");
        mMainThreadHandler = new Handler();

    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        System.out.println("Hello from this intent thingy 👋");

        RequestQueue queue = Volley.newRequestQueue(this);
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

                            mMainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Sent location to Pokémon Radar 📍", Toast.LENGTH_LONG).show();
                                }
                            });
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

    }
}
