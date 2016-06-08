package fr.utbm.info.lo53_calibration;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

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
 * Created by florianeennaji on 25/05/16.
 */
public class PositioningActivity  extends AppCompatActivity {

    private ScrollableView scrollableView;
    private PointScrollableViewComponent target;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        scrollableView = (ScrollableView) findViewById(R.id.map_viewCalibration);


        ImageScrollableViewComponent map = new ImageScrollableViewComponent(getResources(), R.drawable.no_padding_map);
        scrollableView.addComponents(map);
        target = new PointScrollableViewComponent(Color.RED, 30.0f);
        scrollableView.addComponents(target);

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

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        System.out.println("PAUSED");
        thread.interrupt();
    }

    protected void placeLocationPoint(int x, int y){
        target.setPos(x, y);
        scrollableView.setViewTo(x, y);
    }
}
