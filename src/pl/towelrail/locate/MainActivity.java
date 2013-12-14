package pl.towelrail.locate;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.db.DatabaseHelper;
import pl.towelrail.locate.receivers.*;
import pl.towelrail.locate.service.TowelLocationServiceHelper;
import pl.towelrail.locate.view.LocationListActivity;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private DrawLocationReceiver mDrawLocationReceiver;
    private GpsStatusReceiver mGpsStatusReceiver;
    private NetworkStatusReceiver mNetworkStatusReceiver;
    private PostTowelLocationReceiver mPostTowelLocationReceiver;
    private ProgressReceiver mProgressReceiver;
    private MapFragment mMapFragment;
    private GoogleMap mGmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createMapFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.opts_menu, menu);

        if (TowelLocationServiceHelper.getInstance(this).isRunning()) {
            MenuItem item = menu.findItem(R.id.start_recording_item);
            if (item != null) {
                item.setIcon(android.R.drawable.ic_menu_save);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TowelLocationServiceHelper helper = TowelLocationServiceHelper.getInstance(this);

        switch (item.getItemId()) {
            case R.id.start_recording_item:
                if (helper.isRunning()) {
                    helper.stop();
                    item.setIcon(android.R.drawable.presence_video_online);

                } else {
                    mGmap.clear();
                    helper.initialize();
                    item.setIcon(android.R.drawable.presence_video_busy);
                }
                break;
            case R.id.view_all_recording_sessions_item:
                Intent intent = new Intent(getApplicationContext(), LocationListActivity.class);
                startActivity(intent);
                break;
            case R.id.send_data_to_server_item:
                if (!helper.isRunning()) {
                    sendBroadcast(new Intent(PostTowelLocationReceiver.class.getName()));
                } else {
                    Toast.makeText(this, "Stop recording first!", Toast.LENGTH_SHORT).show();
                }
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

        unregisterReceiver(mProgressReceiver);
        unregisterReceiver(mDrawLocationReceiver);
        unregisterReceiver(mGpsStatusReceiver);
        unregisterReceiver(mNetworkStatusReceiver);
        unregisterReceiver(mPostTowelLocationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGmap = mMapFragment.getMap();
        mGmap.getUiSettings().setCompassEnabled(true);
        mGmap.setMyLocationEnabled(true);

        TowelLocationServiceHelper helper = TowelLocationServiceHelper.getInstance(this);
        if (helper.isRunning() && helper.getCurrentRoute().getLocations() != null) {
            TowelRoute.drawRoute(helper.getCurrentRoute(), mGmap);
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lastLocation = locationManager
                .getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
        if (lastLocation != null) {
            mGmap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 10));
        }

        mDrawLocationReceiver = new DrawLocationReceiver(mGmap, this);
        IntentFilter filter = new IntentFilter(DrawLocationReceiver.class.getName());
        registerReceiver(mDrawLocationReceiver, filter);

        mProgressReceiver = new ProgressReceiver(this);
        filter = new IntentFilter(ProgressReceiver.class.getName());
        registerReceiver(mProgressReceiver, filter);

        mGpsStatusReceiver = new GpsStatusReceiver(locationManager);
        filter = new IntentFilter(GpsStatusReceiver.class.getName());
        registerReceiver(mGpsStatusReceiver, filter);

        mNetworkStatusReceiver = new NetworkStatusReceiver();
        filter = new IntentFilter(NetworkStatusReceiver.class.getName());
        registerReceiver(mNetworkStatusReceiver, filter);

        mPostTowelLocationReceiver = new PostTowelLocationReceiver(this);
        filter = new IntentFilter(PostTowelLocationReceiver.class.getName());
        registerReceiver(mPostTowelLocationReceiver, filter);
    }
}
