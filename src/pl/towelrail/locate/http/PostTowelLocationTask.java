package pl.towelrail.locate.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.github.kevinsawicki.http.HttpRequest;
import pl.towelrail.locate.receivers.PostTowelLocationReceiver;

public class PostTowelLocationTask extends AsyncTask<String, Void, String> {
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public PostTowelLocationTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("Posting location");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();
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
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        Log.d(PostTowelLocationTask.class.getName(), s);
//        Toast.makeText(mContext, "finished", Toast.LENGTH_LONG).show();
        mContext.sendBroadcast(new Intent(mContext, PostTowelLocationReceiver.class));
    }
}
