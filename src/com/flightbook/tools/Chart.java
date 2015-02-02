package com.flightbook.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;

import com.flightbook.model.Aeronef;
import com.flightbook.model.Vol;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by o.chauvie on 02/02/2015.
 * See more at: http://www.achartengine.org/index.html
 */
public class Chart {

    public static final String CHART_TIME = "TIME";
    public static final String CHART_NB = "NB";

    private Context context;
    private ArrayList<Vol> vols;
    private String chartType;
    private String chartTitle;

    public Chart(Context context, ArrayList<Vol> vols, String chartType, String chartTitle) {
        this.context = context;
        this.vols = vols;
        this.chartType = chartType;
        this.chartTitle = chartTitle;
    }

    /**
     * Return pie chart
     * @return
     */
    public Intent getIntentPieChart() {
        CategorySeries distributionSeries = getDistributionSeriesForPieChart();

        // Instantiating a renderer for the Pie Chart
        DefaultRenderer defaultRenderer  = new DefaultRenderer();
        for(int i = 0 ;i<distributionSeries.getItemCount();i++){
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            seriesRenderer.setColor(Aeronef.getColor(distributionSeries.getCategory(i)));
            seriesRenderer.setDisplayChartValues(true);

            // Adding a renderer for a slice
            defaultRenderer.addSeriesRenderer(seriesRenderer);
        }

        defaultRenderer.setChartTitle("Temps (min)");
        defaultRenderer.setChartTitleTextSize(25);
        defaultRenderer.setZoomButtonsVisible(true);
        defaultRenderer.setShowLabels(false);
        defaultRenderer.setLabelsTextSize(25);
        defaultRenderer.setLegendTextSize(25);
        defaultRenderer.setDisplayValues(true);

        return ChartFactory.getPieChartIntent(context, distributionSeries, defaultRenderer, chartTitle);
    }


    /**
     * Retrun bar chart
     * @return
     */
    public Intent getIntentChartByMachine() {

        // Instantiating a renderer
        XYMultipleSeriesRenderer defaultRenderer  = new XYMultipleSeriesRenderer ();
        defaultRenderer.setZoomButtonsVisible(false);
        defaultRenderer.setLabelsTextSize(25);
        defaultRenderer.setShowLegend(false);
        defaultRenderer.setMargins(new int[]{20, 30, 15, 0});
        defaultRenderer.setBarSpacing(0.1);
        defaultRenderer.setYAxisMin(0);
        defaultRenderer.setYLabelsAlign(Paint.Align.LEFT);
        defaultRenderer.setXAxisMin(0);
        defaultRenderer.setXLabels(0);
        defaultRenderer.setBackgroundColor(Color.BLACK);
        defaultRenderer.setApplyBackgroundColor(true);
        defaultRenderer.setShowGrid(true);


        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        if (Chart.CHART_TIME.equals(chartType)) {
            r.setColor(Color.rgb(219,23,2));
        } else {
            r.setColor(Color.rgb(31,160,85));
        }
        r.setDisplayChartValues(true);
        r.setChartValuesTextSize(25);
        r.setChartValuesTextAlign(Paint.Align.CENTER);
        defaultRenderer.addSeriesRenderer(r);

        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
        CategorySeries series = new CategorySeries("");
        Hashtable hashVols = new Hashtable();

        if (vols!=null) {
            for (Vol vol : vols) {
                String aeronef = vol.getAeronef();
                int value = 0;
                if (Chart.CHART_TIME.equals(chartType)) {
                    value =  vol.getMinutesVol();
                } else {
                    value++;
                }
                if (hashVols.containsKey(aeronef)) {
                    int old = (Integer) hashVols.get(aeronef);
                    hashVols.put(aeronef, value + old);

                } else {
                    hashVols.put(aeronef, value);
                }
            }
        }

        Iterator iter = hashVols.keySet().iterator();
        int i=0;
        int maxY = 0;
        while (iter.hasNext())
        {
            String clef = (String)iter.next();
            int value = (Integer) hashVols.get(clef);
            if (value>maxY) {
                maxY = value;
            }
            series.add(value);
            i++;
            defaultRenderer.addXTextLabel(i, clef);
            defaultRenderer.setXLabelsAngle(45);
        }
        defaultRenderer.setXAxisMax(hashVols.size()+1);
        defaultRenderer.setYAxisMax(maxY + 10*maxY/100);
        dataSet.addSeries(series.toXYSeries());

        // Creating an intent to plot bar chart using dataset and multipleRenderer
        return ChartFactory.getBarChartIntent(context, dataSet, defaultRenderer, BarChart.Type.DEFAULT, chartTitle);
    }


    private CategorySeries getDistributionSeriesForPieChart() {
        CategorySeries distributionSeries = new CategorySeries(" Vols ");
        double dataPlaneur = 0;
        double dataAvion = 0;
        double dataParamoteur = 0;
        double dataHelico = 0;
        double dataAuto = 0;
        double dataDivers = 0;
        double dataAutre = 0;

        if (vols!=null) {
            for (Vol vol : vols) {
                String type = vol.getType();
                if (Aeronef.T_PLANEUR.equals(type)) {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataPlaneur = dataPlaneur + vol.getMinutesVol();
                    } else {
                        dataPlaneur++;
                    }
                } else if (Aeronef.T_AVION.equals(type)) {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataAvion = dataAvion + vol.getMinutesVol();
                    } else {
                        dataAvion++;
                    }
                } else if (Aeronef.T_PARAMOTEUR.equals(type)) {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataParamoteur = dataParamoteur + vol.getMinutesVol();
                    } else {
                        dataParamoteur++;
                    }

                } else if (Aeronef.T_HELICO.equals(type)) {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataHelico = dataHelico + vol.getMinutesVol();
                    } else {
                        dataHelico++;
                    }
                } else if (Aeronef.T_AUTO.equals(type)) {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataAuto = dataAuto + vol.getMinutesVol();
                    } else {
                        dataAuto++;
                    }
                } else if (Aeronef.T_DIVERS.equals(type)) {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataDivers = dataDivers + vol.getMinutesVol();
                    } else {
                        dataDivers++;
                    }
                } else {
                    if (Chart.CHART_TIME.equals(chartType)) {
                        dataAutre = dataAutre + vol.getMinutesVol();
                    } else {
                        dataAutre++;
                    }
                }
            }
        }

        if (dataPlaneur>0) {
            distributionSeries.add(Aeronef.T_PLANEUR, dataPlaneur);
        }
        if (dataAvion>0) {
            distributionSeries.add(Aeronef.T_AVION, dataAvion);
        }
        if (dataParamoteur>0) {
            distributionSeries.add(Aeronef.T_PARAMOTEUR, dataParamoteur);
        }
        if (dataHelico>0) {
            distributionSeries.add(Aeronef.T_HELICO, dataHelico);
        }
        if (dataAuto>0) {
            distributionSeries.add(Aeronef.T_AUTO, dataAuto);
        }
        if (dataDivers>0) {
            distributionSeries.add(Aeronef.T_DIVERS, dataDivers);
        }
        if (dataAutre>0) {
            distributionSeries.add("Inconnu", dataAutre);
        }
        return distributionSeries;
    }




}
