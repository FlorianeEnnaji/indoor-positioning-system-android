package com.example.florianeennaji.lo53_calibration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void startCalibration(View v) {
        this.startActivity(new Intent(this, WiFiActivity.class));
        this.finish();
    }
}
