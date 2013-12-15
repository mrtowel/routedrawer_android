package pl.towelrail.locate.http;


import org.apache.http.conn.scheme.PlainSocketFactory;

public class TowelHttpConstants {
    public static final String API_KEY_AUTHENTICATION_HEADER = "X-TOWELRAIL-APIKEY-AUTH";
    public static final String TOWEL_ROUTE_POST_URL = "https://shop-manager.herokuapp.com/route";
    public static final String TOWEL_ROUTE_POST_URL_LOCAL = "http://192.168.56.1:3000/route";

    private TowelHttpConstants() {}

}
