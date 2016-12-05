package gr.devian.talosquests.backend.LocationProvider;

import java.io.Serializable;

/**
 * Created by Nikolas on 3/12/2016.
 */
public class Distance extends com.google.maps.model.Distance implements Serializable {
    public Distance(com.google.maps.model.Distance dist) {
        inMeters = dist.inMeters;
        humanReadable = dist.humanReadable;
    }
}