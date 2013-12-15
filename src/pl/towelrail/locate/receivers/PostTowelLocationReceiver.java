package pl.towelrail.locate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import pl.towelrail.locate.R;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;
import pl.towelrail.locate.http.TowelHttpConstants;
import pl.towelrail.locate.service.PostRouteService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PostTowelLocationReceiver extends BroadcastReceiver {
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

        Resources resources = mContext.getResources();
        String authHeaderValue =
                resources.getString(R.string.email)
                .concat(":")
                .concat(resources.getString(R.string.api_key));

        postRouteService.putExtra("data", filteredRoutes);
        postRouteService.putExtra("url", TowelHttpConstants.TOWEL_ROUTE_POST_URL_LOCAL);
        postRouteService.putExtra("auth_header_key", TowelHttpConstants.API_KEY_AUTHENTICATION_HEADER);
        postRouteService.putExtra("auth_header_value", authHeaderValue);
        mContext.startService(postRouteService);

    }
}
