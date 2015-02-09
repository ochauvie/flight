package com.flightbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.flightbook.R;
import com.flightbook.model.Vol;
import com.flightbook.sqllite.DbVol;

import java.util.ArrayList;

/**
 * Created by o.chauvie on 09/02/2015.
 */
public class FlightWidgetProvider extends AppWidgetProvider {

    private DbVol dbVol;
    private ArrayList<Vol> vols;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        dbVol = new DbVol(context);
        dbVol.open();
            vols = dbVol.getVols();
        dbVol.close();

        for(int i=0; i<appWidgetIds.length; i++){
            int appWidgetId = appWidgetIds[i];

        /*
            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ExampleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
         */

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

            views.setTextViewText(R.id.nbVol, getNbVol());
            views.setTextViewText(R.id.totalVol, getTotalVol());


            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);


        }
    }

    /**
     * Total heures de vol
     * @return
     */
    private String getTotalVol() {
        int total = 0;
        if (vols!=null) {
            for (Vol vol:vols) {
                total = total + vol.getMinutesVol();
            }
        }
        int heu = total/60;
        int min = total % 60;
        String sMin = String.valueOf(min);
        if (min<10) {
            sMin = "0" + sMin;
        }
        return String.valueOf(heu + "h" + sMin);
    }

    /**
     * Total nombre de vol
     * @return
     */
    private String getNbVol() {
        int total = 0;
        if (vols!=null) {
            total = vols.size();
        }

        return String.valueOf(total + " vols");
    }


}
