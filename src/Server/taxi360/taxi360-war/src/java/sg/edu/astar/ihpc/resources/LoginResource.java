/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.ihpc.resources;

import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import sg.edu.astar.taxi360.ejb.CommonEJB;
import sg.edu.astar.taxi360.ejb.PassengerEJB;
import sg.edu.astar.taxi360.entity.Credential;
import sg.edu.astar.taxi360.entity.Driver;
import sg.edu.astar.taxi360.entity.Passenger;

/**
 * REST Web Service
 *
 * @author Thilak
 */
@Path("passenger/accesscontrol")
@RequestScoped
public class LoginResource {

    @Context
    private UriInfo context;
    
   @EJB PassengerEJB passEJB;

    @EJB
    private CommonEJB commonEJB;
    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {
    }

    /**
     * This method is called when a passenger tries to log-in via any social network credential.
     * @param pass The passenger to be logged in.
     * @return passenger id, if the passenger is already registered otherwise it returns 0. If 0 is
     * returned, the client must try to register the passenger. 
     */
    @Path("login")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Long loginViaSocialMedia(Passenger pass) {
        return passEJB.loginPassenger(pass);
    }
    
    /**
     * This method is called when the passenger log out of the system. This method de-activates the access key.
     * @param pass passenger to be logged out.
     * @return 
     */
    @Path("logout")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public boolean logout( Passenger pass ) {
        passEJB.updateAccessKey(pass);
        return true;
    }
    
        
    
        
    /**
     * This method is used to validate the driver.
     * @param driver  the driver being verified.
     * @return Driver value object.
     */
    @POST
    @Produces("application/json")
    @Path("driver")    
    public Driver validateDriver(Credential credential){
        return commonEJB.validateDriver(credential);
    }
    
    @POST 
    @Produces("application/json")
    @Path("passenger")
    public Passenger validatePassenger(Credential credential){
        return commonEJB.validatePassenger(credential);
    }

}
