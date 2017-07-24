package in.skylinelabs.digiPune.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.skylinelabs.digiPune.R;
import in.skylinelabs.digiPune.complaint_cardview.FeedItem;
import in.skylinelabs.digiPune.complaint_cardview.FeedItemAdapter;


public class ComplaintPortal extends ActionBarActivity {

    private Toolbar mToolbar;
    FloatingActionButton btn;

    JSONParser jsonParser = new JSONParser();

    View.OnClickListener snackaction,snackaction1;

    List<FeedItem> result = new ArrayList<FeedItem>();
    RecyclerView recList;
    FeedItemAdapter ca;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_portal);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("Swach Bharat");
        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        new GetCategoryUpdate().execute();
        ca = new FeedItemAdapter(this, result);
        recList.setAdapter(ca);
        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whateverv
                        //switch (position) {
                           // case 0:
                                TextView tid = (TextView) view.findViewById(R.id.ID);
                                TextView lat = (TextView) view.findViewById(R.id.latitude);
                                TextView lon = (TextView) view.findViewById(R.id.logitude);
                                TextView ttitle = (TextView) view.findViewById(R.id.title);
                                TextView td = (TextView) view.findViewById(R.id.description);
                                TextView turl = (TextView) view.findViewById(R.id.url);
                                Intent i = new Intent(ComplaintPortal.this, SwachBharatActivity.class);
                                i.putExtra("title", ttitle.getText());
                                i.putExtra("id", tid.getText());
                                i.putExtra("description", td.getText());
                                i.putExtra("url", turl.getText());
                                i.putExtra("lat", lat.getText());
                                i.putExtra("lon", lon.getText());
                                startActivity(i);
                                //break;

                    }
                })
        );
        btn = (FloatingActionButton) findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ComplaintPortal.this, Upload_Complaint.class);
                startActivity(i);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complaint_portal, menu);
        return true;



    }
    class GetCategoryUpdate extends AsyncTask<String, String, String> {

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
            ca.notifyDataSetChanged();

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("category", "police"));


            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
            Boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                try {

                    json2 = jsonParser.makeHttpRequest(
                            "http://www.skylinelabs.in/Compalaint_Portal/fetch_category.php", "GET", params);
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

                        JSONArray values = null;
                        try {
                            values = json2.getJSONArray("result");
                            final int numberOfItemsInResp = values.length();



                            for (int i = 0; i < numberOfItemsInResp; i++) {
                                JSONObject coordinates = values.getJSONObject(i);
                                Log.e("result", coordinates.toString());
                                FeedItem fi1 = new FeedItem();
                                fi1.title = coordinates.getString("title");
                                fi1.id = coordinates.getString("id");
                                fi1.description = coordinates.getString("description");
                                String imgurl =  coordinates.getString("image");
                                fi1.imgurl = imgurl.replaceAll("\\\\", "");
                                fi1.latitude = coordinates.getString("latitude");
                                fi1.longitude = coordinates.getString("longitude");
                                result.add(fi1);

                            }

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


}
