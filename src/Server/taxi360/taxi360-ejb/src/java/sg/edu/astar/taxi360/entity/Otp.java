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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This Entity class holds details of OTP sent to mobile number.
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Entity
@Table(name = "otp")
@XmlRootElement
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
@NamedQueries({
    @NamedQuery(name = "Otp.findAll", query = "SELECT o FROM Otp o")})
public class Otp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "mobilenumber")
    private String mobilenumber;
    @Column(name = "otpnumber")
    private Integer otpnumber;
    @Column(name = "createtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;

    /**
     *
     */
    public Otp() {
    }

    /**
     *
     * @param mobilenumber
     */
    public Otp(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }
    
    
    /**
     *
     * @param mobilenumber
     * @param otpnumber
     */
    public Otp(String mobilenumber, int otpnumber) {
        this.mobilenumber = mobilenumber;
        this.otpnumber=otpnumber;
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
    public Integer getOtpnumber() {
        return otpnumber;
    }

    /**
     *
     * @param otpnumber
     */
    public void setOtpnumber(Integer otpnumber) {
        this.otpnumber = otpnumber;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mobilenumber != null ? mobilenumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Otp)) {
            return false;
        }
        Otp other = (Otp) object;
        if ((this.mobilenumber == null && other.mobilenumber != null) || (this.mobilenumber != null && !this.mobilenumber.equals(other.mobilenumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sg.edu.astar.taxi360.entity.Otp[ mobilenumber=" + mobilenumber + " ]";
    }
    
}
