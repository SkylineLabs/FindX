package in.skylinelabs.digiPune.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.skylinelabs.digiPune.R;

/**
 * Created by Jay Lohokare on 01-Oct-15.
 */
public class SelectWork extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>, android.location.LocationListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback{

    int a[]={0,0};

    double SavLat = 0;
    double SavLon = 0;
    View.OnClickListener snackaction ,snackaction1;

    AlertDialog alertDialog;

    private static final String url_update_home = "http://www.skylinelabs.in/Geo/update_work.php";


    JSONParser jsonParser = new JSONParser();


    String addresstxt;

    Double FLat, FLon;

    MarkerOptions tempo = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions finalo = new MarkerOptions().position(new LatLng(10, 10));

    Marker tempm, finalm;

    private FloatingActionButton mAddGeofencesButton;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    TextView textView3;

    ProgressBar prgbr;

    Boolean home;

    String Latitude, Longitude;

    GoogleMap map;

    String mystringLatitude;
    String mystringLongitude;


    String  post;
    //flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; //0 metters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 10 sec

    //Declaring a Location Manager
    protected LocationManager locationManager;


    final Context context = this;

    FloatingActionButton btn3,addb ;

    EditText add, edt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_home);

        addb = (FloatingActionButton) findViewById(R.id.sel);
        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        snackaction1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        final String PREFS_NAME = "GeoPreferences";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mAddGeofencesButton = (FloatingActionButton) findViewById(R.id.add);
        mAddGeofencesButton.setVisibility(View.GONE);

        mAddGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent1 = cd1.isConnectingToInternet();

                if(isInternetPresent1) {

                    new UpdateHome().execute();
                }

                else
                {
                    Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }
        });

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {

            //Toast.makeText(getApplicationContext(), " Please Enable Internet ", Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();

        }

        getLocation();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Work");


        edt1 = (EditText) findViewById(R.id.search_view);
        add = (EditText) findViewById(R.id.search_view);
        add.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    InputMethodManager inputManager =
                            (InputMethodManager) context.
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            SelectWork.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });


        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText("Loading Map");
        textView3.setVisibility(View.VISIBLE);

        prgbr = (ProgressBar) findViewById(R.id.progressBar1);
        prgbr.setVisibility(View.VISIBLE);

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


        map.setOnMapLoadedCallback(this);

        map.setOnCameraChangeListener(this);
        CameraPosition camPos = new CameraPosition.Builder()

                .target(new LatLng(getLatitude(), getLongitude()))

                .zoom(12.8f)

                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        map.moveCamera(camUpdate);


        tempm = map.addMarker(tempo);
        tempm.setVisible(false);

        finalm = map.addMarker(finalo);
        finalm.setVisible(false);


        if(settings.getString("work_lat","") != ""  && settings.getString("","") != null ) {
            finalm.setPosition(new LatLng(Double.parseDouble(settings.getString("work_lat", "")), Double.parseDouble(settings.getString("work_lon", ""))));
            finalm.setTitle("Work");
            finalm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            finalm.setVisible(true);

            CameraPosition camPos2 = new CameraPosition.Builder()

                    .target(new LatLng(Double.parseDouble(settings.getString("work_lat", "")),Double.parseDouble(settings.getString("work_lon", "")) ))

                    .zoom(12.8f)

                    .build();

            CameraUpdate camUpdate2 = CameraUpdateFactory.newCameraPosition(camPos2);

            map.moveCamera(camUpdate2);

        }



        btn3 = (FloatingActionButton) findViewById(R.id.location);
        btn3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(new LatLng(getLatitude(), getLongitude()))
                        .zoom(12.8f)
                        .build();
                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
                map.moveCamera(camUpdate);
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                try {
                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                    Boolean isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {

                        Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                    }
                    else {

                        FLat = point.latitude;
                        FLon = point.longitude;
                        SavLat = FLat;
                        SavLon = FLon;

                        tempm.setPosition(new LatLng(FLat, FLon));
                        tempm.setTitle("Click to Set this as Work");
                        tempm.setSnippet("");
                        tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        tempm.setVisible(true);

                        mAddGeofencesButton.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(android.R.id.content), "Couldnt fetch location please check internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }
        });


    }






    @Override
    public void onLocationChanged(Location location) {

        map.setOnMapLoadedCallback(this);
        this.location = location;


        if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
        {

            mystringLatitude = String.valueOf(getLatitude());
            mystringLongitude = String.valueOf(getLongitude());
            // Toast.makeText(this,"Activity OLC Update " + networkTS,Toast.LENGTH_SHORT).show();


        }


        this.location = location;
        Latitude = String.valueOf(getLatitude());
        Longitude = String.valueOf(getLongitude());
    }

    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

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
            locationManager.removeUpdates(this);
        }
    }



    public List<Address> getGeocoderAddress(Context context)
    {
        if (location != null)
        {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                return addresses;
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                Log.e("Error : Geocoder", "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

        finish();
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
        // TODO Auto-generated method stub
        map.setOnMapLoadedCallback(null);
        prgbr.setVisibility(View.VISIBLE);
        map.setOnCameraChangeListener(this);
        map.setOnMapLoadedCallback(this);
        textView3.setVisibility(View.VISIBLE);

		/* map.setOnCameraChangeListener(MyLocation.this);*/
    }

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub
        prgbr.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(Status status) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_event, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


       int id = item.getItemId();

        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if(id == R.id.info) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    this);
            LayoutInflater factory = LayoutInflater.from(this);
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

        if(id == R.id.action_search) {

            ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent1 = cd1.isConnectingToInternet();

            if(isInternetPresent1) {
                search();
                InputMethodManager inputManager =
                        (InputMethodManager) context.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            else
            {
                Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    void search()
    {
        ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent1 = cd1.isConnectingToInternet();

        if(isInternetPresent1) {
            addresstxt = edt1.getText().toString();
            new GetLocation().execute();
        }
        else
        {
            Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();
        }
    }

    /*******************************************************************************/
    class GetLocation extends AsyncTask<String, String, String> {

        Boolean i = false;
        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            try {
                Geocoder coder = new Geocoder(context);
                List<Address> listadd;
                listadd = coder.getFromLocationName(addresstxt, 5);
                if (listadd == null) {
                }
                Address location = listadd.get(0);
                FLat = location.getLatitude();
                FLon = location.getLongitude();
                SavLat = FLat;
                SavLon = FLon;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tempm.setPosition(new LatLng(FLat, FLon));
                        tempm.setTitle(addresstxt + "\n");
                        tempm.setSnippet("Click to set as work ");
                        tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        tempm.setVisible(true);
                        mAddGeofencesButton.setVisibility(View.VISIBLE);
                        CameraPosition camPos = new CameraPosition.Builder()
                                .target(new LatLng(FLat, FLon))
                                .zoom(12.8f)
                                .build();

                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
                        map.moveCamera(camUpdate);
                    }
                });
            }
            catch(Exception e)
            {
            }
            return null;
        }
    }

    class UpdateHome extends AsyncTask<String, String, String> {

        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ProgressDialog pDialog;
        JSONObject json;
        Boolean i = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectWork.this);
            pDialog.setMessage("Updating work location to servers....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", settings.getString("user_name","")));
            params.add(new BasicNameValuePair("work_lat", String.valueOf(SavLat)));
            params.add(new BasicNameValuePair("work_lon", String.valueOf(SavLon)));

            try
            {
                json = jsonParser.makeHttpRequest(
                        url_update_home, "POST", params);
                i=true;
            }
            catch (Exception e1) {
                i=false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(findViewById(android.R.id.content), "Couldnt update work location please check internet connection", Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                        pDialog.dismiss();
                    }
                });
            }

            try {
                if (i) {
                    settings.edit().putString("work_lat", String.valueOf(SavLat)).commit();
                    settings.edit().putString("work_lon", String.valueOf(SavLon)).commit();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tempm.setVisible(false);
                            finalm.setPosition(new LatLng(SavLat, SavLon));
                            finalm.setTitle("Work");
                            finalm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            finalm.setVisible(true);
                            pDialog.dismiss();
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(findViewById(android.R.id.content), "Couldnt update work location please check internet connection", Snackbar.LENGTH_LONG)
                                    .setAction("Done", snackaction1)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            pDialog.dismiss();
                        }
                    });
                }
            }
            catch(Exception e)
            {
            }
            return null;
        }
    }
}




