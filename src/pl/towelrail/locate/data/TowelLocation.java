package pl.towelrail.locate.data;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;

@DatabaseTable(tableName = "locations")
public class TowelLocation implements Serializable {
    @Expose
    @DatabaseField(dataType = DataType.BIG_DECIMAL_NUMERIC)
    private BigDecimal lat;
    @Expose
    @DatabaseField(dataType = DataType.BIG_DECIMAL_NUMERIC)
    private BigDecimal lng;
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
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private TowelRoute route;

    public TowelLocation(Location location, TowelRoute route) {
        this.lat = BigDecimal.valueOf(location.getLatitude());
        this.lng = BigDecimal.valueOf(location.getLongitude());
        this.accuracy = location.getAccuracy();
        this.measured_at = System.currentTimeMillis() / 1000L;
        this.provider = location.getProvider();
        this.route = route;
    }

    public TowelLocation() {
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
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

    public LatLng getLatLng() {
        return new LatLng(getLat().doubleValue(), getLng().doubleValue());
    }

    public TowelRoute getRoute() {
        return route;
    }

    public void setRoute(TowelRoute route) {
        this.route = route;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson.toJson(this);
    }
}
