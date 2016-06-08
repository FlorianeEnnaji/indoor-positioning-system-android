package fr.utbm.info.lo53_calibration;

import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 * @file SettingsActivity.java
 * @brief Setting Activity that manages the Settings View where the user can change his preferences
 * @date June 3, 2016
 * @see android.preference.PreferenceActivity
 */
public class SettingsActivity extends PreferenceActivity {

    /**
     * Automatically called function when we create Settings Activity
     * @brief Initializes the settings
     * @param savedInstanceState (Bundle) the instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
