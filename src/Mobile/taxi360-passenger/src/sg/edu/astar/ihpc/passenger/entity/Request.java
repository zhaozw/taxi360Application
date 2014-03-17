package sg.edu.astar.ihpc.passenger.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import java.util.Date;





/**
 *
 * @author mido
 */

public class Request implements Serializable {
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

    public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passenger != null ? passenger.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((this.passenger == null && other.passenger != null) || (this.passenger != null && !this.passenger.equals(other.passenger))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sg.edu.astar.taxi360.entity.Request[ requestid=" + passenger + " ]";
    }
    
}

