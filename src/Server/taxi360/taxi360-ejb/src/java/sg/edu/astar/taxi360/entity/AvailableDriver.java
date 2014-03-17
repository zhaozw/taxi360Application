/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This Entity class holds the details about the active driver.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Entity
@Table(name = "availabledriver")
@XmlRootElement
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@NamedQueries({
    @NamedQuery(name = "AvailableDriver.findAll", query = "SELECT a FROM AvailableDriver a"),
    @NamedQuery(name = "AvailableDriver.findById", query = "SELECT a FROM AvailableDriver a where a.driver.id = :id")})
public class AvailableDriver implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JoinColumn(name = "availabledriverid", referencedColumnName = "driverid")
    @OneToOne(optional = false)
    private Driver driver;
    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Embedded
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
