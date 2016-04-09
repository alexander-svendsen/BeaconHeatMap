package no.bekk.beaconheatmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    private Region region = new Region("rid",
            Constants.PROXIMITY_UUID,
            Constants.BEACON_MAJOR_NUMBER,
            null);

    private BeaconManager beaconManager;
    private BeaconAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        // Configure device list.
        adapter = new BeaconAdapter(this, new ArrayList<Beacon>());
        final ListView list = (ListView) findViewById(R.id.beacon_list);
        list.setAdapter(adapter);

        beaconManager = new BeaconManager(this);

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, final List<Beacon> beacons) {
                Log.d("Main", "Found beacons");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceWith(beacons);
                    }
                });
            }
            @Override
            public void onExitedRegion(Region region) {}
        });

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(SystemRequirementsChecker.checkWithDefaultDialogs(this)){
            startScanning();
            Intent service = new Intent(this, BeaconMonitorService.class);
            startService(service);
        }
    }

    @Override
    protected void onPause() {
        beaconManager.stopMonitoring(region);
        super.onPause();
    }

    private void startScanning() {
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO fix
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
