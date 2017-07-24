package in.skylinelabs.digiPune.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.skylinelabs.digiPune.R;

public class FindGov extends ActionBarActivity implements android.location.LocationListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback {


    private Toolbar mToolbar;

    MaterialSheetFab materialSheetFab;

    ListView listView;
    String categorys;

    GoogleMap map;
    public static int i;

    ProgressBar prgbr, marker;

    TextView txt1, txt2, textView3, textView4;

    final Context context = this;

    String mystringuername;
    String mystringpassword;



    String post;
    //flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Boolean catopen = false;

    Location location;
    double latitude;
    double longitude;

    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;//0 metters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; // 1/2 minute

    //Declaring a Location Manager
    protected LocationManager locationManager;


    FloatingActionButton  btn2;

    Marker[] mrkgrp = new Marker[1000000];


    JSONParser jsonParser = new JSONParser();
    private static final String url_update_product = "http://www.skylinelabs.in/Geo/get_location.php";
    private static final String url_get_category = "http://www.skylinelabs.in/Geo/fetch_category.php";


    AlertDialog alertDialog;


    Fab btn1;


    View.OnClickListener snackaction,snackaction1;;




    @Override
    protected void onStop() {

        super.onStop();  // Always call the superclass method first


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_gov);

        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };



        getLocation();

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText("Loading Map");
        textView3.setVisibility(View.VISIBLE);

        prgbr = (ProgressBar) findViewById(R.id.progressBar1);
        prgbr.setVisibility(View.VISIBLE);

        textView4 = (TextView) findViewById(R.id.textView4);
        marker = (ProgressBar) findViewById(R.id.progressBar1);
        textView4.setVisibility(View.GONE);
        marker.setVisibility(View.GONE);


        map = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);




        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            public void onMapLoaded() {
                prgbr.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                final Marker mk = marker;

                snackaction1 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent callvictim = new Intent(Intent.ACTION_CALL);
                        callvictim.setData(Uri.parse("tel:" + mk.getSnippet()));
                        startActivity(callvictim);
                    }
                };

                Snackbar.make(findViewById(android.R.id.content), "Call " + marker.getSnippet() + " ?", Snackbar.LENGTH_LONG)
                        .setAction("Call", snackaction1)
                        .setActionTextColor(Color.WHITE)
                        .show();

                return false;
            }
        });


        map.setOnCameraChangeListener(this);
        map.setOnMapLoadedCallback(FindGov.this);

        CameraPosition camPos = new CameraPosition.Builder()

                .target(new LatLng(getLatitude(), getLongitude()))

                .zoom(12.8f)

                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        map.moveCamera(camUpdate);

        mystringuername = settings.getString("user_name", "");
        mystringpassword = settings.getString("password", "");

        txt1 = (TextView) findViewById(R.id.textView1);
        txt2 = (TextView) findViewById(R.id.textView2);

        btn1 = (Fab)findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });

        /***************************************************************************************************************/

        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.white);
        int fabColor = getResources().getColor(R.color.colorPrimary);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(btn1, sheetView, overlay, sheetColor, fabColor);

        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {

                catopen = true;
                // Called when sheet is shown
            }

            @Override
            public void onHideSheet() {

                catopen = false;
                // Called when sheet is hidden
            }
        });


        listView = (ListView) findViewById(R.id.fab_list);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                categorys = (String) parent.getItemAtPosition(position);
                materialSheetFab.hideSheet();
                if(categorys != "PMPML")
                {

                }

                else {
                    prgbr.setVisibility(View.VISIBLE);
                    map.clear();
                    new GetCategory().execute();
                }

            }
        });

