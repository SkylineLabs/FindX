package in.skylinelabs.digiPune.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationListener;

import in.skylinelabs.digiPune.R;
public class Log_in extends ActionBarActivity implements LocationListener {

    Button btn;
    EditText edttxt1,edttxt2, edttxt3, edttxt4, edttxt5;

    Boolean error;
    TextView txt1, txt2, help;
    AlertDialog alertDialog1;

    Button txt;

    private ProgressDialog pDialog;

    GPSTracker gps;

    TextInputLayout lNameLayout,lNameLayout1,lNameLayout2,lNameLayout4,lNameLayout5;

    AlertDialog alertDialog;

    String email;

    long date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    String networkTS;

    Date date1;

    Bundle savedInstanceState;

    String mystringLatitude;
    String mystringLongitude;

    String name;

    static String mydevice_id, password;

    final Context context = this;

    private static final String url_create_product = "http://www.skylinelabs.in/Geo/signup_app.php";
    private static final String url_product_exists = "http://www.skylinelabs.in/Geo/get_product_exists.php";
    private static final String url_check_password =  "http://www.skylinelabs.in/Geo/check_pass.php";
    private static final String url_login = "http://www.skylinelabs.in/Geo/login.php";
    JSONParser jsonParser = new JSONParser();


    GoogleCloudMessaging gcm;
    String regid = "";

    String myPhoneNumber;


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {


            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_in);
        //this.getActionBar().hide();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);




