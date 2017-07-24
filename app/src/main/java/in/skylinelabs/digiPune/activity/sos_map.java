package in.skylinelabs.digiPune.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import in.skylinelabs.digiPune.R;


public class sos_map extends ActionBarActivity implements android.location.LocationListener, OnCameraChangeListener, OnMapLoadedCallback {

	
	GoogleMap map;
	ProgressBar prgbr;
	Marker mrk1;

	TextView txt1,textView3;
	
	final Context context = this;
	
	MarkerOptions marker1 = new MarkerOptions().position(new LatLng(10,10));
	
	Double Latitude, Longitude;
	String user_name , user_id, number, time;
	
	Button call, navigate;


	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_map);

		snackaction = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		};
		
		
		final String PREFS_NAME = "GeoPreferences";
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	       Latitude = Double.parseDouble(settings.getString("sos_lat", ""));
	       Longitude = Double.parseDouble(settings.getString("sos_lon", ""));
	       user_name = settings.getString("sos_user_name", "");
	       user_id = settings.getString("sos_user_id", "");
	       number = settings.getString("sos_number", "");
	       time = settings.getString("sos_time", "");
	       
	   //	getActionBar().setTitle("Help " +user_name);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar1);

		setSupportActionBar(mToolbar);

		getSupportActionBar().setTitle("Help " +user_name);
		//getSupportActionBar().setLogo(R.drawable.findx_logo);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	       call = (Button) findViewById(R.id.button1);
	       call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Log.d(null,number);
				Intent callvictim = new Intent(Intent.ACTION_CALL);
				callvictim.setData(Uri.parse("tel:" + number));
				startActivity(callvictim);
				
			}
		});
	       
	       

	       navigate = (Button) findViewById(R.id.button2);
	       navigate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW, 
					    Uri.parse("http://maps.google.com/maps?f=d&daddr="+Latitude+","+Longitude));
					intent.setComponent(new ComponentName("com.google.android.apps.maps", 
					    "com.google.android.maps.MapsActivity"));
					startActivity(intent);
				
			}
		});
	       
	  
	 
	       txt1  = (TextView) findViewById(R.id.textView1);
	       txt1.setText(user_name+" needs your help");
	
	       
		textView3 = (TextView) findViewById(R.id.textView3);
		textView3.setText("Loading Map");
		textView3.setVisibility(View.VISIBLE);
		
		prgbr = (ProgressBar) findViewById(R.id.progressBar1);
		prgbr.setVisibility(View.VISIBLE);
		
		
		   map = ((MapFragment) getFragmentManager().findFragmentById(
	               R.id.map)).getMap();
	       map.setMyLocationEnabled(true);
	       map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	       map.getUiSettings().setZoomControlsEnabled(true);
	       
	       map.setOnMapLoadedCallback(new OnMapLoadedCallback() {
	           public void onMapLoaded() {
	        	   prgbr.setVisibility(View.GONE);
	        	   textView3.setVisibility(View.GONE);
	           }
	       });
	       
	       map.setOnCameraChangeListener(sos_map.this);
	       map.setOnMapLoadedCallback(sos_map.this);
	       
	       CameraPosition camPos = new CameraPosition.Builder()

	       .target(new LatLng(Latitude, Longitude))

	       .zoom(12.8f)

	       .build();

	      CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

	      map.moveCamera(camUpdate);
	    
	        mrk1 =  map.addMarker(marker1);

			mrk1.setPosition(new LatLng(Latitude, Longitude));
			mrk1.setTitle(user_name + " ("+user_id +")" );
		
			mrk1.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			mrk1.setVisible(true);
			mrk1.setSnippet("\nSOS Sent at "+time);
		
	}
	
	 @Override
		public void onCameraChange(CameraPosition arg0) {
			// TODO Auto-generated method stub
		 map.setOnMapLoadedCallback(sos_map.this);
		 map.setOnCameraChangeListener(sos_map.this);
		 textView3.setVisibility(View.VISIBLE);
			prgbr.setVisibility(View.VISIBLE);
		}



	@Override
	public void onMapLoaded() {

		prgbr.setVisibility(View.GONE);
		textView3.setVisibility(View.GONE);
		}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	View.OnClickListener snackaction;

	@Override
	public void onProviderDisabled(String provider) {
		// .makeText(getApplicationContext(),"Please enable internet",Toast.LENGTH_LONG).show();

		Snackbar.make(findViewById(android.R.id.content), "Please enable location", Snackbar.LENGTH_LONG)
				.setAction("Ok", snackaction)
				.setActionTextColor(Color.WHITE)
				.show();

	}
	
	 @Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			//super.onBackPressed();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		     final AlertDialog alert; 
		     
		        builder.setMessage("Are you sure you want to exit ?")
		               .setCancelable(true)
		               .setTitle("Exit?")
		               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
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
