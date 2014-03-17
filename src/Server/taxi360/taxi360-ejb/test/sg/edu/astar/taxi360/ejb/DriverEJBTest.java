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
        request = new Request();
        passenger=new Passenger(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))));
        passenger.setCreatetime(new Date());
        passengerFacade.create(passenger);
        request.setPassenger(passenger);
        request.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4)));  
        
        driver=new Driver(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))));
        driver.setCreatetime(new Date());
        driver.setNumberofratings(new Long(1));
        driver.setRating(4.0);
        driverFacade.create(driver);
        availableDriver=new AvailableDriver();
        System.out.println("pass="+passenger);
        System.out.println("dri="+driver);
        ride=new Ride();
        ride.setCreatetime(new Date());
        ride.setPassenger(passenger);
        ride.setDriver(driver);
        ride.setPassengerRating(2.0);
        ride.setId(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))).toString());
        availableDriver.setDriver(driver);
        availableDriver.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4)));
        
        
    }
    
    @After
    public void tearDown() {
        requestFacade.remove(request);
        passengerFacade.remove(passenger);        
        rideFacade.remove(ride);
        availableDriverFacade.remove(availableDriver);
        driverFacade.remove(driver);
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
        
//        request = new Request();
//        request.setPassenger(passenger);
//        request.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4))); 
        if(requestFacade.find(request.getPassenger().getId())==null)
            requestFacade.create(request);
        if(availableDriverFacade.find(availableDriver.getDriver().getId())==null)
            availableDriverFacade.create(availableDriver);
        assertNotNull(requestFacade.find(request.getPassenger().getId()));
        assertNotNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        ride=driverEJB.startRide(ride);
        assertNull(requestFacade.find(request.getPassenger().getId()));
        assertNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        rideFacade.remove(ride);        
    }
    
    

    /**
     * Test of endRide method, of class DriverEJB.
     */
    @Test
    public void testEndRide() throws Exception {
        System.out.println("endRide");
        if(requestFacade.find(request.getPassenger().getId())==null)
            requestFacade.create(request);
        if(availableDriverFacade.find(availableDriver.getDriver().getId())==null)
            availableDriverFacade.create(availableDriver);
        assertNotNull(requestFacade.find(request.getPassenger().getId()));
        assertNotNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        ride=driverEJB.startRide(ride);
        assertNull(requestFacade.find(request.getPassenger().getId()));
        assertNull(availableDriverFacade.find(availableDriver.getDriver().getId()));
        driverEJB.endRide(ride);
        assertNotNull(availableDriverFacade.find(ride.getDriver().getId()));
        rideFacade.remove(ride);        
        
    }
    
    

    /**
     * Test of getDriver method, of class DriverEJB.
     */
    @Test
    public void testGetDriver() throws Exception {
        System.out.println("getDriver");
        Driver driver2=new Driver(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))));
        driver2.setCreatetime(new Date());
        assertNull(driverEJB.getDriver(driver2.getId()));        
        driverFacade.create(driver2);
        assertNotNull(driverEJB.getDriver(driver2.getId()));
        driverFacade.remove(driver2);
        
    }
    
    
    
    /**
     * Test of getAvailableDriver method, of class DriverEJB.
     */
    @Test
    public void testGetAvailableDriver() throws Exception {
        
        System.out.println("getAvailableDriver");
        assertNull(driverEJB.getAvailableDriver(driver.getId()));   
        driverEJB.changeStatus(availableDriver, true);
        assertNotNull(driverEJB.getAvailableDriver(driver.getId()));
        driverEJB.changeStatus(availableDriver, false);
        assertNull(driverEJB.getAvailableDriver(driver.getId()));   
        
    }
    
    
    /**
     * Test of updateDriverLocation method, of class DriverEJB.
     */
    @Test
    public void testUpdateDriverLocation() throws Exception {
        AvailableDriver ad2;
        System.out.println("updateDriverLocation");
        driverEJB.changeStatus(availableDriver, true);        
        ad2=driverEJB.getAvailableDriver(availableDriver.getDriver().getId());
        assertEquals(ad2.getLocation(),availableDriver.getLocation());   
        ad2.setLocation(new Location(1.25+(Math.random()*.1), 103.5+(Math.random()*.4)));
        driverEJB.updateDriverLocation(ad2);
        ad2=driverEJB.getAvailableDriver(availableDriver.getDriver().getId());
        assertNotSame(ad2.getLocation(),availableDriver.getLocation());           
    }
    
    
    /**
     * Test of createDriver method, of class DriverEJB.
     */
    @Test
    public void testCreateDriver() throws Exception {
        Driver driver2=new Driver(new Long((int) (10000000 + (Math.random() * (99999999 - 10000000)))));
        driver2.setCreatetime(new Date());
        Long id;
        id=driverEJB.createDriver(driver2);
        assertNotSame(0, id);
        assertNotNull(driverEJB.getDriver(id));
        driverFacade.remove(driver2);
    }
    
    

    /**
     * Test of findWithin method, of class DriverEJB.
     */
    @Test
    public void testFindWithin() throws Exception {
        System.out.println("findWithin");
        int count;
        count=driverEJB.findAvailableDriverWithin(new Location(1.301, 103.801), 100.01).size();
        driverEJB.changeStatus(availableDriver, true);
        assertEquals(count+1, driverEJB.findAvailableDriverWithin(new Location(1.301, 103.801), 100.01).size());
        driverEJB.changeStatus(availableDriver, false);
    }
    
    

    /**
     * Test of updateRating method, of class DriverEJB.
     */
    @Test
    public void testUpdateRating() throws Exception {
        System.out.println("updateRating");
        rideFacade.create(ride);
        Long nRatings=driver.getNumberofratings();
        Double rating=driver.getRating();
        Double aRating=ride.getPassengerRating();
        driverEJB.updateRating(ride);
        assertTrue((nRatings+1)== driverEJB.getDriver(ride.getDriver().getId()).getNumberofratings());
        assertTrue((aRating+nRatings*rating)/(nRatings+1)== driverEJB.getDriver(ride.getDriver().getId()).getRating());
        driver.setNumberofratings(new Long(1));
        driver.setRating(4.0);
    }
    
    

    
}
