package fr.utbm.info.lo53_calibration;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @file Constants.java
 * @brief Main constants of the application
 * @date June 3, 2016
 */
public class Constants{
    public static final String SERVER_GET_PARAMETERS = "/api/";
    public static final String SERVER_PING = "/api/ping";
    public static final String SERVER_GET_LOCATION_URL ="/api/locate-me";
    public static final String SERVER_SEND_CALIBRATION_URL ="/api/send-probe";

    /**
     * @brief Function that gets the full url to make a request from the path on the server
     * @param ctx (Context) the context of the application
     * @param path (String) the final path on the server
     */
    public static String getFullUrl(Context ctx, String path){ // get the base url from the preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String host = sharedPref.getString(ctx.getString(R.string.pref_key_server_host), "192.168.1.1");
        String proto = sharedPref.getString(ctx.getString(R.string.pref_key_server_proto), "HTTP");
        String port = sharedPref.getString(ctx.getString(R.string.pref_key_server_port), "8090");

        return proto.toLowerCase() + "://" + host + ":" + port + path;
    }
}
