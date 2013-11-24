package pl.towelrail.locate.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;
import pl.towelrail.locate.http.PostTowelLocationTask;
import pl.towelrail.locate.receivers.PostTowelLocationReceiver;

import java.util.Date;
import java.util.List;

public class TowelLocationListener implements LocationListener {
    private final String URL = "https://shop-manager.herokuapp.com/location";
//    private final String URL = "http://192.168.56.1:3000/location";
    private Context ctx;

    public TowelLocationListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onLocationChanged(Location location) {
        Date start = new Date();
        TowelLocation towelLocation = new TowelLocation(location);
        DatabaseModel<TowelLocation, Integer> model =
                new DatabaseModel<TowelLocation, Integer>(TowelLocation.class, Integer.class, ctx);

        Intent mapFragmentIntent = new Intent("TowelLocationReceiver");
        mapFragmentIntent.putExtra("location", towelLocation);

        ctx.sendBroadcast(mapFragmentIntent);

        int persist = DatabaseTools.persist(towelLocation, model);

//        List<TowelLocation> locations = null;
//        if (persist == 0) {
//            locations = DatabaseTools.fetchAll(model);
//            StringBuilder sb = new StringBuilder();
//            for (TowelLocation l : locations) {
//                sb.append(l.toString()).append(":").append(l.getId()).append(" | ");
//            }
//            Toast.makeText(ctx, "location count: " + locations.size(), Toast.LENGTH_SHORT).show();
//        }

//        if (locations != null) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                PostTowelLocationTask task = new PostTowelLocationTask(ctx);
                task.execute(URL, towelLocation.toString());
            }
//        }
        Date stop = new Date();
        Toast.makeText(ctx, "Executed in: " + String.valueOf(stop.getTime() - start.getTime()) + " ms", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(ctx, "stat changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(ctx, "prov enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(ctx, "prov disabled", Toast.LENGTH_SHORT).show();
    }


}
