package pl.towelrail.locate.service;

import android.content.Context;
import android.content.Intent;

public class TowelLocationServiceHelper {
    public final static String LOCATION_MANAGER = "location_manager";
    private Context mContext;
    private ServiceState mState;

    private static TowelLocationServiceHelper ourInstance;

    public static TowelLocationServiceHelper getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new TowelLocationServiceHelper(context);
        }
        return ourInstance;
    }

    private TowelLocationServiceHelper(Context context) {
        this.mContext = context;
    }

    public boolean isRunning() {
        return mState == ServiceState.RUNNING;
    }

    public void initialize() {
        if (mContext != null) {
            Intent intent = new Intent(mContext, TowelLocationService.class);
            mContext.startService(intent);

            mState = ServiceState.RUNNING;
        }
    }


}
