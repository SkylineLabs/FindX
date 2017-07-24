package in.skylinelabs.digiPune.activity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import in.skylinelabs.digiPune.R;


public class digiPune extends Service implements android.location.LocationListener
{

    String reminder_text;
    int a[]={0,0};
    int b[]={0,0};
    int c[]={0,0};

    /*****************************************GPSVariables***********************************************/
    //flag for GPS Status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    public static Boolean isOn = false;
    boolean canGetLocation = false;

    String networkTS;
    long date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Location location;
    double latitude;
    double longitude;

    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; //0 metters

    //The minimum time beetwen updates in milliseconds
    public static long MIN_TIME_BW_UPDATES = 1000 * 30 * 1  ; // 10 minutes

    //Declaring a Location Manager
    protected LocationManager locationManager;

    /**********************************************************************************************/


    String mystringLatitude;
    String mystringLongitude;

    static String mydevice_id, password;
    JSONParser jsonParser = new JSONParser();
    private static final String url_update_product = "http://www.skylinelabs.in/Geo/update_app.php";

    public static boolean mRunning = false;

    Boolean warned,internetw = false;

    @Override
    public void onCreate()
    {
        mRunning = true;
        super.onCreate();


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
            locationManager.removeUpdates(digiPune.this);
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


    /**
     * Get list of address by latitude and longitude
     * @return null or List<Address>
     */
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

    /**
     * Try to get AddressLine
     * @return null or addressLine
     */
    public String getAddressLine(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        }
        else
        {
            return null;
        }
    }

