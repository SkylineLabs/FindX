/******************No check on both edttxt being non empty********************/


package in.skylinelabs.digiPune.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.LocationListener;

import in.skylinelabs.digiPune.R;


public class History extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,LocationListener {

	final Context context = this;

	View.OnClickListener snackaction;


	Double Latitude, Longitude;

	AlertDialog alertDialog;

	GPSTracker gps;

	String URL = "http://skylinelabs.in/Geo/history.php";

	ProgressBar prgbr;
	FloatingActionButton btn1;
	EditText edttxt1;
	String user, number;
	WebView webView;

	private Toolbar mToolbar;
	private FragmentDrawer drawerFragment;

	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		gps = new GPSTracker(History.this);
		Latitude = gps.getLatitude();
		Longitude = gps.getLongitude();


        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };


		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(mToolbar);
		getSupportActionBar().setElevation(5);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle("digiPune");
		drawerFragment = (FragmentDrawer)
				getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
		drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragment.setDrawerListener(History.this);

		EditText toolbarSearchView = (EditText) findViewById(R.id.search_view);
		toolbarSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					history();
					InputMethodManager inputManager =
							(InputMethodManager) context.
									getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(
							History.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					return true;
				}
				return false;
			}
		});

		
		prgbr = (ProgressBar) findViewById(R.id.progressBar1);
		prgbr.setVisibility(View.GONE);

		webView = (WebView) findViewById(R.id.map);
        webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        
        webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress < 100 && prgbr.getVisibility() == ProgressBar.GONE) {
					prgbr.setVisibility(ProgressBar.VISIBLE);
				}
				prgbr.setProgress(progress);
				if (progress == 100) {
					prgbr.setVisibility(ProgressBar.GONE);
				}
			}
		});

		webView.loadUrl("http://www.skylinelabs.in/Geo/history.php");
        
        edttxt1 = (EditText) findViewById(R.id.search_view);
	    edttxt1.setHint("  digiPune ID");

		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();

		if(!isInternetPresent) {

		//	Toast.makeText(getApplicationContext(), " Please Enable Internet " , Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();

		}

		}



	@Override
	public void onBackPressed() {
		this.finish();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id == R.id.info) {
			AlertDialog.Builder alertadd = new AlertDialog.Builder(
					this);
			LayoutInflater factory = LayoutInflater.from(this);

			final View view = factory.inflate(R.layout.dialog_main, null);

			ImageView image = (ImageView) view.findViewById(R.id.imageView);
			image.setImageResource(R.drawable.history_help);

			alertadd.setView(view);


			alertadd.setPositiveButton("ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dlg, int sumthin) {

				}
			});
			alertDialog =  alertadd.create();
			alertDialog.show();


		}


		if(id == R.id.action_search) {
			history();
			InputMethodManager inputManager =
					(InputMethodManager) context.
							getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(
					History.this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

		if(id == R.id.action_history) {
			URL = "http://skylinelabs.in/Geo/history.php";
		}

		if(id == R.id.action_status) {
			URL = "http://skylinelabs.in/Geo/history_status.php";
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
				finish();
				i = new Intent(this, MyLocation.class);
				startActivity(i);
				break;
			case 5:

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

	@Override
	public void onDrawerItemSelected(View view, int position) {
		displayView(position);
	}


	void history()
	{
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		Boolean isInternetPresent = cd.isConnectingToInternet();

		if(isInternetPresent) {
			user = edttxt1.getText().toString();

			if (user == null && user == "") {
				webView.setVisibility(ProgressBar.GONE);
			//	Toast.makeText(this, "Please enter digiPune ID", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), "Please enter digiPune ID", Snackbar.LENGTH_LONG)
                        .setAction("Ok", snackaction)
                        .setActionTextColor(Color.WHITE)
                        .show();

			} else {
				webView.loadUrl(URL+"?user_name=" + user + "&num=5000");

			}
		}

		else
		{
			Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), "Please connect to internet", Snackbar.LENGTH_LONG)
                    .setAction("Ok", snackaction)
                    .setActionTextColor(Color.WHITE)
                    .show();


		}
	}
	@Override
	public void onLocationChanged(Location gps) {
		// TODO Auto-generated method stub
		Latitude = gps.getLatitude();
		Longitude = gps.getLongitude();
	}



}


