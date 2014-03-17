package sg.edu.astar.ihpc.passenger;

/**
 The Screen allows the user to register the mobile number for the application
 **/

/** Author :Hemanth **/

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.android.gcm.GCMRegistrar;

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.OTP;
import sg.edu.astar.ihpc.passenger.entity.Passenger;
import sg.edu.astar.ihpc.passenger.util.CheckConnectivity;
import sg.edu.astar.ihpc.passenger.util.Server;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterPhoneActivity extends Activity {
	private String phonenumber;
	private Button button;
	private Button button2;
	EditText OTP;
	EditText number;
	Context context;
	Passenger passenger;
	TextView error;
	private Date otpdate;
	CheckConnectivity connectivity;
	private static final String PROJECT_ID = "282452700157";

	// This tag is used in Log.x() calls
	private static final String TAG = "MainActivity";

	// This string will hold the lengthy registration id that comes
	// from GCMRegistrar.register()
	private String regId = "";

	// These strings are hopefully self-explanatory
	private String registrationStatus = "Not yet registered";
	private String broadcastMessage = "No broadcast message";

	// This intent filter will be set to filter on the string
	// "GCM_RECEIVED_ACTION"
	IntentFilter gcmFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_phone);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

		}
		context = this;

		setContentView(R.layout.activity_register_phone);
		passenger = (Passenger) (getIntent().getSerializableExtra("Title"));
		connectivity = new CheckConnectivity();
		OTP = (EditText) findViewById(R.id.otp);
		OTP.setEnabled(false);
		button2 = (Button) findViewById(R.id.validate);
		button2.setEnabled(false);
		error = (TextView) findViewById(R.id.error);
		error.setEnabled(false);

		mobileNumberEntered();
		number = (EditText) findViewById(R.id.number);
	}

	@Override
	public void onBackPressed() {

	}

	/**
	 * The button allows the user to enter the mobile number
	 **/
	private void mobileNumberEntered() {

		button = (Button) findViewById(R.id.register);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!CheckConnectivity.isNetworkAvailable(context)
						&& (!connectivity.isConnectedToServer())) {
					connectivity.alertshow(context);
				} else {
					error.setVisibility(View.GONE);
					button.setEnabled(false);
					number.setEnabled(false);
					phonenumber = number.getText().toString();
					if (validate(phonenumber)) {

						sendOTP(phonenumber);
						button2.setEnabled(true);
						OTP.setEnabled(true);
						button2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (!CheckConnectivity.isNetworkAvailable(context)
										&& (!connectivity.isConnectedToServer())) {
									connectivity.alertshow(context);
								} else {
									error.setVisibility(View.GONE);

									Date date = new Date();
									if ((date.getTime() - otpdate.getTime()) > 300000) {
										error.setVisibility(View.VISIBLE);
										error.setEnabled(true);
										error.setText("Please resend as OTP has expired");
										button.setEnabled(true);
										number.setEnabled(true);
										mobileNumberEntered();
									} else {
										if (validateotp(OTP.getText()
												.toString())) {
											Registerpassenger();
											Intent intent = new Intent(context,
													MainActivity.class);
											intent.putExtra("Title", passenger);
											startActivity(intent);
										} else {
											error.setVisibility(View.VISIBLE);
											error.setEnabled(true);
											error.setText("Invalid OTP.Please retry");
										}

									}

								}

							}
						});
					} else {
						error.setVisibility(View.VISIBLE);
						error.setEnabled(true);
						error.setText("Invalid Phone number");
						button.setEnabled(true);
						number.setEnabled(true);

					}
				}

			}
		});

	}

	/**
	 * The button allows the user to validate the OTP
	 **/

	private boolean validate(String phonenumber) {

		Pattern pattern = Pattern.compile("\\d{8}");
		Matcher matcher = pattern.matcher(phonenumber);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * The fuction allows the send the request to server
	 **/
	protected void send(String phonenumber) {

		String URL = "http://137.132.247.133:8080/taxi360-war/api/common/generateOTP";
		OTP otp = new OTP();
		otp.setMobilenumber(phonenumber);
		Server.getInstance().connect("POST", URL, otp);
		otpdate = new Date();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_phone, menu);
		return true;
	}

	/**
	 * The fuction allows the otp to be validated at server side
	 **/
	private boolean validateotp(String string) {
		Date date = new Date();
		OTP requestotp=new OTP(string);
		requestotp.setMobilenumber(number.getText().toString());
		requestotp.setCreatetime(date);
		String url="http://137.132.247.133:8080/taxi360-war/api/common/validateOTP";
		return Server.getInstance().connect("POST", url,requestotp).getStatus();
	}

	private void Registerpassenger() {passenger.setMobilenumber(number.getText().toString());
	try {
		// Check that the device supports GCM (should be in a try / catch)
		GCMRegistrar.checkDevice(this);

		// Check the manifest to be sure this app has all the required
		// permissions.
		GCMRegistrar.checkManifest(this);

		// Get the existing registration id, if it exists.
		regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {

			// register this device for this project
			GCMRegistrar.register(this, PROJECT_ID);
			while(regId.equals("")){
			regId = GCMRegistrar.getRegistrationId(this);
			
			Log.d("insidewhile","regid"+regId);
			}

			// This is actually a dummy function. At this point, one
			// would send the registration id, and other identifying
			// information to your server, which should save the id
			// for use when broadcasting messages.
			

		} else {

			registrationStatus = "Already registered";

		}


	} catch (Exception e) {

		e.printStackTrace();
		registrationStatus = e.getMessage();

	}

	Log.d("RegID", regId);
passenger.setAccesskey(regId);
	String url="http://137.132.247.133:8080/taxi360-war/api/passenger";
    String responses=null;
    
    	passenger.setId(Long.parseLong(Server.getInstance().connect("POST", url, passenger).getResponse()));
	
           
      

	}

	private void sendOTP(String phonenumber) {

		send(phonenumber);

	}
}
