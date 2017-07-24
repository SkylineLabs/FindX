package in.skylinelabs.digiPune.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.skylinelabs.digiPune.R;


public class Bus_Fetch extends ActionBarActivity implements android.location.LocationListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback {

    private ProgressDialog pDialog;


    private Toolbar mToolbar;

    MaterialSheetFab materialSheetFab;

    String BusNumber;

    String start, end;

    GoogleMap map;

    public static int i;

    ProgressBar prgbr, markerp;

    TextView txt1, txt2, textView3;

    long date;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final Context context = this;

    String post;
    //flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Boolean catopen = false;

    EditText toolbarSearchView;

    Location location;
    double latitude;
    double longitude;

    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;//0 metters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; // 1/2 minute

    //Declaring a Location Manager
    protected LocationManager locationManager;


    FloatingActionButton btn2;



    JSONParser jsonParser = new JSONParser();

    private static final String url_get_category = "http://www.skylinelabs.in/Geo/PMPML/routewise.php";


    AlertDialog alertDialog;

    Boolean searchByNumberb = false, searchByRoute = false;

    FloatingActionButton btn1;

    Marker[] mrkgrp = new Marker[1000000];

    View.OnClickListener snackaction, snackaction1;

    CustomAutoCompleteView myAutoComplete, myAutoComplete2;

    // adapter for auto-complete
    ArrayAdapter<String> myAdapter, myAdapter2;

    // for database operations
    DatabaseHandler databaseH;

    // just to add some initial value
    String[] item = new String[]{"Please search..."};


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

    public String[] getItemsFromDb(String searchTerm) {

        // add items on the array dynamically
        List<MyObject> products = databaseH.read(searchTerm);
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (MyObject record : products) {

            item[x] = record.objectName;
            x++;
        }

        return item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus__fetch);

        /**************************************************************************************/

        try {

        } catch (Exception e) {

        }

        /**************************************************************************************/

