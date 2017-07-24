package in.skylinelabs.digiPune.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootCompletedReceiver extends BroadcastReceiver {

    final static String TAG = "BootCompletedReceiver";

    
    
    @Override
    public void onReceive(Context context, Intent arg1) {
       

    	 ActivityManager manager = (ActivityManager)context.   getSystemService(Context.ACTIVITY_SERVICE);
    	 for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		  {

		      if (digiPune.class.getName().equals(service.service.getClassName())) {
		                          //running 
		         }
		     else{
		    	 context.startService(new Intent(context, digiPune.class));
		         }

		  }
    }
}