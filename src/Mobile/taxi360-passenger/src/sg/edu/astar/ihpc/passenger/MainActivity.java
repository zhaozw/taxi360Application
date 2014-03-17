package sg.edu.astar.ihpc.passenger;

/** Author :Hemanth **/
/**
 * this is the main screen for the passenger once the user logs in . This screen  allows to request a taxi and cancel the taxi request
 */
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.Location;
import sg.edu.astar.ihpc.passenger.entity.Passenger;
import sg.edu.astar.ihpc.passenger.entity.Request;
import sg.edu.astar.ihpc.passenger.util.CheckConnectivity;
import sg.edu.astar.ihpc.passenger.util.GPSTracker;
import sg.edu.astar.ihpc.passenger.util.Response;
import sg.edu.astar.ihpc.passenger.util.Server;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Session;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
	TextView passengerAddress;
	Button button;
	EditText name;
	Passenger passenger;
	Button logout;
	Context context;
	AlertDialog.Builder alertDialogBuilder;
	CheckConnectivity connectivity;
	Handler handler = new Handler();
	Runnable refresh;
	// This is the project id generated from the Google console when
	// you defined a Google APIs project.
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

	/**
	 * This is called whenever it recieves a notification from the google server
	 */
	public void createNotification(Context context, String payload) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon,
				"Message received", System.currentTimeMillis());
		// hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent intent = new Intent(context, RideActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, "Message", payload,
				pendingIntent);
		notificationManager.notify(0, notification);

	}

	private BroadcastReceiver gcmReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(final Context context, Intent intent) {

			broadcastMessage = intent.getExtras().getString("gcm");

			if (broadcastMessage != null) {
				// display our received message
				if (broadcastMessage.equals("Ride Finished")) {
					createNotification(context, broadcastMessage);
					AlertDialog alertDialog1 = new AlertDialog.Builder(context)
							.create();

					// Setting Dialog Title
					alertDialog1.setTitle("Alert");

					// Setting Dialog Message
					alertDialog1.setMessage(broadcastMessage);

					// Setting OK Button
					alertDialog1.setButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									button.setText("Need Taxi");
									button.setEnabled(true);
									dialog.cancel();

								}
							});

					// Showing Alert Message
					alertDialog1.show();
				} else {
					createNotification(context, "Ride Started");

					AlertDialog alertDialog1 = new AlertDialog.Builder(context)
							.create();

					// Setting Dialog Title
					alertDialog1.setTitle("Alert");

					// Setting Dialog Message
					alertDialog1.setMessage("Driver Accepted the request");

					// Setting OK Button
					alertDialog1.setButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(context,
											RideActivity.class);
									intent.putExtra("ride", broadcastMessage);
									startActivity(intent);

									button.setEnabled(false);
									dialog.cancel();
								}
							});

					// Showing Alert Message
					alertDialog1.show();
				}
			}
		}
	};

	Geocoder geocoder;
	List<Address> addresses;
	Request reqest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		 reqest= new Request();
		geocoder = new Geocoder(this.getBaseContext());
		gcmFilter = new IntentFilter();
		gcmFilter.addAction("GCM_RECEIVED_ACTION");
		registerClient();
		setContentView(R.layout.activity_main);
		button = (Button) findViewById(R.id.button1);
		name = (EditText) findViewById(R.id.name);
		passenger = (Passenger) (getIntent().getSerializableExtra("Title"));
		Log.d("passid", passenger.toString());
		connectivity = new CheckConnectivity();
		checkrequest();
		context = this;
		name.setText(passenger.getName().toString());

		passengerAddress = (TextView) findViewById(R.id.address);
		if (!passenger.isRelocated()) {
			refresh = new Runnable() {
				public void run() {
					GPSTracker gps = new GPSTracker(MainActivity.this);
					double lat = 0.0;
					double lon = 0.0;
					// check if GPS enabled
					if (gps.canGetLocation()) {

						lat = gps.getLatitude();
						lon = gps.getLongitude();
						// url =
						// "http://137.132.247.133:8080/taxi360-war/webresources/generic?lat="+lat+"&lng="+lon+"&radius=1000000";
						// \n is for new line

					} else {
						// can't get location
						// GPS or Network is not enabled
						// Ask user to enable GPS/network in settings
						gps.showSettingsAlert();
					}

					Location l = new Location(lat, lon);
					passenger.setLocation(l);

					try {
						addresses = geocoder.getFromLocation(passenger
								.getLocation().getLatitude(), passenger
								.getLocation().getLongitude(), 1);
						String address = addresses.get(0).getAddressLine(0);
						// String city = addresses.get(0).getAddressLine(1);
						// String country = addresses.get(0).getAddressLine(2);
						passengerAddress.setText(address);

					} catch (IOException e) {
						e.printStackTrace();
					}

					handler.postDelayed(refresh, 5000);
				}
			};
			handler.post(refresh);
		} else {
			try {
				addresses = geocoder.getFromLocation(passenger.getLocation()
						.getLatitude(), passenger.getLocation().getLongitude(),
						1);
				String address = addresses.get(0).getAddressLine(0);
				// String city = addresses.get(0).getAddressLine(1);
				// String country = addresses.get(0).getAddressLine(2);
				passengerAddress.setText(address);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addListenerOnButton();

		alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
	}

	@Override
	public void onBackPressed() {

	}

	/**
	 * The function takes care of request fot taxi and cancel the taxi request
	 */
	private void checkrequest() {
		try {
			new LongOperation().execute().get();
			Log.d("hi", "hi");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	private class LongOperation extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String url = "http://137.132.247.133:8080/taxi360-war/api/request/"
					+ passenger.getId().toString();
			Response response = Server.getInstance().connect("GET", url);
			Log.d("requesttaxi", "Response =" + response.getResponse());
			if (Boolean.parseBoolean(response.getResponse())) {
				button.setText("Cancel Request");
			}
			return "Need Taxi";
		}

	}

	public void addListenerOnButton() {

		logout = (Button) findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!CheckConnectivity.isNetworkAvailable(context)
						&& (!connectivity.isConnectedToServer())) {

					connectivity.alertshow(context);
				} else {
					Session.getActiveSession().closeAndClearTokenInformation();
					Intent intent = new Intent(context, LoginActivity.class);
					// intent.putExtra("Title",passenger);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			}
		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!CheckConnectivity.isNetworkAvailable(context)
						&& (!connectivity.isConnectedToServer())) {
					connectivity.alertshow(context);
				} else {

					if (button.getText().toString() != "Cancel Request") {

						double lat, lon;

						GPSTracker gps = new GPSTracker(MainActivity.this);

						// check if GPS enabled
						if (gps.canGetLocation()) {

							lat = gps.getLatitude();
							lon = gps.getLongitude();

						} else {
							// can't get location
							// GPS or Network is not enabled
							// Ask user to enable GPS/network in settings
							gps.showSettingsAlert();
						}

						Log.d("name", name.getText().toString());
						String url = "http://137.132.247.133:8080/taxi360-war/api/request";
						try {

							reqest.setLocation(passenger.getLocation());
							reqest.setPassenger(passenger);
							Server.getInstance()
									.connect("POST", url, reqest);
								name.setEnabled(false);
								button.setText("Cancel Request");
							
						}

						catch (Exception e) {
							e.printStackTrace();

						}

					} else {

						// set title
						alertDialogBuilder.setTitle("Confirmation!!!");

						// set dialog message
						alertDialogBuilder
								.setMessage("Click yes to Cancel!")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// close
												// current activity

												String url = "http://137.132.247.133:8080/taxi360-war/api/request/"
														+ passenger.getId();
												Server.getInstance()
														.connect("DELETE", url);
														
													button.setText("Need Taxi");
													name.setEnabled(true);
												

											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// just close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}
				}

			}
		});

	}

	/**
	 * Attempts to register the client phone to the google server
	 */
	public void registerClient() {

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
				sendRegistrationToServer();

			} else {

				registrationStatus = "Already registered";

			}

		} catch (Exception e) {

			e.printStackTrace();
			registrationStatus = e.getMessage();

		}

		Log.d("RegID","hi"+ regId);

	}

	private void sendRegistrationToServer() {
		// This is an empty placeholder for an asynchronous task to post the
		// registration
		// id and any other identifying information to your server.
	}

	// If the user changes the orientation of his phone, the current activity
	// is destroyed, and then re-created. This means that our broadcast message
	// will get wiped out during re-orientation.
	// So, we save the broadcastmessage during an onSaveInstanceState()
	// event, which is called prior to the destruction of the activity.
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putString("BroadcastMessage", broadcastMessage);

	}

	// When an activity is re-created, the os generates an
	// onRestoreInstanceState()
	// event, passing it a bundle that contains any values that you may have put
	// in during onSaveInstanceState()
	// We can use this mechanism to re-display our last broadcast message.

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);

		broadcastMessage = savedInstanceState.getString("BroadcastMessage");

	}

	// If our activity is paused, it is important to UN-register any
	// broadcast receivers.
	@Override
	protected void onPause() {

		unregisterReceiver(gcmReceiver);
		super.onPause();
	}

	// When an activity is resumed, be sure to register any
	// broadcast receivers with the appropriate intent
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(gcmReceiver, gcmFilter);

	}

	// There are no menus for this demo app. This is just
	// boilerplate code.

	// NOTE the call to GCMRegistrar.onDestroy()
	@Override
	public void onDestroy() {

		GCMRegistrar.onDestroy(this);

		super.onDestroy();
	}

	/**
	 * The button allows to relocate the user to a noew location
	 */
	public void relocateme(View v) {
		handler.removeCallbacks(refresh);
		Intent intent = new Intent(context, RelocateMeActivity.class);
		intent.putExtra("Passenger", passenger);
		startActivity(intent);
	}

	/**
	 * The button allows the user to view the taxi drivers around
	 */
	public void viewtaxiaround(View v) {

		Intent intent = new Intent(context, ViewTaxiActivity.class);
		intent.putExtra("Passenger", passenger);
		startActivity(intent);
	}

	
	public void destinationset(View v) {
		Intent intent = new Intent(MainActivity.this,BookActivity.class);
	    
	    startActivityForResult(intent,90);
		
	
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch(requestCode) {
	    case 90:
	        if (resultCode == RESULT_OK) {
	        	Bundle res = data.getExtras();
	            reqest.setDestination(res.getString("param_result"));
	            Log.d("FIRST", "result:"+reqest.getDestination());
	        }
	        break;
	    }
	}
}
