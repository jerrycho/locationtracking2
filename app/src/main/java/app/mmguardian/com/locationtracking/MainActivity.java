package app.mmguardian.com.locationtracking;

import android.Manifest;
import android.app.PendingIntent;
import android.app.job.JobService;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();

        doGetLocation();
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_ACCESS_LOCATION)
    public void doGetLocation(){
        if (EasyPermissions.hasPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        })){
            createLocationRequest();
        }
        else {
            EasyPermissions.requestPermissions(this,
                    "Please approved : ",
                    R.string.yes,
                    R.string.no,
                    PERMISSIONS_REQUEST_ACCESS_LOCATION,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }
            );
        }
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        createLocationRequest();
    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        Toast.makeText(this, "Please accept the permission request", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void requestLocationUpdates(View view){
        try {
            Log.i("ABC", "requestLocationUpdates>>>");
            //Utils.setRequestingLocationUpdates(this, true)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, pendingIntent());
        } catch (SecurityException e) {

            e.printStackTrace();
        }
    }

    private PendingIntent pendingIntent(){
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createLocationRequest() {

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        // Note: apps running on "O" devices (regardless of targetSdkVersion) may receive updates
        // less frequently than this interval when the app is no longer in the foreground.
        mLocationRequest.setInterval(60000); //every 60sec

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(30000); //every 30 sec;

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(60000);

        requestLocationUpdates(null);

        TestJobService.schedule(this);
    }

}
