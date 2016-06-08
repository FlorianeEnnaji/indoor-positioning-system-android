package fr.utbm.info.lo53_calibration;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    protected enum Mode{
        CALIBRATION, LOCATION
    }

    protected Mode actualMode = Mode.LOCATION;
    protected String computationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setTitle(R.string.app_name_short);
        setSupportActionBar(myToolbar);

        setServerState(null);
        setMode(Mode.LOCATION);

        String wifiName = isConnectedByWifi();
        setConnectionState(wifiName);

        if(wifiName != null){
            setMode(Mode.LOCATION);
            getServerParameter();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_check_connection:
                String wifiName = isConnectedByWifi();
                setConnectionState(wifiName);

                if(wifiName != null){
                    setMode(Mode.LOCATION);
                    getServerParameter();
                }else{
                    setServerState(null);
                }

                Toast.makeText(getApplicationContext(), "Verification", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Checks if a wifi connection is enabled
     * @return (String) The name of the connection OR null
     */
    public String isConnectedByWifi(){
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager wifiMgr = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                String wifiName = wifiInfo.getSSID();
               return wifiName;
            }else
                return null;
        } else
           return null;
    }

    /**
     * Updates the view about the connectivity
     * @param wifiName (String) The name of the wifi connection OR null
     */
    public void setConnectionState(String wifiName){
        if(wifiName != null){
            Spannable sb = buildText("Wifi : ",
                    wifiName.substring(1,wifiName.length()-1),
                    new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                    new StyleSpan(android.graphics.Typeface.BOLD));
            ((TextView) findViewById(R.id.WifiState)).setText(sb);
            ((ImageView) findViewById(R.id.WifiIcon)).setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
        }else{
            Spannable sb = buildText("Wifi : ",
                    "Not connected",
                    new ForegroundColorSpan(getResources().getColor(R.color.colorWarning)),
                    new StyleSpan(android.graphics.Typeface.BOLD));
            ((TextView) findViewById(R.id.WifiState)).setText(sb);
            ((ImageView) findViewById(R.id.WifiIcon)).setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
        }
    }

    /**
     * Updates the view about the server state
     * @param modelName (String) The name of the computation model OR null
     */
    public void setServerState(String modelName) {
        computationModel = modelName;
        if (modelName != null) {
            Spannable sb = buildText("Server : ",
                    "connected",
                    new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                    "\nComputation model : ",
                    new ForegroundColorSpan(getResources().getColor(R.color.colorSecondaryText)),
                    new RelativeSizeSpan(0.8f),
                    modelName,
                    new ForegroundColorSpan(getResources().getColor(R.color.colorSecondaryText)),
                    new RelativeSizeSpan(0.8f),
                    new StyleSpan(android.graphics.Typeface.BOLD));
            ((TextView) findViewById(R.id.ServerState)).setText(sb);
            ((ImageView) findViewById(R.id.ServerStateIcon)).setImageResource(R.drawable.ic_swap_horiz_black_24dp);
        } else {
            Spannable sb = buildText("Server : ",
                    "Not connected",
                    new ForegroundColorSpan(getResources().getColor(R.color.colorWarning)),
                    new StyleSpan(android.graphics.Typeface.BOLD));
            ((TextView) findViewById(R.id.ServerState)).setText(sb);
            ((ImageView) findViewById(R.id.ServerStateIcon)).setImageResource(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp);
        }
    }

    /**
     * Updates the view about the mode (Calibration or location)
     * @param mode (Mode) The selected mode
     */
    public void setMode(Mode mode) {
        if (mode == Mode.CALIBRATION) {
            actualMode = Mode.CALIBRATION;
            Button mainBtn = (Button) findViewById(R.id.MainBtn);
            mainBtn.setText("       " + getString(R.string.main_btn_calibration));
            mainBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_gps_fixed_accent_24dp, 0, 0, 0);
            ((ImageView) findViewById(R.id.MainImageBg)).setImageResource(R.drawable.bg_main_calibration);
        }
        if (mode == Mode.LOCATION) {
            actualMode = Mode.LOCATION;
            Button mainBtn = (Button) findViewById(R.id.MainBtn);
            mainBtn.setText("     " + getString(R.string.main_btn_location));
            mainBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_room_black_24dp, 0, 0, 0);
            ((ImageView) findViewById(R.id.MainImageBg)).setImageResource(R.drawable.bg_main_location);
        }
    }

    /**
     * Do a request to ask the server configuration
     * According to the respond this method will set de mode and update the view
     */
    public void getServerParameter(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.getFullUrl(this, Constants.SERVER_GET_PARAMETERS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response.substring(response.indexOf("{"),response.indexOf("}")+1));
                            if(obj.getBoolean("isInCalibrationMode"))
                                setMode(Mode.CALIBRATION);
                            else
                                setMode(Mode.LOCATION);

                            setServerState(obj.getString("computationModel"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setServerState(null);
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Sends a ping request on the server (OnClick method)
     * @param v
     */
    public void sendPing(View v) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constants.getFullUrl(this, Constants.SERVER_PING);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Launch other activity (OnClick method)
     * @param v
     */
    public void startMode(View v) {
        if(actualMode == Mode.LOCATION){
            Intent i = new Intent(this, PositioningActivity.class);
            i.putExtra("ComputationModel",computationModel);
            this.startActivity(i);
        }
        if(actualMode == Mode.CALIBRATION)
            this.startActivity(new Intent(this, CalibrationActivity.class));
    }


    /**
     * Helper method who build automatically a Spannable form parameters
     * @param params (Object )
     * @return The Spannable
     */
    protected Spannable buildText(Object ...params){
        String finalStr = "";
        for(Object p : params){
            if(p instanceof String)
                finalStr += p;
        }
        Spannable sb = new SpannableString(finalStr);
        String prevStr = "";
        for(Object p : params){
            if(p instanceof String)
                prevStr = (String) p;
            else {
                sb.setSpan(p, finalStr.indexOf(prevStr), finalStr.indexOf(prevStr)+ prevStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // add Style
            }
        }
        return sb;
    }

}
