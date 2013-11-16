package pl.towelrail.locate.view;

import android.app.ListActivity;
import android.os.Bundle;
import pl.towelrail.locate.R;

import java.util.List;

public class LocationListActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> locations = savedInstanceState.getStringArrayList("locations");
        LocationAdapter adapter = new LocationAdapter(getApplicationContext(), R.layout.rowlayout, locations);
        setListAdapter(adapter);
    }
}
