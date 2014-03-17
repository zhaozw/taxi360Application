/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.ejb;

import java.util.Date;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sg.edu.astar.taxi360.bo.AvailableDriverFacade;
import sg.edu.astar.taxi360.bo.DriverFacade;
import sg.edu.astar.taxi360.bo.PassengerFacade;
import sg.edu.astar.taxi360.bo.RequestFacade;
import sg.edu.astar.taxi360.bo.RideFacade;
import sg.edu.astar.taxi360.entity.AvailableDriver;
import sg.edu.astar.taxi360.entity.Driver;
import sg.edu.astar.taxi360.entity.Location;
import sg.edu.astar.taxi360.entity.Passenger;
import sg.edu.astar.taxi360.entity.Request;
import sg.edu.astar.taxi360.entity.Ride;

/**
 *
 * @author mido
 */
public class DriverEJBTest {
    
    public DriverEJBTest() {
    }
    
    private static EJBContainer container;
    private static DriverEJB driverEJB;
    private static RequestFacade requestFacade;
    private static PassengerFacade passengerFacade;
    private static RideFacade rideFacade;
    private static AvailableDriverFacade availableDriverFacade;
    private static DriverFacade driverFacade;
    private static Request request;
    private static Passenger passenger;
    private static Driver driver;
    private static Ride ride;
    private static AvailableDriver availableDriver;
    
    @BeforeClass
    public static void setUpClass() throws NamingException {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        driverEJB = (DriverEJB) container.getContext().lookup("java:global/classes/DriverEJB");
        requestFacade = (RequestFacade) container.getContext().lookup("java:global/classes/RequestFacade");
        passengerFacade = (PassengerFacade) container.getContext().lookup("java:global/classes/PassengerFacade");
        driverFacade = (DriverFacade) container.getContext().lookup("java:global/classes/DriverFacade");
        availableDriverFacade = (AvailableDriverFacade) container.getContext().lookup("java:global/classes/AvailableDriverFacade");
        rideFacade = (RideFacade) container.getContext().lookup("java:global/classes/RideFacade");
    }
    
    @AfterClass
    public static void tearDownClass() {
        container.close();        
    }
    
    @Before
    public void setUp() {
//        request = new Request();
//        passenger=new Passenger(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))));
//        passenger.setCreatetime(new Date());
//        passengerFacade.create(passenger);
//        request.setPassenger(passenger);
//        request.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4))); 
        passenger=passengerFacade.find(new Long(13));
        driver=driverFacade.find(new Long(1));
        availableDriver=new AvailableDriver();
        System.out.println("pass="+passenger);
        System.out.println("dri="+driver);
        ride=new Ride();
        ride.setCreatetime(new Date());
        ride.setPassenger(passenger);
        ride.setDriver(driver);
        availableDriver.setDriver(driver);
        availableDriver.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4)));
        
        
    }
    
    @After
    public void tearDown() {
        //requestFacade.remove(request);
        //passengerFacade.remove(passenger);
        //request=null;
        passenger=null;
        ride=null;
        availableDriver=null;
        driver=null;
    }

    /**
     * Test of changeStatus method, of class DriverEJB.
     */
    @Test
    public void testChangeStatus() throws Exception {
        System.out.println("changeStatus");
        assertNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        driverEJB.changeStatus(availableDriver, true);
        assertNotNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        driverEJB.changeStatus(availableDriver, true);
        assertNotNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        driverEJB.changeStatus(availableDriver, false);
        assertNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        driverEJB.changeStatus(availableDriver, false);
        assertNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
    }

    /**
     * Test of startRide method, of class DriverEJB.
     */
    @Test
    public void testStartRide() throws Exception {
        System.out.println("startRide");
        
        request = new Request();
        request.setPassenger(passenger);
        request.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4))); 
        if(requestFacade.find(request.getPassenger().getId())==null)
            requestFacade.create(request);
        if(availableDriverFacade.find(availableDriver.getDriver().getId())==null)
            availableDriverFacade.create(availableDriver);
        assertNotNull(requestFacade.find(request.getPassenger().getId()));
        assertNotNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        driverEJB.startRide(ride);
        assertNull(requestFacade.find(request.getPassenger().getId()));
        assertNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
    }
    
}
