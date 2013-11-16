package pl.towelrail.locate;

import android.os.Bundle;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.db.DatabaseHelper;
import pl.towelrail.locate.service.TowelLocationServiceHelper;

import java.util.List;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private TowelLocationServiceHelper helper;
    private List<TowelLocation> locations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        helper = TowelLocationServiceHelper.getInstance(this);
        helper.initialize();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
