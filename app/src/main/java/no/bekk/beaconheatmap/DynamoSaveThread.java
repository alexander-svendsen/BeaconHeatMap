package no.bekk.beaconheatmap;

import android.os.AsyncTask;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.util.List;


class DynamoSaveThread extends AsyncTask<List<Beacon>, Void, Void> {

    private DynamoDBMapper mapper = null;

    public DynamoSaveThread(DynamoDBMapper mapper){
        this.mapper = mapper;
    }

    @Override
    protected Void doInBackground(List<Beacon>... beacons) {
        for (Beacon beacon : beacons[0]) {
            HeatMap hm = new HeatMap();

            hm.setBeaconId(String.valueOf(beacon.getMinor()));
            hm.setDist(String.valueOf(Utils.computeAccuracy(beacon)));
            hm.setTimestamp(String.valueOf(System.currentTimeMillis()));

            mapper.save(hm);
        }

        return null;
    }
}