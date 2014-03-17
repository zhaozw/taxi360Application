package sg.edu.astar.ihpc.passenger;

/**
 The Screen is called once the request is accepted by the taxi driver the location of the driver is updated in map continuously
 **/
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.AvailableDriver;
import sg.edu.astar.ihpc.passenger.entity.Location;
import sg.edu.astar.ihpc.passenger.entity.Request;
import sg.edu.astar.ihpc.passenger.entity.Ride;
import sg.edu.astar.ihpc.passenger.util.CheckConnectivity;
import sg.edu.astar.ihpc.passenger.util.GPSTracker;

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
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RideActivity extends Activity {
	JSONArray requests = null;
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Location myLocation;
	private Location driverGeo;
	private List<Request> myObjects;
	ObjectMapper mapper = new ObjectMapper();
	Handler handler = new Handler();
	private Ride ride;
	private CompoundButton toggle;
	Button rides;
	AlertDialog.Builder alertDialogBuilder;
	CheckConnectivity connectivity;
	Context context;
	AvailableDriver ad;

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
	 * The function helps in creating notification once we recieve a
	 * notification from the google server
	 **/
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
				if (!broadcastMessage.equals("Ride Finished")) {
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
									Log.d("ride comple", "ride gomplete");
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
					alertDialog1.setMessage("Ride Completed");

					// Setting OK Button
					alertDialog1.setButton("GiveRating",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(context,
											Rating.class);
									intent.putExtra("ride", ride);
									startActivity(intent);

								}
							});

					// Showing Alert Message
					alertDialog1.show();
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

		}
		gcmFilter = new IntentFilter();
		gcmFilter.addAction("GCM_RECEIVED_ACTION");
		registerClient();
		context = this;
		connectivity = new CheckConnectivity();
		initilizeMap();
		this.driverGeo = setUpMap();
		ad = new AvailableDriver();
		ride = new Ride();
		ride.setId(getIntent().getStringExtra("ride"));

		ride = getride(ride);
		handler.post(updateTextRunnable);
	}

	/**
	 * The function is used to get the ride details for the particular request
	 **/
	public Ride getride(Ride ride) {
		String url = "http://137.132.247.133:8080/taxi360-war/api/ride/"
				+ ride.getId();
		try {
			ride = mapper.readValue(Server.getInstance().connect("GET", url)
					.getResponse(), Ride.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ride;
	}

	/**
	 * The function is used to continuously update the location of the driver in
	 * the map
	 **/

	Runnable updateTextRunnable = new Runnable() {
		public void run() {
			// Loading map

			// fetch current location of Driver

			googleMap.clear();
			Log.d("geo", ride.getDriver().getId().toString());
			// form URL to call service
			String url = "http://137.132.247.133:8080/taxi360-war/api/availabledriver/"
					+ ride.getDriver().getId().toString();
			Log.d("taxi number", url);
			try {
				ad = mapper.readValue(Server.getInstance().connect("GET", url)
						.getResponse(), AvailableDriver.class);

			}catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			}  catch (IOException e) {
				e.printStackTrace();
			}
			// setting markers

			googleMap.addMarker(new MarkerOptions().position(
					new LatLng(ad.getLocation().getLatitude(), ad.getLocation()
							.getLongitude())).title("Driver"));
			handler.postDelayed(this, 10000);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ride, menu);
		return true;
	}

	private void initilizeMap() {
		Log.d("de", "de 1");
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * The function is used to set up the map for the view
	 **/
	private Location setUpMap() {
		// Enable MyLocation Layer of Google Map
		Log.d("de", "de 2");
		googleMap.setMyLocationEnabled(true);

		// Get LocationManager object from System Service LOCATION_SERVICE
		this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		//
		// // Create a criteria object to retrieve provider
		// Criteria criteria = new Criteria();
		//
		//
		// // Get the name of the best provider
		// String provider = locationManager.getBestProvider(criteria, true);
		//
		// // Get Current Location
		// android.location.Location driverLocation=
		// locationManager.getLastKnownLocation(provider);

		// set map type
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		double lat = 0.0;
		double lon = 0.0;

		GPSTracker gps = new GPSTracker(RideActivity.this);

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

		// Create a LatLng object for the current location
		this.myLocation = new Location(lat, lon);

		// Show the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory
				.newLatLng((new LatLng(lat, lon))));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
		// googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
		// longitude)).title("driver").visible(false));
		// return latLng;

		Log.d("driver", myLocation.getLatitude().toString());
		return myLocation;
	}

	/**
	 * The function is used to register the clientfor the notifications
	 **/
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
				regId = GCMRegistrar.getRegistrationId(this);

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

		Log.d("RegID", regId);

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

}
