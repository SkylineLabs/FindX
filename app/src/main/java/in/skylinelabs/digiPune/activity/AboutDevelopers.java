package in.skylinelabs.digiPune.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import in.skylinelabs.digiPune.R;

public class AboutDevelopers extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {


    FloatingActionButton fab, fb, web, email;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    Boolean closed;
    TextView webtxt, emailtxt, fbtxt;

    RelativeLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_developers);

        displayView(8);

        closed = true;

        rel = (RelativeLayout) findViewById(R.id.rel);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("digiPune");
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        web = (FloatingActionButton) findViewById(R.id.web);
        fb = (FloatingActionButton) findViewById(R.id.fb);
        email = (FloatingActionButton) findViewById(R.id.email);

        webtxt = (TextView) findViewById(R.id.textWebsite);
        fbtxt = (TextView) findViewById(R.id.textFacebook);
        emailtxt = (TextView) findViewById(R.id.textEmail);


        web.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        webtxt.setVisibility(View.GONE);
        fbtxt.setVisibility(View.GONE);
        emailtxt.setVisibility(View.GONE);


        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.skylinelabs.in";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.facebook.com/theterribilis";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","contact@skylinelabs.in", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "digiPune feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi ! It was an awesome experience using digiPune! ");
                startActivity(Intent.createChooser(intent, "Contact developers"));
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(closed)
              {
                  open();
                  closed = false;

                  final OvershootInterpolator interpolator = new OvershootInterpolator();
                  ViewCompat.animate(fab).rotation(135f).withLayer().setDuration(300).setInterpolator(interpolator).start();

              }

              else
              {
                  close();
                  closed = true;

                  final OvershootInterpolator interpolator = new OvershootInterpolator();
                  ViewCompat.animate(fab).rotation(0f).withLayer().setDuration(300).setInterpolator(interpolator).start();


              }
            }
        });

    }

    void open()
    {
        ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#00ffffff")), new ColorDrawable(Color.parseColor("#CC000000"))};
        TransitionDrawable trans = new TransitionDrawable(color);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        rel.setBackgroundDrawable(trans);
        trans.startTransition(100);


        //rel.setBackgroundColor(Color.parseColor("#CC000000"));
        web.setVisibility(View.VISIBLE);
        fb.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        webtxt.setVisibility(View.VISIBLE);
        fbtxt.setVisibility(View.VISIBLE);
        emailtxt.setVisibility(View.VISIBLE);

    }

    void close()
    {
        rel.setBackgroundColor(Color.parseColor("#00ffffff"));
        web.setVisibility(View.GONE);
        fb.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        webtxt.setVisibility(View.GONE);
        fbtxt.setVisibility(View.GONE);
        emailtxt.setVisibility(View.GONE);


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
                title = "digiPune";
                break;
            case 1:
                finish();
                i = new Intent(this, MyLocation.class);
                startActivity(i);
                title = "My Location";
                break;
            case 2:
                finish();
                i = new Intent(this, Location_event.class);
                startActivity(i);
                title ="GeoFencing";
                break;
            case 3:
                finish();
                i = new Intent(this, Setting.class);
                startActivity(i);
                title ="Settings";
                break;
            case 4:
                finish();
                i = new Intent(this, SOS_Contacts.class);
                startActivity(i);
                title ="SOS Contacts";
                break;
            case 5:
                finish();
                i = new Intent(this, Favourites.class);
                startActivity(i);
                title ="Favourites";
                break;
            case 6:
                finish();
                i = new Intent(this, History.class);
                startActivity(i);
                title ="History";
                break;
			/*case 6:
                finish();
                i = new Intent(AboutDevelopers.this, GeoFencing.class);
                startActivity(i);
                break;
            case 7:
                finish();
                i = new Intent(AboutDevelopers.this, Help.class);
                startActivity(i);
                break;*/
            case 7:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","contact@skylinelabs.in", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "digiPune feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi ! It was an awesome experience using digiPune! ");
                startActivity(Intent.createChooser(intent, "Contact developers"));
                break;
            case 8:

                title ="About";
                break;
            case 9:
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                }
                catch(Exception e)
                {

                }
                break;

            case 10:
                finish();
                i = new Intent(this, Privacypolicy.class);
                startActivity(i);
                title ="Privacy";
                break;

            default:
                break;
        }
    }
}
