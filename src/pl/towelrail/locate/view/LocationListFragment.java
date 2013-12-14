package pl.towelrail.locate.view;

import android.R;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.db.DatabaseModel;
import pl.towelrail.locate.db.DatabaseTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationListFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseModel<TowelLocation, Long> model = new DatabaseModel<TowelLocation, Long>(
                TowelLocation.class, Long.class, getActivity().getApplicationContext());
        List<TowelLocation> locations = DatabaseTools.fetchAll(model);

        ArrayAdapter<String> adapter;
        List<String> locationsStringList = new ArrayList<String>();

        for (TowelLocation location : locations) {
            StringBuilder sb = new StringBuilder();
            sb.append(location.getLatLng())
                    .append(", ").append(" @ ")
                    .append(new Date(location.getMeasured_at()));
            locationsStringList.add(sb.toString());
        }
        adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.simple_list_item_multiple_choice, locationsStringList);
        setListAdapter(adapter);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (v.isSelected()) {
            v.setSelected(false);
        } else {
            v.setBackgroundResource(R.color.background_light);
            l.setSelection(position);
            l.setBackgroundResource(R.color.white);
            l.setSelected(true);
        }
        Toast.makeText(getActivity().getApplicationContext(),
                String.format("pos: %s, id: %s", position, id), Toast.LENGTH_SHORT).show();
    }
}
