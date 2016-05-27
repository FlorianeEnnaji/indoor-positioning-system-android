package com.example.florianeennaji.lo53_calibration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.florianeennaji.lo53_calibration.view.ScrollableView;
import com.example.florianeennaji.lo53_calibration.viewComponent.ImageScrollableViewComponent;
import com.example.florianeennaji.lo53_calibration.viewComponent.PointScrollableViewComponent;


/**
 * Example of a activity who use the Scrollable view
 */
public class ScrollableViewActivity extends AppCompatActivity implements OnTouchListener {

    private ScrollableView scrollableView;
    private PointScrollableViewComponent target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scrollable_view);
        scrollableView = (ScrollableView) findViewById(R.id.map_viewCalibration);

        ImageScrollableViewComponent map = new ImageScrollableViewComponent(getResources(), R.drawable.no_padding_map);
        scrollableView.addComponents(map);
        target = new PointScrollableViewComponent(20.0f);
        scrollableView.addComponents(target);

        scrollableView.setOnTouchListener(this);

        // ProgressDialog.show(this, "Loading", "Wait while loading...");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        float[] realCoords =  scrollableView.getRealPos(motionEvent.getX(),motionEvent.getY());
        target.setPos(realCoords[0], realCoords[1]);

      //  System.out.println("Real coordinates : " + realCoords[0] + ", " + realCoords[1]);
      //  System.out.println("given coordinates : " + motionEvent.getX() + ", " + motionEvent.getY());
        return false;
    }


    public  void setCenter(View v) {
        scrollableView.setViewTo(800.0f, 300.0f);
    }


}


