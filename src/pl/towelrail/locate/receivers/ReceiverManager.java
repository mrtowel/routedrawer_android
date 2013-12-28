package pl.towelrail.locate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Performs mass register/unregister of broadcast receivers.
 */
public class ReceiverManager {
    private Context mContext;

    public ReceiverManager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Registers receivers array.
     *
     * @param receivers Receivers array.
     * @param <T>       Receiver class extending {@code BroadcastReceiver}.
     */
    public <T extends BroadcastReceiver> void registerReceivers(T... receivers) {
        for (T receiver : receivers) {
            IntentFilter filter = new IntentFilter(receiver.getClass().getName());
            mContext.registerReceiver(receiver, filter);
        }
    }

    /**
     * Registers receivers array.
     *
     * @param receivers Receivers array.
     * @param <T>       Receiver class extending {@code BroadcastReceiver}.
     */
    public <T extends BroadcastReceiver> void unregisterReceivers(T... receivers) {
        for (T receiver : receivers) {
            mContext.unregisterReceiver(receiver);
        }
    }
}
