package pl.towelrail.locate.view;

import android.app.Activity;
import android.os.Bundle;
import pl.towelrail.locate.R;

public class MapActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.map_fragment);
    }
}
