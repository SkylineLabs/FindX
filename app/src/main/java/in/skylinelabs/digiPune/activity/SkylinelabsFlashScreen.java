package in.skylinelabs.digiPune.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import in.skylinelabs.digiPune.R;

public class SkylinelabsFlashScreen extends ActionBarActivity {
    private static int SPLASH_TIME_OUT = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skyline_labs_flash_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //this.getActionBar().hide();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SkylinelabsFlashScreen.this, Pre_launch_activity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }



}
