package sg.edu.astar.ihpc.taxidriver.entity;

import java.io.Serializable;
import java.util.Date;



public class Passenger implements Serializable  {
   
    
	private Long id;
    
    private Date createtime;
    
    private String name;
    
    private String emailid;
   
    private String accesskey;
    
    
    private Date lastlogintime;
    
    private Integer mobilenumber;
    
    private String username;
   
    private String password;
    private Long numberofratings;
    
    private Double rating;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public Integer getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(Integer mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegistrationtype() {
		return registrationtype;
	}

	public void setRegistrationtype(String registrationtype) {
		this.registrationtype = registrationtype;
	}

	public Long getNumberofratings() {
		return numberofratings;
	}

	public void setNumberofratings(Long numberofratings) {
		this.numberofratings = numberofratings;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	private String registrationtype;



}
