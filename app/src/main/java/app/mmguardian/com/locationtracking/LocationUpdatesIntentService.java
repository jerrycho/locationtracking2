package app.mmguardian.com.locationtracking;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;


public class LocationUpdatesIntentService extends IntentService {

    public static final String ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.locationupdatespendingintent.action" + ".PROCESS_UPDATES";

    public LocationUpdatesIntentService(String name){
        super(name);
    }

    public LocationUpdatesIntentService() {
        super(LocationUpdatesIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String  action = intent.getAction();
            if (ACTION_PROCESS_UPDATES == action) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    //TODO
                    Log.d("ABC", locations.get(0).toString());
                }
            }
        }
    }

}
