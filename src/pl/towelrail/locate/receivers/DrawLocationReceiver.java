package pl.towelrail.locate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.service.TowelLocationServiceHelper;

public class DrawLocationReceiver extends BroadcastReceiver {
    private GoogleMap mGmap;
    private Context ctx;

    public DrawLocationReceiver(GoogleMap mGmap, Context ctx) {
        this.mGmap = mGmap;
        this.ctx = ctx;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TowelLocation location = (TowelLocation) intent.getSerializableExtra("location");

        mGmap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.getLatLng(), 12));
        mGmap.setMyLocationEnabled(true);
        TowelRoute route = TowelLocationServiceHelper.getInstance(ctx).getCurrentRoute();
        TowelRoute.drawRoute(route, mGmap);
    }
}
