package in.skylinelabs.digiPune.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import in.skylinelabs.digiPune.R;

public class Privacypolicy extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    WebView wb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypolicy);


        displayView(10);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        wb = (WebView) findViewById(R.id.webView);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl("http://www.skylinelabs.in/digiPune/privacy/");

        final ProgressBar Pbar;

        Pbar = (ProgressBar)findViewById(R.id.progressBar1);

        wb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                return false;
            }
        });

        wb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if(progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_privacypolicy, menu);
        return true;

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
                        "mailto", "contact@skylinelabs.in", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "digiPune feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi ! It was an awesome experience using digiPune! ");
                startActivity(Intent.createChooser(intent, "Contact developers"));
                break;
            case 8:
                finish();
                i = new Intent(this, AboutDevelopers.class);
                startActivity(i);
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
               /* finish();
                i = new Intent(this, Privacypolicy.class);
                startActivity(i);*/
                title ="Privacy";
                break;


            default:
                break;
        }
    }
}
