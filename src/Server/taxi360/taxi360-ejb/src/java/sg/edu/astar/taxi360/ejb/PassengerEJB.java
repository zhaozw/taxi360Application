/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.ejb;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.PassengerFacade;
import sg.edu.astar.taxi360.bo.RideFacade;
import sg.edu.astar.taxi360.entity.Passenger;
import sg.edu.astar.taxi360.entity.Ride;

/**
 *
 * @author Thilak
 */
@Stateless
public class PassengerEJB {

    @EJB
    PassengerFacade passengerFacade;
    @EJB
    private RideFacade rideFacade;

    /**
     * This method takes the passenger id as input and returns the corresponding
     * passenger object if found, otherwise it returns null
     *
     * @param id - Passenger id
     * @return Passenger object or null
     */
    public Passenger getPassenger(long id) {
        return passengerFacade.find(id);
    }

    /**
     * This method logs in the input passenger for social media use cases and
     * updates the access key accordingly
     *
     * @param p Passenger to be logged in with access key
     * @return passenger id or 0
     */
    public long loginPassenger(Passenger p) {
        Passenger pass = passengerFacade.findByEmail(p);
        if (pass != null) {
            passengerFacade.updateAccessKey(pass);
            return pass.getId();
        } else {
            return (long) 0;
        }

    }

    /**
     * This method creates new passenger in the system.
     *
     * @param pass Passenger to be registered
     * @return passenger id or 0
     */
    public Long createPasenger(Passenger pass) {
        pass.setCreatetime(new Date());
        return passengerFacade.createPassenger(pass);
    }

    /**
     * This method updates the access key for the input passenger
     *
     * @param pass Passenger to be updated(Access Key)
     */
    public void updateAccessKey(Passenger pass) {
        passengerFacade.updateAccessKey(pass);
    }

    public void updateRating(Ride r) {
        Ride ride;
        Passenger passenger;
        ride = rideFacade.find(r.getId());
        if (ride != null) {
            ride.setPassengerRating(r.getPassengerRating());
            passenger = getPassenger(ride.getPassenger().getId());

            if ((passenger.getNumberofratings() == null) || (passenger.getNumberofratings() == 0)) {
                passenger.setNumberofratings(Long.valueOf(1));

                //gfchgcffg
                passenger.setRating(r.getPassengerRating());
                passengerFacade.edit(passenger);
                return;
            }

            passenger.setNumberofratings(passenger.getNumberofratings() + 1);
            passenger.setRating((passenger.getNumberofratings() * passenger.getRating() + r.getPassengerRating()) / (passenger.getNumberofratings() + 1));
            passengerFacade.edit(passenger); //To change body of generated methods, choose Tools | Templates.
            rideFacade.edit(ride);
        }
    }
}
