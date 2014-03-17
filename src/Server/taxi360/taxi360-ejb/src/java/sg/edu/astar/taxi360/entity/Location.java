/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.entity;

import java.util.Objects;
import javax.persistence.Embeddable;

/**
 * This Embeddable class holds location details.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Embeddable
public class Location {

    private Double latitude;
    private Double longitude;

    /**
     *
     * @return
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    

    /**
     *
     */
    public Location() {
    }
    

    /**
     *
     * @param latitude
     * @param longitude
     */
    public Location( Double latitude,Double longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.latitude);
        hash = 89 * hash + Objects.hashCode(this.longitude);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (!Objects.equals(this.latitude, other.latitude)) {
            return false;
        }
        if (!Objects.equals(this.longitude, other.longitude)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "latitude="+latitude+",longitude="+longitude;
    }
    
    
}
