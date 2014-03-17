/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sg.edu.astar.taxi360.bo.PassengerFacade;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Passenger;
import sg.edu.astar.taxi360.entity.Request;

/**
 *
 * @author mido
 */
public class RequestEJBTest {
    
    public RequestEJBTest() {
    }
    private static EJBContainer container;
    private static RequestEJB requestEJB;
    private static PassengerFacade passengerFacade;
    private static Request request;
    private static Passenger passenger;
    @BeforeClass
    public static void setUpClass() throws NamingException {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        requestEJB = (RequestEJB) container.getContext().lookup("java:global/classes/RequestEJB");
        passengerFacade = (PassengerFacade) container.getContext().lookup("java:global/classes/PassengerFacade");
    }
    
    @AfterClass
    public static void tearDownClass() {
        container.close();        
    }
    
    @Before
    public void setUp() {
        request = new Request();
        passenger=new Passenger(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))));
        passenger.setCreatetime(new Date());
        passengerFacade.create(passenger);
        request.setPassenger(passenger);
        request.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4)));  
    }
    
    @After
    public void tearDown() {
        requestEJB.cancel(request.getPassenger().getId());
        passengerFacade.remove(passenger);
        request=null;
        passenger=null;
    }

    /**
     * Test of create method, of class RequestEJB.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        assertNull(requestEJB.find(request.getPassenger().getId()));
        requestEJB.create(request);
        assertNotNull(requestEJB.find(request.getPassenger().getId()));
        assertEquals(request,requestEJB.find(request.getPassenger().getId()));
    }

    /**
     * Test of cancel method, of class RequestEJB.
     */
    @Test
    public void testCancel() throws Exception {
        System.out.println("cancel");
        assertNull(requestEJB.find(request.getPassenger().getId()));
        requestEJB.create(request);
        assertNotNull(requestEJB.find(request.getPassenger().getId()));
        requestEJB.cancel(request.getPassenger().getId());
        assertNull(requestEJB.find(request.getPassenger().getId()));
    }


    /**
     * Test of findWithin method, of class RequestEJB.
     */
    @Test
    public void testFindWithin() throws Exception {
        System.out.println("findWithin");
        assertNotNull(requestEJB.findWithin(new Location(1.301, 103.801), 100.01));
    }

    

    /**
     * Test of checkRequestExists method, of class RequestEJB.
     */
    @Test
    public void testCheckRequestExists() throws Exception {
        System.out.println("checkRequestExists");
        assertFalse(requestEJB.checkRequestExists(request.getPassenger().getId()));
        requestEJB.create(request);
        assertTrue(requestEJB.checkRequestExists(request.getPassenger().getId()));
    }

    

    /**
     * Test of findAll method, of class RequestEJB.
     */
    @Test
    public void testFindAll() throws Exception {
        int count;
        System.out.println("findAll");
        assertFalse(requestEJB.checkRequestExists(request.getPassenger().getId()));
        count=requestEJB.findAll().size();
        requestEJB.create(request);
        assertEquals(requestEJB.findAll().size(),(count+1));
    }

    

    /**
     * Test of find method, of class RequestEJB.
     */
    @Test
    public void testFind() throws Exception {
        System.out.println("find");
        assertNull(requestEJB.find(request.getPassenger().getId()));
        requestEJB.create(request);
        assertNotNull(requestEJB.find(request.getPassenger().getId()));
    }

    
}