/***************************************************************************************************************/


        btn2 = (FloatingActionButton)findViewById(R.id.location);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CameraPosition camPos = new CameraPosition.Builder()

                        .target(new LatLng(getLatitude(), getLongitude()))

                        .zoom(12.8f)

                        .build();
                // Toast.makeText(getApplicationContext(),getLatitude(),Toast.LENGTH_LONG).show();

                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                map.moveCamera(camUpdate);

            }

        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_gov, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.info) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    FindGov.this);
            LayoutInflater factory = LayoutInflater.from(FindGov.this);

            final View view = factory.inflate(R.layout.dialog_main, null);

            ImageView image = (ImageView) view.findViewById(R.id.imageView);
            image.setImageResource(R.drawable.friendlocation_help);

            alertadd.setView(view);


            alertadd.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });
            alertDialog =  alertadd.create();
            alertDialog.show();


        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        if(!catopen) {
            finish();
        }

        else
        {
            materialSheetFab.hideSheet();
        }

    }
    @Override
    public void onStatusChanged(String provider, int status,
                                Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // .makeText(getApplicationContext(),"Please enable internet",Toast.LENGTH_LONG).show();

        Snackbar.make(findViewById(android.R.id.content),   "Please enable location", Snackbar.LENGTH_LONG)
                .setAction("Ok", snackaction)
                .setActionTextColor(Color.WHITE)
                .show();

    }
    @Override
    public void onCameraChange(CameraPosition arg0) {
        // TODO Auto-generated method stub
        map.setOnMapLoadedCallback(FindGov.this);
        map.setOnCameraChangeListener(FindGov.this);
        textView3.setVisibility(View.VISIBLE);
        prgbr.setVisibility(View.VISIBLE);
    }



    @Override
    public void onMapLoaded() {

        prgbr.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
    }

    @Override
    public void onLocationChanged(Location arg0) {

        map.setOnMapLoadedCallback(FindGov.this);
        //map.clear();
        new GetCategoryUpdate().execute();


    }

    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager)this.getBaseContext().getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled)
            {
                // no network provider is enabled
            }
            else
            {
                this.canGetLocation = true;

                //First get location from Network Provider
                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");

                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateGPSCoordinates();
                    }
                }

                //if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateGPSCoordinates();
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.e("Error : Location", "Impossible to connect to LocationManager", e);
        }

        return location;
    }


    public void updateGPSCoordinates()
    {
        if (location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS()
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(FindGov.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude()
    {
        if (location != null)
        {
            latitude = location.getLatitude();

        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude()
    {
        if (location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     */


    class GetCategory extends AsyncTask<String, String, String> {

        JSONObject json2;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category", categorys));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_get_category, "GET", params);

                }

                catch (Exception e1) {
                    // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                    return null;
                }

            }


            ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent1 = cd1.isConnectingToInternet();

            if(isInternetPresent1)
            {
                runOnUiThread(new Runnable() {
                    //
                    @Override
                    public void run() {

                        JSONArray values = null;
                        try {
                            values = json2.getJSONArray("result");
                            final int numberOfItemsInResp = values.length();

                            if(numberOfItemsInResp == 0)
                            {
                                Snackbar.make(findViewById(android.R.id.content), "No digiPune user found belonging to the selected category", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                            }


                            for (int i = 0; i < numberOfItemsInResp; i++)
                            {
                                JSONObject coordinates = values.getJSONObject(i);
                                Log.e("Values bagh", coordinates.toString());

                                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(10, 10));
                                mrkgrp[i] =   map.addMarker(markerOption);
                                mrkgrp[i].setVisible(false);
                                mrkgrp[i].setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrkgrp[i].setTitle(coordinates.getString("user_name") + "\n" + "(Last seen :  " + coordinates.getString("time") + ")");
                                mrkgrp[i].setSnippet(coordinates.getString("contact"));
                                mrkgrp[i].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrkgrp[i].setVisible(true);

                            }
                            prgbr.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("Error in markers", e.toString());
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                        }
                    }
                });
            }
            return null;
        }
    }


    class GetCategoryUpdate extends AsyncTask<String, String, String> {

        JSONObject json2;
        Boolean updated = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category", categorys));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_get_category, "GET", params);
                    updated = true;

                } catch (Exception e1) {
                    // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                    updated = false;
                    return null;
                }

            }


            ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent1 = cd1.isConnectingToInternet();

            if (isInternetPresent1 && updated) {
                runOnUiThread(new Runnable() {
                    //
                    @Override
                    public void run() {

                        JSONArray values = null;
                        try {
                            values = json2.getJSONArray("result");
                            final int numberOfItemsInResp = values.length();

                            map.clear();


                            for (int i = 0; i < numberOfItemsInResp; i++) {
                                JSONObject coordinates = values.getJSONObject(i);
                                Log.e("Values bagh", coordinates.toString());

                                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(10, 10));
                                mrkgrp[i] = map.addMarker(markerOption);
                                mrkgrp[i].setVisible(false);
                                mrkgrp[i].setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrkgrp[i].setTitle(coordinates.getString("user_name") + "\n" + "(Last seen :  " + coordinates.getString("time") + ")");
                                mrkgrp[i].setSnippet(coordinates.getString("contact"));
                                mrkgrp[i].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrkgrp[i].setVisible(true);

                            }
                            prgbr.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("Error in markers", e.toString());
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                        }
                    }
                });
            }
            return null;
        }
    }

}


