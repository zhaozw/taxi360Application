/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.bo;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Request;

/**
 * This class interacts with Database and returns the list of requests that matches the criteria 
 * @author mido 
 * @author Neil Shah
 * @version 1.0
 */
@Stateless
public class RequestFacade extends AbstractFacade<Request> {
    @PersistenceContext(unitName = "taxi360-ejbPU")
    /**
     * Parameter for creating an EntityManger object
     */
    private EntityManager em;
    
    /**
     * Default Constructor which calls its superclass's(AbstrcatFaceade) default constructor 
     */
    public RequestFacade() {      
        super(Request.class);
    }
    
    /**
     * This method overrides the default EntityManger method 
     * @return EntityManager object
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * This method contains the details of interaction with Database and query which finds the requests from
     * database within given distance from a location
     * @param location
     * @param distance
     * @return  List of Requests which matches the criteria and is within certain distance from given point
     */
    public List<Request> findWithin(Location location, Double distance) {
        System.out.println("location="+location+", dis="+distance);
        String sql = "Select request.* from request where ST_DWithin(location, ST_GeographyFromText('SRID=4326;POINT( "+location.getLongitude()+" "+location.getLatitude()+")'),"+distance+")";
        System.out.println("sql stmt= "+sql);
        Query query = em.createNativeQuery(sql, Request.class);
        return query.getResultList();
    }
}
