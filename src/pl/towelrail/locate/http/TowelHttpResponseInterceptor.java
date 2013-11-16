package pl.towelrail.locate.http;

import org.apache.http.*;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class TowelHttpResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        HttpEntity entity = httpResponse.getEntity();
        Header ceheader = entity.getContentEncoding();
        if (ceheader != null) {
            HeaderElement[] codecs = ceheader.getElements();
            for (int i = 0; i < codecs.length; i++) {
                if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                    httpResponse.setEntity(
                            new GzipDecompressingEntity(httpResponse.getEntity()));
                    return;
                }
            }
        }
    }

    private static class GzipDecompressingEntity extends HttpEntityWrapper {
        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent() throws IOException, IllegalStateException {
            InputStream wrappedIn = wrappedEntity.getContent();
            return new GZIPInputStream(wrappedIn);
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }
}
