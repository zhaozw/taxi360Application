/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.taxi360.ejb;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sg.edu.astar.taxi360.entity.Passenger;

/**
 *
 * @author Thilak
 */
public class PassengerEJBTest {
    private static EJBContainer container;
    private static PassengerEJB passEJB;
    private static Passenger testPass;
    
    public PassengerEJBTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws NamingException {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        passEJB = (PassengerEJB)container.getContext().lookup("java:global/classes/PassengerEJB");
        testPass = new Passenger();
        testPass.setUsername("Hemanth Nair");
        testPass.setEmailid("hemanth_18_1988@yahoo.co.in");
        testPass.setRegistrationtype("facebook");
        testPass.setAccesskey("accesskey");
    }
    
    @AfterClass
    public static void tearDownClass() {
        container.close();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPassenger method, of class PassengerEJB.
     */
    @Test
    public void testGetPassenger() throws Exception {
        System.out.println("getPassenger");
        long id = 1352;
       // EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        //PassengerEJB instance = (PassengerEJB)container.getContext().lookup("java:global/classes/PassengerEJB");
        //Passenger expResult = new Passenger();      
        Passenger result = passEJB.getPassenger(id);
        assertEquals("hemanth_18_1988@yahoo.co.in", result.getEmailid());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of loginPassenger method, of class PassengerEJB.
     */
    @Test
    public void testLoginPassenger() throws Exception {
        System.out.println("loginPassenger");
        //EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        //PassengerEJB instance = (PassengerEJB)container.getContext().lookup("java:global/classes/PassengerEJB");
        long expResult = 1352L;
        long result = passEJB.loginPassenger(testPass);
        assertEquals(expResult, result);
        //container.close();
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of createPasenger method, of class PassengerEJB.
     */
    @Test
    public void testCreatePasenger() throws Exception {
        System.out.println("createPasenger");
        //Passenger pass = null;
        //EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        //PassengerEJB instance = (PassengerEJB)container.getContext().lookup("java:global/classes/PassengerEJB");
       // long expResult = 0L;
        testPass.setRegistrationtype("google");
        long result = passEJB.createPasenger(testPass);
        Passenger p = passEJB.getPassenger(result);
        assertEquals(p.getEmailid(), "hemanth_18_1988@yahoo.co.in");
        //container.close();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}