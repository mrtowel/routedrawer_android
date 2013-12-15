package pl.towelrail.locate.receivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;
import pl.towelrail.locate.R;

public class GpsStatusReceiver extends BroadcastReceiver {
    private LocationManager mLocationManager;

    public GpsStatusReceiver(LocationManager mLocationManager) {
        this.mLocationManager = mLocationManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog gpsDialog = createGpsSettingsDialog(context);
            gpsDialog.show();
        }
    }

    private AlertDialog createGpsSettingsDialog(final Context ctx) {
        DialogInterface.OnClickListener mGpsPositiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ctx.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        DialogInterface.OnClickListener mGpsNegativeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ctx, "Your choice anyway...", Toast.LENGTH_SHORT).show();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setTitle(R.string.gps_dialog_title)
                .setMessage(R.string.turn_on_gps)
                .setPositiveButton(R.string.gps_settings, mGpsPositiveListener)
                .setNegativeButton(R.string.cancel, mGpsNegativeListener);

        return builder.create();

    }
}