        try {

            // instantiate database handler
            databaseH = new DatabaseHandler(Bus_Fetch.this);

            // put sample data to database
            new CreateBusDatabase().execute();
            // autocompletetextview is in activity_main.xml
            myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete1);

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener1(this));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**************************************************************************************/

        /**************************************************************************************/

        try {

            myAutoComplete2 = (CustomAutoCompleteView) findViewById(R.id.myautocomplete2);
            // add the listener so it will tries to suggest while the user types
            myAutoComplete2.addTextChangedListener(new CustomAutoCompleteTextChangedListener2(this));
            // set our adapter
            myAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            myAutoComplete2.setAdapter(myAdapter2);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**************************************************************************************/


        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        toolbarSearchView = (EditText) findViewById(R.id.search_view);
        toolbarSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchByNumber();
                    InputMethodManager inputManager =
                            (InputMethodManager) context.
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            Bus_Fetch.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // display the first navigation drawer view on app launch

        getLocation();

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText("Loading Map");
        textView3.setVisibility(View.VISIBLE);

        prgbr = (ProgressBar) findViewById(R.id.progressBar1);
        prgbr.setVisibility(View.VISIBLE);

        markerp = (ProgressBar) findViewById(R.id.progressBar1);
        markerp.setVisibility(View.GONE);
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

                Snackbar.make(findViewById(android.R.id.content), "Estimated arrival time :", Snackbar.LENGTH_LONG)
                        .setAction("Call", snackaction1)
                        .setActionTextColor(Color.WHITE)
                        .show();

                return false;
            }
        });


        map.setOnCameraChangeListener(this);
        map.setOnMapLoadedCallback(Bus_Fetch.this);

        CameraPosition camPos = new CameraPosition.Builder()

                .target(new LatLng(getLatitude(), getLongitude()))

                .zoom(12.8f)

                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        map.moveCamera(camUpdate);


        txt1 = (TextView) findViewById(R.id.textView1);
        txt2 = (TextView) findViewById(R.id.textView2);

        btn1 = (FloatingActionButton) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent = cd.isConnectingToInternet();

                if(isInternetPresent) {
                    start = myAutoComplete.getText().toString();
                    end = myAutoComplete2.getText().toString();
                    if (end.equals(start) || start == null || end == null || start == "" || end == "") {
                        if (end.equals(start)) {
                            Snackbar.make(findViewById(android.R.id.content), "Start and End points cant be same", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();

                        } else if (start == null || start == "") {
                            Snackbar.make(findViewById(android.R.id.content), "Start point can't be empty", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();

                        } else if (end == null || end == "") {
                            Snackbar.make(findViewById(android.R.id.content), "End point can't be empty", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                        }
                    } else {
                        searchByNumberb = false;
                        searchByRoute = true;
                        new GetByRoute().execute();
                        map.clear();
                        markerp.setVisibility(View.VISIBLE);
                    }
                }
            }

        });

        btn2 = (FloatingActionButton) findViewById(R.id.location);
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
        getMenuInflater().inflate(R.menu.menu_bus__fetch, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.info) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    Bus_Fetch.this);
            LayoutInflater factory = LayoutInflater.from(Bus_Fetch.this);

            final View view = factory.inflate(R.layout.dialog_main, null);

            ImageView image = (ImageView) view.findViewById(R.id.imageView);
            image.setImageResource(R.drawable.friendlocation_help);

            alertadd.setView(view);


            alertadd.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dlg, int sumthin) {

                }
            });
            alertDialog = alertadd.create();
            alertDialog.show();
        }

        if (id == R.id.action_search) {

            searchByNumber();
            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    Bus_Fetch.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        if (!catopen) {
            finish();
        } else {
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

        Snackbar.make(findViewById(android.R.id.content), "Please enable location", Snackbar.LENGTH_LONG)
                .setAction("Ok", snackaction)
                .setActionTextColor(Color.WHITE)
                .show();

    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
        // TODO Auto-generated method stub
        map.setOnMapLoadedCallback(Bus_Fetch.this);
        map.setOnCameraChangeListener(Bus_Fetch.this);
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

        map.setOnMapLoadedCallback(Bus_Fetch.this);

        if(searchByNumberb) {
            new GetByNumberUpdate().execute();
            markerp.setVisibility(View.VISIBLE);
        }

        else if(searchByRoute){

            markerp.setVisibility(View.VISIBLE);
        }
        else
            markerp.setVisibility(View.GONE);


    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) this.getBaseContext().getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                //First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateGPSCoordinates();
                    }
                }

                //if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateGPSCoordinates();
                        }
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("Error : Location", "Impossible to connect to LocationManager", e);
        }

        return location;
    }


    public void updateGPSCoordinates() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();

        }

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     */

    class CreateBusDatabase extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Bus_Fetch.this);
            pDialog.setMessage("Settings up bus database....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            databaseH.create(new MyObject("PMC"));
            databaseH.create(new MyObject("E_SQUARE"));
            databaseH.create(new MyObject("AUNDH"));
            databaseH.create(new MyObject("WAKAD"));
            databaseH.create(new MyObject("RAKSHAK_CHOWK"));
            databaseH.create(new MyObject("INFOSYS_PHASE_2"));
            databaseH.create(new MyObject("HINJEWADI_PHASE_3"));
            databaseH.create(new MyObject("PUNE_STATION"));
            databaseH.create(new MyObject("PUNE_UNIVERSITY"));
            databaseH.create(new MyObject("BANER"));
            databaseH.create(new MyObject("BALEWADI"));
            databaseH.create(new MyObject("DANGE_CHOWK"));
            databaseH.create(new MyObject("DANGE_CHOWK"));

            pDialog.dismiss();

            return null;
        }
    }

    public void searchByNumber() {
        searchByNumberb = true;
        searchByRoute = false;
        BusNumber = toolbarSearchView.getText().toString();
        new GetByNumber().execute();
        map.clear();
        markerp.setVisibility(View.VISIBLE);
    }



    class GetByNumber extends AsyncTask<String, String, String> {

        JSONObject json2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("route", BusNumber));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_get_category, "GET", params);
                    Log.d("These are your markers",json2.toString());

                }

                catch (Exception e1) {
                    runOnUiThread(new Runnable() {
                                      //
                                      @Override
                                      public void run() {
                                          // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                                          Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                                  .setAction("Ok", snackaction)
                                                  .setActionTextColor(Color.WHITE)
                                                  .show();
                                          markerp.setVisibility(View.GONE);

                                      }
                    });
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
                                Snackbar.make(findViewById(android.R.id.content), "No buses found", Snackbar.LENGTH_LONG)
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
                                mrkgrp[i].setTitle("From :" + coordinates.getString("start") + "  " + "To :" + coordinates.getString("end"));
                                mrkgrp[i].setSnippet("Bus number " + BusNumber);
                                mrkgrp[i].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrkgrp[i].setVisible(true);

                            }
                            markerp.setVisibility(View.GONE);
                            int no = numberOfItemsInResp ;
                            Snackbar.make(findViewById(android.R.id.content), no+ " buses found", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();

                        } catch (JSONException e) {
                            Log.e("Error in markers", e.toString());
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            markerp.setVisibility(View.GONE);
                        }
                    }
                });
            }
            return null;
        }
    }


    class GetByNumberUpdate extends AsyncTask<String, String, String> {

        JSONObject json2;
        Boolean updated = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("route", BusNumber));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_get_category, "GET", params);
                    updated = true;

                }
                catch (Exception e1) {
                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            updated = false;
                            markerp.setVisibility(View.GONE);


                        }
                    });
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


                            for (int i = 0; i < numberOfItemsInResp; i++)
                            {
                                JSONObject coordinates = values.getJSONObject(i);
                                Log.e("Values bagh", coordinates.toString());

                                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(10, 10));
                                mrkgrp[i] =   map.addMarker(markerOption);
                                mrkgrp[i].setVisible(false);
                                mrkgrp[i].setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrkgrp[i].setTitle("From :" + coordinates.getString("start") + "  " + "To :" + coordinates.getString("end"));
                                mrkgrp[i].setSnippet("Bus number " + BusNumber);
                                mrkgrp[i].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrkgrp[i].setVisible(true);

                            }
                            markerp.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("Error in markers", e.toString());
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            markerp.setVisibility(View.GONE);
                        }
                    }
                });
            }
            return null;
        }
    }


    class GetByRoute extends AsyncTask<String, String, String> {

        JSONObject json2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("endloc", end));
            params.add(new BasicNameValuePair("startloc", start));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            "http://www.skylinelabs.in/Geo/PMPML/from_to.php", "GET", params);
                    Log.d("These are your markers",json2.toString());

                }

                catch (Exception e1) {
                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            markerp.setVisibility(View.GONE);

                        }
                    });
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
                                Snackbar.make(findViewById(android.R.id.content), "No buses found", Snackbar.LENGTH_LONG)
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
                                mrkgrp[i].setTitle("From :" + coordinates.getString("start") + "  " + "To :" + coordinates.getString("end"));
                                mrkgrp[i].setSnippet("Bus number " + BusNumber);
                                mrkgrp[i].setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrkgrp[i].setVisible(true);

                            }
                            markerp.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("Error in markers", e.toString());
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            markerp.setVisibility(View.GONE);
                        }
                    }
                });
            }
            return null;
        }
    }



}


