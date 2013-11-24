package pl.towelrail.locate.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

public class TowelLocationService extends Service {
    private LocationManager mLocationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_LONG).show();

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(
                mLocationManager.getBestProvider(new Criteria(), true), 0, 5,
                new TowelLocationListener(getApplicationContext()));

        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "bind", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        TowelLocationServiceHelper.getInstance(this).stop();

    }
}
