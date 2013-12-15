package pl.towelrail.locate.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;
import pl.towelrail.locate.R;

public class TowelLocationService extends Service {
    private LocationManager mLocationManager;
    private TowelLocationListener mTowelLocationListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTowelLocationListener = new TowelLocationListener(getApplicationContext());
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(
                mLocationManager.getBestProvider(new Criteria(), true), 0, 5, mTowelLocationListener);

        Toast.makeText(getApplicationContext(), getText(R.id.start_recording_toast), Toast.LENGTH_LONG).show();

        return Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(mTowelLocationListener);
        Toast.makeText(getApplicationContext(), getText(R.id.stop_recording_toast), Toast.LENGTH_SHORT).show();
    }
}
