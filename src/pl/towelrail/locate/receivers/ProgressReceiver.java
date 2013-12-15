package pl.towelrail.locate.receivers;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProgressReceiver extends BroadcastReceiver {
    private ProgressDialog mDialog;
    private Context mContext;

    public ProgressReceiver(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra("show_dialog", false)) {
            String title = intent.getStringExtra("title");
            String message = intent.getStringExtra("message");
            int maxProgress = intent.getIntExtra("progress_max", 100);

            mDialog = new ProgressDialog(mContext);
            mDialog.setTitle(title);
            mDialog.setMessage(message);
            mDialog.setMax(maxProgress);
            mDialog.setProgress(0);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.show();
        } else if (intent.hasExtra("update_progress")) {
            mDialog.incrementProgressBy(1);
        } else {
            mDialog.dismiss();
        }
    }
}
