package com.example.florianeennaji.lo53_calibration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * Created by florianeennaji on 19/05/16.
 */
public class CalibrationActivity extends AppCompatActivity implements OnTouchListener {

    private ImageView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        mapView = (ImageView) findViewById(R.id.map_view);
        mapView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int[] viewCoords = new int[2];
        view.getLocationOnScreen(viewCoords);
        int touchX = (int) motionEvent.getX();
        int touchY = (int) motionEvent.getY();

        int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X coordinate
        int imageY = touchY - viewCoords[1]; // viewCoords[1] is the y coordinate

        System.out.println(imageX + ", " + imageY);
        return false;
    }
}
