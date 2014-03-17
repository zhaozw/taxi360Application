/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.ejb;


import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.RequestFacade;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Request;

/**
 * This EJB class interacts with the business object class RequestFacade and provides the necessary inputs to
 * that class
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 * @see RequestFacade.java
 */
@Stateless
public class RequestEJB {
    
    @EJB RequestFacade requestFacade;
    
    /**
     * This method would validate if there is an existing request for that passenger. If there is no
     * existing request for that passenger, it would create a new request.
     * @param request
     */
    public void create(Request request){
        request.setCreatetime(new Date());
        if(requestFacade.find(request.getPassenger().getId())==null)
            requestFacade.create(request);
        else
            requestFacade.edit(request);
    }
    
    /**
     * This method contains the implementation for canceling  the request. It would find the request based
     * on the request id and when it is found, it would remove the request.
     * @param id
     */
    public void cancel(Long id){
        if(requestFacade.find(id)!=null)
            requestFacade.remove(requestFacade.find(id));
    }
    
    /**
     * This method calls findAll method from requestFacade class which would find the list of all
     * existing requests in the database
     * @return List of all existing requests
     */
    public List<Request> findAll() {
        return requestFacade.findAll();
    }
        
    /**
     * This method contains would call the method in RequestFacade class which would in turn find the list of
     * requests which matches the distance criteria from that location.
     * @param location
     * @param distance
     * @return List of matching requests
     */
    public List<Request> findWithin(Location location, Double distance) {
        return requestFacade.findWithin(location, distance);
    }
    
    /**
     * This method calls find method from requestFacade class which would find the request based on request id
     * @param id
     * @return request object
     */
    public Request find(Long id){
        return requestFacade.find(id);
    }
    
}
