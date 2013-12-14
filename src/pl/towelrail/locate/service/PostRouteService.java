package pl.towelrail.locate.service;

import android.app.IntentService;
import android.content.Intent;
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
        PostTowelLocationTask task = new PostTowelLocationTask(getApplicationContext(), url);
        task.execute(data.toArray(new TowelRoute[data.size()]));
    }
}
