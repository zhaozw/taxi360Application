/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import sg.edu.astar.taxi360.ejb.RequestEJB;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Request;

/**
 * REST Web Service
 *
 * @author Varun
 */
@Path("request")
@RequestScoped
public class RequestResource {

    @Context
    private UriInfo context;
    @EJB
    RequestEJB requestEJB;

    /**
     *
     */
    public RequestResource() {
    }

    /**
     * This method is used to create a booking request for taxi.
     * @param request  the request object that has to be added to the database.
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public void create(Request request) {
        requestEJB.create(request);
    }

    /**
     * This method is used to cancel a booking request.
     * @param id  the id of the passenger who is canceling his request.
     */
    @DELETE
    @Path("{id}")
    public void cancel(@PathParam("id") Long id) {
        requestEJB.cancel(id);
    }

    /**
     * This method is used to retrieve all the active requests for taxi.
     * @return  List of Request objects(, usually used for display in the map of Driver Screen).
     */
    @GET
    @Path("all")
    @Produces({"application/json"})
    public List<Request> findAll() {
        return requestEJB.findAll();
    }

    /**
     * This method is used to retrieve all the active requests for taxi within a certain range of requester.
     * @param lat  Latitude of the requester.
     * @param log  Longitude of the requester.
     * @param distance  Radius limit of circle within which requester wants to see active requests.
     * @return  List of Request objects(, usually used for display in the map of Driver Screen).
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Request> findWithin(@QueryParam("lat") Double lat, @QueryParam("log") Double log, @QueryParam("distance") Double distance) {
        return requestEJB.findWithin(new Location(lat, log), distance);
    }
    
    @GET
    @Path("range")
    public int[] findRequestsPerDirection(@QueryParam("lat") Double lat, @QueryParam("log") Double log, 
    @QueryParam("lower") Double lower, @QueryParam("higher") Double higher) {
        return requestEJB.findRequestsPerQuarter(new Location(lat, log), lower, higher);
    }
    
    /**
     * This method is used to check if booking request exists.
     * @param id  the id of the passenger who's request we are checking.
     * @return whether the request already exists.
     */
    @GET
    @Path("{id}")
    public boolean checkRequestExists(@PathParam("id") Long id) {
        return requestEJB.checkRequestExists(id);
    }
    

}
