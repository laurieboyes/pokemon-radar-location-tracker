package uk.co.lrnk.pokemonradarlocationtracker;

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
        System.out.println("Woohoo! I can still Java!");
    }
}
