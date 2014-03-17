package sg.edu.astar.ihpc.taxidriver.entity;

import java.io.Serializable;
import java.util.Date;


public class Ride implements Serializable{
	private String id;
	private static final long serialVersionUID = 1L;

	private Date createtime;
    
    private Integer feedback;
    private Double passengerRating;
    private Double driverRating;
    private Date endtime;
    private String place;
    private String distance;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Double getPassengerRating() {
        return passengerRating;
    }

    public void setPassengerRating(Double passengerRating) {
        this.passengerRating = passengerRating;
    }

    public Double getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(Double driverRating) {
        this.driverRating = driverRating;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
   

    
    public String getEndStatus() {
		return endStatus;
	}



	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}



	private Driver driver;
    
    private Passenger passenger;
    
    private String passengerKey;
    
    
    private String endStatus;
    public String getPassengerKey() {
		return passengerKey;
	}



	public void setPassengerKey(String passengerKey) {
		this.passengerKey = passengerKey;
	}



	private Location driverStartLocation;

    
    
    private Location passengerStartLocation;

    
    
    private Location passengerEndLocation;
    public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Date getCreatetime() {
		return createtime;
	}



	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}



	public Integer getFeedback() {
		return feedback;
	}



	public void setFeedback(Integer feedback) {
		this.feedback = feedback;
	}



	public Driver getDriver() {
		return driver;
	}



	public void setDriver(Driver driver) {
		this.driver = driver;
	}



	public Passenger getPassenger() {
		return passenger;
	}



	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}



	public Location getDriverStartLocation() {
		return driverStartLocation;
	}



	public void setDriverStartLocation(Location driverStartLocation) {
		this.driverStartLocation = driverStartLocation;
	}



	public Location getPassengerStartLocation() {
		return passengerStartLocation;
	}



	public void setPassengerStartLocation(Location passengerStartLocation) {
		this.passengerStartLocation = passengerStartLocation;
	}



	public Location getPassengerEndLocation() {
		return passengerEndLocation;
	}



	public void setPassengerEndLocation(Location passengerEndLocation) {
		this.passengerEndLocation = passengerEndLocation;
	}





}
