package pl.towelrail.locate;

import android.app.FragmentTransaction;
import android.content.*;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import pl.towelrail.locate.data.TowelLocation;
import pl.towelrail.locate.db.DatabaseHelper;
import pl.towelrail.locate.receivers.GpsStatusReceiver;
import pl.towelrail.locate.receivers.ProgressReceiver;
import pl.towelrail.locate.service.TowelLocationServiceHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private List<TowelLocation> locations;
    private BroadcastReceiver mDrawLocationReceiver;
    private GpsStatusReceiver mGpsStatusReceiver;
    private MapFragment mMapFragment;
    private GoogleMap mGmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createMapFragment();


        locations = new ArrayList<TowelLocation>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.opts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start_recording:
                TowelLocationServiceHelper helper = TowelLocationServiceHelper.getInstance(this);
                helper.initialize();
                break;
            case R.id.view_last_record_session:
            case R.id.view_all_recording_sessions:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createMapFragment() {
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();

//        unregisterReceiver(mProgressReceiver);
        unregisterReceiver(mDrawLocationReceiver);
        unregisterReceiver(mGpsStatusReceiver);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> locationSet = new HashSet<String>();
        for (TowelLocation towelLocation : locations) {
            locationSet.add(towelLocation.toString());
        }

        editor.putStringSet("locations", locationSet);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Set<String> locationSet = preferences.getStringSet("locations", new HashSet<String>());

        if (locationSet.size() > 0) {
            for (String jsonLocation : locationSet) {
                locations.add(gson.fromJson(jsonLocation, TowelLocation.class));
            }
        }


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lastLocation = locationManager
                .getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));

        mGmap = mMapFragment.getMap();
        mGmap.getUiSettings().setCompassEnabled(true);
        mGmap.setMyLocationEnabled(true);

        try {

            mGmap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 10));
        } catch (Exception e) {

        }


        mDrawLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TowelLocation location = (TowelLocation) intent.getSerializableExtra("location");

                LatLng currentLatLng = new LatLng(location.getLat(), location.getLng());
                mGmap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
                mGmap.setMyLocationEnabled(true);

                if (locations.size() > 0) {
                    mGmap.addPolyline(new PolylineOptions().add(currentLatLng).add(locations.get(locations.size() - 1).getLatLng()));
                }
                locations.add(location);
            }
        };
        IntentFilter mLocationFilter = new IntentFilter("TowelLocationReceiver");
        registerReceiver(mDrawLocationReceiver, mLocationFilter);


        ProgressReceiver mProgressReceiver = new ProgressReceiver();
        IntentFilter mProgressFilter = new IntentFilter(ProgressReceiver.class.getName());
//        registerReceiver(mProgressReceiver, mProgressFilter);

        mGpsStatusReceiver = new GpsStatusReceiver(locationManager);
        IntentFilter mGpsFilter = new IntentFilter(GpsStatusReceiver.class.getName());
        registerReceiver(mGpsStatusReceiver, mGpsFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
