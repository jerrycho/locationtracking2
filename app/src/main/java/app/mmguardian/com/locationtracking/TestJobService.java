package app.mmguardian.com.locationtracking;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestJobService extends JobService {

    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static final int JOB_ID = 1;
    private static final int TWO_SEC = 2 * 1000;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    public static void schedule(Context context) {
        ComponentName component = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, component)
                // schedule it to run any time between 1 - 5 minutes
                .setMinimumLatency(TWO_SEC)
                .setOverrideDeadline(5 * TWO_SEC)
                .setPersisted(true);

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        doMyWork();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void doMyWork() {
        Log.d("ABC", "doMyWork>>>" );
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                doGetCurrentLocaiton();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void doGetCurrentLocaiton() {
        Log.d("ABC", "doGetCurrentLocaiton0 :" );
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("ABC","onSuccess>>" );
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ABC","onFailure>>" + e.toString());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d("ABC","OnCompleteListener>>" + task.toString());
                    }
                });
    }
}
