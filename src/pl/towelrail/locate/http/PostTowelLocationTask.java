package pl.towelrail.locate.http;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import org.apache.http.Header;
import pl.towelrail.locate.R;
import pl.towelrail.locate.data.TowelRoute;
import pl.towelrail.locate.receivers.DatabaseUpdateReceiver;
import pl.towelrail.locate.receivers.ProgressReceiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PostTowelLocationTask extends AsyncTask<TowelRoute, Integer, ArrayList<TowelHttpResponse>> {
    private Context mContext;
    private Intent mProgressDialogIntent;
    private String mUrl;
    private Header mAuthHeader;

    public PostTowelLocationTask(Context mContext, String mUrl, Header mAuthHeader) {
        this.mContext = mContext;
        this.mUrl = mUrl;
        this.mAuthHeader = mAuthHeader;
    }

    @Override
    protected void onPreExecute() {
        Resources resources = mContext.getResources();
        String title = resources.getString(R.string.http_request);
        String message = resources.getString(R.string.please_wait);

        mProgressDialogIntent = new Intent(ProgressReceiver.class.getName());
        mProgressDialogIntent.putExtra("show_dialog", true);
        mProgressDialogIntent.putExtra("title", title);
        mProgressDialogIntent.putExtra("message", message);
    }

    @Override
    protected ArrayList<TowelHttpResponse> doInBackground(TowelRoute... params) {
        ArrayList<TowelHttpResponse> responses = new ArrayList<TowelHttpResponse>();

        mProgressDialogIntent.putExtra("progress_max", params.length);
        mContext.sendBroadcast(mProgressDialogIntent);

        for (int i = 0; i < params.length; i++) {
            HttpRequest request = HttpRequest.post(mUrl)
                    .contentType(HttpRequest.CONTENT_TYPE_JSON)
                    .header(mAuthHeader.getName(), mAuthHeader.getValue())
                    .acceptJson()
                    .acceptGzipEncoding()
                    .uncompress(true)
                    .acceptCharset(HttpRequest.CHARSET_UTF8)
                    .send(params[i].toString());

            responses.add(new TowelHttpResponse(request.code(), request.body(), params[i].getId()));
            publishProgress(i);
            request.disconnect();
        }
        return responses;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mProgressDialogIntent.removeExtra("show_dialog");
        mProgressDialogIntent.putExtra("update_progress", true);
        mContext.sendBroadcast(mProgressDialogIntent);

        Log.d(PostTowelLocationTask.class.getName(), Arrays.toString(values));
    }

    @Override
    protected void onPostExecute(ArrayList<TowelHttpResponse> responses) {
        mProgressDialogIntent.removeExtra("update_progress");
        mProgressDialogIntent.putExtra("show_dialog", false);
        mContext.sendBroadcast(mProgressDialogIntent);

        Intent updateDb = new Intent(DatabaseUpdateReceiver.class.getName());
        updateDb.putExtra("post_route_responses", responses);
        mContext.sendBroadcast(updateDb);

        Log.d(PostTowelLocationTask.class.getName(), responses.toString());

    }
}
