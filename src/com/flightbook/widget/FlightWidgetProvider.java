package com.flightbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.flightbook.R;
import com.flightbook.activity.SplashActivity;
import com.flightbook.model.Vol;
import com.flightbook.sqllite.DbVol;

import java.util.ArrayList;

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

            // Create an Intent to launch application
            Intent intent = new Intent(context, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Get the layout for the App Widget
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);
            views.setTextViewText(R.id.widgetText, getWidgetText());
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    private String getWidgetText() {
        int total = 0;
        int nbVols = 0;
        if (vols!=null) {
            nbVols = vols.size();
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
        return String.valueOf(heu + "h" + sMin + " " + nbVols + " vols");
    }


}
