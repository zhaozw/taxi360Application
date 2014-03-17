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
import javax.persistence.FetchType;
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
 * This Entity class holds Request details.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Entity
@Table(name = "request")
@XmlRootElement
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
//@NamedQueries({
//    @NamedQuery(name = "Request.findAll", query = "SELECT r FROM Request r"),
//    @NamedQuery (name = "Request.findWithin", query = "Select r from Request r where ST_DWithin(geog, ST_GeographyFromText('SRID=4326;POINT(:log :lat)'), :distance)")})
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JoinColumn(name = "passengerid", referencedColumnName = "passengerid")
    @OneToOne(optional = false,fetch = FetchType.LAZY)
    private Passenger passenger;
    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Embedded
    private Location location;
    @Column(name = "passengerkey")
    private String passengerKey;
    @Column(name="destination")
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
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
