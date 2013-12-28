package pl.towelrail.locate.receivers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;
import pl.towelrail.locate.R;

/**
 * Checks connectivity state. Suggests networking settings change if offline.
 */
public class NetworkStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            createNetworkSettingsDialog(context).show();
        }
    }

    private AlertDialog createNetworkSettingsDialog(final Context ctx) {
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ctx.startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        };

        DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(ctx, "Your choice anyway...", Toast.LENGTH_SHORT).show();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx)
                .setTitle(R.string.network_dialog_title)
                .setMessage(R.string.turn_on_network)
                .setPositiveButton(R.string.network_settings, positiveListener)
                .setNegativeButton(R.string.cancel, negativeListener);

        return builder.create();
    }
}
