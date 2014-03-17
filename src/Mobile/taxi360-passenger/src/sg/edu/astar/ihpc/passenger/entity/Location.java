package sg.edu.astar.ihpc.passenger.entity;

import java.io.Serializable;


public class Location implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    

    public Location() {
    }
    

    public Location( Double latitude,Double longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }

  

    @Override
    public String toString() {
        return "latitude="+latitude+",longitude="+longitude;
    }
    
    
}
