package in.skylinelabs.digiPune.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.skylinelabs.digiPune.R;


public class Favourites extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {
	
	FloatingActionButton btn;
	EditText edt1, edt2, edt3, edt4;
	String one,two,three,four,name;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;

	AlertDialog alertDialog;



	private static final String favourites = "http://www.skylinelabs.in/Geo/favourite.php";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favourites);


		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setElevation(5);
		getSupportActionBar().setTitle("digiPune");
		drawerFragment = (FragmentDrawer)
				getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(this);
		displayView(8);

        edt1 = (EditText)  findViewById(R.id.editText1);
        edt2 = (EditText)  findViewById(R.id.editText2);
        edt3 = (EditText)  findViewById(R.id.editText3);
        edt4 = (EditText)  findViewById(R.id.editText4);
        
        edt1.setHint("Enter fav 1");
        edt2.setHint("Enter fav 2");
        edt3.setHint("Enter fav 3");
        edt4.setHint("Enter fav 4");
        
        final String PREFS_NAME = "GeoPreferences";
      	final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
          
      	name =settings.getString("user_name", "");

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		edt1.setText(settings.getString("fav_1", ""));
        edt2.setText(settings.getString("fav_2", ""));
        edt3.setText(settings.getString("fav_3", ""));
        edt4.setText(settings.getString("fav_3", ""));
        

        
         btn = (FloatingActionButton)  findViewById(R.id.button1);
		 btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					
					one = edt1.getText().toString();
					
					
					two = edt2.getText().toString();
					
					three = edt3.getText().toString();
					
					
					four = edt4.getText().toString();
					
	
					ConnectionDetector cd1 = new ConnectionDetector(Favourites.this);
				    Boolean isInternetPresent1 = cd1.isConnectingToInternet();
			    
					if(!isInternetPresent1)
					{
						AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this);
					       builder.setMessage("Internet Unavailable. Please enable internet and try again")
					              .setCancelable(true)
					              .setTitle("Network Error")
					              .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					                  public void onClick(DialogInterface dialog, int id) {
					                	  
					                  }
					              });
					       AlertDialog alert1 = builder.create();
					       alert1.show();
					}
					else
					{
					new CreateNewProduct().execute();
					}
				}
			});
		

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
				finish();
				i = new Intent(this, MyLocation.class);
				startActivity(i);
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

				break;

			default:
				break;
		}
	}


	class CreateNewProduct extends AsyncTask<String, String, String> {

			JSONObject json, json1;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(Favourites.this);
				pDialog.setMessage("Updating favourites");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			
			
			protected String doInBackground(String... args) {
				
				
				final String PREFS_NAME = "GeoPreferences";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("fav_1", one));
				params.add(new BasicNameValuePair("fav_2", two));
				params.add(new BasicNameValuePair("fav_3",three));
				params.add(new BasicNameValuePair("fav_4",four));
				params.add(new BasicNameValuePair("user_name",name));
				 
										try {

											 JSONObject json = jsonParser.makeHttpRequest(favourites,
														"GET", params);
										
											 	settings.edit().putString("fav_1", one).commit();
												settings.edit().putString("fav_2", two).commit();
												settings.edit().putString("fav_3", three).commit();
												settings.edit().putString("fav_4", four).commit();
												 pDialog.dismiss();

											runOnUiThread(new Runnable() {
												//
												@Override
												public void run() {
													AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this);
													builder.setMessage("Favourites updated")
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
									
									catch (Exception e1) {
										runOnUiThread(new Runnable() {
											//
											@Override
											public void run() {
												AlertDialog.Builder builder = new AlertDialog.Builder(Favourites.this);
												builder.setMessage("Something went wrong while trying to set FAVs. Please retry.")
														.setCancelable(true)
														.setTitle("Network Error")
														.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
															public void onClick(DialogInterface dialog, int id) {
																final String PREFS_NAME = "GeoPreferences";
																SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

																settings.edit().putBoolean("sos_error", false).commit();

																new CreateNewProduct().execute();
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
		int id = item.getItemId();

		if(id == R.id.info) {
			AlertDialog.Builder alertadd = new AlertDialog.Builder(
					this);
			LayoutInflater factory = LayoutInflater.from(this);

			final View view = factory.inflate(R.layout.dialog_main, null);

			ImageView image = (ImageView) view.findViewById(R.id.imageView);
			image.setImageResource(R.drawable.sos_help);

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
	 
}
