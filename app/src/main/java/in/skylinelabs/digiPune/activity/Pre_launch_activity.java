package in.skylinelabs.digiPune.activity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;



public class Pre_launch_activity extends Activity{

    final Context context = this;
    public static int post=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getActionBar().hide();

        final String PREFS_NAME = "GeoPreferences";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            // Success! Do what you want

            if (settings.getBoolean("my_first_time", true)) {

                Intent i2 = new Intent(Pre_launch_activity.this, Sign_up.class);
                startActivity(i2);
                finish();
            }
            else{

                if(settings.getBoolean("signup_skip",false))
                {
                    Intent i2 = new Intent(Pre_launch_activity.this, No_SignUp.class);
                    startActivity(i2);
                    int flag=(PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
                    ComponentName component=new ComponentName(Pre_launch_activity.this, BootCompletedReceiver.class);
                    getPackageManager()
                            .setComponentEnabledSetting(component, flag,
                                    PackageManager.DONT_KILL_APP);

                    finish();
                }

                else {
                    Intent i2 = new Intent(Pre_launch_activity.this, FriendLocation.class);
                    startActivity(i2);

                    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

                        if (digiPune.class.getName().equals(service.service.getClassName())) {
                            //running
                        } else {
                            digiPune.MIN_TIME_BW_UPDATES = 1000 * 2 * 60;
                            context.startService(new Intent(context, digiPune.class));

                        }

                    }
                    finish();
                }

            }

        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    Pre_launch_activity.this);

            // set title
            alertDialogBuilder.setTitle("Google play services error");

            // set dialog message
            alertDialogBuilder
                    .setMessage(
                            "This Application wants you to update Google Play Services App")
                    .setCancelable(false)
                    .setPositiveButton("Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    callMarketPlace();


                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    finish();
                                    dialog.cancel();
                                }
                            });

            alertDialogBuilder.show();

        }

    }

    public void callMarketPlace() {
        try {

            startActivityForResult(
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + "com.google.android.gms")), 1);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + "com.google.android.gms")), 1);
        }
    }
}














/*package in.skylinelabs.digiPune.activity;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;



public class Pre_launch_activity extends Activity{

    final Context context = this;
    public static int post=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getActionBar().hide();

        final String PREFS_NAME = "GeoPreferences";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            // Success! Do what you want

            if (settings.getBoolean("my_first_time", true)) {

                Intent i2 = new Intent(Pre_launch_activity.this, Sign_up.class);
                startActivity(i2);
                finish();
            }
            else{


                if(settings.getBoolean("sign_up_skip", false)) {
                    Intent i2 = new Intent(this, No_SignUp.class);
                    startActivity(i2);
                    finish();
                }
                else{

                Intent i2 = new Intent(this, FriendLocation.class);
                startActivity(i2);

                ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

                    if (digiPune.class.getName().equals(service.service.getClassName())) {
                        //running
                    } else {

                        context.startService(new Intent(context, digiPune.class));
                        digiPune.MIN_TIME_BW_UPDATES = 1000 * 2 * 60;

                    }

                }

                    finish();
                }




            }




        }
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    Pre_launch_activity.this);

            // set title
            alertDialogBuilder.setTitle("Google play services error");

            // set dialog message
            alertDialogBuilder
                    .setMessage(
                            "This Application wants you to update Google Play Services App")
                    .setCancelable(false)
                    .setPositiveButton("Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    callMarketPlace();


                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    finish();
                                    dialog.cancel();
                                }
                            });

            alertDialogBuilder.show();

        }

    }

    public void callMarketPlace() {
        try {

            startActivityForResult(
                    new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id="
                                    + "com.google.android.gms")), 1);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + "com.google.android.gms")), 1);
        }
    }
}


*/

