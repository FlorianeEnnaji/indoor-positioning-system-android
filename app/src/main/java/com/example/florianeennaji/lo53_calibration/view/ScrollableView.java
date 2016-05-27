package com.example.florianeennaji.lo53_calibration.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 * This class is a scrollable and zoomable view where components can be added dynamically
 */
public class ScrollableView extends ImageView {

    private static final int INVALID_POINTER_ID = -1;
    private ScaleGestureDetector mScaleDetector;
    private float scaleFactor = 1.f;
    private float offsetX;
    private float offsetY;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ArrayList<ScrollableViewComponent> components = new ArrayList<>();

    private float minScale = 0.65f;
    private float maxScale = 3.5f;

    public ScrollableView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }
    public ScrollableView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle);}
    public ScrollableView(Context context){
        super(context);
    }

    /**
     * Sets the view to a given position
     * @param posX (float) the x coordinate
     * @param posY (float) the y coordinate
     * @param scale (float) the new scale, if the given scale is below zero it will not change the current scale
     */
    public void setViewTo(float posX, float posY, float scale){
        offsetX = -posX * scaleFactor + getWidth()/2;
        offsetY = -posY * scaleFactor + getHeight()/2;
        if(scale > 0)
            scaleFactor = Math.max(minScale, Math.min(scale, maxScale));

        invalidate();
    }
    /**
     * Sets the view to a given position without touching the scale
     * @param posX (float) the x coordinate
     * @param posY (float) the y coordinate
     */
    public void setViewTo(float  posX, float posY){
        setViewTo(posX, posY, -1.0f);
    }

    /**
     * Adds a component to the view
     * @param component (ScrollableViewComponent) the component we want to add
     */
    public void addComponents(ScrollableViewComponent component){
        component.bindView(this);
        components.add(component);
    }

    /**
     * Gets the real position on the view with a given position on the screen
     * @param posX
     * @param posY
     * @return (float[2])
     */
    public float[] getRealPos(float posX, float posY){
        float realPos[] = new float[2];
        realPos[0] = (posX - offsetX)/ scaleFactor;
        realPos[1] = (posY - offsetY)/ scaleFactor;
        return realPos;
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(offsetX, offsetY); // apply the offset
        canvas.scale(scaleFactor, scaleFactor); // apply the scale

        // onDraw() code from all components
        for (ScrollableViewComponent component : components) {
            component.onDraw(canvas);
        }

        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                //System.out.println("ACTION_DOWN, x: " + ev.getX() + " Y: " + ev.getY());
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                //System.out.println("ACTION_MOVE, x: " + ev.getX() + " Y: " + ev.getY());
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    offsetX += dx;
                    offsetY += dy;

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
               // System.out.println("ACTION_POINTER_UP");
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float posX = ( getWidth()/2 - offsetX)/ scaleFactor;
            float posY = ( getHeight()/2 - offsetY)/ scaleFactor;

            scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(minScale, Math.min(scaleFactor, maxScale));

            offsetX = -posX * scaleFactor + getWidth()/2; // set the screen on the same location before the scale
            offsetY = -posY * scaleFactor + getHeight()/2;

            invalidate();
            return true;
        }
    }





}


