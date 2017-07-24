
package in.skylinelabs.digiPune.activity;



        import android.annotation.SuppressLint;
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
        import android.os.Bundle;
        import android.support.design.widget.Snackbar;
        import android.support.v4.widget.DrawerLayout;
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
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.provider.Settings;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.List;

        import in.skylinelabs.digiPune.R;

        import com.github.clans.fab.FloatingActionButton;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
        import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
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

public class FriendLocation extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener, android.location.LocationListener, OnCameraChangeListener, OnMapLoadedCallback {

    private static String TAG = FriendLocation.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    MaterialSheetFab materialSheetFab;

    ListView listView;
    String categorys;

    GoogleMap map;
    ProgressBar pgb;
    JSONObject json1;


    public static int i;

    ProgressBar prgbr, marker;

    TextView txt1, txt2, textView3, textView4;

    long date;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final Context context = this;

    String mystringuername;
    String mystringpassword;

    String mydevice_id1, password1, mydevice_id2, password2, mydevice_id3, password3, mydevice_id4, password4;


    MarkerOptions marker2 = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions marker3 = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions marker4 = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions marker1 = new MarkerOptions().position(new LatLng(10, 10));

    Marker mrk1, mrk2, mrk3, mrk4;


    String nation, post, area, address;
    //flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    public static boolean mylocationOn;
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

    boolean first, second, third, fourth;

    FloatingActionButton  btn2;

    Marker[] mrkgrp = new Marker[1000000];

    EditText edttxt1;

    JSONParser jsonParser = new JSONParser();
    private static final String url_update_product = "http://www.skylinelabs.in/Geo/get_location.php";
    private static final String url_get_category = "http://www.skylinelabs.in/Geo/fetch_category.php";

    AlertDialog alertDialog;

    FloatingActionButton btn1;


    View.OnClickListener snackaction,snackaction1;;




    @Override
    protected void onStop() {

        super.onStop();  // Always call the superclass method first
        digiPune.MIN_TIME_BW_UPDATES = 1000 * 60 * 10;

        digiPune.isOn = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        digiPune.isOn = false;
    }

