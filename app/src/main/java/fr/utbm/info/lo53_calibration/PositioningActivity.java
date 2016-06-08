package fr.utbm.info.lo53_calibration;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utbm.info.lo53_calibration.view.ScrollableView;
import fr.utbm.info.lo53_calibration.viewComponent.ImageScrollableViewComponent;
import fr.utbm.info.lo53_calibration.viewComponent.PointScrollableViewComponent;

/**
 * @file PositioningActivity.java
 * @brief Positioning Activity that manages the Positioning View where the user can see his location
 * @date May 25, 2016
 * @see android.support.v7.app.AppCompatActivity
 *
 * Is composed of a map view and an image showing where the user is
 * Sends requests to the server to get the user's location
 */
public class PositioningActivity  extends AppCompatActivity {

    /**scrollableView (ScrollableView)*/
    private ScrollableView scrollableView;
    /**target (PointScrollableViewComponent)*/
    private PointScrollableViewComponent target;
    /**thread (Thread) we're using to send location requests to the server*/
    private Thread thread;

    /**
     * Automatically called function when we create Main Activity
     * @brief Initializes the view, adds target and launches the thread
     * @param savedInstanceState (Bundle) the instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);

        scrollableView = (ScrollableView) findViewById(R.id.map_viewCalibration);


        ImageScrollableViewComponent map = new ImageScrollableViewComponent(getResources(), R.drawable.no_padding_map);
        scrollableView.addComponents(map);
        target = new PointScrollableViewComponent(Color.RED, 30.0f);
        scrollableView.addComponents(target);

        thread = new Thread() {

            private boolean running = true;

            /**
             * Automatically called function when we the thread is running
             * @brief While we don't stop the thread, we wait and call locateMe
             */
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

    /**
     * @brief Function that sends a get request to the server
     * asks the server for the device's location and parses the answer
     */
    private void locateMe() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.getFullUrl(this, Constants.SERVER_GET_LOCATION_URL);
        System.out.println("Error in the request: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        int length = response.length();
                        // Display the first 500 characters of the response string.

                        if (length > 500)
                            length = 500;
                        try {
                            JSONObject obj = new JSONObject(response.substring(response.indexOf("{"),response.indexOf("}")+1));
                            placeLocationPoint(Integer.parseInt(obj.getString("x")),Integer.parseInt(obj.getString("y")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error in the request: " + error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    /**
     * Automatically called function when the activity enters in pause mode
     * @brief Stops the requests to the API
     */
    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("PAUSED");
        thread.interrupt();
    }

    /**
     * @brief Places the target point on the map and center the map on this point
     * @param x (int) the x position on the map
     * @param y (int) the y position on the map
     */
    protected void placeLocationPoint(int x, int y){
        target.setPos(x, y);
        scrollableView.setViewTo(x, y);
    }
}
