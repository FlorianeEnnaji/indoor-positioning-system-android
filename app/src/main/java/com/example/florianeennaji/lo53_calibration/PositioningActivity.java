package com.example.florianeennaji.lo53_calibration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.os.SystemClock.sleep;

/**
 * Created by florianeennaji on 25/05/16.
 */
public class PositioningActivity  extends AppCompatActivity {

    private ImageView mapView;
    private ImageView pinView;
    private int imageWidth = 1183;
    private int imageHeight = 506;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);

        mapView = (ImageView) findViewById(R.id.map_view);
        pinView = (ImageView) findViewById(R.id.pin_view);

        thread = new Thread() {

            private boolean running = true;

            public void run() {
                while(this.running) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        this.running = false;
                    }
                    locateMe();
                }
            }
        };
        thread.start();
    }

    private void locateMe() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.200:8090/api/locate-me";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        int length = response.length();
                        // Display the first 500 characters of the response string.

                        if (length > 500)
                            length = 500;
                        int[] position = parse(response.substring(1,length-1));
                        System.out.println("Position : " + position[0] + ", " + position[1]);
                        pinView.setX(position[0]);
                        pinView.setY(position[1]);
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

    private int[] parse(String substring) {
        int i = 0;
        int x = 0;
        int y = 0;

        for (i = 4; i < substring.length() ; i++) {
            if (substring.charAt(i) == ',') {
                x = Integer.parseInt(substring.substring(4, i));
                break;
            }
        }

        for (i = substring.length()-1 ; i > 0 ; i--) {
            if (substring.charAt(i) == ':') {
                y = Integer.parseInt(substring.substring(i+1, substring.length()));
                break;
            }
        }

        return new int[]{x,y};
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("PAUSED");
        thread.interrupt();
    }
}