    @Override
    protected void onResume() {
        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (digiPune.mRunning){
        }
        else{
            context.startService(new Intent(context, digiPune.class));
        }

        if(settings.getBoolean("FindX_update_enabled", true))
        {
            digiPune.isOn = true;
        }
        else
            digiPune.isOn = false;


        digiPune.MIN_TIME_BW_UPDATES = 1000 * 60 * 2;
        // TODO Auto-generated method stub
        super.onResume();

        //Pre_launch_activity.post = 0;

        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Location Access Error");
            alertDialogBuilder.setMessage("Please allow digiPune to access location");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            alertDialog.dismiss();

                        }
                    });
            alertDialogBuilder.setNegativeButton("Exit",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_location);
        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        if(settings.getBoolean("FindX_update_enabled", true))
        {
            digiPune.isOn = true;
        }
        else {
            digiPune.isOn = false;
        }

        digiPune.MIN_TIME_BW_UPDATES = 1000 * 60 * 2;


        if (digiPune.mRunning){
            // Toast.makeText(getApplicationContext(),"Service chalu hoti re",Toast.LENGTH_LONG).show();


        }
        else{
            context.startService(new Intent(context, digiPune.class));
            //  Toast.makeText(getApplicationContext(),"Chalu keli service",Toast.LENGTH_LONG).show();

        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("digiPune");
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

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


        EditText toolbarSearchView = (EditText) findViewById(R.id.search_view);
        toolbarSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    InputMethodManager inputManager =
                            (InputMethodManager) context.
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            FriendLocation.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        map = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);




        map.setOnMapLoadedCallback(new OnMapLoadedCallback() {
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


        map.setOnCameraChangeListener(FriendLocation.this);
        map.setOnMapLoadedCallback(FriendLocation.this);

        CameraPosition camPos = new CameraPosition.Builder()

                .target(new LatLng(getLatitude(), getLongitude()))

                .zoom(12.8f)

                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        map.moveCamera(camUpdate);

        mrk1 =  map.addMarker(marker1);
        mrk1.setVisible(false);

        mrk2 =  map.addMarker(marker2);
        mrk2.setVisible(false);

        mrk3 =  map.addMarker(marker3);
        mrk3.setVisible(false);

        mrk4 =  map.addMarker(marker4);
        mrk4.setVisible(false);


        mystringuername = settings.getString("user_name", "");
        mystringpassword = settings.getString("password", "");

        txt1 = (TextView) findViewById(R.id.textView1);
        txt2 = (TextView) findViewById(R.id.textView2);

        edttxt1 = (EditText) findViewById(R.id.search_view);

        edttxt1.setHint("  digiPune ID");

        first = true;
        second = false;
        third = false;
        fourth = false;

        btn1 = (FloatingActionButton)findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi! Locate me using digiPune app" + "\n" + "\n" + "digiPune ID :" + "\n" + mystringuername + "\n" + "\n" + "\n" + "To access location live, download digiPune on google play" + "\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n\n" + "Or access the location online at" + "\n" + "www.skylinelabs.in/digiPune" + "\n\n" + "Shared via digiPune");
                startActivity(Intent.createChooser(sharingIntent, "Send digiPune ID"));
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
        getMenuInflater().inflate(R.menu.friendlocation_menu, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.info) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    FriendLocation.this);
            LayoutInflater factory = LayoutInflater.from(FriendLocation.this);

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
        if(id == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi! Locate me using digiPune app" + "\n" + "\n" + "digiPune ID :" + "\n" + mystringuername + "\n" + "\n" + "\n" + "To access location live, download digiPune on google play" + "\n" + "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "\n\n" + "Or access the location online at" + "\n" + "www.skylinelabs.in/digiPune" + "\n\n" + "Shared via digiPune");
            startActivity(Intent.createChooser(sharingIntent, "Send digiPune ID"));

        }
        if(id == R.id.action_search) {
            search();
            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    FriendLocation.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        //Fragment fragment = null;
        String title = getString(R.string.app_name);
        Intent i;
        switch (position) {
            case 0:
                Pre_launch_activity.post=0;
                break;
            case 1:
                Intent i2 = new Intent(this, ComplaintPortal.class);
                startActivity(i2);
                break;
            case 2:
                i = new Intent(this, Bus_Fetch.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(this, FindGov.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(this, MyLocation.class);
                startActivity(i);
                break;
            case 5:
                i = new Intent(this, History.class);
                startActivity(i);
                break;
            case 6:
                i = new Intent(this, Setting.class);
                startActivity(i);
                break;
            case 7:
                i = new Intent(this, SOS_Contacts.class);
                startActivity(i);
                break;
            case 8:
                i = new Intent(this, Favourites.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        if(!catopen) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog alert;

            builder.setMessage("Are you sure you want to exit ?")
                    .setCancelable(true)
                    .setTitle("Exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            digiPune.MIN_TIME_BW_UPDATES = 1000 * 60 * 10;
                            finish();
                        }
                    });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alert = builder.create();
            alert.show();
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
        map.setOnMapLoadedCallback(FriendLocation.this);
        map.setOnCameraChangeListener(FriendLocation.this);
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

        map.setOnMapLoadedCallback(FriendLocation.this);

        if(first)
        {

        }

        if(second)
        {
            mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                if(mrk1.isVisible())
                    new UpdateProduct1().execute();
            }

            else
                //Toast.makeText(getApplicationContext(),"Unable to update marker locations",Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(android.R.id.content),  "Unable to update marker locations", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();

        }

        if(third)
        {

            mrk2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                if(mrk1.isVisible() && mrk2.isVisible())
                {
                    new UpdateProduct1().execute();
                    new UpdateProduct2().execute();
                }
            }
            else
                //    Toast.makeText(getApplicationContext(),"Unable to update marker locations",Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(android.R.id.content),  "Unable to update marker locations", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();


        }

        if(fourth)
        {

            mrk2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mrk3.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                if(mrk1.isVisible() && mrk2.isVisible() && mrk3.isVisible())
                {
                    new UpdateProduct1().execute();
                    new UpdateProduct2().execute();
                    new UpdateProduct3().execute();
                }
            }
            else
                //Toast.makeText(getApplicationContext(),"Unable to update marker locations",Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(android.R.id.content),  "Unable to update marker locations", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();


        }

        if(!first && !second && !third && !fourth)
        {

            mrk2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mrk3.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mrk4.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                if(mrk1.isVisible() && mrk2.isVisible() && mrk3.isVisible() && mrk4.isVisible())
                {
                    new UpdateProduct1().execute();
                    new UpdateProduct2().execute();
                    new UpdateProduct3().execute();
                    new UpdateProduct4().execute();
                }
            }
            else
                // Toast.makeText(getApplicationContext(),"Unable to update marker locations",Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(android.R.id.content),  "Unable to update marker locations", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();

        }


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
            locationManager.removeUpdates(FriendLocation.this);
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
    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }
    class UpdateProduct1 extends AsyncTask<String, String, String> {

        JSONObject json2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id1));
            params.add(new BasicNameValuePair("password", password1));
            params.add(new BasicNameValuePair("self", mystringuername));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();


            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

                }

                catch (Exception e1) {
                    // Toast.makeText(getApplicationContext(), "Network error Unable to fetch location", Toast.LENGTH_LONG).show();
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


                        try {
                            int success = json2.getInt("success");

                            if(success == 1)
                            {

                                JSONArray values = json2.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);

                                mrk1.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk1.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk1.setVisible(true);

                                json2 = null;
                            }

                            else if(success ==0)
                            {
                                //Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();

                            }



                        } catch (JSONException e1) {
                            //  Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
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



    class UpdateProduct2 extends AsyncTask<String, String, String> {

        JSONObject json2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id2));
            params.add(new BasicNameValuePair("password", password2));
            params.add(new BasicNameValuePair("self", mystringuername));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

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


                        try {
                            int success = json2.getInt("success");

                            if(success == 1)
                            {

                                JSONArray values = json2.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);

                                mrk2.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk2.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk2.setVisible(true);

                                json2 = null;
                            }

                            else if(success ==0)
                            {
                                // Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                            }



                        } catch (JSONException e1) {
                            //  Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
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

    class UpdateProduct3 extends AsyncTask<String, String, String> {

        JSONObject json2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id3));
            params.add(new BasicNameValuePair("password", password3));
            params.add(new BasicNameValuePair("self", mystringuername));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

                }

                catch (Exception e1) {
                    //  Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
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


                        try {
                            int success = json2.getInt("success");

                            if(success == 1)
                            {

                                JSONArray values = json2.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);

                                mrk3.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk3.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk3.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk3.setVisible(true);

                                json2 = null;
                            }

                            else if(success ==0)
                            {
                                //  Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();

                            }



                        } catch (JSONException e1) {
                            //Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
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

    class UpdateProduct4 extends AsyncTask<String, String, String> {

        JSONObject json2;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id4));
            params.add(new BasicNameValuePair("password", password4));
            params.add(new BasicNameValuePair("self", mystringuername));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

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


                        try {
                            int success = json2.getInt("success");

                            if (success == 1)
                            {

                                JSONArray values = json2.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);

                                mrk4.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk4.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk4.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk4.setVisible(true);

                                json2 = null;
                            }

                            else if(success ==0)
                            {
                                //Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                            }



                        } catch (JSONException e1) {
                            //Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
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
    class CreateNewProduct1 extends AsyncTask<String, String, String> {

        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id1));
            params.add(new BasicNameValuePair("password", password1));
            params.add(new BasicNameValuePair("self", mystringuername));



            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

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


                        try {
                            int success = json.getInt("success");
                            if(success == 1)
                            {

                                JSONArray values = json.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);


                                mrk1.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk1.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk1.setSnippet(coordinates.getString("contact"));
                                mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk1.setVisible(true);
                                mrk1.showInfoWindow();
                                first = false;
                                second = true;
                                third = false;
                                fourth = false;
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                                edttxt1.setText("");
                                CameraPosition camPos = new CameraPosition.Builder()

                                        .target(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))))

                                        .zoom(12.8f)

                                        .build();
                                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                                map.moveCamera(camUpdate);


                            }

                            if(success ==0)
                            {
                                // Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                            }


                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            //Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            textView4.setVisibility(View.GONE);
                            marker.setVisibility(View.GONE);
                        }
                        btn1.setClickable(true);

                    }
                });
            }


            return null;
        }


    }


    class CreateNewProduct2 extends AsyncTask<String, String, String> {


        JSONObject json1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id2));
            params.add(new BasicNameValuePair("password", password2));
            params.add(new BasicNameValuePair("self", mystringuername));

            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json1 = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

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
            Boolean isInternetPresent1 = cd.isConnectingToInternet();

            if(isInternetPresent1)
            {
                runOnUiThread(new Runnable() {
                    //
                    @Override
                    public void run() {


                        try {
                            int success = json1.getInt("success");
                            if(success == 1)
                            {

                                JSONArray values = json1.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);


                                mrk2.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk2.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk2.setSnippet(coordinates.getString("contact"));
                                mrk2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk2.setVisible(true);
                                mrk2.showInfoWindow();

                                first = false;
                                second = false;
                                third = true;
                                fourth = false;
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                                edttxt1.setText("");
                                CameraPosition camPos = new CameraPosition.Builder()

                                        .target(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))))

                                        .zoom(12.8f)

                                        .build();
                                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                                map.moveCamera(camUpdate);

                            }

                            if(success ==0)
                            {
                                // Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                            }


                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            //  Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            textView4.setVisibility(View.GONE);
                            marker.setVisibility(View.GONE);
                        }
                        btn1.setClickable(true);

                    }
                });

            }



            return null;
        }


    }

    class CreateNewProduct3 extends AsyncTask<String, String, String> {

        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id3));
            params.add(new BasicNameValuePair("password", password3));
            params.add(new BasicNameValuePair("self", mystringuername));


            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent)
            {
                try {

                    json = jsonParser.makeHttpRequest(
                            url_update_product, "GET", params);

                }

                catch (Exception e1) {
                    //  Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
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


                        try {
                            int success = json.getInt("success");
                            if(success == 1)
                            {

                                JSONArray values = json.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);


                                mrk3.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk3.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk3.setSnippet(coordinates.getString("contact"));
                                mrk3.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk3.setVisible(true);
                                mrk3.showInfoWindow();

                                first = false;
                                second = false;
                                third = false;
                                fourth = true;
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                                edttxt1.setText("");
                                CameraPosition camPos = new CameraPosition.Builder()

                                        .target(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))))

                                        .zoom(12.8f)

                                        .build();
                                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                                map.moveCamera(camUpdate);

                            }

                            if(success ==0)
                            {
                                //  Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                            }


                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            textView4.setVisibility(View.GONE);
                            marker.setVisibility(View.GONE);
                        }
                        btn1.setClickable(true);

                    }
                });
            }

            return null;
        }


    }

    class CreateNewProduct4 extends AsyncTask<String, String, String> {

        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id4));
            params.add(new BasicNameValuePair("password", password4));
            params.add(new BasicNameValuePair("self", mystringuername));


            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();
            if(isInternetPresent)
            {
                try {

                    json = jsonParser.makeHttpRequest(
                            url_update_product, "POST", params);
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


                        try {
                            int success = json.getInt("success");
                            if(success == 1)
                            {

                                JSONArray values = json.getJSONArray("gps_coordinates");
                                JSONObject coordinates = values.getJSONObject(0);


                                mrk4.setPosition(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))));
                                mrk4.setTitle("(Last seen :  " + coordinates.getString("time") + ")");
                                mrk4.setSnippet(coordinates.getString("contact"));
                                mrk4.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                mrk4.setVisible(true);
                                mrk4.showInfoWindow();

                                first = false;
                                second = false;
                                third = false;
                                fourth = false;
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                                edttxt1.setText("");
                                CameraPosition camPos = new CameraPosition.Builder()

                                        .target(new LatLng(Double.parseDouble(coordinates.getString("latitude")), Double.parseDouble(coordinates.getString("longitude"))))

                                        .zoom(12.8f)

                                        .build();
                                CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                                map.moveCamera(camUpdate);

                            }

                            if(success ==0)
                            {
                                //    Toast.makeText(getApplicationContext(),"Location access denied",Toast.LENGTH_LONG).show();
                                Snackbar.make(findViewById(android.R.id.content), "Location access denied", Snackbar.LENGTH_LONG)
                                        .setAction("Ok", snackaction)
                                        .setActionTextColor(Color.WHITE)
                                        .show();
                                textView4.setVisibility(View.GONE);
                                marker.setVisibility(View.GONE);
                            }


                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(android.R.id.content), "Network error Unable to fetch location", Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                            textView4.setVisibility(View.GONE);
                            marker.setVisibility(View.GONE);
                        }
                        btn1.setClickable(true);

                    }
                });
            }

            return null;
        }


    }

    void search() {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            if (first) {
                mydevice_id1 = edttxt1.getText().toString();
                password1 = "";
                btn1.setClickable(false);
                textView4.setText("Forming marker");
                textView4.setVisibility(View.VISIBLE);
                marker.setVisibility(View.VISIBLE);
                new CreateNewProduct1().execute();

            }

            if (second) {


                if (!edttxt1.getText().toString().equalsIgnoreCase(mydevice_id1)) {
                    mydevice_id2 = edttxt1.getText().toString();
                    password2 = "";
                    btn1.setClickable(false);
                    textView4.setText("Forming marker");
                    textView4.setVisibility(View.VISIBLE);
                    marker.setVisibility(View.VISIBLE);
                    new CreateNewProduct2().execute();
                } else
                    // Toast.makeText(getApplicationContext(), "Marker already exists for " + mydevice_id1, Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), "Marker already exists for " + mydevice_id1, Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();

            }

            if (third) {

                if (!edttxt1.getText().toString().equalsIgnoreCase(mydevice_id1)) {
                    if (!edttxt1.getText().toString().equalsIgnoreCase(mydevice_id2)) {
                        mydevice_id3 = edttxt1.getText().toString();
                        password3 = "";
                        btn1.setClickable(false);
                        textView4.setText("Forming marker");
                        textView4.setVisibility(View.VISIBLE);
                        marker.setVisibility(View.VISIBLE);


                        new CreateNewProduct3().execute();
                    } else
                        //Toast.makeText(getApplicationContext(), "Marker already exists for " + mydevice_id2, Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(android.R.id.content), "Marker already exists for " + mydevice_id2, Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                } else
                    // Toast.makeText(getApplicationContext(), "Marker already exists for " + mydevice_id1, Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), "Marker already exists for " + mydevice_id1, Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();

            }


            if (fourth) {
                if (!edttxt1.getText().toString().equalsIgnoreCase(mydevice_id1)) {
                    if (!edttxt1.getText().toString().equalsIgnoreCase(mydevice_id2)) {
                        if (!edttxt1.getText().toString().equalsIgnoreCase(mydevice_id3)) {
                            mydevice_id4 = edttxt1.getText().toString();
                            password4 = "";
                            btn1.setClickable(false);
                            textView4.setText("Forming marker");
                            textView4.setVisibility(View.VISIBLE);
                            marker.setVisibility(View.VISIBLE);
                            new CreateNewProduct4().execute();
                        } else
                            // Toast.makeText(getApplicationContext(), "Marker already exists for " + mydevice_id3, Toast.LENGTH_SHORT).show();
                            Snackbar.make(findViewById(android.R.id.content), "Marker already exists for " + mydevice_id3, Snackbar.LENGTH_LONG)
                                    .setAction("Ok", snackaction)
                                    .setActionTextColor(Color.WHITE)
                                    .show();
                    } else
                        //   Toast.makeText(getApplicationContext(), "Marker already exists for " + mydevice_id2, Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(android.R.id.content), "Marker already exists for " + mydevice_id2, Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                } else
                    // Toast.makeText(getApplicationContext(), "Marker already exists for " + mydevice_id1, Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), "Marker already exists for " + mydevice_id1, Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
            }


            if (!first && !second && !third && !fourth) {
                //  Toast.makeText(getApplicationContext(), "Currently digiPune can track only 4 friend", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content),  "Currently digiPune can track only 4 friend", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();
                btn1.setVisibility(View.GONE);

            }

        } else
            //   Toast.makeText(getApplicationContext(), "Please conect to internet", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content),   "Please conect to internet", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();

    }

}


