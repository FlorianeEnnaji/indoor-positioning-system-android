package fr.utbm.info.lo53_calibration.viewComponent;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import fr.utbm.info.lo53_calibration.view.ScrollableViewComponent;

/**
 * @file ImageScrollableViewComponent.java
 * @brief ScrollableViewComponent for am image that draws an image on the view
 * @date June 3, 2016
 * @see fr.utbm.info.lo53_calibration.view.ScrollableViewComponent
 */
public class ImageScrollableViewComponent extends ScrollableViewComponent {

    /**img (Bitmap)*/
    private Bitmap img;

    /**
     * @brief constructor with resources and id
     * @param id (int) the identifier
     * @param res (Resources) the resources
     */
    public ImageScrollableViewComponent(Resources res,  int id){
        img = BitmapFactory.decodeResource(res, id);
    }

    /**
     * Automatically called function
     * @brief draws image on the canvas
     * @param canvas (Canvas)
     */
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(img, 0,0, null);
    }
}
