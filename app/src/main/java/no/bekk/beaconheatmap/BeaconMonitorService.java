package no.bekk.beaconheatmap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

import java.util.List;

public class BeaconMonitorService extends Service {

    private BeaconManager beaconManager = null;
    public DynamoDBMapper mapper = null;

    private Region region =  new Region("monitorService",
            Constants.PROXIMITY_UUID,
            Constants.BEACON_MAJOR_NUMBER,
            null);

    private static final String TAG = "BeaconMonitorService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.enableDebugLogging(true);

        Log.d(TAG, "Service Created");

        AmazonClientManager amazonClientManager = new AmazonClientManager(getApplicationContext());
        mapper = new DynamoDBMapper(amazonClientManager.ddb());

        beaconManager = new BeaconManager(this);
        beaconManager.setForegroundScanPeriod(300, 10000);
        beaconManager.setBackgroundScanPeriod(300, 10000);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                Log.d(TAG, "Found beacons");
                new DynamoSaveThread(mapper).execute(beacons);
            }
        });

        startScanning();
    }

    private void startScanning() {
        Log.d(TAG, "Starting scanning");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    public void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

}