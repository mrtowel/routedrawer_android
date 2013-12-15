package pl.towelrail.locate.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ExpandableListView;
import pl.towelrail.locate.R;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;

import java.util.Collections;
import java.util.List;

public class LocationListActivity extends Activity {
    private SparseArray<TowelRoute> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes_expandable_list);
        fetchRoutesFromDb();
        ExpandableListView expandableListView =
                (ExpandableListView) findViewById(R.id.route_list_view);

        ExpandableRouteListAdapter adapter = new ExpandableRouteListAdapter(routes, this);
        expandableListView.setAdapter(adapter);
    }

    public void fetchRoutesFromDb() {
        DatabaseModel<TowelRoute, Long> model =
                new DatabaseModel<TowelRoute, Long>(TowelRoute.class, Long.class, this);
        routes = new SparseArray<TowelRoute>();
        List<TowelRoute> routeList = DatabaseTools.fetchAll(model);
        Collections.reverse(routeList);

        int i = 0;
        for (TowelRoute route : routeList) {
            if (route.getLocations() != null) routes.append(i++, route);
        }
    }
}
