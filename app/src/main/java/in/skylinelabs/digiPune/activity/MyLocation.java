package in.skylinelabs.digiPune.activity;

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
import android.os.Bundle;
import android.provider.Settings;
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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import in.skylinelabs.digiPune.R;

public class MyLocation extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener, android.location.LocationListener,  OnCameraChangeListener, OnMapLoadedCallback {

	GoogleMap map;

	AlertDialog alertDialog;

	EditText edttxt;

	WebView web;

	String status, Latitude, Longitude;


	TextView txt1, txt2,textView3;
	
	ProgressBar prgbr, prgbr1;

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;
	
	long date;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	final Context context = this;
	
	String mystringLatitude;
	String mystringLongitude;
	
	static String mydevice_id;
	
	String nation, post, area, address;
	    //flag for GPS Status
	    boolean isGPSEnabled = false;

	    //flag for network status
	    boolean isNetworkEnabled = false;

	    boolean canGetLocation = false;
	    
	    //public static  boolean mylocationOn; 

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

	    FloatingActionButton btn1,btn2;
	 	
		@Override
		public void onPause() {
		    super.onPause();  // Always call the superclass method first
		   
		}
		
		@Override
		public void onResume() {
		    super.onResume();  // Always call the superclass method first


		    String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (locationProviders == null || locationProviders.equals("")) {
			finish();

		}
		}

	View.OnClickListener snackaction;


