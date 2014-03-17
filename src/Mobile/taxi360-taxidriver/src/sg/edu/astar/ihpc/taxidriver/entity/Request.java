package sg.edu.astar.ihpc.taxidriver.entity;

import java.io.Serializable;
import java.util.Date;



import android.os.Parcel;
import android.os.Parcelable;


public class Request implements Serializable {
	
	    
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private Passenger passenger;
	    
	    private Date createtime;
	   
	    private Location location;
	    
	    private String passengerKey;
	    private String destination;
	    

	    public String getPassengerKey() {
			return passengerKey;
		}

		public void setPassengerKey(String passengerKey) {
			this.passengerKey = passengerKey;
		}

		public Location getLocation() {
	        return location;
	    }

	    public void setLocation(Location location) {
	        this.location = location;
	    }    

	    public Passenger getPassenger() {
	        return passenger;
	    }

	    public void setPassenger(Passenger passenger) {
	        this.passenger = passenger;
	    }

	    public Date getCreatetime() {
	        return createtime;
	    }

	    public void setCreatetime(Date createtime) {
	        this.createtime = createtime;
	    }

	    public Request() {
	    }

		public String getDestination() {
			return destination;
		}

		public void setDestination(String destination) {
			this.destination = destination;
		}

		


}
