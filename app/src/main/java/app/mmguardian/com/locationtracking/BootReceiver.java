package app.mmguardian.com.locationtracking;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver{

    public static final String TAG = "ABC";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onBootReceived >>>> ");
        TestJobService.schedule(context);
    }

}
