package pl.towelrail.locate.http;

import java.io.Serializable;

public class TowelHttpResponse implements Serializable {
    private int mStatusCode;
    private String mBody;
    private long mObjectId;

    public TowelHttpResponse(int mStatusCode, String mBody, long mObjectId) {
        this.mStatusCode = mStatusCode;
        this.mBody = mBody;
        this.mObjectId = mObjectId;
    }

    public int getmStatusCode() {
        return mStatusCode;
    }

    public String getmBody() {
        return mBody;
    }

    public long getmObjectId() {
        return mObjectId;
    }

    @Override
    public String toString() {
        return "TowelHttpResponse{" +
                "mStatusCode=" + mStatusCode +
                ", mBody='" + mBody + '\'' +
                ", mObjectId=" + mObjectId +
                '}';
    }
}
