package in.skylinelabs.digiPune.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;

import in.skylinelabs.digiPune.R;

public class Alert_Send extends ActionBarActivity {

    FloatingActionButton btn;
    EditText edtTitle, edtMessage;

    AlertDialog alertDialog;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    WebView wb;

    ProgressBar prgbr;

    View.OnClickListener snackaction,snackaction1;;




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert__ssend);

        snackaction1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setElevation(5);
        getSupportActionBar().setTitle("digiPune");


        final String PREFS_NAME = "GeoPreferences";
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn = (FloatingActionButton) findViewById(R.id.button1);
        edtTitle = (EditText) findViewById(R.id.editText2);
        edtMessage = (EditText) findViewById(R.id.editText);

        wb = (WebView) findViewById(R.id.webView3);
        wb.setVisibility(View.GONE);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setAllowFileAccess(true);
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setSupportZoom(true);
        wb.setWebViewClient(new WebViewClient());

        wb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100) {

                }

                if (progress == 100) {
                    Snackbar.make(findViewById(android.R.id.content), "Alert Sent", Snackbar.LENGTH_LONG)
                            .setAction("OK", snackaction1)
                            .setActionTextColor(Color.WHITE)
                            .show();

                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title, Message;
                Title = edtTitle.getText().toString();
                Message = edtMessage.getText().toString();
                wb.loadUrl("http://www.skylinelabs.in/Geo/alert.php?user_name="+settings.getString("user_name","")+"&title="+Title+"&message="+Message);

            }
        });



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
