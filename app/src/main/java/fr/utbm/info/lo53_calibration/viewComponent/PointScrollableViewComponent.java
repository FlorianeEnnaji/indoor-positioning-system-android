package fr.utbm.info.lo53_calibration.viewComponent;

import android.graphics.Canvas;
import android.graphics.Paint;

import fr.utbm.info.lo53_calibration.view.ScrollableViewComponent;

/**
 * ScrollableViewComponent for a point
 * Draw a circle on screen
 */
public class PointScrollableViewComponent extends ScrollableViewComponent {
    protected Paint paint = new Paint();
    protected float posX = 0.0f;
    protected float posY = 0.0f;
    protected float diameter = 15.0f;

    public PointScrollableViewComponent(int color, float mSize){
        paint.setColor(color);
        diameter = mSize;
    }
    public PointScrollableViewComponent(float mSize){
        diameter = mSize;
    }
    public PointScrollableViewComponent(int color){
        paint.setColor(color);
    }
    public PointScrollableViewComponent(){}


    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(posX, posY, diameter /2, paint);
    }

    public void setPos(float mPosX, float mPosY){
        posX = mPosX;
        posY = mPosY;
        requestUpdate();
    }
    public void setDiameter(float mSize){
        diameter = mSize;
        requestUpdate();
    }
}
