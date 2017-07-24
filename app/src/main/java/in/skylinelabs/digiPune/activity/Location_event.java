package in.skylinelabs.digiPune.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.skylinelabs.digiPune.R;

public class Location_event extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status>,FragmentDrawer.FragmentDrawerListener, android.location.LocationListener, GoogleMap.OnCameraChangeListener, GoogleMap.OnMapLoadedCallback{

    int a[]={0,0};

    private View mFab;
    public FrameLayout mFabContainer;
    public RelativeLayout mControlsContainer;

    public final static float SCALE_FACTOR      = 130f;
    public final static int ANIMATION_DURATION  = 300;
    public final static int MINIMUN_X_DISTANCE  = 200;

    private boolean mRevealFlag;
    private float mFabSize;

    Boolean rev = false;


    View.OnClickListener snackaction;

    AlertDialog alertDialog;

    String addresstxt;

    Double FLat, FLon;

    MarkerOptions homeo = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions worko = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions othero = new MarkerOptions().position(new LatLng(10, 10));
    MarkerOptions tempo = new MarkerOptions().position(new LatLng(10, 10));

    Marker homem,otherm, tempm, workm;

    private FloatingActionButton mAddGeofencesButton;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    TextView textView3;

    ProgressBar prgbr, prgbr1;

    Boolean home, other, work;

    String Latitude, Longitude;

    GoogleMap map;

    String mystringLatitude;
    String mystringLongitude;


    String nation, post, area, address;
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

    String networkTS;

    final Context context = this;

    FloatingActionButton btn3,addb ;

    private SharedPreferences mSharedPreferences;

