package pl.towelrail.locate.service;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;
import pl.towelrail.locate.receivers.DrawLocationReceiver;

public class TowelLocationListener implements LocationListener {

    private Context ctx;

    public TowelLocationListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!TowelLocationServiceHelper.getInstance(ctx).isRunning()) return;
        DatabaseModel<TowelRoute, Long> routeModel =
                new DatabaseModel<TowelRoute, Long>(TowelRoute.class, Long.class, ctx);

        TowelRoute route = TowelLocationServiceHelper.getInstance(ctx).getCurrentRoute();
        DatabaseTools.createOrUpdate(route, routeModel);

        TowelLocation towelLocation = new TowelLocation(location, route);

        DatabaseModel<TowelLocation, Long> model =
                new DatabaseModel<TowelLocation, Long>(TowelLocation.class, Long.class, ctx);
        DatabaseTools.persist(towelLocation, model);

        DatabaseTools.refresh(route);

        Intent locationDrawer = new Intent(DrawLocationReceiver.class.getName());
        locationDrawer.putExtra("location", towelLocation);
        ctx.sendBroadcast(locationDrawer);
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
