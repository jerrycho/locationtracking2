package app.mmguardian.com.locationtracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

/**
 * Created by jerry on 6/1/18.
 */

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver{

    public static String  ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.locationupdatespendingintent.action" + ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent!=null){
            String action = intent.getAction();
            if (action == ACTION_PROCESS_UPDATES){
                LocationResult result = LocationResult.extractResult(intent);
                if (result!=null){
                    List<Location> locations = result.getLocations();
                    //TODO
                    Log.d("ABC", locations.get(0).toString());
                }
            }
        }
    }
}
