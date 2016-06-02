package fr.utbm.info.lo53_calibration;

import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import fr.utbm.info.lo53_calibration.viewComponent.ImageScrollableViewComponent;


/**
 * Activity use to do the calivb
 */
public class CalibrationActivity extends AppCompatActivity {

    private ScrollableView scrollableView;
    private int increment = 10;
    final private int probeSendDelay = 200;
    final private int nbProbe = 15;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calibration);
        scrollableView = (ScrollableView) findViewById(R.id.map_viewCalibration);

        ImageScrollableViewComponent map = new ImageScrollableViewComponent(getResources(), R.drawable.no_padding_map);
        scrollableView.addComponents(map);
    }

    public void setCenter(View v) {
        scrollableView.setViewTo(800.0f, 300.0f);
    }

    public void addUp(View v) {
        float[] realCoords = scrollableView.getRealPos(scrollableView.getWidth() / 2, (scrollableView.getHeight() / 2) - increment);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    public void addRigth(View v) {
        float[] realCoords = scrollableView.getRealPos((scrollableView.getWidth() / 2) + increment, scrollableView.getHeight() / 2);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    public void addDown(View v) {
        float[] realCoords = scrollableView.getRealPos(scrollableView.getWidth() / 2, (scrollableView.getHeight() / 2) + increment);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

    public void addLeft(View v) {
        // System.out.println("given coordinates : " + scrollableView.getWidth()/2 + ", " +  scrollableView.getHeight()/2);
        final float[] realCoords = scrollableView.getRealPos((scrollableView.getWidth() / 2) - increment, scrollableView.getHeight() / 2);
        scrollableView.setViewTo(realCoords[0], realCoords[1]);
    }

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

    private void sendProbes(final int x, final int y, final int RequestNumber) {

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Make the request, this request will be use as many time as we want to send probes
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.SERVER_SEND_CALIBRATION_URL,
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


