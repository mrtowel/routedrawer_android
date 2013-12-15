package pl.towelrail.locate.service;

import android.app.IntentService;
import android.content.Intent;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.http.PostTowelLocationTask;

import java.util.ArrayList;
import java.util.UUID;

public class PostRouteService extends IntentService {
    public PostRouteService() {
        super(UUID.randomUUID().toString());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<TowelRoute> data = (ArrayList<TowelRoute>) intent.getSerializableExtra("data");
        String url = intent.getStringExtra("url");
        String authHeaderKey = intent.getStringExtra("auth_header_key");
        String authHeaderValue = intent.getStringExtra("auth_header_value");
        Header authHeader = new BasicHeader(authHeaderKey, authHeaderValue);

        PostTowelLocationTask task = new PostTowelLocationTask(getApplicationContext(), url, authHeader);
        task.execute(data.toArray(new TowelRoute[data.size()]));
    }
}
