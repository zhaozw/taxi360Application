/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.ejb;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.DriverFacade;
import sg.edu.astar.taxi360.bo.OtpFacade;
import sg.edu.astar.taxi360.bo.PassengerFacade;
import sg.edu.astar.taxi360.entity.Credential;
import sg.edu.astar.taxi360.entity.Driver;
import sg.edu.astar.taxi360.entity.Otp;
import sg.edu.astar.taxi360.entity.Passenger;
import sg.edu.astar.taxi360.util.Resources;

/**
 * This EJB class interacts with the business object classes for functionalities
 * common between driver and customer.
 *
 * @author mido
 * @author Neil Shah
 * @version 1.0
 * @see OtpFacade.java
 */
@Stateless
public class CommonEJB {

    @EJB
    OtpFacade otpFacade;
    @EJB
    DriverFacade driverFacade;
    @EJB
    PassengerFacade passengerFacade;
    /**
     * This method is used to add/update the Otp record in database.
     *
     * @param otp the Otp object, that needed to be added/updated.
     */
    public void generateOTP(Otp otp) {
        if (otpFacade.find(otp.getMobilenumber()) == null) {
            otpFacade.create(otp);
        } else {
            otpFacade.edit(otp);
        }
    }

    /**
     * This method is used to verify the mobile number of the user, we verify
     * the One Time password..
     *
     * @param otp the OTP which is being verified.
     * @return true if OTP matches with database, else returns false.
     */
    public boolean validateOTP(Otp otp) {
        if (otpFacade.find(otp.getMobilenumber()) == null) {
            return false;
        }
        return (otpFacade.find(otp.getMobilenumber()).getOtpnumber().equals(otp.getOtpnumber()));
    }
    
    /**
     * This method is used to validate the input credentials of a driver
     * @param credential Credential of a driver
     * @return Authenticated driver or null
     */

    public Driver validateDriver(Credential credential) {
        Driver driver = driverFacade.validatePassword(credential.getUsername(), credential.getPassword());
        if (driver != null && driver.getAccesskey() == null) {
            driver.setAccesskey(Resources.getAccessKey());
        }
        driver.setLastlogintime(new Date());
        driverFacade.edit(driver);
        return driver;
    }

    /**
     * This method is used to validate the input credentials of  a passenger 
     * @param credential Credential of a Passenger
     * @return Authenticated Passenger or null
     */
    public Passenger validatePassenger(Credential credential) {
        Passenger passenger = passengerFacade.validatePassword(credential.getUsername(),credential.getPassword());
        if (passenger != null && passenger.getAccesskey() == null) {
            passenger.setAccesskey(Resources.getAccessKey());
        }
        passenger.setLastlogintime(new Date());
        passengerFacade.edit(passenger);
        return passenger;
    }

}
