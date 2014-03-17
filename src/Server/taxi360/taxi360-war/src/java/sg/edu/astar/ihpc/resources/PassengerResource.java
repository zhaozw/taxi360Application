/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.ihpc.resources;

import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import sg.edu.astar.taxi360.ejb.PassengerEJB;
import sg.edu.astar.taxi360.entity.Passenger;

/**
 * REST Web Service
 *
 * @author Thilak
 */
@Path("passenger")
@RequestScoped
public class PassengerResource {

    @Context
    private UriInfo context;

    @EJB
    PassengerEJB passengerEJB;

    /**
     * Creates a new instance of PassengerResource
     */
    public PassengerResource() {
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Passenger getPassenger(@PathParam("id") Long id) {
        return passengerEJB.getPassenger(id);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Long registerPassenger(Passenger pass) {
        
        if("facebook".equalsIgnoreCase(pass.getRegistrationtype()))
            return passengerEJB.createPasenger(pass);
        
        String s=pass.getEmailid();
        int idx=s.indexOf('*');
        if(idx==-1 || idx==s.length()-1)
            return null;
        pass.setEmailid(s.substring(0, idx));
        pass.setPassword(s.substring(idx));
        return passengerEJB.createPasenger(pass);
    }
}
