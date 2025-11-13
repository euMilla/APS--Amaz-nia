import java.text.SimpleDateFormat;
import java.util.Date;

public class SatelliteImage implements Comparable<SatelliteImage> {
    private String imageId;
    private double latitude;
    private double longitude;
    private long timestamp;
    private String quality;
    private String region;
    private int sizeKB;
    
    public SatelliteImage(String imageId, double latitude, double longitude, 
                         long timestamp, String quality, String region, int sizeKB) {
        this.imageId = imageId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.quality = quality;
        this.region = region;
        this.sizeKB = sizeKB;
    }
    
    public SatelliteImage(String imageId, double latitude, double longitude, 
                         long timestamp, String quality) {
        this(imageId, latitude, longitude, timestamp, quality, "Amazônia", 2048);
    }
    
    public String getImageId() { return imageId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public long getTimestamp() { return timestamp; }
    public String getQuality() { return quality; }
    public String getRegion() { return region; }
    public int getSizeKB() { return sizeKB; }
    
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
    
    @Override
    public int compareTo(SatelliteImage other) {
        return this.imageId.compareTo(other.imageId);
    }
    
    public int compareByTimestamp(SatelliteImage other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
    
    public int compareByLatitude(SatelliteImage other) {
        return Double.compare(this.latitude, other.latitude);
    }
    
    public int compareByLongitude(SatelliteImage other) {
        return Double.compare(this.longitude, other.longitude);
    }
    
    public int compareByQuality(SatelliteImage other) {
        return this.quality.compareTo(other.quality);
    }
    
    @Override
    public String toString() {
        return String.format("Image[%s] Lat: %.4f, Long: %.4f, Time: %s, Quality: %s, Region: %s, Size: %dKB",
                           imageId, latitude, longitude, getFormattedDate(), quality, region, sizeKB);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SatelliteImage that = (SatelliteImage) obj;
        return imageId.equals(that.imageId);
    }
    
    @Override
    public int hashCode() {
        return imageId.hashCode();
    }
}