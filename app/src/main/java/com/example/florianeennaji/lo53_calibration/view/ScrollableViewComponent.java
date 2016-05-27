package com.example.florianeennaji.lo53_calibration.view;

import android.graphics.Canvas;

/**
 * Base class for components who can added to the Scrollable view
 */
public class ScrollableViewComponent {
    private ScrollableView attachedScrollableView = null;

    /**
     * This method need to be override by subclass
     */
    public void onDraw(Canvas canvas){ }

    /**
     * Call by the Scrolable view when the component is added
     * @param view (ScrollableView)
     */
    public void bindView(ScrollableView view){
        attachedScrollableView = view;
    }

    /**
     * Call when the component need to by updated on screen
     */
    public void requestUpdate(){
        if(attachedScrollableView != null)
            attachedScrollableView.invalidate();
    }

}
