package in.skylinelabs.digiPune.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.github.clans.fab.FloatingActionButton;

import in.skylinelabs.digiPune.R;

public class Setting extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {


    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    AlertDialog alertDialog;

    FloatingActionButton redo;

    Switch bck, upd, hst, doupdt, onDty;

    String user_name;

    String category;


    WebView webView;

    View.OnClickListener snackaction;

    Boolean firsttime = true;

    ProgressBar prgbr;

    Spinner cat;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        onDty = (Switch) findViewById(R.id.switchonDuty);




        snackaction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };


        final String PREFS_NAME = "GeoPreferences";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        prgbr = (ProgressBar) findViewById(R.id.pgb);
        prgbr.setVisibility(View.GONE);

        cat = (Spinner) findViewById(R.id.spinner);
        cat.setSelection(settings.getInt("category_pos", 1));

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                category = parent.getItemAtPosition(position).toString();

                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent = cd.isConnectingToInternet();

                if (firsttime) {
                    firsttime = false;
                } else {

                    if (!isInternetPresent) {


                        Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                                .setAction("Ok", snackaction)
                                .setActionTextColor(Color.WHITE)
                                .show();
                    } else {


                        String user_name = settings.getString("user_name", "");
                        webView = (WebView) findViewById(R.id.webView2);
                        webView.setWebChromeClient(new WebChromeClient() {
                            public void onProgressChanged(WebView view, int progress) {


                                if (progress < 100 && prgbr.getVisibility() == ProgressBar.GONE) {
                                    prgbr.setVisibility(ProgressBar.VISIBLE);
                                    cat.setClickable(false);
                                    redo.setClickable(false);
                                }
                                prgbr.setProgress(progress);
                                if (progress == 100) {
                                    prgbr.setVisibility(ProgressBar.GONE);
                                    cat.setClickable(true);
                                    redo.setClickable(true);
                                    settings.edit().putString("category", category).commit();
                                    settings.edit().putInt("category_pos", position).commit();
                                    Snackbar.make(findViewById(android.R.id.content), "Updated category", Snackbar.LENGTH_LONG)
                                            .setAction("Ok", snackaction)
                                            .setActionTextColor(Color.WHITE)
                                            .show();
                                }

                            }
                        });

                        webView.loadUrl("http://www.skylinelabs.in/Geo/category_update.php?category=" + category + "&user_name=" + user_name);


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        RelativeLayout mControlsContainer = (RelativeLayout) findViewById(R.id.rel3);

        mControlsContainer.setVisibility(View.GONE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("digiPune");
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(6);

        redo = (FloatingActionButton) findViewById(R.id.redo);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent i2 = new Intent(Setting.this, App_intro.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                    final View myView = findViewById(R.id.rel3);
                    final View myView1 = findViewById(R.id.rel4);
                    final View myView2 = findViewById(R.id.scrollView1);
                    myView2.setVisibility(View.GONE);

                    try{
                        // get the center for the clipping circle
                        int cx = myView1.getMeasuredWidth();
                        int cy =  myView1.getMeasuredHeight();

                        // get the final radius for the clipping circle
                        int finalRadius = Math.max(myView1.getWidth() * 2, myView1.getHeight() * 2) / 2;

                        //     Toast.makeText(getApplicationContext(), finalRadius + "   " + cx + "   " + cy, Toast.LENGTH_LONG).show();

                        // create the animator for this view (the start radius is zero)

                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                        anim.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                Intent i2 = new Intent(Setting.this, App_intro.class);
                                i2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i2);
                                finish();
                            }
                        });

                        anim.start();

                    }

                    catch(Exception e)
                    {


                    }

                    myView.setVisibility(View.VISIBLE);




                    // previously invisible view

                }

                else
                    startActivity(i2);
            }
        });

/****************************************************************************************************************/
       /*Background update switch*/
        bck = (Switch)findViewById(R.id.switch2);
        if (settings.getBoolean("FindX_background_enabled", true))
            bck.setChecked(true);
        else
            bck.setChecked(false);

        bck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(bck.isChecked()){
                    settings.edit().putBoolean("FindX_background_enabled", true).commit();

                }
                else{
                    settings.edit().putBoolean("FindX_background_enabled", false).commit();

                }
            }
        });

/**************************************************************************************************************/
          /*Background update switch*/
        doupdt = (Switch)findViewById(R.id.switch4);
        if (settings.getBoolean("FindX_update_enabled", true))
            doupdt.setChecked(true);
        else
            doupdt.setChecked(false);

        doupdt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (doupdt.isChecked()) {
                    settings.edit().putBoolean("FindX_update_enabled", true).commit();

                } else {
                    settings.edit().putBoolean("FindX_update_enabled", false).commit();

                }
            }
        });

/****************************************************************************************************************/
        /*Sharing on switch*/
        upd= (Switch)findViewById(R.id.switch1);

        if (settings.getBoolean("FindX_share_enabled", true))
        {
            upd.setChecked(true);
        }

        else
        {
            upd.setChecked(false);
        }

        upd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(upd.isChecked()){
                    settings.edit().putBoolean("FindX_share_enabled", true).commit();

                }
                else{
                    settings.edit().putBoolean("FindX_share_enabled", false).commit();
                    settings.edit().putBoolean("FindX_onDuty_enabled", false).commit();
                    onDty.setChecked(false);

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setMessage("Changes will take place only when digiPune connects to internet")
                        .setCancelable(true)
                        .setTitle("Information")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

        /****************************************************************************************************************/
        /*On Duty switch*/

        if (settings.getBoolean("FindX_onDuty_enabled", true))
        {
            onDty.setChecked(true);
        }

        else
        {
            onDty.setChecked(false);
        }

        onDty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (upd.isChecked()) {

                    if (onDty.isChecked()) {
                        settings.edit().putBoolean("FindX_onDuty_enabled", true).commit();

                    } else {
                        settings.edit().putBoolean("FindX_onDuty_enabled", false).commit();

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                    builder.setMessage("Changes will take place only when digiPune connects to internet")
                            .setCancelable(true)
                            .setTitle("Information")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                else
                {
                    onDty.setChecked(false);
                    Snackbar.make(findViewById(android.R.id.content), "Switch on Sharing Location", Snackbar.LENGTH_LONG)
                            .setAction("Ok", snackaction)
                            .setActionTextColor(Color.WHITE)
                            .show();
                }
            }

        });
/****************************************************************************************************************/
       /*History on switch*/
        hst= (Switch)findViewById(R.id.switch3);

        if (settings.getBoolean("FindX_history_enabled", true))
        {
            hst.setChecked(true);
        }

        else
        {
            hst.setChecked(false);
        }

        hst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(hst.isChecked()){
                    settings.edit().putBoolean("FindX_history_enabled", true).commit();

                }
                else{
                    settings.edit().putBoolean("FindX_history_enabled", false).commit();

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setMessage("Changes will take place only when digiPune connects to internet")
                        .setCancelable(true)
                        .setTitle("Information")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.info) {
            Intent i2 = new Intent(this, Alert_Send.class);
            startActivity(i2);
        }



               if(id == R.id.info) {
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    Setting.this);
            LayoutInflater factory = LayoutInflater.from(Setting.this);

            final View view = factory.inflate(R.layout.dialog_main, null);

            ImageView image = (ImageView) view.findViewById(R.id.imageView);
            image.setImageResource(R.drawable.settings_help);

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