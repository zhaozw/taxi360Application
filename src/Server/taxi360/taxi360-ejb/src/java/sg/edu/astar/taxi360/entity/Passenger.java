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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This Entity class holds passenger details.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Entity
@Table(name = "passenger")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@NamedQueries({
    @NamedQuery(name = "Passenger.findAll", query = "SELECT p FROM Passenger p"),
    @NamedQuery(name = "Passenger.findbyemail", query = "SELECT p FROM Passenger p where p.emailid = :emailid and p.registrationtype = :registrationtype"),
    @NamedQuery(name = "Passenger.validatePassword", query = "SELECT p FROM Passenger p where p.emailid = :emailid and p.password=:password"),
    @NamedQuery(name = "Passenger.updateAccessKey", query = "update Passenger p set p.accesskey = :accesskey where p.id = :id")})
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "passengerid")
    private Long id;
    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "name")
    private String name;
    @Column(name = "emailid")
    private String emailid;
    @Column(name = "accesskey")
    private String accesskey;
    @Column(name = "lastlogintime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastlogintime;
    @Column(name = "mobilenumber")
    private String mobilenumber;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "registrationtype")
    private String registrationtype;
     @Column(name = "numberofratings")
    private Long numberofratings;
    @Column(name = "rating")
    private Double rating;
    /**
     *
     */
    public Passenger() {
    }

    /**
     *
     * @param id
     */
    public Passenger(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
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
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getEmailid() {
        return emailid;
    }

    /**
     *
     * @param emailid
     */
    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    /**
     *
     * @return
     */
    public String getAccesskey() {
        return accesskey;
    }

    /**
     *
     * @param accesskey
     */
    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    /**
     *
     * @return
     */
    public Date getLastlogintime() {
        return lastlogintime;
    }

    /**
     *
     * @param lastlogintime
     */
    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    /**
     *
     * @return
     */
    public String getMobilenumber() {
        return mobilenumber;
    }

    /**
     *
     * @param mobilenumber
     */
    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    @XmlTransient
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getRegistrationtype() {
        return registrationtype;
    }

    /**
     *
     * @param registrationtype
     */
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
        return "sg.edu.astar.taxi360.entity.Passenger[ passengerid=" + id + " ]";
    }

    /**
     * @return the numberofratings
     */
    public Long getNumberofratings() {
        return numberofratings;
    }

    /**
     * @param numberofratings the numberofratings to set
     */
    public void setNumberofratings(Long numberofratings) {
        this.numberofratings = numberofratings;
    }

    /**
     * @return the rating
     */
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating=rating;
    }
}
