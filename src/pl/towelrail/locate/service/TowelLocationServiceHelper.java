package pl.towelrail.locate.service;

import android.content.Context;
import android.content.Intent;
import pl.towelrail.locate.receivers.GpsStatusReceiver;

public class TowelLocationServiceHelper {
    public final static String LOCATION_MANAGER = "location_manager";
    private Context mContext;
    private ServiceState mState;

    private static TowelLocationServiceHelper ourInstance;

    public static TowelLocationServiceHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TowelLocationServiceHelper(context);
        }
        context.sendBroadcast(new Intent(GpsStatusReceiver.class.getName()));

        return ourInstance;
    }

    private TowelLocationServiceHelper(Context context) {
        this.mContext = context;
    }

    public boolean isRunning() {
        return mState == ServiceState.RUNNING;
    }

    public void initialize() {
        if (mContext != null && mState != ServiceState.RUNNING) {
            Intent intent = new Intent(mContext, TowelLocationService.class);
            mContext.startService(intent);
            mState = ServiceState.RUNNING;
        }
    }

    public void stop() {
        ourInstance = null;
        mState = ServiceState.STOPPED;
    }
}
