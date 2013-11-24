package pl.towelrail.locate.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import pl.towelrail.locate.R;
import pl.towelrail.locate.receivers.PostTowelLocationReceiver;
import pl.towelrail.locate.receivers.ProgressReceiver;
import pl.towelrail.locate.service.TowelLocationServiceHelper;

public class PostTowelLocationTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private Intent mProgressReceiverIntent;

    public PostTowelLocationTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        Resources resources = mContext.getResources();
        String title = resources.getString(R.string.http_request);
        String message = resources.getString(R.string.please_wait);

        mProgressReceiverIntent = new Intent(ProgressReceiver.class.getName());
        mProgressReceiverIntent.putExtra("show_dialog", true);

        mContext.sendBroadcast(mProgressReceiverIntent);
    }

    @Override
    protected String doInBackground(String... params) {
        HttpRequest request = HttpRequest.post(params[0])
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .acceptJson()
                .acceptGzipEncoding()
                .uncompress(true)
                .acceptCharset(HttpRequest.CHARSET_UTF8)
                .send(params[1]);

        String body = request.body();
        return body;
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(PostTowelLocationTask.class.getName(), s);

        mProgressReceiverIntent = new Intent(ProgressReceiver.class.getName());
        mContext.sendBroadcast(mProgressReceiverIntent);
    }
}
