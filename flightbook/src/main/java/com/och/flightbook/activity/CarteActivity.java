package com.och.flightbook.activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.och.flightbook.R;
import com.och.flightbook.model.GpsPosition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class CarteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;
    private LatLng myLatLng;
    private GpsPosition gpsPosition = new GpsPosition(0, 0, 0);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carte);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init position
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gpsPosition = (GpsPosition) bundle.getSerializable(GpsPosition.class.getName());
        }
        myLatLng = new LatLng(gpsPosition.getLat(), gpsPosition.getLon());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            //uiSettings.setMapToolbarEnabled(true);
            uiSettings.setCompassEnabled(true);

            mMap.setMapType(mapType);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));

            // Add marker for current position
            mMap.addMarker(new MarkerOptions()
                    .position(myLatLng)
                    .title(myLatLng.toString())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.punaise)));


            // Setting a custom info window adapter for the google map
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.map_windowlayout, null);

                    // Getting the position from the marker
                    LatLng latLng = arg0.getPosition();

                    // Getting reference to the TextView to set latitude
                    TextView tvPosition = (TextView) v.findViewById(R.id.tv_position);

                    // Setting the latitude
                    tvPosition.setText(arg0.getTitle());

                    // Getting reference to the TextView to set longitude
                    TextView tv100 = (TextView) v.findViewById(R.id.tv_100);
                    TextView tv250 = (TextView) v.findViewById(R.id.tv_250);
                    TextView tv500 = (TextView) v.findViewById(R.id.tv_500);
                    tv100.setTextColor(Color.GREEN);
                    tv250.setTextColor(Color.BLUE);
                    tv500.setTextColor(Color.RED);

                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });

            // Add circles
            addCircles();

            //Move the camera instantly to position with a zoom of 15.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));

            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }


    @Override
    public void onBackPressed() {
        // Nothings
    }


    private void addCircles() {
        CircleOptions circleOptions100 = new CircleOptions()
                .center(myLatLng)
                .radius(100)
                .strokeColor(Color.GREEN)
                .strokeWidth(2);
        mMap.addCircle(circleOptions100);

        CircleOptions circleOptions250 = new CircleOptions()
                .center(myLatLng)
                .radius(250)
                .strokeColor(Color.BLUE)
                .strokeWidth(2);
        mMap.addCircle(circleOptions250);

        CircleOptions circleOptions500 = new CircleOptions()
                .center(myLatLng)
                .radius(500)
                .strokeColor(Color.RED)
                .strokeWidth(2);
        mMap.addCircle(circleOptions500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_style_menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.normal_map:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                mMap.setMapType(mapType);
                break;
            case R.id.satellite_map:
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                mMap.setMapType(mapType);
                break;
            case R.id.terrain_map:
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                mMap.setMapType(mapType);
                break;
            case R.id.hybrid_map:
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                mMap.setMapType(mapType);
                break;
        }
        return false;
    }


}