package sg.edu.astar.ihpc.passenger.entity;

import java.util.Date;

public class OTP {
public String otpnumber;
public Date createtime;
private String mobilenumber;
public String getMobilenumber() {
	return mobilenumber;
}
public void setMobilenumber(String mobilenumber) {
	this.mobilenumber = mobilenumber;
}


public String getOtpnumber() {
	return otpnumber;
}
public void setOtpnumber(String otpnumber) {
	this.otpnumber = otpnumber;
}
public Date getCreatetime() {
	return createtime;
}
public void setCreatetime(Date createtime) {
	this.createtime = createtime;
}
public OTP(String otpnumber) {
	super();
	this.otpnumber = otpnumber;
	
}

public OTP(){}

}