    EditText add, edt1;
    private Spinner spinner_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_event);
        home = true;
        other=false;
        work = false;

        addb = (FloatingActionButton) findViewById(R.id.sel);




        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        final String PREFS_NAME = "GeoPreferences";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mAddGeofencesButton= (FloatingActionButton) findViewById(R.id.add);
        mAddGeofencesButton.setVisibility(View.GONE);

        mAddGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(home) {

                    homem.setPosition(new LatLng(FLat, FLon));

                    homem.setSnippet("Home");
                    try {
                       // homem.setTitle(address.toString());
                    } catch (Exception e) {
                      //  Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                    }
                    homem.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    homem.setVisible(true);
                    homem.showInfoWindow();
                    tempm.setVisible(false);





                    settings.edit().putBoolean("home_geofence", true).commit();
                    settings.edit().putString("home_geofence_lat", FLat.toString()).commit();
                    settings.edit().putString("home_geofence_lon", FLon.toString()).commit();

  /////////////////////int x = array_loader(getLatitude(),getLongitude(),FLat,FLon,0.005);
                    //Toast.makeText(getApplicationContext(), " Geofence set  "  + x, Toast.LENGTH_LONG).show();

                }
                if(other){


                    otherm.setPosition(new LatLng(FLat,FLon));

                    otherm.setSnippet("Home");
                    try {
                       // otherm.setTitle(address.toString());
                    } catch (Exception e) {
                       // Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                    }
                    otherm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    otherm.setVisible(true);
                    otherm.showInfoWindow();
                    tempm.setVisible(false);



                    settings.edit().putBoolean("other_geofence", true).commit();
                    settings.edit().putString("other_geofence_lat", FLat.toString()).commit();
                    settings.edit().putString("other_geofence_lon", FLon.toString()).commit();
                   // Toast.makeText(getApplicationContext(), " Geofence set  ", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Geofence set", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }

                if(work){


                    workm.setPosition(new LatLng(FLat, FLon));

                    workm.setSnippet("Work");
                    try {
                       // workm.setTitle(address.toString());
                    } catch (Exception e) {
                       // Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                    }
                    workm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    workm.setVisible(true);
                    workm.showInfoWindow();
                    tempm.setVisible(false);



                    settings.edit().putBoolean("work_geofence", true).commit();
                    settings.edit().putString("work_geofence_lat", FLat.toString()).commit();
                    settings.edit().putString("work_geofence_lon", FLon.toString()).commit();
                    // Toast.makeText(getApplicationContext(), " Geofence set  ", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Geofence set", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }
        });

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if(!isInternetPresent) {

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
        getSupportActionBar().setTitle("digiPune");
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);


        addItemsToSpinner();

        Button ab =(Button) findViewById(R.id.actionbutton);
     //   final Dialog homedialog = new Dialog(context);
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // setContentView(R.layout.alert);
                final Switch wifi_toggle, data_toggle, silent_toggle;
                final CheckBox wifi, data, silent,notify, reminder;
                final EditText reminder_edit;

                wifi_toggle = (Switch) findViewById(R.id.wifi_switch);
                data_toggle = (Switch)findViewById(R.id.data_switch);
                silent_toggle = (Switch) findViewById(R.id.silent_switch);

                wifi= (CheckBox) findViewById(R.id.wifi_check);
                data= (CheckBox) findViewById(R.id.data_check);
                silent= (CheckBox) findViewById(R.id.silent_check);
                notify= (CheckBox) findViewById(R.id.notify_check);
                reminder= (CheckBox) findViewById(R.id.reminder_check);

                reminder_edit = (EditText) findViewById(R.id.reminder_editText);
                Button ok = (Button)findViewById(R.id.ok);

                if(home)
                {
                    wifi.setChecked(settings.getBoolean("home_wifi", false));
                    data.setChecked(settings.getBoolean("home_data", false));
                    silent.setChecked(settings.getBoolean("home_silent", false));
                    notify.setChecked(settings.getBoolean("home_notify", false));
                    reminder.setChecked(settings.getBoolean("home_reminder", false));

                    wifi_toggle.setChecked(settings.getBoolean("home_wifi_state", false));
                    data_toggle.setChecked(settings.getBoolean("home_data_state", false));
                    silent_toggle.setChecked(settings.getBoolean("home_silent_state", false));

                    reminder_edit.setText(settings.getString("home_reminder_text", ""));

                    if(!wifi.isChecked())
                        wifi_toggle.setEnabled(false);
                    if(!data.isChecked())
                        data_toggle.setEnabled(false);
                    if(!silent.isChecked())
                        silent_toggle.setEnabled(false);
                    if(!reminder.isChecked())
                        reminder_edit.setEnabled(false);

                    wifi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wifi.isChecked()) {
                                wifi_toggle.setEnabled(true);
                            }
                            else
                                wifi_toggle.setEnabled(false);
                        }
                    });
                    data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(data.isChecked()) {
                                data_toggle.setEnabled(true);
                            }
                            else
                                data_toggle.setEnabled(false);

                        }
                    });
                    silent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(silent.isChecked()) {
                                silent_toggle.setEnabled(true);
                            }
                            else
                                silent_toggle.setEnabled(false);
                        }
                    });
                    reminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(reminder.isChecked()){
                                reminder_edit.setEnabled(true);
                            }
                            else
                            reminder_edit.setEnabled(false);
                        }
                    });


                   ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            settings.edit().putBoolean("home_wifi", wifi.isChecked()).commit();
                            settings.edit().putBoolean("home_data", data.isChecked()).commit();
                            settings.edit().putBoolean("home_silent", silent.isChecked()).commit();
                            settings.edit().putBoolean("home_notify", notify.isChecked()).commit();
                            settings.edit().putBoolean("home_reminder", reminder.isChecked()).commit();


                            settings.edit().putBoolean("home_wifi_state", wifi_toggle.isChecked()).commit();
                            settings.edit().putBoolean("home_data_state", data_toggle.isChecked()).commit();
                            settings.edit().putBoolean("home_silent_state", silent_toggle.isChecked()).commit();

                            settings.edit().putString("home_reminder_text", reminder_edit.getText().toString()).commit();
                            mControlsContainer.setVisibility(View.GONE);


                        }
                    });


                }

                if(work)
                {
                    wifi.setChecked(settings.getBoolean("work_wifi", false));
                    data.setChecked(settings.getBoolean("work_data", false));
                    silent.setChecked(settings.getBoolean("work_silent", false));
                    notify.setChecked(settings.getBoolean("work_notify", false));
                    reminder.setChecked(settings.getBoolean("work_reminder", false));

                    wifi_toggle.setChecked(settings.getBoolean("work_wifi_state", false));
                    data_toggle.setChecked(settings.getBoolean("work_data_state", false));
                    silent_toggle.setChecked(settings.getBoolean("work_silent_state", false));

                    reminder_edit.setText(settings.getString("work_reminder_text", ""));
                    if(!wifi.isChecked())
                        wifi_toggle.setEnabled(false);
                    if(!data.isChecked())
                        data_toggle.setEnabled(false);
                    if(!silent.isChecked())
                        silent_toggle.setEnabled(false);
                    if(!reminder.isChecked())
                        reminder_edit.setEnabled(false);
                    wifi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wifi.isChecked()) {
                                wifi_toggle.setEnabled(true);
                            }
                            else
                                wifi_toggle.setEnabled(false);
                        }
                    });
                    data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(data.isChecked()) {
                                data_toggle.setEnabled(true);
                            }
                            else
                                data_toggle.setEnabled(false);

                        }
                    });
                    silent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(silent.isChecked()) {
                                silent_toggle.setEnabled(true);
                            }
                            else
                                silent_toggle.setEnabled(false);
                        }
                    });
                    reminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(reminder.isChecked()){
                                reminder_edit.setEnabled(true);
                            }
                            else
                                reminder_edit.setEnabled(false);
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            settings.edit().putBoolean("work_wifi", wifi.isChecked()).commit();
                            settings.edit().putBoolean("work_data", data.isChecked()).commit();
                            settings.edit().putBoolean("work_silent", silent.isChecked()).commit();
                            settings.edit().putBoolean("work_notify", notify.isChecked()).commit();
                            settings.edit().putBoolean("work_reminder", reminder.isChecked()).commit();


                            settings.edit().putBoolean("work_wifi_state", wifi_toggle.isChecked()).commit();
                            settings.edit().putBoolean("work_data_state", data_toggle.isChecked()).commit();
                            settings.edit().putBoolean("work_silent_state", silent_toggle.isChecked()).commit();

                            settings.edit().putString("work_reminder_text", reminder_edit.getText().toString()).commit();
                            mControlsContainer.setVisibility(View.GONE);



                        }
                    });


                }


                if(other)
                {
                    wifi.setChecked(settings.getBoolean("other_wifi", false));
                    data.setChecked(settings.getBoolean("other_data", false));
                    silent.setChecked(settings.getBoolean("other_silent", false));
                    notify.setChecked(settings.getBoolean("other_notify", false));
                    reminder.setChecked(settings.getBoolean("other_reminder", false));

                    wifi_toggle.setChecked(settings.getBoolean("other_wifi_state", false));
                    data_toggle.setChecked(settings.getBoolean("other_data_state", false));
                    silent_toggle.setChecked(settings.getBoolean("other_silent_state", false));

                    reminder_edit.setText(settings.getString("other_reminder_text", ""));
                    if(!wifi.isChecked())
                        wifi_toggle.setEnabled(false);
                    if(!data.isChecked())
                        data_toggle.setEnabled(false);
                    if(!silent.isChecked())
                        silent_toggle.setEnabled(false);
                    if(!reminder.isChecked())
                        reminder_edit.setEnabled(false);

                    wifi.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(wifi.isChecked()) {
                                wifi_toggle.setEnabled(true);
                            }
                            else
                                wifi_toggle.setEnabled(false);
                        }
                    });
                    data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(data.isChecked()) {
                                data_toggle.setEnabled(true);
                            }
                            else
                                data_toggle.setEnabled(false);

                        }
                    });
                    silent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(silent.isChecked()) {
                                silent_toggle.setEnabled(true);
                            }
                            else
                                silent_toggle.setEnabled(false);
                        }
                    });
                    reminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(reminder.isChecked()){
                                reminder_edit.setEnabled(true);
                            }
                            else
                                reminder_edit.setEnabled(false);
                        }
                    });

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            settings.edit().putBoolean("other_wifi", wifi.isChecked()).commit();
                            settings.edit().putBoolean("other_data", data.isChecked()).commit();
                            settings.edit().putBoolean("other_silent", silent.isChecked()).commit();
                            settings.edit().putBoolean("other_notify", notify.isChecked()).commit();
                            settings.edit().putBoolean("other_reminder", reminder.isChecked()).commit();


                            settings.edit().putBoolean("other_wifi_state", wifi_toggle.isChecked()).commit();
                            settings.edit().putBoolean("other_data_state", data_toggle.isChecked()).commit();
                            settings.edit().putBoolean("other_silent_state", silent_toggle.isChecked()).commit();

                            settings.edit().putString("other_reminder_text", reminder_edit.getText().toString()).commit();

                            mControlsContainer.setVisibility(View.GONE);


                        }
                    });


                }




                Window window = getWindow();
                window.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
            }
        });
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
                            Location_event.this.getCurrentFocus().getWindowToken(),
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






        map.setOnMapLoadedCallback(Location_event.this);

        map.setOnCameraChangeListener(Location_event.this);
        CameraPosition camPos = new CameraPosition.Builder()

                .target(new LatLng(getLatitude(), getLongitude()))

                .zoom(12.8f)

                .build();

        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

        map.moveCamera(camUpdate);


        homem =  map.addMarker(homeo);
        homem.setVisible(false);
        otherm =  map.addMarker(othero);
        otherm.setVisible(false);
        tempm =  map.addMarker(tempo);
        tempm.setVisible(false);
        workm =  map.addMarker(worko);
        workm.setVisible(false);

        btn3 = (FloatingActionButton)findViewById(R.id.location);
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


        if(settings.getBoolean("home_geofence",false))
        {
            homem.setPosition(new LatLng(Double.parseDouble(settings.getString("home_geofence_lat", "0")), Double.parseDouble(settings.getString("home_geofence_lon", "0"))));

            homem.setTitle("Home");

            camPos = new CameraPosition.Builder()

                    .target(new LatLng(Double.parseDouble(settings.getString("home_geofence_lat", "0")), Double.parseDouble(settings.getString("home_geofence_lon", "0"))))
                    .zoom(12.8f)

                    .build();

            camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

            map.moveCamera(camUpdate);
            try {
               // homem.setTitle(address.toString());
            } catch (Exception e) {
               // Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

            }
            homem.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            homem.setVisible(true);
            homem.showInfoWindow();

        }



        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                try {
                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                    Boolean isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {

                        //  Toast.makeText(getApplicationContext(), " Please Enable Internet ", Toast.LENGTH_LONG).show();
                        Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                    } else {


                        FLat = point.latitude;
                        FLon = point.longitude;
                        //  Toast.makeText(getApplicationContext(), FLat.toString() + FLon.toString(), Toast.LENGTH_LONG).show();

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(context, Locale.getDefault());


                        try {
                            addresses = geocoder.getFromLocation(FLat, FLon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (Exception e) {

                        }
                        if (home) {
                            tempm.setPosition(new LatLng(FLat, FLon));

                            tempm.setTitle("Click '+' to Set this as Home");
                            try {
                                //  tempm.setTitle(address.toString());
                            } catch (Exception e) {
                                //    Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                            }
                            tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            tempm.setVisible(true);
                        }
                        if (work) {
                            tempm.setPosition(new LatLng(FLat, FLon));

                            tempm.setTitle("Click '+' to Set this as Work");
                            try {
                                //  tempm.setTitle(address.toString());
                            } catch (Exception e) {
                                //  Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                            }
                            tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            tempm.setVisible(true);
                        }

                        if (other) {
                            tempm.setPosition(new LatLng(FLat, FLon));

                            tempm.setTitle("Click '+' to Set this as Other");
                            try {
                                //tempm.setTitle(address.toString());
                            } catch (Exception e) {
                                //    Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                            }
                            tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            tempm.setVisible(true);
                        }

                        if (!home && !other && !work) {
                            //   Toast.makeText(getApplicationContext(), "Select Geofence category ", Toast.LENGTH_LONG).show();

                        }

                        mAddGeofencesButton.setVisibility(View.VISIBLE);


                    }
                } catch (Exception e) {
                    // Toast.makeText(getApplicationContext(), " Couldnt fetch location please check internet connection ", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Couldnt fetch location please check internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }
        });

        homem.showInfoWindow();
        otherm.showInfoWindow();
        workm.showInfoWindow();








        /**************************************************Animation Part**************************************/

        mFab = findViewById(R.id.sel);
        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);

        mFabContainer = (FrameLayout) findViewById(R.id.fab_container2);
        mControlsContainer = (RelativeLayout) findViewById(R.id.inflate);

        mControlsContainer.setVisibility(View.GONE);

        rev = false;

        /**************************************************Animation Part**************************************/

    }



    private void displayView(int position) {
        //Fragment fragment = null;
        String title = getString(R.string.app_name);
        Intent i;
        switch (position) {
            case 0:
                finish();
                Pre_launch_activity.post=0;
                title = "digiPune";
                break;
            case 1:
                finish();
                i = new Intent(this, MyLocation.class);
                startActivity(i);
                title = "My Location";
                break;
            case 2:

                title ="GeoFencing";
                break;
            case 3:
                finish();
                i = new Intent(this, Setting.class);
                startActivity(i);
                title ="Settings";
                break;
            case 4:
                finish();
                i = new Intent(this, SOS_Contacts.class);
                startActivity(i);
                title ="SOS Contacts";
                break;
            case 5:
                finish();
                i = new Intent(this, Favourites.class);
                startActivity(i);
                title ="Favourites";
                break;
            case 6:
                finish();
                i = new Intent(this, History.class);
                startActivity(i);
                title ="History";
                break;

            case 7:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","contact@skylinelabs.in", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "digiPune feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi ! It was an awesome experience using digiPune! ");
                startActivity(Intent.createChooser(intent, "Contact developers"));
                break;
            case 8:
                finish();
                i = new Intent(this, AboutDevelopers.class);
                startActivity(i);
                title ="About";
                break;
            case 9:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                }
                catch(Exception e)
                {

                }
                break;

            case 10:
                finish();
                i = new Intent(this, Privacypolicy.class);
                startActivity(i);
                title ="Privacy";
                break;

            default:
                break;
        }
    }




    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @Override
    public void onLocationChanged(Location location) {

        map.setOnMapLoadedCallback(Location_event.this);
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
            locationManager.removeUpdates(Location_event.this);
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
        map.setOnCameraChangeListener(Location_event.this);
        map.setOnMapLoadedCallback(Location_event.this);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        /*
        if(!settings.getBoolean("other_geofence",false) && !settings.getBoolean("home_geofence",false) && !settings.getBoolean("work_geofence",false))
        {
            Toast.makeText(getApplicationContext(), "Geo fence not set", Toast.LENGTH_LONG).show();

        }*/



        if(id == R.id.info) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    Location_event.this);
            LayoutInflater factory = LayoutInflater.from(Location_event.this);

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

            search();
            InputMethodManager inputManager =
                    (InputMethodManager) context.
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    Location_event.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }



        return super.onOptionsItemSelected(item);
    }


    void search()
    {
        addresstxt = edt1.getText().toString();
        new GetLocation().execute();
    }

    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("Home");
        list.add("Work");
        list.add("Other");

        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(
                getApplicationContext(), list);

        spinner_nav.setAdapter(spinAdapter);

        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                // On selecting a spinner item
                final String PREFS_NAME = "GeoPreferences";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

                String item = adapter.getItemAtPosition(position).toString();
                if (item == "Home") {
                    if (settings.getBoolean("home_geofence", false)) {
                        homem.setPosition(new LatLng(Double.parseDouble(settings.getString("home_geofence_lat", "0")), Double.parseDouble(settings.getString("home_geofence_lon", "0"))));

                        homem.setSnippet("Home");

                        CameraPosition camPos = new CameraPosition.Builder()

                                .target(new LatLng(Double.parseDouble(settings.getString("home_geofence_lat", "0")), Double.parseDouble(settings.getString("home_geofence_lon", "0"))))
                                .zoom(12.8f)

                                .build();

                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                        map.moveCamera(camUpdate);
                        try {
                            // homem.setTitle(address.toString());
                        } catch (Exception e) {
                            //  Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                        }
                        homem.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        homem.setVisible(true);
                        otherm.hideInfoWindow();
                        homem.showInfoWindow();
                        workm.hideInfoWindow();

                    }
                    homem.setVisible(true);
                    otherm.hideInfoWindow();
                    homem.showInfoWindow();
                    workm.hideInfoWindow();

                    otherm.setVisible(false);
                    workm.setVisible(false);
                    tempm.setVisible(false);

                    home = true;
                    other = false;
                    work = false;
                    if (settings.getBoolean("home_geofence", false)) {
                        //   Toast.makeText(getApplicationContext(), "Home already exists", Toast.LENGTH_LONG).show();

                    }
                }

                if (item == "Work") {
                    if (settings.getBoolean("work_geofence", false)) {
                        workm.setPosition(new LatLng(Double.parseDouble(settings.getString("work_geofence_lat", "0")), Double.parseDouble(settings.getString("work_geofence_lon", "0"))));

                        workm.setTitle("Work");

                        CameraPosition camPos = new CameraPosition.Builder()

                                .target(new LatLng(Double.parseDouble(settings.getString("work_geofence_lat", "0")), Double.parseDouble(settings.getString("work_geofence_lon", "0"))))
                                .zoom(12.8f)

                                .build();

                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                        map.moveCamera(camUpdate);

                        workm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        workm.setVisible(true);


                        workm.showInfoWindow();
                        otherm.hideInfoWindow();
                        homem.hideInfoWindow();
                    }

                    workm.setVisible(true);
                    workm.showInfoWindow();
                    otherm.hideInfoWindow();
                    homem.hideInfoWindow();
                    homem.setVisible(false);
                    otherm.setVisible(false);
                    tempm.setVisible(false);


                    home = false;
                    other = false;
                    work = true;
                    if (settings.getBoolean("work_geofence", false)) {
                        //       Toast.makeText(getApplicationContext(), "Work already exists", Toast.LENGTH_LONG).show();

                    }
                }

                if (item == "Other") {
                    if (settings.getBoolean("other_geofence", false)) {
                        otherm.setPosition(new LatLng(Double.parseDouble(settings.getString("other_geofence_lat", "0")), Double.parseDouble(settings.getString("other_geofence_lon", "0"))));

                        otherm.setTitle("Other");

                        CameraPosition camPos = new CameraPosition.Builder()

                                .target(new LatLng(Double.parseDouble(settings.getString("other_geofence_lat", "0")), Double.parseDouble(settings.getString("other_geofence_lon", "0"))))
                                .zoom(12.8f)

                                .build();

                        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

                        map.moveCamera(camUpdate);

                        try {
                            // otherm.setTitle(address.toString());
                        } catch (Exception e) {
                            //Toast.makeText(getApplicationContext(), "Unable to fetch address", Toast.LENGTH_LONG).show();

                        }
                        otherm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        otherm.setVisible(true);


                        otherm.showInfoWindow();
                        homem.hideInfoWindow();
                        workm.hideInfoWindow();

                    }
                    otherm.setVisible(true);

                    otherm.showInfoWindow();
                    homem.hideInfoWindow();
                    workm.hideInfoWindow();


                    workm.setVisible(false);
                    homem.setVisible(false);
                    tempm.setVisible(false);


                    home = false;
                    other = true;
                    work = false;
                    if (settings.getBoolean("other_geofence", false)) {
                        //   Toast.makeText(getApplicationContext(), "Other already exists", Toast.LENGTH_LONG).show();

                    }
                }
                // Showing selected spinner item
                // Toast.makeText(getApplicationContext(), "Selected  : " + item,
                //         Toast.LENGTH_LONG).show();

                Snackbar.make(findViewById(android.R.id.content), "Selected  : " + item, Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }



    int array_loader(double curr_lat,double curr_lon,double centre_lat,double centre_lon,double radius)
    {


             a[0]=geofence(curr_lat, curr_lon, centre_lat, centre_lon, radius);
    //    Toast.makeText(getApplicationContext(), String.valueOf(a[0]) , Toast.LENGTH_LONG).show();

     //   Toast.makeText(getApplicationContext(), String.valueOf(a[0])+"  " + String.valueOf(a[1]) , Toast.LENGTH_LONG).show();


        if(a[0]==0&&a[1]==0)
            {   a[1]=a[0];
                return 2;} //NO change

            if(a[0]==1&&a[1]==1)
            {a[1]=a[0];
                return 2;} //No change

            if(a[0]==1&&a[1]==0)
            {a[1]=a[0];
                return 1;}  //came in

            if(a[0]==0&&a[1]==1)
            {a[1]=a[0];
                return 0;} // went out




        return 5;



    }

    int geofence(double curr_lat,double curr_lon,double centre_lat,double centre_lon,double radius)
    {
        double k;
        k=Math.sqrt(Math.sqrt(((curr_lat-centre_lat)*(curr_lat-centre_lat)-(curr_lon-centre_lon)*(curr_lon-centre_lon))*((curr_lat-centre_lat)*(curr_lat-centre_lat)-(curr_lon-centre_lon)*(curr_lon-centre_lon))));
       // Toast.makeText(getApplicationContext(), String.valueOf(k) , Toast.LENGTH_LONG).show();

        if(k<radius)
        {return 1;}
        else
        {return 0;}

    }







    public void onFabPressed(View view) {

        if(!rev) {

            spinner_nav.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                enterReveal();
            } else {
                mControlsContainer.setVisibility(View.VISIBLE);
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.animate(addb).rotation(135f).withLayer().setDuration(300).setInterpolator(interpolator).start();

            }


            ClickStuff();
            rev = true;

        }

        else
        {
            spinner_nav.setVisibility(View.VISIBLE);
          rev = false;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                exitReveal();
            }

            else
            {
                mControlsContainer.setVisibility(View.GONE);
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.animate(addb).rotation(0f).withLayer().setDuration(300).setInterpolator(interpolator).start();

            }
        }

    }




    void ClickStuff()
    {

        final String PREFS_NAME = "GeoPreferences";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        // setContentView(R.layout.alert);
        final Switch wifi_toggle, data_toggle, silent_toggle;
        final CheckBox wifi, data, silent,notify, reminder;
        final EditText reminder_edit;

        wifi_toggle = (Switch) findViewById(R.id.wifi_switch);
        data_toggle = (Switch)findViewById(R.id.data_switch);
        silent_toggle = (Switch) findViewById(R.id.silent_switch);

        wifi= (CheckBox) findViewById(R.id.wifi_check);
        data= (CheckBox) findViewById(R.id.data_check);
        silent= (CheckBox) findViewById(R.id.silent_check);
        notify= (CheckBox) findViewById(R.id.notify_check);
        reminder= (CheckBox) findViewById(R.id.reminder_check);

        reminder_edit = (EditText) findViewById(R.id.reminder_editText);
        Button ok = (Button)findViewById(R.id.ok);

        if(home)
        {
            wifi.setChecked(settings.getBoolean("home_wifi", false));
            data.setChecked(settings.getBoolean("home_data", false));
            silent.setChecked(settings.getBoolean("home_silent", false));
            notify.setChecked(settings.getBoolean("home_notify", false));
            reminder.setChecked(settings.getBoolean("home_reminder", false));

            wifi_toggle.setChecked(settings.getBoolean("home_wifi_state", false));
            data_toggle.setChecked(settings.getBoolean("home_data_state", false));
            silent_toggle.setChecked(settings.getBoolean("home_silent_state", false));

            reminder_edit.setText(settings.getString("home_reminder_text", ""));

            if(!wifi.isChecked())
                wifi_toggle.setEnabled(false);
            if(!data.isChecked())
                data_toggle.setEnabled(false);
            if(!silent.isChecked())
                silent_toggle.setEnabled(false);
            if(!reminder.isChecked())
                reminder_edit.setEnabled(false);

            wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(wifi.isChecked()) {
                        wifi_toggle.setEnabled(true);
                    }
                    else
                        wifi_toggle.setEnabled(false);
                }
            });
            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.isChecked()) {
                        data_toggle.setEnabled(true);
                    }
                    else
                        data_toggle.setEnabled(false);

                }
            });
            silent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(silent.isChecked()) {
                        silent_toggle.setEnabled(true);
                    }
                    else
                        silent_toggle.setEnabled(false);
                }
            });
            reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reminder.isChecked()){
                        reminder_edit.setEnabled(true);
                    }
                    else
                        reminder_edit.setEnabled(false);
                }
            });


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    spinner_nav.setVisibility(View.VISIBLE);

                    settings.edit().putBoolean("home_wifi", wifi.isChecked()).commit();
                    settings.edit().putBoolean("home_data", data.isChecked()).commit();
                    settings.edit().putBoolean("home_silent", silent.isChecked()).commit();
                    settings.edit().putBoolean("home_notify", notify.isChecked()).commit();
                    settings.edit().putBoolean("home_reminder", reminder.isChecked()).commit();
                    settings.edit().putBoolean("home_wifi_state", wifi_toggle.isChecked()).commit();
                    settings.edit().putBoolean("home_data_state", data_toggle.isChecked()).commit();
                    settings.edit().putBoolean("home_silent_state", silent_toggle.isChecked()).commit();
                    settings.edit().putString("home_reminder_text", reminder_edit.getText().toString()).commit();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        exitReveal();
                    }

                    else
                    {
                        mControlsContainer.setVisibility(View.GONE);
                    }



                }
            });


        }

        if(work)
        {
            wifi.setChecked(settings.getBoolean("work_wifi", false));
            data.setChecked(settings.getBoolean("work_data", false));
            silent.setChecked(settings.getBoolean("work_silent", false));
            notify.setChecked(settings.getBoolean("work_notify", false));
            reminder.setChecked(settings.getBoolean("work_reminder", false));

            wifi_toggle.setChecked(settings.getBoolean("work_wifi_state", false));
            data_toggle.setChecked(settings.getBoolean("work_data_state", false));
            silent_toggle.setChecked(settings.getBoolean("work_silent_state", false));

            reminder_edit.setText(settings.getString("work_reminder_text", ""));
            if(!wifi.isChecked())
                wifi_toggle.setEnabled(false);
            if(!data.isChecked())
                data_toggle.setEnabled(false);
            if(!silent.isChecked())
                silent_toggle.setEnabled(false);
            if(!reminder.isChecked())
                reminder_edit.setEnabled(false);
            wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(wifi.isChecked()) {
                        wifi_toggle.setEnabled(true);
                    }
                    else
                        wifi_toggle.setEnabled(false);
                }
            });
            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.isChecked()) {
                        data_toggle.setEnabled(true);
                    }
                    else
                        data_toggle.setEnabled(false);

                }
            });
            silent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(silent.isChecked()) {
                        silent_toggle.setEnabled(true);
                    }
                    else
                        silent_toggle.setEnabled(false);
                }
            });
            reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reminder.isChecked()){
                        reminder_edit.setEnabled(true);
                    }
                    else
                        reminder_edit.setEnabled(false);
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    settings.edit().putBoolean("work_wifi", wifi.isChecked()).commit();
                    settings.edit().putBoolean("work_data", data.isChecked()).commit();
                    settings.edit().putBoolean("work_silent", silent.isChecked()).commit();
                    settings.edit().putBoolean("work_notify", notify.isChecked()).commit();
                    settings.edit().putBoolean("work_reminder", reminder.isChecked()).commit();


                    settings.edit().putBoolean("work_wifi_state", wifi_toggle.isChecked()).commit();
                    settings.edit().putBoolean("work_data_state", data_toggle.isChecked()).commit();
                    settings.edit().putBoolean("work_silent_state", silent_toggle.isChecked()).commit();

                    settings.edit().putString("work_reminder_text", reminder_edit.getText().toString()).commit();



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        exitReveal();
                    }

                    else
                    {
                        mControlsContainer.setVisibility(View.GONE);
                    }



                }
            });


        }


        if(other)
        {
            wifi.setChecked(settings.getBoolean("other_wifi", false));
            data.setChecked(settings.getBoolean("other_data", false));
            silent.setChecked(settings.getBoolean("other_silent", false));
            notify.setChecked(settings.getBoolean("other_notify", false));
            reminder.setChecked(settings.getBoolean("other_reminder", false));

            wifi_toggle.setChecked(settings.getBoolean("other_wifi_state", false));
            data_toggle.setChecked(settings.getBoolean("other_data_state", false));
            silent_toggle.setChecked(settings.getBoolean("other_silent_state", false));

            reminder_edit.setText(settings.getString("other_reminder_text", ""));
            if(!wifi.isChecked())
                wifi_toggle.setEnabled(false);
            if(!data.isChecked())
                data_toggle.setEnabled(false);
            if(!silent.isChecked())
                silent_toggle.setEnabled(false);
            if(!reminder.isChecked())
                reminder_edit.setEnabled(false);

            wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(wifi.isChecked()) {
                        wifi_toggle.setEnabled(true);
                    }
                    else
                        wifi_toggle.setEnabled(false);
                }
            });
            data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.isChecked()) {
                        data_toggle.setEnabled(true);
                    }
                    else
                        data_toggle.setEnabled(false);

                }
            });
            silent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(silent.isChecked()) {
                        silent_toggle.setEnabled(true);
                    }
                    else
                        silent_toggle.setEnabled(false);
                }
            });
            reminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(reminder.isChecked()){
                        reminder_edit.setEnabled(true);
                    }
                    else
                        reminder_edit.setEnabled(false);
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    settings.edit().putBoolean("other_wifi", wifi.isChecked()).commit();
                    settings.edit().putBoolean("other_data", data.isChecked()).commit();
                    settings.edit().putBoolean("other_silent", silent.isChecked()).commit();
                    settings.edit().putBoolean("other_notify", notify.isChecked()).commit();
                    settings.edit().putBoolean("other_reminder", reminder.isChecked()).commit();


                    settings.edit().putBoolean("other_wifi_state", wifi_toggle.isChecked()).commit();
                    settings.edit().putBoolean("other_data_state", data_toggle.isChecked()).commit();
                    settings.edit().putBoolean("other_silent_state", silent_toggle.isChecked()).commit();

                    settings.edit().putString("other_reminder_text", reminder_edit.getText().toString()).commit();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        exitReveal();
                    }

                    else
                    {
                        mControlsContainer.setVisibility(View.GONE);
                    }


                }
            });

        }

    }


    void enterReveal() {

        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(addb).rotation(135f).withLayer().setDuration(300).setInterpolator(interpolator).start();


        final View myView = findViewById(R.id.map);


        try{
            // get the center for the clipping circle
            int cx = myView.getMeasuredWidth();
            int cy = 0;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth() * 2, myView.getHeight() * 2) / 2;

       //     Toast.makeText(getApplicationContext(), finalRadius + "   " + cx + "   " + cy, Toast.LENGTH_LONG).show();

            // create the animator for this view (the start radius is zero)

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mControlsContainer, cx, cy, 0, finalRadius);

            anim.start();

        }

        catch(Exception e)
        {


        }

        mControlsContainer.setVisibility(View.VISIBLE);

        // previously invisible view


    }



        void exitReveal() {

            final OvershootInterpolator interpolator = new OvershootInterpolator();
            ViewCompat.animate(addb).rotation(0f).withLayer().setDuration(300).setInterpolator(interpolator).start();

            final View myView = findViewById(R.id.inflate);

        try{
            // previously visible view

            // get the center for the clipping circle
            int cx = myView.getMeasuredWidth() - 25;
            int cy = 0;

            // get the final radius for the clipping circle
            int initialRadius = Math.max(myView.getWidth() * 2, myView.getHeight() * 2) / 2;

        //    Toast.makeText(getApplicationContext(), initialRadius + "   " + cx + "   " + cy, Toast.LENGTH_LONG).show();

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
    }

        catch(Exception e)
        {
            myView.setVisibility(View.GONE);

        }



    }




    /*******************************************************************************/

    class GetLocation extends AsyncTask<String, String, String> {

        JSONObject json, json2;
        Boolean i,j,k = false;


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
                            //Toast.makeText(getApplicationContext(), "Location not found, try again error step 1",
                            // .LENGTH_LONG).show();

                        }

                        Address location = listadd.get(0);
                        FLat = location.getLatitude();
                        FLon = location.getLongitude();


                        runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {


                                // Toast.makeText(getApplicationContext(), "  " + FLat + "   " + FLon, Toast.LENGTH_LONG).show();

                    if (home) {
                        tempm.setPosition(new LatLng(FLat, FLon));
                        tempm.setTitle(addresstxt + "\n");
                        tempm.setSnippet("Home");
                        tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        tempm.setVisible(true);
                        mAddGeofencesButton.setVisibility(View.VISIBLE);
                    }

                    if (work) {
                        tempm.setPosition(new LatLng(FLat, FLon));
                        tempm.setTitle(addresstxt + "\n");
                        tempm.setSnippet("Work");
                        tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        tempm.setVisible(true);
                        mAddGeofencesButton.setVisibility(View.VISIBLE);
                    }

                    if (other) {
                        tempm.setPosition(new LatLng(FLat, FLon));
                        tempm.setTitle(addresstxt + "\n");
                        tempm.setSnippet("Other");
                        tempm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        tempm.setVisible(true);
                        mAddGeofencesButton.setVisibility(View.VISIBLE);
                    }

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
}



/************************************************/



