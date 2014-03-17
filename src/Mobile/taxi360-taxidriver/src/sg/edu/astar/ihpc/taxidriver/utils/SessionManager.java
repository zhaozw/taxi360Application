package sg.edu.astar.ihpc.taxidriver.utils;
import java.util.Date;

import sg.edu.astar.ihpc.taxidriver.activity.LoginActivity;
import sg.edu.astar.ihpc.taxidriver.entity.Driver;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

 
@SuppressLint("CommitPrefEdits") 
public class SessionManager {
	
    private SharedPreferences pref;   
    private Editor editor;     
    private static Context context=null;
    private static int PRIVATE_MODE = 0;         
    private static final String PREF_NAME = "taxi360pref";    	 // Sharedpref file name
    private static final String IS_LOGIN = "IsLoggedIn";        // All Shared Preferences Keys  
    private static Driver driver;    
	private static SessionManager instance=null;


    public static final String KEY_ID = "id";     			
    public static final String KEY_CREATETIME = "createtime";				   
    public static final String KEY_NAME = "name";     			// User name (make variable public to access from outside)
    public static final String KEY_EMAIL = "emailid";				// Email address (make variable public to access from outside)
    public static final String KEY_ACCESSKEY = "accesskey";     			
    public static final String KEY_LASTLOGINTIME = "lastlogintime";		
    public static final String KEY_MOBILENUMBER="mobilenumber";
    public static final String KEY_USERNAME = "username";				
    public static final String KEY_PASSWORD = "password";				
    public static final String KEY_LICENSEID = "licenseid";		
    public static final String KEY_NUMBER_RATING = "numberofratings";				
    public static final String KEY_RATING = "rating";		
    
    // Constructor
    private  SessionManager() {     }
	
	@SuppressLint("CommitPrefEdits") 
	public static synchronized SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager ();
			driver=null;
			instance.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			instance.editor = instance.pref.edit();
			instance.readDetails();
		}
		return instance;
	}
	
	public static void setContext(Context c){
		if(context==null)
			context=c;
	}	
	
	public Context getContext(){
		return context;
	}
			
     
    /**
     * Create login session
     * */
    public void createLoginSession(Driver d){
        driver=d;
        editor.putBoolean(IS_LOGIN, true);// Storing login value as TRUE     
        editor.putLong(KEY_ID, d.getId()); 
        if(d.getCreatetime()!=null)
        	editor.putLong(KEY_CREATETIME, d.getCreatetime().getTime());
        editor.putString(KEY_NAME, d.getName());
        editor.putString(KEY_EMAIL, d.getEmailid());
        editor.putString(KEY_ACCESSKEY, d.getAccesskey());
        editor.putLong(KEY_LASTLOGINTIME, d.getLastlogintime().getTime());
        editor.putString(KEY_MOBILENUMBER, d.getMobilenumber());
        editor.putString(KEY_USERNAME, d.getUsername());
        editor.putString(KEY_PASSWORD, d.getPassword());
        editor.putString(KEY_LICENSEID, d.getLicenseid());
        if(d.getNumberofratings()!=null)
        	editor.putLong(KEY_NUMBER_RATING, d.getNumberofratings());
        if(d.getRating()!=null)
        	editor.putFloat(KEY_RATING,((Double)d.getRating()).floatValue());
        editor.commit(); // commit changes
    }   
     
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public boolean checkLogin(){
        if(!isLoggedIn()){            
            Intent i = new Intent(context, LoginActivity.class);// user is not logged in redirect him to Login Activity
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	// Closing all the Activities                         
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add new Flag to start new Activity            
            context.startActivity(i);
            return false;
        }         
        
        return true;
    }      
     
    /**
     * Get stored session data
     * */
    public Driver getUser(){
    	if(driver==null)
    		readDetails();
        return driver;
    }
    
    private void readDetails(){
    	if(!isLoggedIn())	
    		return;
    	
        driver=new Driver();
        driver.setId(pref.getLong(KEY_ID, 0));
        driver.setCreatetime(new Date(pref.getLong(KEY_CREATETIME, 0)));
        driver.setLastlogintime(new Date(pref.getLong(KEY_LASTLOGINTIME, 0)));
        driver.setName(pref.getString(KEY_NAME, null));
        driver.setEmailid(pref.getString(KEY_EMAIL, null));
        driver.setAccesskey(pref.getString(KEY_ACCESSKEY, null));
        driver.setMobilenumber(pref.getString(KEY_MOBILENUMBER, null));
        driver.setUsername(pref.getString(KEY_USERNAME, null));
        driver.setPassword(pref.getString(KEY_PASSWORD, null));
        driver.setLicenseid(pref.getString(KEY_LICENSEID, null));
        driver.setNumberofratings(pref.getLong(KEY_NUMBER_RATING, 0));
        driver.setRating(Double.valueOf(pref.getFloat(KEY_RATING, 0)));
    	
    }
     
    /**
     * Clear session details
     * */
    public void logoutUser(){
        driver=null;
        editor.clear();// Clearing all data from Shared Preferences
        editor.commit();
        
        Intent i = new Intent(context, LoginActivity.class);// After logout redirect user to Log in Activity        
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// Closing all the Activities                 
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add new Flag to start new Activity
        context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    
    public Driver getDriver(){
    	return getUser();
    }
    
    public void setDriver(Driver d){
    	driver=d;
    }
}