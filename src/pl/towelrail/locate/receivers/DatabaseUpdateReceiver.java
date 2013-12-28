package pl.towelrail.locate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;
import pl.towelrail.locate.http.TowelHttpResponse;

import java.util.ArrayList;

/**
 * Sets upload state to {@code true}. Intent must contain {@code ArrayList} of {@code TowelHttpResponse}.
 */
public class DatabaseUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<TowelHttpResponse> responses =
                (ArrayList<TowelHttpResponse>) intent.getSerializableExtra("post_route_responses");
        int uploadCount = 0;
        for (TowelHttpResponse towelHttpResponse : responses) {
            DatabaseModel<TowelRoute, Long> model =
                    new DatabaseModel<TowelRoute, Long>(TowelRoute.class, Long.class, context);
            TowelRoute route = DatabaseTools.getById(model, towelHttpResponse.getmObjectId());
            if (route != null && !route.getUploaded()) {
                route.setUploaded(true);
                if (DatabaseTools.update(model, route)) {
                    ++uploadCount;
                }
            }
        }

        Toast.makeText(context,
                String.format("Successfully uploaded %s routes to server", uploadCount), Toast.LENGTH_SHORT).show();
    }
}
