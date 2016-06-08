package fr.utbm.info.lo53_calibration.ComputationModels;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import fr.utbm.info.lo53_calibration.Constants;

/**
 * Histogram computation model
 */
public class Histogram extends ComputationModel {

    private int nbRequests = 40;

    public Histogram(ComputationModelInterface ctx) {
        super(ctx);
    }

    @Override
    public void locateMe() {
        RequestQueue MainQueue = Volley.newRequestQueue(getContext());
        RequestQueue subQueue = Volley.newRequestQueue(getContext());
        String urlMain = Constants.getFullUrl(getContext(), Constants.SERVER_GET_LOCATION_URL);
        String url = Constants.getFullUrl(getContext(), Constants.SERVER_PING);
       // System.out.println("Start");
        StringRequest mainRequest = new StringRequest(Request.Method.GET, urlMain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Main response: " + response);
                    try {
                            JSONObject obj = new JSONObject(response.substring(response.indexOf("{"),response.lastIndexOf("}")+1));
                            onLocation(Integer.parseInt(obj.getString("x")),Integer.parseInt(obj.getString("y")));
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
        StringRequest subRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // System.out.println("sub response");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error in the request: " + error.getMessage());
            }
        });

        MainQueue.add(mainRequest);
        for(int i = 0; i<nbRequests; i++){
            subQueue.add(subRequest);
        }

    }

    @Override
    public int getWaitingTime() {
        return 4000;
    }
}
