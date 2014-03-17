/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.PassengerFacade;
import sg.edu.astar.taxi360.entity.Passenger;

/**
 *
 * @author Thilak
 */
@Stateless
public class PassengerEJB {
    @EJB PassengerFacade passFac;
   
    /**
     *
     * @param id
     * @return
     */
    public Passenger getPassenger(long id) {
        return passFac.find(id);
    }
    
    /**
     *
     * @param p
     * @return
     */
    public long loginPassenger( Passenger p ) {
        Passenger pass = passFac.findByEmail(p);
        if ( pass != null ) {
            updateAccessKey(pass);
            return pass.getId();
        }
        else
            return (long)0;
            
    }
    
    /**
     *
     * @param pass
     * @return
     */
    public long createPasenger( Passenger pass ) {
        return passFac.createPassenger(pass);
    }
    
    /**
     *
     * @param p
     */
    public void updateAccessKey( Passenger p ) {
        passFac.updateAccessKey(p);
    }
}
