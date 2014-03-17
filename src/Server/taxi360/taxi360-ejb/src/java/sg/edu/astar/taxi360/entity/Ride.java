/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This Entity class holds Ride details.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Entity
@Table(name = "ride")
@XmlRootElement
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@NamedQueries({
    @NamedQuery(name = "Ride.findAll", query = "SELECT r FROM Ride r")})
public class Ride implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rideid")
    private String id;
    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "feedback")
    private Integer feedback;
    @JoinColumn(name = "driverid", referencedColumnName = "driverid")
    @OneToOne(optional = false)
    private Driver driver;
    @JoinColumn(name = "passengerid", referencedColumnName = "passengerid")
    @OneToOne(optional = false)
    private Passenger passenger;
    
    @Embedded
    @AttributeOverrides({
    @AttributeOverride(name="latitude", column=@Column(name="Driver_Start_latitude")),
    @AttributeOverride(name="longitude", column=@Column(name="Driver_Start_longitude"))})
    private Location driverStartLocation;

    
    @Embedded
    @AttributeOverrides({
    @AttributeOverride(name="latitude", column=@Column(name="Passenger_Start_latitude")),
    @AttributeOverride(name="longitude", column=@Column(name="Passenger_Start_longitude"))})
    private Location passengerStartLocation;

    
    @Embedded
    @AttributeOverrides({
    @AttributeOverride(name="latitude", column=@Column(name="Passenger_end_latitude")),
    @AttributeOverride(name="longitude", column=@Column(name="Passenger_end_longitude"))})
    private Location passengerEndLocation;
    
    
    
    @Column(name = "passengerkey")
    private String passengerKey;
    @Column(name = "endstatus")
    private String endStatus;
    @Column(name = "passengerrating")
    private Double passengerRating;
    @Column(name = "driverrating")
    private Double driverRating;
    @Column(name = "endtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @Column(name = "place")
    private String place;
    @Column(name = "distance")
    private Double distance;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
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

    
    

    public String getPassengerKey() {
        return passengerKey;
    }

    public void setPassengerKey(String passengerKey) {
        this.passengerKey = passengerKey;
    }

    public String getEndStatus() {
        return endStatus;
    }

    public void setEndStatus(String endStatus) {
        this.endStatus = endStatus;
    }

    /**
     *
     * @return
     */
    public Location getDriverStartLocation() {
        return driverStartLocation;
    }

    /**
     *
     * @param driverStartLocation
     */
    public void setDriverStartLocation(Location driverStartLocation) {
        this.driverStartLocation = driverStartLocation;
    }

    /**
     *
     * @return
     */
    public Location getPassengerStartLocation() {
        return passengerStartLocation;
    }

    /**
     *
     * @param passengerStartLocation
     */
    public void setPassengerStartLocation(Location passengerStartLocation) {
        this.passengerStartLocation = passengerStartLocation;
    }

    /**
     *
     * @return
     */
    public Location getPassengerEndLocation() {
        return passengerEndLocation;
    }

    /**
     *
     * @param passengerEndLocation
     */
    public void setPassengerEndLocation(Location passengerEndLocation) {
        this.passengerEndLocation = passengerEndLocation;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     *
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     *
     * @return
     */
    public Integer getFeedback() {
        return feedback;
    }

    /**
     *
     * @param feedback
     */
    public void setFeedback(Integer feedback) {
        this.feedback = feedback;
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
    public Passenger getPassenger() {
        return passenger;
    }

    /**
     *
     * @param passenger
     */
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
    
    

    /**
     *
     */
    public Ride() {
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
        if (!(object instanceof Ride)) {
            return false;
        }
        Ride other = (Ride) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ride{" + "id=" + id + ", createtime=" + createtime + ", feedback=" + feedback + ", driver=" + driver + ", passenger=" + passenger + ", driverStartLocation=" + driverStartLocation + ", passengerStartLocation=" + passengerStartLocation + ", passengerEndLocation=" + passengerEndLocation + ", passengerKey=" + passengerKey + ", endStatus=" + endStatus + '}';
    }

    
    
}
