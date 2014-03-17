package sg.edu.astar.ihpc.passenger.util;

import java.util.Date;

import sg.edu.astar.ihpc.passenger.LoginActivity;
import sg.edu.astar.ihpc.passenger.entity.Passenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
public class SessionManager {

	private SharedPreferences pref;
	private Editor editor;
	private static Context context = null;
	private static int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "taxi360pref"; // Sharedpref file
															// name
	private static final String IS_LOGIN = "IsLoggedIn"; // All Shared
															// Preferences Keys
	private static Passenger passenger;
	private static SessionManager instance = null;

	public static final String KEY_ID = "id";
	public static final String KEY_CREATETIME = "createtime";
	public static final String KEY_NAME = "name"; // User name (make variable
													// public to access from
													// outside)
	public static final String KEY_EMAIL = "emailid"; // Email address (make
														// variable public to
														// access from outside)
	public static final String KEY_ACCESSKEY = "accesskey";
	public static final String KEY_LASTLOGINTIME = "lastlogintime";
	public static final String KEY_MOBILENUMBER = "mobilenumber";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_REGISTRATIONTYPE = "registrationtype";

	// Constructor
	private SessionManager() {
	}

	@SuppressLint("CommitPrefEdits")
	public static synchronized SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
			passenger = null;
			instance.pref = context.getSharedPreferences(PREF_NAME,
					PRIVATE_MODE);
			instance.editor = instance.pref.edit();
			instance.readDetails();
		}
		return instance;
	}

	public static void setContext(Context c) {
		if (context == null)
			context = c;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(Passenger d) {
		passenger = d;
		editor.putBoolean(IS_LOGIN, true);// Storing login value as TRUE
		editor.putLong(KEY_ID, d.getId());
		if (d.getCreatetime() != null)
			editor.putLong(KEY_CREATETIME, d.getCreatetime().getTime());
		editor.putString(KEY_NAME, d.getName());
		editor.putString(KEY_EMAIL, d.getEmailid());
		editor.putString(KEY_ACCESSKEY, d.getAccesskey());
		editor.putLong(KEY_LASTLOGINTIME, d.getLastlogintime().getTime());
		editor.putString(KEY_MOBILENUMBER, d.getMobilenumber());
		editor.putString(KEY_USERNAME, d.getUsername());
		editor.putString(KEY_PASSWORD, d.getPassword());
		editor.putString(KEY_REGISTRATIONTYPE, d.getRegistrationtype());
		editor.commit(); // commit changes
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public boolean checkLogin() {
		if (!isLoggedIn()) {
			Intent i = new Intent(context, LoginActivity.class);// user is not
																// logged in
																// redirect him
																// to Login
																// Activity
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Closing all the
														// Activities
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add new Flag to start
														// new Activity
			context.startActivity(i);
			return false;
		}

		return true;
	}

	/**
	 * Get stored session data
	 * */
	public Passenger getUser() {
		if (passenger == null)
			readDetails();
		return passenger;
	}

	private void readDetails() {
		if (!isLoggedIn())
			return;

		passenger = new Passenger();
		passenger.setId(pref.getLong(KEY_ID, 0));
		passenger.setCreatetime(new Date(pref.getLong(KEY_CREATETIME, 0)));
		passenger
				.setLastlogintime(new Date(pref.getLong(KEY_LASTLOGINTIME, 0)));
		passenger.setName(pref.getString(KEY_NAME, null));
		passenger.setEmailid(pref.getString(KEY_EMAIL, null));
		passenger.setAccesskey(pref.getString(KEY_ACCESSKEY, null));
		passenger.setMobilenumber(pref.getString(KEY_MOBILENUMBER, null));
		passenger.setUsername(pref.getString(KEY_USERNAME, null));
		passenger.setPassword(pref.getString(KEY_PASSWORD, null));
		passenger.setRegistrationtype(pref
				.getString(KEY_REGISTRATIONTYPE, null));

	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		passenger = null;
		editor.clear();// Clearing all data from Shared Preferences
		editor.commit();

		Intent i = new Intent(context, LoginActivity.class);// After logout
															// redirect user to
															// Log in Activity
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// Closing all the Activities
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// Add new Flag to start new
													// Activity
		context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger p) {
		passenger = p;
	}

}