package pl.towelrail.locate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;
import pl.towelrail.locate.service.PostRouteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostTowelLocationReceiver extends BroadcastReceiver {
    private final String URL = "https://shop-manager.herokuapp.com/route";
    //    private final String URL = "http://192.168.56.1:3000/route";
    private Context mContext;

    public PostTowelLocationReceiver(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseModel<TowelRoute, Long> model =
                new DatabaseModel<TowelRoute, Long>(TowelRoute.class, Long.class, context);
        ArrayList<TowelRoute> routes = (ArrayList<TowelRoute>) DatabaseTools.fetchAll(model);
        // TODO: preprare filtered query
        ArrayList<TowelRoute> filteredRoutes = new ArrayList<TowelRoute>();

        for (TowelRoute route : routes){
            if (!route.getUploaded()){
                filteredRoutes.add(route);
            }
        }
        context.sendBroadcast(new Intent(NetworkStatusReceiver.class.getName()));

        Intent postRouteService =
                new Intent(mContext.getApplicationContext(), PostRouteService.class);

        postRouteService.putExtra("data", filteredRoutes);
        postRouteService.putExtra("url", URL);

        mContext.startService(postRouteService);

    }
}
