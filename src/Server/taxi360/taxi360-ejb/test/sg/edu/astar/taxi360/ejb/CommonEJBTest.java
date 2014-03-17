/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import sg.edu.astar.taxi360.bo.OtpFacade;
import sg.edu.astar.taxi360.entity.Credential;
import sg.edu.astar.taxi360.entity.Driver;
import sg.edu.astar.taxi360.entity.Otp;
import sg.edu.astar.taxi360.entity.Passenger;

/**
 *
 * @author mido
 */
public class CommonEJBTest {

    private static EJBContainer container;
    private static CommonEJB commonEJB;
    private static OtpFacade otpFacade;

    public CommonEJBTest() {
    }

    @BeforeClass
    public static void setUpClass() throws NamingException {
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        commonEJB = (CommonEJB) container.getContext().lookup("java:global/classes/CommonEJB");
        otpFacade = (OtpFacade) container.getContext().lookup("java:global/classes/OtpFacade");
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
     * Test of generateOTP method, of class CommonEJB.
     */
    //@Test
    public void testGenerateOTP() throws Exception {
        System.out.println("generateOTP");
        int num = (int) (100000 + (Math.random() * (999999 - 100000)));
        Otp otp = new Otp(((Integer) num).toString(), 123456);
        assertNull(otpFacade.find(otp.getMobilenumber()));
        commonEJB.generateOTP(otp);
        assertNotNull(otpFacade.find(otp.getMobilenumber()));
        assertEquals(otpFacade.find(otp.getMobilenumber()).getOtpnumber(), otp.getOtpnumber());
        otpFacade.remove(otp);
    }
    
    @Test
    public void testValidateOTP() {
        Otp otp = new Otp();
        otp.setMobilenumber("88669067");
        otp.setOtpnumber(1234);
        boolean res = commonEJB.validateOTP(otp);
        assert(res);
    }
    
    @Test
    public void testValidateDriver() {
        Long driverid = 3L;
        Credential testCred = new Credential();
        testCred.setPassword("aaaa");
        testCred.setUsername("aaaa@aaa.aaa");
        Driver res = commonEJB.validateDriver(testCred);
        assert(res.getId() == driverid );
    }
    
    @Test
    public void testValidatePassenger() {
        Long passid = 2452L;
        Credential testCred = new Credential();
        testCred.setPassword("asdasdasd");
        testCred.setUsername("aaaa@123");
        Passenger res = commonEJB.validatePassenger(testCred);
        assert(res.getId() == passid );
    }

}
