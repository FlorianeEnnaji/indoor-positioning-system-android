package fr.utbm.info.lo53_calibration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.WindowManager;

import fr.utbm.info.lo53_calibration.ComputationModels.ComputationModel;
import fr.utbm.info.lo53_calibration.ComputationModels.ComputationModelInterface;
import fr.utbm.info.lo53_calibration.ComputationModels.SingleValue;
import fr.utbm.info.lo53_calibration.view.ScrollableView;
import fr.utbm.info.lo53_calibration.viewComponents.ImageScrollableViewComponent;
import fr.utbm.info.lo53_calibration.viewComponents.PointScrollableViewComponent;

/**
 * Activity used to locate the user
 */
public class PositioningActivity  extends AppCompatActivity implements ComputationModelInterface {

    private ScrollableView scrollableView;
    private PointScrollableViewComponent target;
    Thread thread;
    ComputationModel model = new SingleValue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent myIntent = getIntent(); // gets the previously created intent
        String computationModel = myIntent.getStringExtra("ComputationModel");
        Class dynamicModelClass = null;
        try {
            dynamicModelClass = Class.forName( "fr.utbm.info.lo53_calibration.ComputationModels." + computationModel);
        } catch( ClassNotFoundException e ) {
            displayMsgBox("Computation model error !", buildText( "The computation model used by the server (",
                    computationModel,
                    new StyleSpan(android.graphics.Typeface.BOLD),
                    ") is not supported by this application\n\nAs fallback it will use the ",
                    "SingleValue",
                    new StyleSpan(android.graphics.Typeface.BOLD),
                    " model"));
        }

        try {
            if(dynamicModelClass != null)
                model = (ComputationModel) dynamicModelClass.getDeclaredConstructor(ComputationModelInterface.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


        scrollableView = (ScrollableView) findViewById(R.id.map_viewCalibration);

        ImageScrollableViewComponent map = new ImageScrollableViewComponent(getResources(), R.drawable.no_padding_map);
        scrollableView.addComponents(map);
        target = new PointScrollableViewComponent(Color.RED, 30.0f);
        scrollableView.addComponents(target);

        thread = new Thread() {

            private boolean running = true;

            public void run() {
                while(this.running) {
                    try {
                        sleep(model.getWaitingTime());
                    } catch (InterruptedException e) {
                        this.running = false;
                    }
                    model.locateMe();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        System.out.println("PAUSED");
        thread.interrupt();
    }


    public void displayMsgBox(String title, Spannable msg){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.ic_report_problem_red_24dp)
                .show();
    }


    /**
     * Helper method who build automatically a Spannable form parameters
     * @param params (Object )
     * @return The Spannable
     */
    protected Spannable buildText(Object ...params){
        String finalStr = "";
        for(Object p : params){
            if(p instanceof String)
                finalStr += p;
        }
        Spannable sb = new SpannableString(finalStr);
        String prevStr = "";
        for(Object p : params){
            if(p instanceof String)
                prevStr = (String) p;
            else {
                sb.setSpan(p, finalStr.indexOf(prevStr), finalStr.indexOf(prevStr)+ prevStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // add Style
            }
        }
        return sb;
    }

    @Override
    public void setLocation(int x, int y) {
        target.setPos(x, y);
        scrollableView.setViewTo(x, y);
    }

    @Override
    public Context provideContext() {
        return this;
    }
}
