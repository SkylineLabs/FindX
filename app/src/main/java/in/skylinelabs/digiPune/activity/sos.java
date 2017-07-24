package in.skylinelabs.digiPune.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.LocationListener;
import com.skyfishjy.library.RippleBackground;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.skylinelabs.digiPune.R;


public class sos extends ActionBarActivity implements LocationListener {
	
	
	long date;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String networkTS;

	TextView txt;

 	String Latitude;
 	String Longitude;
 	
	
 	
	JSONObject json;
	JSONParser jsonParser = new JSONParser();
	
	private static final String send_sos = "http://www.skylinelabs.in/Geo/sos.php";
	
	 GPSTracker gps;
	
	FloatingActionButton resend;
	
	private ProgressDialog pDialog;
	AlertDialog alertDialog; 
	
	String  myPhoneNumber;
	/**/
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
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
		setContentView(R.layout.sos);


		//getActionBar().setTitle("digiPune SOS");
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setElevation(5);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		//getSupportActionBar().setLogo(R.drawable.findx_logo);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      alertDialogBuilder.setMessage("Please allow digiPune to access location in order to send location");
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
	      
	      String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (locationProviders == null || locationProviders.equals("")) {
				
				 
				if(!alertDialog.isShowing())
			      {
					alertDialog.show();
			      }
				  
		}
			
			else{

/***********************************************************************************************************/		

				resend = (FloatingActionButton) findViewById(R.id.button1);

				resend.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						send();
					}
				});

				send();
			}

		txt = (TextView) findViewById(R.id.textView1);

		final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
		ImageView imageView=(ImageView)findViewById(R.id.centerImage);
		rippleBackground.startRippleAnimation();
		}

	void send()
	{
		final String PREFS_NAME = "GeoPreferences";
		final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


		ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent1 = cd1.isConnectingToInternet();

		if(!isInternetPresent1)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Internet Unavailable. Notification not sent. SMS sent")
					.setCancelable(true)
					.setTitle("Network Error")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

						}
					});
			AlertDialog alert1 = builder.create();
			alert1.show();
		}

		gps = new GPSTracker(sos.this);
		Latitude = String.valueOf(gps.getLatitude());
		Longitude = String.valueOf(gps.getLongitude());

		date = gps.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
		networkTS = sdf.format(date);



/************************************************************************************************************/
		String name = settings.getString("name", "");


		String one = settings.getString("sos_1", "");
		String two = settings.getString("sos_2", "");
		String three = settings.getString("sos_3", "");
		String four = settings.getString("sos_4", "");



		String message = "Hi, this is  " + name +" here. I am in danger. I need immediate help. Here is a link to know my location \n"+
				"http://www.google.com/maps/place/" + Latitude + "," + Longitude;

		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> parts = smsManager.divideMessage(message);

		if(one != "")
			smsManager.sendMultipartTextMessage(one, null, parts, null, null);

		if(two != "")
			smsManager.sendMultipartTextMessage(two, null, parts, null, null);

		if(three != "")
			smsManager.sendMultipartTextMessage(three, null, parts, null, null);

		if(four != "")
			smsManager.sendMultipartTextMessage(four, null, parts, null, null);


		new CreateNewProduct().execute();

	}
	
	class CreateNewProduct extends AsyncTask<String, String, String> {

		final String PREFS_NAME = "GeoPreferences";

		Boolean sent;
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		protected String doInBackground(String... args) {
			
			String user_name = settings.getString("user_name", "");
			String name = settings.getString("name", "");
			myPhoneNumber = settings.getString("number", "");
			
		
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_name",user_name));
			params.add(new BasicNameValuePair("latitude",Latitude));
			params.add(new BasicNameValuePair("longitude",Longitude));
			params.add(new BasicNameValuePair("phone_number", myPhoneNumber));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("time", sdf.format(date)));
				
			
			
			try{
			json = jsonParser.makeHttpRequest(
					send_sos, "GET",params);

				sent = true;

			}
			
			catch(Exception e)
			{
				sent = false;
				runOnUiThread(new Runnable() {
					//
					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(sos.this);
						builder.setMessage("Internet Unavailable. Notification not sent. SMS sent")
								.setCancelable(true)
								.setTitle("Network Error")
								.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});
						AlertDialog alert1 = builder.create();
						alert1.show();
					}

			});



				}

			if(sent)
			{
				runOnUiThread(new Runnable() {
					//
					@Override
					public void run() {

						txt.setText("SOS Sent");

						AlertDialog.Builder builder = new AlertDialog.Builder(sos.this);
						builder.setMessage("SOS Notifications sent. Expecting help soon")
								.setCancelable(true)
								.setTitle("Success")
								.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});
						AlertDialog alert1 = builder.create();
						alert1.show();
					}

				});
			}

			return null;
			}
	}

	@Override
	public void onLocationChanged(Location gps) {
		// TODO Auto-generated method stub
		Latitude = String.valueOf(gps.getLatitude());
		Longitude = String.valueOf(gps.getLongitude());
	}

	
	 @Override
		public void onBackPressed() {

		}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}





