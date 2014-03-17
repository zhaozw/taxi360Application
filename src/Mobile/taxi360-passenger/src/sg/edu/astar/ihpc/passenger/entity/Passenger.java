package sg.edu.astar.ihpc.passenger.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import android.R.bool;

/**
 *
 * @author TEAM5FT
 */
public class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Date createtime;
    private String name;
    private String emailid;
    private String accesskey;
    private Date lastlogintime;
    private String mobilenumber;
    private String username;
    private String password;
    private String registrationtype;
    @JsonIgnore
    private Location location;
    @JsonIgnore
    private boolean relocated;
    private Double rating;
    private Long numberofratings;
    

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	public Location getLocation() {
		return location;
	}
	@JsonIgnore
	public void setLocation(Location location) {
		this.location = location;
	}
	@JsonIgnore
	public boolean isRelocated() {
		return relocated;
	}
	@JsonIgnore
	public void setRelocated(boolean relocated) {
		this.relocated = relocated;
	}

	public Passenger() {
    }

    public Passenger(Long passengerid) {
        this.id = passengerid;
    }

    

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistrationtype() {
        return registrationtype;
    }

    public void setRegistrationtype(String registrationtype) {
        this.registrationtype = registrationtype;
    } 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Passenger)) {
            return false;
        }
        Passenger other = (Passenger) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sg.edu.astar.ihpc.passenger.Passenger[ passengerid=" + id + " ]";
    }

	public Long getNumberofratings() {
		return numberofratings;
	}

	public void setNumberofratings(Long numberofratings) {
		this.numberofratings = numberofratings;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}
    
}