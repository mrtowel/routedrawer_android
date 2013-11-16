package pl.towelrail.locate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import pl.towelrail.locate.R;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> locations;
    private int resource;


    public LocationAdapter(Context context, int resource, List<String> locations) {
        super(context, resource, locations);
        this.context = context;
        this.locations = locations;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(android.R.drawable.ic_dialog_map);
        textView.setText(locations.get(position));

        return rowView;
    }
}

