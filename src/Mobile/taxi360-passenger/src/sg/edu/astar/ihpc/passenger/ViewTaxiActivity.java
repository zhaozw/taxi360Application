package sg.edu.astar.ihpc.passenger;

/**
 The Screen is used to see the cabs nearby by the passenger
 **/
/** Author :Hemanth **/
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.AvailableDriver;
import sg.edu.astar.ihpc.passenger.entity.Location;
import sg.edu.astar.ihpc.passenger.entity.Passenger;
import sg.edu.astar.ihpc.passenger.entity.Ride;
import sg.edu.astar.ihpc.passenger.util.CheckConnectivity;
import sg.edu.astar.ihpc.passenger.util.Server;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewTaxiActivity extends Activity {

	protected static final long TIME_DELAY = 5000;

	// contacts JSONArray
	JSONArray requests = null;
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Location myLocation;
	private Location driverGeo;
	private List<AvailableDriver> myObjects;
	ObjectMapper mapper = new ObjectMapper();
	Handler handler = new Handler();
	private Ride ride;
	private Ride rideresponse;
	private CompoundButton toggle;
	Button rides;
	Passenger passenger;
	Context context;
	CheckConnectivity connectivity;

	public GoogleMap getGoogleMap() {
		return googleMap;
	}

	public void setGoogleMap(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_taxi);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);

		}

		connectivity = new CheckConnectivity();
		passenger = (Passenger) (getIntent().getSerializableExtra("Passenger"));

		context = this;

		initilizeMap();
		this.driverGeo = setUpMap();

		handler.post(updateTextRunnable);
	}

	/**
	 * The function is used to update the available drivers in the location
	 * continuously
	 **/
	Runnable updateTextRunnable = new Runnable() {
		public void run() {
			// Loading map

			// fetch current location of Driver

			googleMap.clear();
			Log.d("geo", driverGeo.getLatitude().toString() + "lng"
					+ driverGeo.getLongitude().toString());
			// form URL to call service
			String url = "http://137.132.247.133:8080/taxi360-war/api/availabledriver?lat="
					+ driverGeo.getLatitude().toString()
					+ "&log="
					+ driverGeo.getLongitude().toString() + "&distance=1000000";

			try {
				myObjects = mapper.readValue(
						Server.getInstance().connect("GET", url).getResponse(),
						new TypeReference<List<AvailableDriver>>() {
						});
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("is", myObjects.toString());

			// setting markers
			for (int i = 0; i < myObjects.size(); i++) {
				Log.d("marker", myObjects.get(i).getDriver().getId().toString());
				HashMap<Integer, AvailableDriver> mapRequest = new HashMap<Integer, AvailableDriver>();
				mapRequest.put(i, myObjects.get(i));
				googleMap
						.addMarker(new MarkerOptions().position(
								new LatLng(myObjects.get(i).getLocation()
										.getLatitude(), myObjects.get(i)
										.getLocation().getLongitude()))
								.title(myObjects.get(i).getDriver().getId()
										.toString()));
			}
			// googleMap.setOnMarkerClickListener(this);

			handler.postDelayed(this, 10000);
		}
	};

	/**
	 * The function is used to setup the map
	 **/

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
	 * function to load map. If map is not created it will create it for you
	 **/
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

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}
