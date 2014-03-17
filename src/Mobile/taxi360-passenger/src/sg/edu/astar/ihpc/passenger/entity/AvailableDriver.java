/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.ihpc.passenger.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * This Entity class holds the details about the active driver.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */

public class AvailableDriver implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Driver driver;
   
    private Date createTime;
    
    private Location location;

    /**
     *
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }    
    
    /**
     *
     * @return
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     *
     * @param driver
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     *
     * @return
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    /**
     *
     */
    public AvailableDriver() {
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (driver != null ? driver.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvailableDriver)) {
            return false;
        }
        AvailableDriver other = (AvailableDriver) object;
        if ((this.driver == null && other.driver != null) || (this.driver != null && !this.driver.equals(other.driver))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sg.edu.astar.taxi360.entity.AvailableDriver[ availabledriverid=" + driver + " ]";
    }
    
}
