/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.ejb;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.AvailableDriverFacade;
import sg.edu.astar.taxi360.bo.DriverFacade;
import sg.edu.astar.taxi360.bo.RequestFacade;
import sg.edu.astar.taxi360.bo.RideFacade;
import sg.edu.astar.taxi360.entity.AvailableDriver;
import sg.edu.astar.taxi360.entity.Driver;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Request;
import sg.edu.astar.taxi360.entity.Ride;
import sg.edu.astar.taxi360.util.GCMMessenger;
import sg.edu.astar.taxi360.util.Resources;

/**
 *
 * @author Althaf
 */
@Stateless
public class DriverEJB {

    @EJB
    private DriverFacade driverFacade;
    @EJB
    private RequestFacade requestFacade;
    @EJB
    private AvailableDriverFacade availableDriverFacade;
    @EJB
    private RideFacade rideFacade;

    public void changeStatus(AvailableDriver availableDriver, boolean status) {
        if (status) {
            if (availableDriverFacade.find(availableDriver.getDriver().getId()) == null) {
                availableDriverFacade.create(availableDriver);
            } else {
                availableDriverFacade.edit(availableDriver);
            }
        } else if (availableDriverFacade.find(availableDriver.getDriver().getId()) != null) {
            availableDriverFacade.remove(availableDriver);
        }
    }

    public Ride startRide(Ride ride) throws IOException {
        ride.setCreatetime(new Date());
        Request r;
        AvailableDriver ad;
        if ((r = requestFacade.find(ride.getPassenger().getId())) != null) {
            ride.setPassengerStartLocation(r.getLocation());
            ride.setPassengerKey(r.getPassengerKey());
            requestFacade.remove(r);
        }
        if ((ad = availableDriverFacade.find(ride.getDriver().getId())) != null) {
            ride.setDriverStartLocation(ad.getLocation());
            availableDriverFacade.remove(ad);
        }

        if (ride.getId() == null) {
            rideFacade.create(ride);
        } else if (rideFacade.find(ride.getId()) != null) {
            rideFacade.edit(ride);
        }
        GCMMessenger.sendMessage(ride.getId(),Resources.get("gcm_ride_collapse_key") , ride.getPassengerKey());
        return ride;
    }

    public void endRide(Ride ride) throws IOException {
        AvailableDriver ad;
        if (rideFacade.find(ride.getId()) != null) {
            rideFacade.edit(ride);
            GCMMessenger.sendMessage(Resources.get("gcm_ride_message"),Resources.get("gcm_ride_collapse_key") , ride.getPassengerKey());
            ad=new AvailableDriver();
            ad.setDriver(ride.getDriver());
            changeStatus(ad,true);
        }
    }
    
    public Driver getDetails(Long id){
    return driverFacade.find(id);
    }
    
    public AvailableDriver getAvailableDriver(long id) {
        return availableDriverFacade.findById(id);
    }
    
    public void updateLocation(AvailableDriver avDriver) {
        availableDriverFacade.edit(avDriver);
    }
    public void ratings(Ride ride){
        Ride rd;
        Driver driver;
        rd=rideFacade.find(ride.getId());
        rd.setRating(ride.getRating());
        driver=rd.getDriver();
        
        if((driver.getNumberofratings()==null)||(driver.getNumberofratings()==0)){
            driver.setNumberofratings(Long.valueOf(1));
        }
        else{
            driver.setNumberofratings(driver.getNumberofratings()+1);
        }
        driver.setRating((driver.getNumberofratings()*driver.getRating()+ride.getRating())/driver.getNumberofratings()+1);
        
        
    }
    
    public List<AvailableDriver> findAvailableDriverWithin(Double lat, Double lng, Double radius) {
        return availableDriverFacade.findWithin(new Location(lat,lng), radius);
    }
    
    public long createDriver(Driver drv) {
        return driverFacade.createDriver(drv);
    }
}
