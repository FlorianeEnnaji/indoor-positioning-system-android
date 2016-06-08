package fr.utbm.info.lo53_calibration.viewComponents;

import android.graphics.Canvas;
import android.graphics.Paint;

import fr.utbm.info.lo53_calibration.view.ScrollableViewComponent;

/**
 * @file PointScrollableViewComponent.java
 * @brief ScrollableViewComponent for a point that draws a circle on the screen
 * @date June 3, 2016
 * @see fr.utbm.info.lo53_calibration.view.ScrollableViewComponent
 */
public class PointScrollableViewComponent extends ScrollableViewComponent {

    /**paint (Paint)*/
    protected Paint paint = new Paint();
    /**posX (float)*/
    protected float posX = 0.0f;
    /**posY (float)*/
    protected float posY = 0.0f;
    /**diameter (float)*/
    protected float diameter = 15.0f;

    /**
     * @brief constructor with color and diameter
     * @param color (int) the color of our circle
     * @param mSize (float) the diameter of our circle
     */
    public PointScrollableViewComponent(int color, float mSize){
        paint.setColor(color);
        diameter = mSize;
    }
    /**
     * @brief constructor with diameter only
     * @param mSize (float) the diameter of our circle
     */
    public PointScrollableViewComponent(float mSize){
        diameter = mSize;
    }
    /**
     * @brief constructor with color only
     * @param color (int) the color of our circle
     */
    public PointScrollableViewComponent(int color){
        paint.setColor(color);
    }
    /**
     * @brief empty constructor
     */
    public PointScrollableViewComponent(){}


    /**
     * Automatically called function
     * @brief draws circle on the canvas
     * @param canvas (Canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(posX, posY, diameter /2, paint);
    }

    /**
     * @brief sets position of our circle
     * @param mPosX the x position of the circle
     * @param mPosY  the y position of the circle
     */
    public void setPos(float mPosX, float mPosY){
        posX = mPosX;
        posY = mPosY;
        requestUpdate();
    }
    /**
     * @brief sets diameter of our circle
     * @param mSize (float) the diameter of the circle
     */
    public void setDiameter(float mSize){
        diameter = mSize;
        requestUpdate();
    }
}
