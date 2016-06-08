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
 * Single value computation model
 */
public class SingleValue extends ComputationModel {

    public SingleValue(ComputationModelInterface ctx) {
        super(ctx);
    }

    public void locateMe() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Constants.getFullUrl(getContext(), Constants.SERVER_GET_LOCATION_URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
        queue.add(stringRequest);

    }

    @Override
    public int getWaitingTime() {
        return 1000;
    }
}
