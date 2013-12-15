package pl.towelrail.locate.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;
import pl.towelrail.locate.receivers.GpsStatusReceiver;

import java.util.Date;

public class TowelLocationServiceHelper {
    private Context mContext;
    private ServiceState mState;
    private TowelRoute mTowelRoute;

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
            mTowelRoute = new TowelRoute(System.currentTimeMillis() / 1000L);
        }
    }

    public void stop() {
        ourInstance = null;
        mState = ServiceState.STOPPED;

        if (mTowelRoute.getLocations() != null && mTowelRoute.getLocations().size() > 1) {
            mTowelRoute.setStop(System.currentTimeMillis() / 1000L);
            mTowelRoute.setTotalPoints(mTowelRoute.getLocations().size());
            mTowelRoute.calculateDistance();
            DatabaseModel<TowelRoute, Long> model =
                    new DatabaseModel<TowelRoute, Long>(TowelRoute.class, Long.class, mContext);
            DatabaseTools.update(model, mTowelRoute);
        } else {
            DatabaseTools.remove(mTowelRoute);
        }

        boolean succStop = mContext.stopService(new Intent(mContext.getApplicationContext(), TowelLocationService.class));

        Log.d(TowelLocationServiceHelper.class.getSimpleName(),"service stop callback: " + String.valueOf(succStop));

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting().create();
        Log.d("route", gson.toJson(mTowelRoute));
    }

    public TowelRoute getCurrentRoute() {
        if (isRunning()) return mTowelRoute;
        else return null;
    }
}
