package fr.utbm.info.lo53_calibration.view;

import android.graphics.Canvas;

/**
 * @file ScrollableViewComponent.java
 * @brief Base class for components whhich are added to the Scrollable view
 * @date June 3, 2016
 */
public class ScrollableViewComponent {
    private ScrollableView attachedScrollableView = null;

    /**
     * @brief This method will be to be overriden by subclass
     */
    public void onDraw(Canvas canvas){ }

    /**
     * @brief Called by the Scrollable view when the component is added
     * @param view (ScrollableView)
     */
    public void bindView(ScrollableView view){
        attachedScrollableView = view;
    }

    /**
     * @brief Called when the component need to by updated on screen
     */
    public void requestUpdate(){
        if(attachedScrollableView != null)
            attachedScrollableView.invalidate();
    }

}
