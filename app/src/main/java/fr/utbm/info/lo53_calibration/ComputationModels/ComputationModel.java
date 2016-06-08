package fr.utbm.info.lo53_calibration.ComputationModels;

import android.content.Context;

/**
 * Basic abstract class for a computation model
 */
public abstract class ComputationModel {
    private ComputationModelInterface ctx;
    protected ComputationModel(ComputationModelInterface ctx ){
        this.ctx = ctx;
    }
    protected void onLocation(int x, int y){
        ctx.setLocation(x,y);
    };
    protected Context getContext(){
        return ctx.provideContext();
    };

    public abstract void locateMe();
    public abstract int getWaitingTime();
}
