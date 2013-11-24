package pl.towelrail.locate.data;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "location")
public class TowelLocation implements Serializable {
    @Expose
    @DatabaseField
    private final String api_key = "fR5UpXDIoPqyVQUR6d2O3LmwqZc6CEZ8";
    @Expose
    @DatabaseField
    private double lat;
    @Expose
    @DatabaseField
    private double lng;
    @Expose
    @DatabaseField
    private double accuracy;
    @Expose
    @DatabaseField
    private long measured_at;
    @Expose
    @DatabaseField
    private String provider;
    @DatabaseField(generatedId = true)
    private int id;

    public TowelLocation(Location location) {
        this.lat = location.getLatitude();
        this.lng = location.getLongitude();
        this.accuracy = location.getAccuracy();
        this.measured_at = System.currentTimeMillis() / 1000L;
        this.provider = location.getProvider();
    }

    TowelLocation() {
    }

    public String getApi_key() {
        return api_key;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public long getMeasured_at() {
        return measured_at;
    }

    public void setMeasured_at(long measured_at) {
        this.measured_at = measured_at;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LatLng getLatLng()
    {
        return new LatLng(getLat(), getLng());
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
