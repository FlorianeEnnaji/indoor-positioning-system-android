package fr.utbm.info.lo53_calibration;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import fr.utbm.info.lo53_calibration.view.ScrollableView;
import fr.utbm.info.lo53_calibration.view.ScrollableViewComponent;
import fr.utbm.info.lo53_calibration.viewComponents.ImageScrollableViewComponent;


/**
 * @file CalibrationActivity.java
 * @brief Calibration Activity that manages the Calibration View where the tester can tap his location
 * @date May 25, 2016
 * @see android.support.v7.app.AppCompatActivity
 *
 * Is composed of a map view and a cursor meant to be used by the tester to tell the server where he is
 * Sends requests to the server to give the tester's location
 */
public class CalibrationActivity extends AppCompatActivity {

    /**scrollableView (ScrollableView) our map view*/
    private ScrollableView scrollableView;
    /**increment (int)*/
    private int increment = 20;
    /**probeSendDelay (int)*/
    private int probeSendDelay = 100;
    /**nbProbe (int)*/
    private int nbProbe = 50;
    /**progress (ProgressDialog)*/
    private ProgressDialog progress;

    /**
     * Automatically called function when we create Calibration Activity
     * @brief Initializes variables, the view, adds the cursor
     * @param savedInstanceState (Bundle) the instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        scrollableView = (ScrollableView) findViewById(R.id.map_viewCalibration);

        ImageScrollableViewComponent map = new ImageScrollableViewComponent(getResources(), R.drawable.no_padding_map);
        scrollableView.addComponents(map);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this); // load preferences
        nbProbe = Integer.parseInt(sharedPref.getString(this.getString(R.string.pref_key_calibration_nb_probe_send), "50"));
        probeSendDelay = Integer.parseInt(sharedPref.getString(this.getString(R.string.pref_key_calibration_time_btw_probe), "100"));

    }

    /**
     * @brief Centers a view
     * @param v (View) the view we want to get centered
     */
    public void setCenter(View v) {
        scrollableView.setViewTo(800.0f, 300.0f);
    }

    /**
     * @brief Moves a view up
     * @param v (View) the view we want to get moved
     */
    public void addUp(View v) {
        float[] realCoords = scrollableView.getRealPos(scrollableView.getWidth() / 2, (scrollableView.getHeight() / 2) - increment);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    /**
     * @brief Moves a view to the right
     * @param v (View) the view we want to get moved
     */
    public void addRight(View v) {
        float[] realCoords = scrollableView.getRealPos((scrollableView.getWidth() / 2) + increment, scrollableView.getHeight() / 2);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    /**
     * @brief Moves a view down
     * @param v (View) the view we want to get moved
     */
    public void addDown(View v) {
        float[] realCoords = scrollableView.getRealPos(scrollableView.getWidth() / 2, (scrollableView.getHeight() / 2) + increment);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    /**
     * @brief Moves a view to the left
     * @param v (View) the view we want to get moved
     */
    public void addLeft(View v) {
        // System.out.println("given coordinates : " + scrollableView.getWidth()/2 + ", " +  scrollableView.getHeight()/2);
        final float[] realCoords = scrollableView.getRealPos((scrollableView.getWidth() / 2) - increment, scrollableView.getHeight() / 2);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    /**
     * Called when the tester taps on the button to send his position
     * @brief get real position of the tester and call the function to send it
     * @param v (View) the view we want to get centered
     */
    public void getPos(View v) {
        final float[] realCoords = scrollableView.getRealPos(scrollableView.getWidth() / 2, scrollableView.getHeight() / 2);
        System.out.println("given coordinates : " + scrollableView.getWidth() / 2 + ", " + scrollableView.getHeight() / 2);
        sendProbes((int) realCoords[0], (int) realCoords[1], nbProbe);

        scrollableView.addComponents(new ScrollableViewComponent(){  // Adds a red dot at each point where we have sent probes
            private Paint paint = new Paint();
            {
                paint.setColor(Color.RED);
            }
            @Override
            public void onDraw(Canvas canvas) {
                canvas.drawCircle(realCoords[0], realCoords[1], 5.0f,paint) ;
            }
        });
    }

    /**
     * @brief sends the position a certain nb of time to the server
     * @param x (int) the x position of the tester
     * @param y (int) the y position of the tester
     * @param RequestNumber (int) the number of requests we have to send
     */
    private void sendProbes(final int x, final int y, final int RequestNumber) {

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Make the request, this request will be use as many time as we want to send probes
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.getFullUrl(this, Constants.SERVER_SEND_CALIBRATION_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Request ERROR: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("PosX", String.valueOf(x));
                params.put("PosY", String.valueOf(y));
                return params;
            }
        };

        progress = ProgressDialog.show(this, "Please wait ...", "Sending probes 0/" + RequestNumber, true);

        // communication between thread
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int val = msg.getData().getInt("val");
                if(val >= 0 )
                    progress.setMessage("Sending probes " + val + "/" + RequestNumber);
                else
                    progress.dismiss();
            }
        };

        // create a new thread to send request at a interval
        Thread thread = new Thread() {

            private boolean running = true;

            public void run() {
                while (this.running) {
                    try {
                        // Add requests to the RequestQueue.
                        for (int i = 0; i < RequestNumber; i++) {
                            sleep(probeSendDelay);
                            queue.add(stringRequest);
                            sendMsg(i+1);
                        }
                        this.running = false;
                        sleep(probeSendDelay);
                        sendMsg(-1);

                    } catch (InterruptedException e) {
                        this.running = false;
                    }
                }
            }
            // communication between thread
            private void sendMsg(int msg) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("val", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
            }
        };
        thread.start();
    }


}


