package com.example.florianeennaji.lo53_calibration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendPacket(View v) {
        System.out.println("Sending packet...");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constants.SERVER_BASE_URL;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        int length = response.length();
                        if (length > 500)
                            length = 500;

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        System.out.println("Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void seeWifi(View v) {
        this.startActivity(new Intent(this, WiFiActivity.class));
    }


    public void startPositioning(View v) {
        this.startActivity(new Intent(this, PositioningActivity.class));
    }

    public void startCalibration(View v) {
        this.startActivity(new Intent(this, CalibrationActivity.class));
    }
}
