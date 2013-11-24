package pl.towelrail.locate.receivers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProgressReceiver extends BroadcastReceiver {
    private ProgressDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (dialog != null && intent.getBooleanExtra("show_dialog", false)) {
            dialog.dismiss();
            dialog.show();
        } else if (intent.getBooleanExtra("show_dialog", false)) {
            dialog = ProgressDialog.show(context, "1", "2");
        }
        else {
            dialog.dismiss();
        }
    }
}
