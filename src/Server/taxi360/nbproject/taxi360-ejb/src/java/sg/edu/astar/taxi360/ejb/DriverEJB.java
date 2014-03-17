/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.ejb;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.AvailableDriverFacade;
import sg.edu.astar.taxi360.bo.DriverFacade;
import sg.edu.astar.taxi360.bo.RequestFacade;
import sg.edu.astar.taxi360.bo.RideFacade;
import sg.edu.astar.taxi360.entity.AvailableDriver;
import sg.edu.astar.taxi360.entity.Request;
import sg.edu.astar.taxi360.entity.Ride;

/**
 *
 * @author mido
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
    
    public void changeStatus(AvailableDriver availableDriver,boolean status){
        if(status){
            if(availableDriverFacade.find(availableDriver.getDriver().getId())==null)
                availableDriverFacade.create(availableDriver);
            else
                availableDriverFacade.edit(availableDriver);             
        }
        else            
            if(availableDriverFacade.find(availableDriver.getDriver().getId())!=null)
                availableDriverFacade.remove(availableDriver);                
    }
    
    public void startRide(Ride ride){
        ride.setCreatetime(new Date());
        Request r;
        AvailableDriver ad;
        if((r= requestFacade.find(ride.getPassenger().getId()))!=null) {
            ride.setPassengerStartLocation(r.getLocation());
            requestFacade.remove(r);
        }
        if((ad=availableDriverFacade.find(ride.getDriver().getId()))!=null){
            ride.setDriverStartLocation(ad.getLocation());
            availableDriverFacade.remove(ad);
        }

        if(ride.getId()==null)
            rideFacade.create(ride);
        else if(rideFacade.find(ride.getId())!=null)
            rideFacade.edit(ride);
    }
    
}