        txt = (Button) findViewById(R.id.textView5);
        txt.getBackground().setColorFilter(Color.parseColor("#1565c0"), PorterDuff.Mode.MULTIPLY);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Log_in.this, Sign_up.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(i,
                            ActivityOptions.makeSceneTransitionAnimation(Log_in.this).toBundle());
                } else {
                    startActivity(i);
                }
                finish();
            }
        });


        lNameLayout = (TextInputLayout) findViewById(R.id
                .fNameLayout);
        lNameLayout1 = (TextInputLayout) findViewById(R.id
                .fNameLayout2);
        lNameLayout2 = (TextInputLayout) findViewById(R.id
                .fNameLayout3);
        lNameLayout4 = (TextInputLayout) findViewById(R.id
                .fNameLayout4);
        lNameLayout5 = (TextInputLayout) findViewById(R.id
                .fNameLayoutEmail);


        btn = (Button) findViewById(R.id.button1);
        btn.getBackground().setColorFilter(Color.parseColor("#1565c0"), PorterDuff.Mode.MULTIPLY);

        edttxt1 = (EditText) findViewById(R.id.editText3);

        edttxt2 = (EditText) findViewById(R.id.editText1);


        edttxt3 = (EditText) findViewById(R.id.editText2);


        edttxt4 = (EditText) findViewById(R.id.editText4);

        edttxt5 = (EditText) findViewById(R.id.editTextEmail);



        edttxt2.clearFocus();
        edttxt1.requestFocus();


        try{

            TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            myPhoneNumber = tMgr.getLine1Number();
            edttxt3.setText(myPhoneNumber);
        }

        catch(Exception e)
        {

        }

        final String PREFS_NAME = "GeoPreferences";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Please allow digiPune to access location");
        alertDialogBuilder.setTitle("Location Access Error");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alertDialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

        gps = new GPSTracker(Log_in.this);

        txt1 = (TextView) findViewById(R.id.textView1);
        txt2 = (TextView) findViewById(R.id.textView2);


        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {


            if(!alertDialog.isShowing())
            {
                alertDialog.show();
            }

        }



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent1 = cd1.isConnectingToInternet();

                if(isInternetPresent1)
                {
                    final String PREFS_NAME = "GeoPreferences";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


                    mydevice_id = edttxt1.getText().toString();
                    name = edttxt2.getText().toString();
                    myPhoneNumber =  edttxt3.getText().toString();
                    password = edttxt4.getText().toString();
                    settings.edit().putString("number", myPhoneNumber).commit();

                    lNameLayout.setErrorEnabled(false);

                    lNameLayout1.setErrorEnabled(false);

                    lNameLayout2.setErrorEnabled(false);

                    lNameLayout4.setErrorEnabled(false);

                    lNameLayout5.setErrorEnabled(false);





                    if(edttxt5.getText().toString().matches("") || edttxt1.getText().toString().matches("") || edttxt2.getText().toString().matches("") || edttxt3.getText().toString().matches(""))
                    {
                        if(edttxt1.getText().toString().matches(""))
                        {
                            lNameLayout.setErrorEnabled(true);
                            lNameLayout.setError("*Required field");
                        }

                        if(edttxt2.getText().toString().matches(""))
                        {
                            lNameLayout1.setErrorEnabled(true);
                            lNameLayout1.setError("*Required field");
                        }

                        if(edttxt3.getText().toString().matches(""))
                        {
                            lNameLayout2.setErrorEnabled(true);
                            lNameLayout2.setError("*Required field");
                        }

                        if(edttxt5.getText().toString().matches(""))
                        {
                            lNameLayout5.setErrorEnabled(true);
                            lNameLayout5.setError("*Required field");
                        }


                    }




                    else
                    {


                        try
                        {
                            mystringLatitude = String.valueOf(gps.getLatitude());
                            mystringLongitude = String.valueOf(gps.getLongitude());

                            email = edttxt5.getText().toString();


                            date = gps.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
                            networkTS = sdf.format(date);


                            new CreateNewProduct().execute();
                        }

                        catch(Exception e)
                        {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Log_in.this);
                            alertDialogBuilder.setMessage("It appears that you device hasnt connected to google location service. Please do the following : \n" + "1.Ensure that device has working internet connection\n" +"2.Ensure that location access in device settings has been turned with mobile network location activated\n" + "3.Finally, restart your device to obtain location fix if above steps dont work" );
                            alertDialogBuilder.setTitle("Location Access Error");

                            alertDialogBuilder.setPositiveButton("Exit",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    });

                            AlertDialog alertDialog1 = alertDialogBuilder.create();
                            alertDialog1.show();
                        }



                    }


                }

                else
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Log_in.this);
                    alertDialogBuilder.setMessage("Unable to connect to internet. Please connect to internet" );
                    alertDialogBuilder.setTitle("Internet Error");

                    alertDialogBuilder.setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog alertDialog1 = alertDialogBuilder.create();
                    alertDialog1.show();
                }
            }



        });

    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        JSONObject json, json2;
        Boolean i,j,k = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Log_in.this);
            pDialog.setMessage("Loging in....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();


        }


        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", mydevice_id));

            //Check if userId exists
            try
            {
                runOnUiThread(new Runnable() {
                    //
                    @Override
                    public void run() {

                        pDialog.setMessage("Checking if user name exists");
                    }
                });

                json = jsonParser.makeHttpRequest(
                        url_product_exists, "GET", params);
                i = true;
            }
            catch (Exception e1) {


                i=false;

                pDialog.dismiss();

                runOnUiThread(new Runnable() {
                    //
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Log_in.this);
                        alertDialogBuilder.setMessage("Something went wrong while signing up. Please try again" );
                        alertDialogBuilder.setTitle("Error");

                        alertDialogBuilder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                        AlertDialog alertDialog1 = alertDialogBuilder.create();
                        alertDialog1.show();
                    }
                });

            }

            if(i)

            {
                try {

                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {

                            pDialog.setMessage("Registering with GCM");
                        }
                    });

                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }

                    regid = gcm.register("324467972262");

                    final String PREFS_NAME = "GeoPreferences";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    settings.edit().putString("gcm_regid", regid).commit();
                    j = true;


                } catch (Exception ex) {
                    j = false;
                    pDialog.dismiss();

                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Log_in.this);
                            alertDialogBuilder.setMessage("Error communicating to GCM servers. Please try again");
                            alertDialogBuilder.setTitle("Connection Error");

                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                            AlertDialog alertDialog1 = alertDialogBuilder.create();
                            alertDialog1.show();
                        }
                    });
                }

                if (j) {
                    try {

                        int success = json.getInt("success");

                        //UserID doesnt exist, create new product
                        if (success == 1) {

                            runOnUiThread(new Runnable() {
                                //
                                @Override
                                public void run() {

                                    pDialog.setMessage("Checking if password matches");
                                }
                            });

                            params.add(new BasicNameValuePair("password", password));

                            json2 = jsonParser.makeHttpRequest(url_check_password,
                                    "POST", params);

                            k=true;

                        }

                        //User Id exists
                        if (success == 0) {

                            pDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                //
                                @Override
                                public void run() {
                                    lNameLayout.setErrorEnabled(true);
                                    lNameLayout.setError("digiPune ID doesnt exist");
                                    k=false;
                                }
                            });
                        }

                    } catch (Exception e1) {

                        k=false;
                        pDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Log_in.this);
                                alertDialogBuilder.setMessage("Something went wrong.Please try again");
                                alertDialogBuilder.setTitle("Error");

                                alertDialogBuilder.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                AlertDialog alertDialog1 = alertDialogBuilder.create();
                                alertDialog1.show();
                            }
                        });


                    }
                }
            }


            if(k)
            {
                try
                {
                    int success2 = json2.getInt("success");
                    if (success2 == 1) {

                        runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {

                                pDialog.setMessage("Loging in....");
                            }
                        });


                        params.add(new BasicNameValuePair("latitude", mystringLatitude));
                        params.add(new BasicNameValuePair("longitude", mystringLongitude));
                        params.add(new BasicNameValuePair("sharing_on", "1"));
                        params.add(new BasicNameValuePair("time", sdf.format(date)));
                        params.add(new BasicNameValuePair("gcm_regid", regid));
                        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        String imei = tMgr.getDeviceId();
                        params.add(new BasicNameValuePair("IMEI",imei));
                        params.add(new BasicNameValuePair("contact", myPhoneNumber));
                        params.add(new BasicNameValuePair("email", email));



                        JSONObject json = jsonParser.makeHttpRequest(url_login,
                                "POST", params);


                        pDialog.dismiss();
                        finish();
                        Intent i = new Intent(Log_in.this, App_intro.class);
                        Intent i2 = new Intent(Log_in.this, FriendLocation.class);
                        startActivity(i2);
                        startActivity(i);

                        final String PREFS_NAME = "GeoPreferences";

                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

                        settings.edit().putString("name", name).commit();
                        settings.edit().putString("user_name", mydevice_id).commit();
                        settings.edit().putString("password", password).commit();
                        settings.edit().putBoolean("FindX_share_enabled", true).commit();
                        settings.edit().putBoolean("FindX_history_enabled", true).commit();
                        settings.edit().putBoolean("FindX_background_enabled", true).commit();
                        settings.edit().putBoolean("my_first_time", false).commit();
                        settings.edit().putString("category_pos", "1").apply();


                        //Checking if background activity running
                        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

                            if (digiPune.class.getName().equals(service.service.getClassName())) {
                                //running
                            } else {
                                context.startService(new Intent(context, digiPune.class));
                            }

                        }

                    }

                    if (success2 == 0) {

                        pDialog.dismiss();
                        runOnUiThread(new Runnable() {
                            //
                            @Override
                            public void run() {
                                lNameLayout4.setErrorEnabled(true);
                                lNameLayout4.setError("*Password doesnt match");
                            }
                        });


                    }



                }

                catch (Exception e1) {


                    pDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        //
                        @Override
                        public void run() {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Log_in.this);
                            alertDialogBuilder.setMessage("Something went wrong.Please try again");
                            alertDialogBuilder.setTitle("Error");

                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                            AlertDialog alertDialog1 = alertDialogBuilder.create();
                            alertDialog1.show();
                        }
                    });


                }



            }

            return null;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent i2 = new Intent(Log_in.this, Sign_up.class);
                i2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i2);
                finish();
                return true;
            }


            case R.id.info: {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(
                        Log_in.this);
                LayoutInflater factory = LayoutInflater.from(Log_in.this);

                final View view = factory.inflate(R.layout.dialog_main, null);

                ImageView image = (ImageView) view.findViewById(R.id.imageView);
                image.setImageResource(R.drawable.mylocation_help);

                alertadd.setView(view);


                alertadd.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dlg, int sumthin) {

                    }
                });
                alertDialog = alertadd.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onLocationChanged(Location gps) {
        // TODO Auto-generated method stub
        mystringLatitude = String.valueOf(gps.getLatitude());
        mystringLongitude = String.valueOf(gps.getLongitude());
    }

    @Override
    public void onBackPressed() {
        Intent i2 = new Intent(Log_in.this, Sign_up.class);
        i2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i2);
        finish();
    }
}

