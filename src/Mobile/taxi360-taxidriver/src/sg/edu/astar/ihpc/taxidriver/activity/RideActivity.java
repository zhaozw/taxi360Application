package sg.edu.astar.ihpc.taxidriver.activity;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;


import sg.edu.astar.ihpc.taxidriver.entity.Location;
import sg.edu.astar.ihpc.taxidriver.entity.Ride;
import sg.edu.astar.ihpc.taxidriver.utils.AvailableDriver;
import sg.edu.astar.ihpc.taxidriver.utils.Server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import sg.edu.astar.ihpc.taxidriver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Activity which is used to display Ride Complete Screen Once the driver has
 * started his Journey Mohammed Althaf A0107629
 * */
public class RideActivity extends Activity {

	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Location myLocation;
	private Location driverGeo;
	private Handler handler = new Handler();
	private Ride ride;
	private Context context;
	private AvailableDriver ad;

	/**
	 * Loading the Ride Screen
	 * 
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride);
		ride = (Ride) (getIntent().getSerializableExtra("ride"));
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		setTitle("End the ride!!");
		context = this;
		initilizeMap();
		this.driverGeo = setUpMap();
		
		handler.post(updateTextRunnable);
	}

	Runnable updateTextRunnable = new Runnable() {
		public void run() {
			updatelocationtoserver();
			handler.postDelayed(this, 10000);
		}
	};

	/**
	 * Called once the driver clicks Ride Completed and it is updated in the
	 * server that the ride is complete
	 * 
	 */

	public void ridecompleted(View view) {

		// if this button is clicked, close
		// current activity
		String url = "http://137.132.247.133:8080/taxi360-war/api/ride/end";
		ride.setEndStatus("0");
		setUpMap();
		ride.setPassengerEndLocation(new Location(myLocation.getLatitude(),myLocation.getLongitude()));
		if (Server.getInstance().connect("POST", url, ride).getStatus()) {
			Intent intent = new Intent(context, RatingActivity.class);
			intent.putExtra("ride", ride);
			startActivity(intent);
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.ride, menu);
		return true;
	}

	/**
	 * Initializes the View with Map
	 * 
	 */

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
	 * Gets the current Location of the driver and zooms the camera to focus on
	 * the Driver Location
	 * 
	 */

	private Location setUpMap() {
		// Enable MyLocation Layer of Google Map
		Log.d("de", "de 2");
		googleMap.setMyLocationEnabled(true);

		// Get LocationManager object from System Service LOCATION_SERVICE
		this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Create a criteria object to retrieve provider
		Criteria criteria = new Criteria();

		// Get the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Get Current Location
		android.location.Location driverLocation = locationManager
				.getLastKnownLocation(provider);

		// set map type
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// Get latitude of the current location
		double latitude = driverLocation.getLatitude();

		// Get longitude of the current location
		double longitude = driverLocation.getLongitude();

		// Create a LatLng object for the current location
		this.myLocation = new Location(latitude, longitude);

		// Show the current location in Google Map
		googleMap.moveCamera(CameraUpdateFactory.newLatLng((new LatLng(
				latitude, longitude))));

		// Zoom in the Google Map
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
		// googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,
		// longitude)).title("driver").visible(false));
		// return latLng;

		Log.d("driver", myLocation.getLatitude().toString());
		return myLocation;
	}

	/**
	 * Updates the current location of the driver to the server
	 * 
	 */

	public void updatelocationtoserver() {
		String url = "http://137.132.247.133:8080/taxi360-war/api/ride/updateDriverLoc";
		
		
		driverGeo = setUpMap();
		ride.setDriverStartLocation(driverGeo);
		
		try {
			
			Server.getInstance().connect("PUT", url, ride);

			// Log.d("id",myObjects.get(0).getPassenger().getId().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
