package in.skylinelabs.digiPune.activity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import in.skylinelabs.digiPune.R;


public class GcmMessageHandler extends IntentService {

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    NotificationManager notificationManager;
    Notification myNotification;

    private static final int MY_NOTIFICATION_ID1 = 1;
    private static final int MY_NOTIFICATION_ID2 = 2;

    String msgFromActivity;
    String extraOut;



    public GcmMessageHandler() {
        super("in.skylinelabs.digiPune.GcmIntentService");

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try{
            Bundle extras = intent.getExtras();

            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);


            String type =extras.getString("type");

            Log.d("Bagh re type",type);
            String abc = type.toString();

            String x = ("sos").toString();

            String xy = ("alert").toString();

            if(abc.equals(x)) {
/***************************************************************************************************************************/



                String name = extras.getString("name");
                String lat = extras.getString("latitude");
                String lon = extras.getString("longitude");
                String user_id = extras.getString("user_name");
                String number = extras.getString("number");
                String time =extras.getString("time");



                final String PREFS_NAME = "GeoPreferences";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                settings.edit().putString("sos_user_id", user_id).commit();
                settings.edit().putString("sos_lat", lat).commit();
                settings.edit().putString("sos_lon", lon).commit();
                settings.edit().putString("sos_user_name", name).commit();
                settings.edit().putString("sos_number", number).commit();
                settings.edit().putString("sos_time", time).commit();

                String actual_name = settings.getString("name", "");


                Intent open_map = new Intent(this, sos_map.class);
                PendingIntent pIntent =
                        PendingIntent.getActivity(this, 0, open_map,
                                PendingIntent.FLAG_UPDATE_CURRENT);


                Intent callvictim = new Intent(Intent.ACTION_CALL);
                callvictim.setData(Uri.parse("tel:" + number));
                PendingIntent pIntent2 =
                        PendingIntent.getActivity(this, 0, callvictim,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                Intent navigate = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?f=d&daddr=" + lat + "," + lon));
                PendingIntent pIntent3 =
                        PendingIntent.getActivity(this, 0, callvictim,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Action act_call =
                        new NotificationCompat.Action.Builder(R.drawable.ic_action_call,
                                "Call Victim", pIntent2)
                                .build();

                NotificationCompat.Action act_nav =
                        new NotificationCompat.Action.Builder(R.drawable.ic_action_directions,
                                "Navigate to Victim", pIntent3)
                                .build();


                myNotification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(type + " received")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(name + " needs your help !!")
                        .setTicker("Notification!")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.findx_logo)
                        .extend(new WearableExtender().addAction(act_call))

                        .setContentIntent(pIntent).setAutoCancel(true)
                        .build();

                notificationManager.notify(MY_NOTIFICATION_ID1, myNotification);


            }

            if(abc.equals(xy)) {
/***************************************************************************************************************************/


                String title = extras.getString("title");
                String message = extras.getString("messageC");

                Intent open_map = new Intent(this, Alert_Send.class);
                PendingIntent pIntent =
                        PendingIntent.getActivity(this, 0, open_map,
                                PendingIntent.FLAG_UPDATE_CURRENT);



                myNotification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle(title)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(message)
                        .setTicker("Notification!")
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.findx_logo)
                        .setContentIntent(pIntent).setAutoCancel(true)
                        .build();

                notificationManager.notify(MY_NOTIFICATION_ID1, myNotification);


            }

        }




        catch(Exception e){}
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
	    
	