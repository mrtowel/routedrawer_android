package pl.towelrail.locate.data;

import android.graphics.Color;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

@DatabaseTable(tableName = "route")
public class TowelRoute extends BaseDaoEnabled implements Serializable {
    @DatabaseField(generatedId = true)
    long id;
    @Expose
    @DatabaseField(canBeNull = true)
    private long start;
    @Expose
    @DatabaseField(canBeNull = true)
    private long stop;
    @Expose
    @DatabaseField(canBeNull = true)
    private BigDecimal distance;
    @Expose
    @DatabaseField(canBeNull = true)
    private Integer totalPoints;
    @DatabaseField(canBeNull = true)
    private boolean uploaded;
    @DatabaseField(canBeNull = true)
    private Date uploadDate;
    @Expose
    @ForeignCollectionField(eager = true)
    private ForeignCollection<TowelLocation> locations;

    public TowelRoute() {
        uploaded = false;
    }

    public TowelRoute(long start) {
        this();
        this.start = start;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public double getStop() {
        return stop;
    }

    public void setStop(long stop) {
        this.stop = stop;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public boolean getUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public ForeignCollection<TowelLocation> getLocations() {
        return locations;
    }

    public BigDecimal calculateDistance() {
        distance = BigDecimal.ZERO;

        if (getLocations() != null && getLocations().size() > 1) {
            double radius = 3958.75;
            TowelLocation last = null;

            for (TowelLocation current : getLocations()) {
                if (last == null) {
                    last = current;
                    continue;
                }
                double lat1 = last.getLat().doubleValue();
                double lat2 = current.getLat().doubleValue();
                double lng1 = last.getLng().doubleValue();
                double lng2 = current.getLng().doubleValue();

                double dLat = Math.toRadians(lat2 - lat1);
                double dLng = Math.toRadians(lng2 - lng1);

                double a = Math.pow(Math.sin(dLat / 2), 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.pow(Math.sin(dLng / 2), 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double meterConversion = 1609.0;
                distance = distance.add(BigDecimal.valueOf(radius * c * meterConversion));

                last = current;

            }
        }
        distance = distance.setScale(2, BigDecimal.ROUND_HALF_DOWN);
        return distance;
    }

    public TowelLocation getLocationByIndex(int index) {
        if (getLocations() == null || getLocations().size() <= index) {
            throw new IllegalArgumentException(
                    String.format("Could not find location with index: %s", index));
        }
        int i = 0;
        for (Iterator<TowelLocation> it = getLocations().iterator(); it.hasNext(); i++) {
            TowelLocation location = it.next();
            if (i == index) return location;
        }

        return null;
    }

    public LatLng[] getLatLngList() {

        LatLng[] latLngs = null;
        if (getLocations() != null) {
            latLngs = new LatLng[getLocations().size()];
            int i = 0;
            for (TowelLocation location : getLocations()) {
                latLngs[i] = location.getLatLng();
                i++;
            }
        }

        return latLngs;
    }

    public static void drawRoute(TowelRoute route, GoogleMap gmap) {
        gmap.addPolyline(new PolylineOptions().add(route.getLatLngList()).color(Color.GRAY));
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
