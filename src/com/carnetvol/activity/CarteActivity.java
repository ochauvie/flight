package com.carnetvol.activity;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.carnetvol.R;
import com.carnetvol.overlay.CarteItemizedOverlay;


public class CarteActivity extends MapActivity {

	private MapController myMapController;
	private int latitude;
	private int longitude; 
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_carte);
	    
	    // Init position
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
        	latitude = (int) bundle.getDouble("latitude");
        	longitude = (int) bundle.getDouble("longitude");
        }
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	
	    mapView.setSatellite(true); //Set satellite view
	    myMapController = mapView.getController();
	    myMapController.setZoom(20); //Fixed Zoom Level
	    
	    GeoPoint point = new GeoPoint(latitude, longitude);
	    myMapController.animateTo(point);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.punaise);
	    CarteItemizedOverlay itemizedoverlay = new CarteItemizedOverlay(drawable,this);
	    
	    String txt = getString(R.string.longitude) + ": " + String.valueOf((float)point.getLongitudeE6()/1000000) 
	    		+ "\n" + getString(R.string.latitude) + ": "+ String.valueOf((float)point.getLatitudeE6()/1000000);
	    OverlayItem overlayitem = new OverlayItem(point, getString(R.string.ici), txt);

	    itemizedoverlay.addOverlay(overlayitem);
	    
	    mapOverlays.add(itemizedoverlay);
	
	   
	}

}
