/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.astar.ihpc.resources;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Random;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import sg.edu.astar.taxi360.ejb.CommonEJB;
import sg.edu.astar.taxi360.entity.Driver;
import sg.edu.astar.taxi360.entity.Otp;

/**
 *
 * @author mido
 */
@Path("common")
@RequestScoped
public class CommonResource {

    @Context
    private UriInfo context;

    @EJB
    private CommonEJB commonEJB;
    private final Random RANDOM;
    
    public CommonResource(){
        RANDOM=new Random();
    }

    /**
     * This method is used to generate the mobile number of the user, we generate One Time password.
     * @param otp  Mobile number which is being verified.
     */
    @POST
    @Produces("application/json")
    @Path("generateOTP")
    public void generateOTP( Otp otp) {
        otp.setOtpnumber(RANDOM.nextInt(900000) + 100000);
        otp.setCreatetime(new Date());
        otp.setOtpnumber(1234); // Remove THIS---------------------------------------------------------------------------------
        sendSMS(otp);    // ---> for Sending SMS
        commonEJB.generateOTP(otp);
    }
    
    /**
     * This method is used to verify the mobile number of the user, we verify the One Time password..
     * @param otp  the OTP which is being verified.
     * @return true if OTP matches with database, else returns false.
     */
    @POST
    @Produces("application/json")
    @Path("validateOTP")    
    public boolean validateOTP(Otp otp){
        return commonEJB.validateOTP(otp);
    }

    /**
     * This method is used to send the generated OTP as message to the mobile number.
     * @param otp  the OTP object that contains both the mobile number and the OTP value.
     */
    public void sendSMS(Otp otp) {
        String surl = "http://gateway.onewaysms.sg:10002/api.aspx?apiusername=APIGIB2XXXLMK&apipassword=APIGIB2XXXLMKGIB2X&mobileno=" + otp.getMobilenumber() + "&senderid=taxi360&languagetype=1&message=" + URLEncoder.encode("Your otp is " + otp.getOtpnumber());
        HttpURLConnection conn = null;
        URL url;
        try {
            url = new URL(surl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");
            conn.connect();
            //int iResponseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
