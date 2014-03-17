/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sg.edu.astar.taxi360.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import sg.edu.astar.taxi360.bo.OtpFacade;
import sg.edu.astar.taxi360.entity.Otp;

/**
 *
 * @author mido
 */
@Stateless
public class CommonEJB {
    @EJB OtpFacade otpFacade;
    
    public void generateOTP(Otp otp){
    if(otpFacade.find(otp.getMobilenumber())==null)
        otpFacade.create(otp);
    else
        otpFacade.edit(otp);
    }
    
}
