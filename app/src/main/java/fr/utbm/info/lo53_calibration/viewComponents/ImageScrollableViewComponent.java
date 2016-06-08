package fr.utbm.info.lo53_calibration.viewComponents;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import fr.utbm.info.lo53_calibration.view.ScrollableViewComponent;

/**
 * ScrollableViewComponent for a image
 * Use to draw a image on the view
 */
public class ImageScrollableViewComponent extends ScrollableViewComponent {

    private Bitmap img;

    public ImageScrollableViewComponent(Resources res,  int id){
        img = BitmapFactory.decodeResource(res, id);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(img, 0,0, null);
    }
}
