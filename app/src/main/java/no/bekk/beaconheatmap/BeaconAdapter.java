package no.bekk.beaconheatmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;
import java.util.ArrayList;
import java.util.Collection;


public class BeaconAdapter extends ArrayAdapter<Beacon> {

    private ArrayList<Beacon> beacons;

    // View lookup cache
    static class ViewHolder {
        final TextView mac;
        final TextView major;
        final TextView minor;
        final TextView distance;

        ViewHolder(View view) {
            mac = (TextView) view.findViewById(R.id.mac);
            major = (TextView) view.findViewById(R.id.major);
            minor = (TextView) view.findViewById(R.id.minor);
            distance = (TextView) view.findViewById(R.id.distance);
        }
    }

    public BeaconAdapter(Context context, ArrayList<Beacon> beacons) {
        super(context, R.layout.beacon_list_item, beacons);
        this.beacons = beacons;
    }

    public void replaceWith(Collection<Beacon> newBeacons) {
        this.beacons.clear();
        this.beacons.addAll(newBeacons);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Beacon beacon = getItem(position);
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.beacon_list_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        populate(beacon, view);

        return view;
    }

    @Override
    public boolean isEnabled(int position){ return false; }

    private void populate(Beacon beacon, View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.mac.setText(String.format("MAC: %s", beacon.getMacAddress().toStandardString()));
        holder.distance.setText(String.format("Distance: %.2fm", Utils.computeAccuracy(beacon)));
        holder.major.setText(String.format("Major: %s", beacon.getMajor()));
        holder.minor.setText(String.format("Minor: %s", beacon.getMinor()));
    }
}