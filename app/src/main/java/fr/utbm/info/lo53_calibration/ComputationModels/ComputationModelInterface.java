package fr.utbm.info.lo53_calibration.ComputationModels;

import android.content.Context;

/**
 * Interface implemented by all class who want to use a computation model
 */
public interface ComputationModelInterface{
    void setLocation(int x, int y );
    Context provideContext();
}
