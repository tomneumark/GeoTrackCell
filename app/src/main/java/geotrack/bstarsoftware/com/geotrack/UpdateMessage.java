package geotrack.bstarsoftware.com.geotrack;

import android.location.Location;

/**
 * Created by Tom on 2/9/2016.
 */
public class UpdateMessage {
    private String deviceId;
    private Location location;

    public UpdateMessage(String deviceId, Location location) {
        this.deviceId = deviceId;
        this.location = location;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "deviceId=" + deviceId + " location=" + location;
    }
}