    /**
     * Try to get Locality
     * @return null or locality
     */
    public String getLocality(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            return locality;
        }
        else
        {
            return null;
        }
    }

    /**
     * Try to get Postal Code
     * @return null or postalCode
     */
    public String getPostalCode(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();

            return postalCode;
        }
        else
        {
            return null;
        }
    }

    /**
     * Try to get CountryName
     * @return null or postalCode
     */
    public String getCountryName(Context context)
    {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0)
        {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();

            return countryName;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        mRunning = true;



        ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent1 = cd1.isConnectingToInternet();
        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders != null || !locationProviders.equals(""))
        {
            //warned = false;

            /************************************************GeoFence*****************************************************/
            if(settings.getBoolean("home_geofence", false))
            {
                int x = array_loader_a(getLatitude(),getLongitude(),Double.parseDouble(settings.getString("home_geofence_lat", "0")), Double.parseDouble(settings.getString("home_geofence_lon", "0")),0.005);
                if(x==0)    //Baher Gela
                {

                }

                if(x==1)   //Aaat Aala
                {


                    if(settings.getBoolean("home_wifi", false))
                    {

                        if((settings.getBoolean("home_wifi_state", false)))
                        {
                            startWifi();
                        }
                        else
                        {
                            stopWifi();
                        }
                    }
                    if(settings.getBoolean("home_data", false))
                    {
                        if((settings.getBoolean("home_data_state", false)))
                        {
                            startData();
                        }
                        else
                        {
                            stopData();
                        }
                    }
                    if(settings.getBoolean("home_silent", false))
                    {
                        if((settings.getBoolean("home_silent_state", false)))
                        {
                            startSilent();
                        }
                        else
                        {
                            stopSilent();
                        }
                    }
                    if(settings.getBoolean("home_notify", false))
                    {
                        donotify();
                    }
                    if(settings.getBoolean("home_reminder", false))
                    {
                        reminder_text = settings.getString("home_reminder_text","");
                        reminder();
                    }



                }

                if(x==2)    //Kahich nahi zhala
                {

                }

            }

            if(settings.getBoolean("work_geofence", false))
            {
                int x = array_loader_b(getLatitude(),getLongitude(),Double.parseDouble(settings.getString("work_geofence_lat", "0")), Double.parseDouble(settings.getString("work_geofence_lon", "0")),0.005);
                if(x==0)    //Baher Gela
                {

                }

                if(x==1)   //Aaat Aala
                {


                    if(settings.getBoolean("work_wifi", false))
                    {

                        if((settings.getBoolean("work_wifi_state", false)))
                        {
                            startWifi();
                        }
                        else
                        {
                            stopWifi();
                        }
                    }
                    if(settings.getBoolean("work_data", false))
                    {
                        if((settings.getBoolean("work_data_state", false)))
                        {
                            startData();
                        }
                        else
                        {
                            stopData();
                        }
                    }
                    if(settings.getBoolean("work_silent", false))
                    {
                        if((settings.getBoolean("work_silent_state", false)))
                        {
                            startSilent();
                        }
                        else
                        {
                            stopSilent();
                        }
                    }
                    if(settings.getBoolean("work_notify", false))
                    {
                        donotify();
                    }
                    if(settings.getBoolean("work_reminder", false))
                    {
                        reminder_text = settings.getString("home_reminder_text","");
                        reminder();
                    }




                }

                if(x==2)    //Kahich nahi zhala
                {

                }

            }

            if(settings.getBoolean("other_geofence", false))
            {
                int x = array_loader_c(getLatitude(),getLongitude(),Double.parseDouble(settings.getString("other_geofence_lat", "0")), Double.parseDouble(settings.getString("other_geofence_lon", "0")),0.005);
                if(x==0)   //Baher Gela
                {

                }

                if(x==1)   //Aaat Aala
                {


                    if(settings.getBoolean("other_wifi", false))
                    {

                        if((settings.getBoolean("other_wifi_state", false)))
                        {
                            startWifi();
                        }
                        else
                        {
                            stopWifi();
                        }
                    }
                    if(settings.getBoolean("other_data", false))
                    {
                        if((settings.getBoolean("other_data_state", false)))
                        {
                            startData();
                        }
                        else
                        {
                            stopData();
                        }
                    }
                    if(settings.getBoolean("other_silent", false))
                    {
                        if((settings.getBoolean("other_silent_state", false)))
                        {
                            startSilent();
                        }
                        else
                        {
                            stopSilent();
                        }
                    }
                    if(settings.getBoolean("other_notify", false))
                    {
                        donotify();
                    }
                    if(settings.getBoolean("other_reminder", false))
                    {
                        reminder_text = settings.getString("other_reminder_text","");
                        reminder();
                    }




                }

                if(x==2)    //Kahich nahi zhala
                {

                }

            }

/**************************************************GeoFence******************************************************/


            if(settings.getBoolean("FindX_background_enabled", false) || isOn)
            {
                if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null || locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
                {




                    if(isInternetPresent1) {
                        //internetw = false;

                        mydevice_id = settings.getString("user_name", "");
                        password = settings.getString("password", "");
                        date = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
                        networkTS = sdf.format(date);

                        this.location = location;
                        mystringLatitude = String.valueOf(getLatitude());
                        mystringLongitude = String.valueOf(getLongitude());
                        new CreateNewProduct().execute();
                    }
                    else
                    {


                    }
                }
                else
                {

                }

            }

            else
            {
                //Toast.makeText(this,"Service update bool false",Toast.LENGTH_SHORT).show();
            }

        }

        else
        {

        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        if(!mRunning)
        {
            runFindX();
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        /*************************************Notify***************************************/

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        Intent resultIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Location_event.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(02,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSmallIcon(R.drawable.findx_logo);
        mBuilder.setContentTitle("digiPune Disconnected");
        mBuilder.setContentText("Please turn on Location");
        mBuilder.addAction(R.drawable.ic_action_settings, "Settings", resultPendingIntent);


// Adds the Intent that starts the Activity to the top of the stack


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

        /*************************************Notify***************************************/




    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, digiPune.class));
        mRunning = false;
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
            //  Toast.makeText(getApplicationContext(),"Oops ! Unable to fetch location ",Toast.LENGTH_SHORT).show();
            return null;
        }

        return location;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders != null || !locationProviders.equals(""))
        {
            locationManager = (LocationManager)this.getBaseContext().getSystemService(LOCATION_SERVICE);
            if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null || locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {


                runFindX();
                mRunning = true;

            }

            else
            {
                /*************************************Notify***************************************/

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

                Intent resultIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(Location_event.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(02,PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentIntent(resultPendingIntent);
                mBuilder.setSmallIcon(R.drawable.findx_logo);
                mBuilder.setContentTitle("digiPune Disconnected");
                mBuilder.setContentText("Please turn on Location");
                mBuilder.addAction(R.drawable.ic_action_settings, "Settings", resultPendingIntent);


// Adds the Intent that starts the Activity to the top of the stack


                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                mNotificationManager.notify(0, mBuilder.build());

                /*************************************Notify***************************************/

                mRunning = false;
            }
        }

        return START_STICKY;
    }


    class CreateNewProduct extends AsyncTask<String, String, String> {

        JSONObject json, json1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        protected String doInBackground(String... args) {


            final String PREFS_NAME = "GeoPreferences";
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("latitude",mystringLatitude));
            params.add(new BasicNameValuePair("longitude",mystringLongitude));

            TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tMgr.getDeviceId();
            params.add(new BasicNameValuePair("IMEI",imei));

            params.add(new BasicNameValuePair("time",sdf.format(date)));
            if(settings.getBoolean("FindX_share_enabled", true))
                params.add(new BasicNameValuePair("sharing_on","1"));
            else
                params.add(new BasicNameValuePair("sharing_on","0"));


            if(settings.getBoolean("FindX_history_enabled", true))
                params.add(new BasicNameValuePair("history","1"));
            else
                params.add(new BasicNameValuePair("history","0"));


            if(settings.getBoolean("FindX_onDuty_enabled", true))
                params.add(new BasicNameValuePair("onDuty","1"));
            else
                params.add(new BasicNameValuePair("onDuty","0"));


            try {

                JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                        "POST", params);
            }

            catch (Exception e1) {
            }

            try {


                Log.d("Create Response", json.toString());
                int success = json.getInt("success");

                //UserID doesnt exist, create new product
                if (success == 0) {

                }
            }
            catch(Exception e)
            {

            }


            return null;
        }


    }


    public void runFindX()
    {

        getLocation();
        mRunning = true;

        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        mydevice_id = settings.getString("user_name", "");
        password = settings.getString("password", "");


        if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
        {

            mystringLatitude = String.valueOf(getLatitude());
            mystringLongitude = String.valueOf(getLongitude());

            date = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
            networkTS = sdf.format(date);
        }

        //Toast.makeText(this,"digiPune background service started",Toast.LENGTH_SHORT).show();

    }

    int array_loader_a(double curr_lat,double curr_lon,double centre_lat,double centre_lon,double radius)
    {


        a[0]=geofence(curr_lat,curr_lon,centre_lat,centre_lon,radius);
        //    Toast.makeText(getApplicationContext(), String.valueOf(a[0]) , Toast.LENGTH_LONG).show();

        // Toast.makeText(getApplicationContext(), String.valueOf(a[0]) + "  " + String.valueOf(a[1]),
        //
        // .LENGTH_LONG).show();


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
        k=Math.sqrt(Math.sqrt(((curr_lat - centre_lat) * (curr_lat - centre_lat) - (curr_lon - centre_lon) * (curr_lon - centre_lon)) * ((curr_lat - centre_lat) * (curr_lat - centre_lat) - (curr_lon - centre_lon) * (curr_lon - centre_lon))));
        // Toast.makeText(getApplicationContext(), String.valueOf(k) , Toast.LENGTH_LONG).show();

        if(k<radius)
        {return 1;}
        else
        {return 0;}

    }

    int array_loader_b(double curr_lat,double curr_lon,double centre_lat,double centre_lon,double radius)
    {


        b[0]=geofence(curr_lat,curr_lon,centre_lat,centre_lon,radius);
        //    Toast.makeText(getApplicationContext(), String.valueOf(a[0]) , Toast.LENGTH_LONG).show();

        //  Toast.makeText(getApplicationContext(), String.valueOf(b[0]) + "  " + String.valueOf(b[1]), Toast.LENGTH_LONG).show();


        if(b[0]==0&&b[1]==0)
        {   b[1]=b[0];
            return 2;} //NO change

        if(b[0]==1&&b[1]==1)
        {b[1]=b[0];
            return 2;} //No change

        if(b[0]==1&&b[1]==0)
        {b[1]=b[0];
            return 1;}  //came in

        if(b[0]==0&&b[1]==1)
        {b[1]=b[0];
            return 0;} // went out


        return 5;

    }

    int array_loader_c(double curr_lat,double curr_lon,double centre_lat,double centre_lon,double radius)
    {


        c[0]=geofence(curr_lat,curr_lon,centre_lat,centre_lon,radius);
        //    Toast.makeText(getApplicationContext(), String.valueOf(a[0]) , Toast.LENGTH_LONG).show();

        //  Toast.makeText(getApplicationContext(), String.valueOf(c[0]) + "  " + String.valueOf(c[1]), Toast.LENGTH_LONG).show();


        if(c[0]==0&&c[1]==0)
        {c[1]=c[0];
            return 2;} //NO change

        if(c[0]==1&&c[1]==1)
        {c[1]=c[0];
            return 2;} //No change

        if(c[0]==1&&c[1]==0)
        {c[1]=c[0];
            return 1;}  //came in

        if(c[0]==0&&c[1]==1)
        {c[1]=c[0];
            return 0;} // went out


        return 5;

    }



    void startWifi()
    {

        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(true);

    }

    void stopWifi(){
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
            wifiManager.setWifiEnabled(false);
    }

    void startData(){
        try {
            setMobileDataEnabled(this.getApplicationContext(), true);
        }
        catch(Exception e){
            //    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.drawable.sos);
            mBuilder.setContentTitle("Reminder !");
            mBuilder.setContentText(e.toString());

            Intent resultIntent = new Intent(this, Location_event.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(Location_event.class);

// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

        }
    }
    void stopData()
    {
        try {
            setMobileDataEnabled(this.getApplicationContext(), false);
        }
        catch(Exception e){
            // Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.drawable.sos);
            mBuilder.setContentTitle("Reminder !");
            mBuilder.setContentText(e.toString());

            Intent resultIntent = new Intent(this, Location_event.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(Location_event.class);

// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

        }
    }

    void reminder()
    {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setSmallIcon(R.drawable.sos);
        mBuilder.setContentTitle("Reminder !");
        mBuilder.setContentText(reminder_text);

        Intent resultIntent = new Intent(this, Location_event.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Location_event.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());

    }

    void startSilent()
    {
        final AudioManager mobilemode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mobilemode.setRingerMode(AudioManager.RINGER_MODE_SILENT);

    }

    void stopSilent()
    {
        final AudioManager mobilemode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

    }



    void donotify()
    {

    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        try{
            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        }

        catch(Exception e){
            //Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setSmallIcon(R.drawable.sos);
            mBuilder.setContentTitle("Reminder !");
            mBuilder.setContentText(e.toString());

            Intent resultIntent = new Intent(this, Location_event.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(Location_event.class);

// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

        }
    }

}