	@Override
		public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.my_location);
	
	getLocation();



			snackaction = new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			};
			edttxt=(EditText) findViewById(R.id.search_view);
			edttxt.setHint("  Enter Status");

			mToolbar = (Toolbar) findViewById(R.id.toolbar);

			setSupportActionBar(mToolbar);
			getSupportActionBar().setElevation(5);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setTitle("digiPune");
			drawerFragment = (FragmentDrawer)
					getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
			drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
			drawerFragment.setDrawerListener(this);
			displayView(4);


			EditText toolbarSearchView = (EditText) findViewById(R.id.search_view);
			toolbarSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_GO) {
						statusUpdate();
						InputMethodManager inputManager =
								(InputMethodManager) context.
										getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.hideSoftInputFromWindow(
								MyLocation.this.getCurrentFocus().getWindowToken(),
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
			prgbr1 = (ProgressBar) findViewById(R.id.progressBar2);
			prgbr1.setVisibility(View.GONE);

			web = (WebView) findViewById(R.id.webView1);
			web.getSettings().setJavaScriptEnabled(true);
			web.getSettings().setBuiltInZoomControls(true);
			web.getSettings().setSupportZoom(true);
			web.setVisibility(View.GONE);
			web.setWebViewClient(new WebViewClient());
			web.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					if (progress < 100 && prgbr.getVisibility() == ProgressBar.GONE) {
						prgbr1.setVisibility(ProgressBar.VISIBLE);
					}
					prgbr1.setProgress(progress);
					if (progress == 100) {
						prgbr1.setVisibility(ProgressBar.GONE);
						edttxt.setText("");
						//Toast.makeText(MyLocation.this, "Status updated", Toast.LENGTH_SHORT).show();
						Snackbar.make(findViewById(android.R.id.content), "Status updated", Snackbar.LENGTH_LONG)
								.setAction("Ok", snackaction)
								.setActionTextColor(Color.WHITE)
								.show();
					}
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
       
       map.setOnMapLoadedCallback(MyLocation.this);

       map.setOnCameraChangeListener(MyLocation.this);
       CameraPosition camPos = new CameraPosition.Builder()

       .target(new LatLng(getLatitude(), getLongitude()))

       .zoom(12.8f)

       .build();

      CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

      map.moveCamera(camUpdate);
    
       
       txt1 = (TextView) findViewById(R.id.textView1);
       txt2 = (TextView) findViewById(R.id.textView2);
       
       
       final String PREFS_NAME = "GeoPreferences";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			
			mydevice_id = settings.getString("user_name", "");
			
			if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
			{
			mystringLatitude = String.valueOf(getLatitude());
			mystringLongitude = String.valueOf(getLongitude());
			date = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
			networkTS = sdf.format(date);
			new CreateNewProduct().execute();
		//	Toast.makeText(this,"Activity Start Update" + networkTS,Toast.LENGTH_SHORT).show();
			}
	       
	       
	       btn1 = (FloatingActionButton)findViewById(R.id.button1);
	       btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				
			}
		});



			btn2 = (FloatingActionButton)findViewById(R.id.location);
			btn2.setOnClickListener(new View.OnClickListener() {

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
	       
	       
	      
				 	
					
	 
	}
	
	@Override
	public void onLocationChanged(Location location) {
		
		map.setOnMapLoadedCallback(MyLocation.this);
		
		
		this.location = location;
		final String PREFS_NAME = "GeoPreferences";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		
		mydevice_id = settings.getString("user_name", "");
		
		if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
		{
		date = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
		networkTS = sdf.format(date);
		mystringLatitude = String.valueOf(getLatitude());
		mystringLongitude = String.valueOf(getLongitude());
		// Toast.makeText(this,"Activity OLC Update " + networkTS,Toast.LENGTH_SHORT).show();
		
		new CreateNewProduct().execute();
		}

		txt1.setText(area);
		txt2.setText(address);

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
	            locationManager.removeUpdates(MyLocation.this);
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
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

			finish();
		}




	class CreateNewProduct extends AsyncTask<String, String, String> {

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
				}
				

				protected String doInBackground(String... args) {
						
													
											try
											{
												if(getLocality(getApplicationContext()) != null)
													nation = getCountryName(getApplicationContext());
													else
														nation = "";
													
																									
													if(getLocality(getApplicationContext()) != null)
														 area = getLocality(getApplicationContext());
														else
															area = "";
													
													if(getAddressLine(getApplicationContext()) != null)
														address = getAddressLine(getApplicationContext());
														else
															address = "";
											}
											catch (Exception e1) {
												 runOnUiThread(new Runnable() {  
										                //       
										                     @Override  
										                     public void run() {  
										                    		//Toast.makeText(getApplicationContext(),"Network error Unable to fetch address",Toast.LENGTH_SHORT).show();
																 Snackbar.make(findViewById(android.R.id.content),  "Network error Unable to fetch address", Snackbar.LENGTH_LONG)
																		 .setAction("Ok", snackaction)
																		 .setActionTextColor(Color.WHITE)
																		 .show();
																	
										                     }  
									                }); 
												
											}  
											

																
									           
									            
											return null;					
					}


		 }
		 
	
		 @Override
		protected void onRestart() {
			// TODO Auto-generated method stub
			super.onRestart();
			String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (locationProviders == null || locationProviders.equals("")) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			      alertDialogBuilder.setMessage("Please allow findX to access location");
			      alertDialogBuilder.setTitle("Location Access Error");
			      
			      alertDialogBuilder.setPositiveButton("Settings", 
			      new DialogInterface.OnClickListener() {
					
			         @Override
			         public void onClick(DialogInterface arg0, int arg1) {
			           
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
				    
			      AlertDialog alertDialog = alertDialogBuilder.create();
			      alertDialog.show();
		}
		 
		
}

		@Override
		public void onCameraChange(CameraPosition arg0) {
			// TODO Auto-generated method stub
			map.setOnMapLoadedCallback(null);
			prgbr.setVisibility(View.VISIBLE);
			map.setOnCameraChangeListener(MyLocation.this);
			map.setOnMapLoadedCallback(MyLocation.this);
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
	public void onBackPressed() {
		this.finish();
	}


	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mylocation_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if(id == R.id.action_go) {
			statusUpdate();
			InputMethodManager inputManager =
					(InputMethodManager) context.
							getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(
					MyLocation.this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		if(id == R.id.share) {
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String nation1, area1, address1;

			if(nation != null)
				nation1 = nation;
			else
				nation1 = "";

			if(area != null)
				area1 = area;
			else
				area1 = "";

			if(address != null)
				address1 = address;
			else
				address1 = "";

			sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hi ! Here is my location"  + "\n\n" +"Address :" + "\n" +"Latitude : " + mystringLatitude + "\n"  + "Longitude : " + mystringLongitude + "\n"  + nation1 + "\n"  + area1 + "\n" + address1+ "\n\n" +"Time : "+sdf.format(date)+"\n"+"\n" +"To know current location, click on the following link :\n"+ "http://www.google.com/maps/place/" + mystringLatitude + "," + mystringLongitude +"\n"  + "Shared via digiPune");
			startActivity(Intent.createChooser(sharingIntent, "Share your location"));

		}

		if(id == R.id.info) {
			AlertDialog.Builder alertadd = new AlertDialog.Builder(
					MyLocation.this);
			LayoutInflater factory = LayoutInflater.from(MyLocation.this);

			final View view = factory.inflate(R.layout.dialog_main, null);

			ImageView image = (ImageView) view.findViewById(R.id.imageView);
			image.setImageResource(R.drawable.mylocation_help);

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

	private void displayView(int position) {
		//Fragment fragment = null;
		String title = getString(R.string.app_name);
		Intent i;
		switch (position) {
			case 0:
				finish();
				Pre_launch_activity.post=0;
				break;
			case 1:
				finish();
				Intent i2 = new Intent(this, ComplaintPortal.class);
				startActivity(i2);
				break;
			case 2:
				finish();
				i = new Intent(this, Bus_Fetch.class);
				startActivity(i);
				break;
			case 3:
				finish();
				i = new Intent(this, FindGov.class);
				startActivity(i);
				break;
			case 4:

				break;
			case 5:
				finish();
				i = new Intent(this, History.class);
				startActivity(i);
				break;
			case 6:
				finish();
				i = new Intent(this, Setting.class);
				startActivity(i);
				break;
			case 7:
				finish();
				i = new Intent(this, SOS_Contacts.class);
				startActivity(i);
				break;
			case 8:
				finish();
				i = new Intent(this, Favourites.class);
				startActivity(i);
				break;

			default:
				break;
		}
	}

	void statusUpdate()
	{
		date = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();
		networkTS = sdf.format(date);

		Latitude = String.valueOf(getLatitude());
		Longitude = String.valueOf(getLongitude());

		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		String status = edttxt.getText().toString();

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();

		if(isInternetPresent) {
			try {
				web.loadUrl("http://skylinelabs.in/Geo/status.php?user_name=" + mydevice_id + "&latitude=" + Latitude + "&longitude=" + Longitude + "&time=" + sdf.format(date) + "&status=" + status);
			} catch (Exception e) {

			}

		}

		else
		{
			//Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
			Snackbar.make(findViewById(android.R.id.content),   "Please connect to internet", Snackbar.LENGTH_LONG)
					.setAction("Ok", snackaction)
					.setActionTextColor(Color.WHITE)
					.show();
		}

	}


}

		

