/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.ihpc.resources;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.QueryParam;
import sg.edu.astar.taxi360.ejb.DriverEJB;
import sg.edu.astar.taxi360.entity.AvailableDriver;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Request;

/**
 * REST Web Service
 *
 * @author Thilak
 */
@Path("availabledriver")
@RequestScoped
public class AvailableDriverResource {

    @Context
    private UriInfo context;
    @EJB
    private DriverEJB driverEJB;

    /**
     * Creates a new instance of AvailableDriverResource
     */
    public AvailableDriverResource() {
    }

    /**
     * Retrieves representation of an instance of sg.edu.astar.ihpc.resources.AvailableDriverResource
     * @return an instance of java.lang.String
     */
    
    @GET
    @Produces({"application/xml", "application/json"})
    public List<AvailableDriver> findWithin(@QueryParam("lat") Double lat, @QueryParam("log") Double log, @QueryParam("distance") Double radius) {
        return driverEJB.findAvailableDriverWithin(new Location(lat,log), radius);
    }
    
    
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public AvailableDriver get(@PathParam("id") long id) {
        return driverEJB.getAvailableDriver(id);
    }

    /**
     * PUT method for updating or creating an instance of AvailableDriverResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public boolean put(AvailableDriver avDriver) {
        driverEJB.updateDriverLocation(avDriver);
        return true;
    }
}
