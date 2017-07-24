package in.skylinelabs.digiPune.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.skylinelabs.digiPune.R;
import in.skylinelabs.digiPune.complaint_cardview.ImageLoader;

public class SwachBharatActivity extends ActionBarActivity {
    JSONParser jsonParser = new JSONParser();
    ImageLoader imageLoader;
    View.OnClickListener snackaction,snackaction1;
    String id, lat, lon;
    String joincount;
    TextView jc;
    ProgressDialog  pd;
    private Toolbar mToolbar;
    GoogleMap map;
    Marker finalm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swach_bharat);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("Swach Bharat");
        Bundle extras = getIntent().getExtras();

        String title = extras.getString("title");
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText(title);

        id = extras.getString("id");
        lat = extras.getString("lat");
        lon = extras.getString("lon");

        MarkerOptions finalo = new MarkerOptions().position(new LatLng(10,10));
        map = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);
        finalm = map.addMarker(finalo);
        finalm.setVisible(false);
        finalm.setPosition(new LatLng((Double.parseDouble(lat)), (Double.parseDouble(lon))));
        finalm.setTitle("Home");
        finalm.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        finalm.setVisible(true);

        CameraPosition camPos2 = new CameraPosition.Builder()

                .target(new LatLng((Double.parseDouble(lat)), (Double.parseDouble(lon))))
                .zoom(12.8f)

                .build();

        CameraUpdate camUpdate2 = CameraUpdateFactory.newCameraPosition(camPos2);

        map.moveCamera(camUpdate2);

        String description = extras.getString("description");
        TextView td = (TextView) findViewById(R.id.description);
        td.setText(description);

        String imgurl = extras.getString("url");
        ImageView iv = (ImageView) findViewById(R.id.img);
        imageLoader = new ImageLoader(SwachBharatActivity.this);
        imageLoader.DisplayImage(imgurl, iv);

        jc = (TextView) findViewById(R.id.joincount);
        new JoinReceive().execute();
        FloatingActionButton join = (FloatingActionButton) findViewById(R.id.joinbutton);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinUpdate().execute();
            }
        });
    }

    class JoinUpdate extends AsyncTask<String, String, String> {

        JSONObject json2;
        Boolean updated = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new JoinReceive().execute();
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));


            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            "http://www.skylinelabs.in/Compalaint_Portal/complaint_count_increase.php", "GET", params);
                    updated = true;

                } catch (Exception e1) {
                    // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Network error. Enable internet", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                    updated = false;
                    return null;
                }

            }



            return null;
        }
    }

    class JoinReceive extends AsyncTask<String, String, String> {

        JSONObject json2;
        Boolean updated = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            json2 = null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jc.setText(joincount);
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", id));


            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            "http://www.skylinelabs.in/Compalaint_Portal/compaint_curr_count.php", "GET", params);
                    updated = true;

                } catch (Exception e1) {
                    // Toast.makeText(getApplicationContext(),"Network error Unable to fetch location",Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content), "Network error. Enable internet", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                    updated = false;
                    return null;
                }

            }


            ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent1 = cd1.isConnectingToInternet();

            if (isInternetPresent1 && updated) {
                runOnUiThread(new Runnable() {
                    //
                    @Override
                    public void run() {


                        try {


                            joincount = json2.getString("count");

                        } catch (JSONException e) {
                            Log.e("Error in markers", e.toString());
                            Snackbar.make(findViewById(android.R.id.content), "Something went wrong. Try again", Snackbar.LENGTH_LONG)
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
