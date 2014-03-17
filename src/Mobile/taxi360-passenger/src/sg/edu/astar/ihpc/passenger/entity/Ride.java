package sg.edu.astar.ihpc.passenger.entity;

import java.io.Serializable;
import java.util.Date;


public class Ride implements Serializable{
	private String id;
	private static final long serialVersionUID = 1L;

	private Date createtime;    
    private Integer feedback;
	private Driver driver;    
    private Passenger passenger;    
    private String passengerKey;
	private Location driverStartLocation;    
    private Location passengerStartLocation;
    private String endStatus;
    private Double rating;
    private Location passengerEndLocation;
    
    
    public String getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
	}    
    
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
    public String getPassengerKey() {
		return passengerKey;
	}

	public void setPassengerKey(String passengerKey) {
		this.passengerKey = passengerKey;
	}    
    
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
