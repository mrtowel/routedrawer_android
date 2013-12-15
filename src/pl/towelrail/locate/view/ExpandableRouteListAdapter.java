package pl.towelrail.locate.view;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;
import pl.towelrail.locate.R;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.data.TowelRoute;

import java.util.Date;

public class ExpandableRouteListAdapter extends BaseExpandableListAdapter {
    private final SparseArray<TowelRoute> routes;
    private Activity activity;
    private LayoutInflater inflater;

    public ExpandableRouteListAdapter(SparseArray<TowelRoute> routes, Activity activity) {
        this.routes = routes;
        this.activity = activity;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getGroupCount() {
        return routes.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return routes.get(i).getLocations().size();
    }

    @Override
    public Object getGroup(int i) {
        return routes.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return routes.get(i).getLocationByIndex(i2);
    }

    @Override
    public long getGroupId(int i) {
        return routes.get(i).getId();
    }

    @Override
    public long getChildId(int i, int i2) {
        return routes.get(i).getLocationByIndex(i2).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.routes_listrow_group, null);
        }

        TowelRoute route = (TowelRoute) getGroup(i);
        if (route != null) {
            String info = String.format(
                    "%s m | %s points | uploaded: %s",
                    route.getDistance(), route.getTotalPoints(), route.getUploaded());
            ((CheckedTextView) view).setText(info);
            ((CheckedTextView) view).setChecked(isExpanded);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.routes_listrow_details, null);
        }
        final TowelLocation location = (TowelLocation) getChild(i, i2);
        if (location == null) {
            return null;
        }

        TextView mainTextView = (TextView) view.findViewById(R.id.routes_detail_main_text_view);
        TextView headerTextView = (TextView) view.findViewById(R.id.routes_detail_header_text_view);
        TextView footerTextView = (TextView) view.findViewById(R.id.routes_detail_footer_text_view);

        mainTextView.setText(
                String.format("latitude: %s, longitude: %s", location.getLat(), location.getLng()));
        headerTextView.setText(
                String.format("measure time: %s", new Date(location.getMeasured_at() * 1000)));
        footerTextView.setText(
                String.format("> accuracy: %s m, provider: %s",
                        location.getAccuracy(), location.getProvider()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(
                        activity.getApplicationContext(), String.valueOf(location.getId()), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }
}
