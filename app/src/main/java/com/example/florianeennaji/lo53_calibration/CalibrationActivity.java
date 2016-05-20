package com.example.florianeennaji.lo53_calibration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

/**
 * Created by florianeennaji on 19/05/16.
 */
public class CalibrationActivity extends AppCompatActivity implements OnTouchListener {

    private ImageView mapView;
    private int imageWidth = 1183;
    private int imageHeight = 506;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        mapView = (ImageView) findViewById(R.id.map_view);
        mapView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int imageX = (int) motionEvent.getX();
        int imageY = (int) motionEvent.getY();

        int realX = (imageX*imageWidth)/mapView.getWidth();
        int realY = (imageY*imageHeight)/mapView.getHeight();

        System.out.println("Real coordinates : " + realX + ", " + realY);
        this.sendPosition(realX, realY);
        return false;
    }

    private void sendPosition(final int x, final int y) {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.201:80/api/calibration/send-probe";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        int length = response.length();
                        if (length > 500)
                            length = 500;
                        System.out.println("Response is: "+ response.substring(0,response.length()));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("x", String.valueOf(x));
                params.put("y", String.valueOf(y));
                params.put("id", String.valueOf(id));

                return params;
            }
        };
        id++;
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
