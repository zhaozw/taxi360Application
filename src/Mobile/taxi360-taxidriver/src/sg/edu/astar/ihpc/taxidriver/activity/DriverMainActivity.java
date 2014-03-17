package sg.edu.astar.ihpc.taxidriver.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


import sg.edu.astar.ihpc.taxidriver.R;
import sg.edu.astar.ihpc.taxidriver.R.id;
import sg.edu.astar.ihpc.taxidriver.entity.Driver;
import sg.edu.astar.ihpc.taxidriver.entity.Location;
import sg.edu.astar.ihpc.taxidriver.entity.Passenger;
import sg.edu.astar.ihpc.taxidriver.entity.Request;
import sg.edu.astar.ihpc.taxidriver.entity.Ride;
import sg.edu.astar.ihpc.taxidriver.utils.AvailableDriver;
import sg.edu.astar.ihpc.taxidriver.utils.Server;
import sg.edu.astar.ihpc.taxidriver.utils.SessionManager;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

	/**
	 * Activity which is used to display Home Screen of the Driver where the driver
	 * is able to find all the passengers who have requested for taxi Mohammed
	 * Althaf A0107629
	 * */

	public class DriverMainActivity extends Activity implements
			OnMarkerClickListener, OnInfoWindowClickListener {

		protected static final long TIME_DELAY = 5000;

		// contacts JSONArray

		private GoogleMap googleMap;
		private LocationManager locationManager;
		private Location myLocation;
		private Location driverGeo;
		private List<Request> myObjects;
		private ArrayList<Request> myObject;
		private ObjectMapper mapper = new ObjectMapper();
		private Handler handler = new Handler();
		private Ride ride;
		private Ride rideresponse;
		private CompoundButton toggle;
		private org.codehaus.jackson.map.ObjectWriter writer;
		private Runnable updateTextRunnable;
		private View v = null;
		private Button rides;
		private Button reached;
		
		private AvailableDriver ad;
		private Driver driver;
		private Context context;

		/**
		 * Loading the Driver Home Screen
		 * 
		 */

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			SessionManager.setContext(this);
			if (!SessionManager.getInstance().checkLogin())
				return;
			setContentView(R.layout.driver_activity_main);
			setTitle("Find a Passenger!!");

			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);

			}
			initialize();
			handler.post(updateTextRunnable);
			start();
		}

		/**
		 * Initializes the Layout Button and gets the drivers location
		 * 
		 */

		public void initialize() {
			this.toggle = (CompoundButton) findViewById(R.id.toggleAvail);
			toggle.setActivated(true);
			rides = (Button) findViewById(R.id.ride);
			reached = (Button) findViewById(R.id.reachedPassenger);
			context = this;
			rides.setVisibility(View.GONE);
			reached.setVisibility(View.GONE);
			final SharedPreferences prefs = getApplicationContext()
					.getSharedPreferences(DriverMainActivity.class.getSimpleName(),
							Context.MODE_PRIVATE);
			boolean avail = prefs.getBoolean("availability", true);
			toggle.setChecked(avail);
			initilizeMap();
			this.driverGeo = setUpMap();

			driver = driverset();
		}

		public void start() {
			updateTextRunnable = new Runnable() {
				public void run() {
					updatelocationtoserver();

					// fetch current location of Driver
					try {

						googleMap.clear();
						Log.d("geo", driverGeo.getLatitude().toString() + "lng"
								+ driverGeo.getLongitude().toString());
						// form URL to call service
						String url = "http://137.132.247.133:8080/taxi360-war/api/request?lat="
								+ driverGeo.getLatitude().toString()
								+ "&log="
								+ driverGeo.getLongitude().toString()
								+ "&distance=1000000";

						try {
							Log.d("return = ", Server.getInstance().connect("GET", url)+"");
							if(Server.getInstance()
									.connect("GET", url).getResponse()!=null)
							{
							myObjects = mapper.readValue(Server.getInstance()
									.connect("GET", url).getResponse(),
									new TypeReference<List<Request>>() {
									});
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.d("myobjects", myObjects.toString());
						if (myObjects.size()>0) {

							for (int i = 0; i < myObjects.size(); i++) {
								Log.d("marker", myObjects.get(i).getPassenger()
										.getId().toString());
								HashMap<Integer, Request> mapRequest = new HashMap<Integer, Request>();
								mapRequest.put(i, myObjects.get(i));
								googleMap.addMarker(new MarkerOptions().position(
										new LatLng(myObjects.get(i).getLocation()
												.getLatitude(), myObjects.get(i)
												.getLocation().getLongitude()))
										.title(myObjects.get(i).getPassenger()
												.getId().toString()));

							}
						}
						googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

							@Override
							public View getInfoWindow(Marker marker) {
								v = getLayoutInflater().inflate(
										R.layout.windowslayout, null);

								for (int i = 0; i < myObjects.size(); i++) {
									// Getting view from the layout file
									// info_window_layout
									if (!((marker.getTitle()
											.equalsIgnoreCase("driver")) || (marker
											.getTitle()
											.equalsIgnoreCase("accepted")))) {

										Log.d("marker id", marker.getTitle()
												.toString());
										Log.d("Title id", marker.getTitle()
												.toString());
										if ((marker.getTitle().toString())
												.equalsIgnoreCase(myObjects.get(i)
														.getPassenger().getId()
														.toString())) {
											Log.d("Matched for", marker.getTitle()
													.toString());
											TextView name = (TextView) v
													.findViewById(R.id.name);
											TextView detail = (TextView) v
													.findViewById(R.id.detail);
											name.setText(myObjects.get(i)
													.getPassenger().getName()
													.toString());
											if(myObjects.get(i)
													.getPassenger()
													.getMobilenumber()!=null)
											detail.setText(myObjects.get(i)
													.getPassenger()
													.getMobilenumber().toString());
											// Use your button like this
											Button mBtn = (Button) v
													.findViewById(R.id.Accept);
											mBtn.setBackgroundColor(color.white);
										}
									} else {
										return v;
									}

								}
								return v;
							}

							// Bringing Window above the marker when touched
							@Override
							public View getInfoContents(Marker marker) {

								return null;
							}

						});

						// On clicking the window
						googleMap
								.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

									@Override
									public void onInfoWindowClick(Marker marker) {
										if (!((marker.getTitle()
												.equalsIgnoreCase("driver")) || (marker
												.getTitle()
												.equalsIgnoreCase("accepted")))) {
											googleMap.clear();
											toggle.setChecked(false);
											rideresponse = rideStart(Long
													.parseLong(marker.getTitle()
															.toString()), marker
													.getPosition());
											googleMap.addMarker(new MarkerOptions()
													.position(marker.getPosition())
													.title("accepted"));
											rides.setVisibility(View.VISIBLE);
											reached.setVisibility(View.VISIBLE);
											rides.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View arg0) {

													Intent intent = new Intent(
															context,
															RideActivity.class);
													intent.putExtra("ride",
															rideresponse);
													startActivity(intent);
													finish();
												}
											});

											// googleMap.addMarker(new
											// MarkerOptions().position(new
											// LatLng(myLocation.getLatitude(),myLocation.getLongitude())).title("driver").visible(false));
											handler.removeCallbacks(updateTextRunnable);
											Log.d("window", marker.getTitle());
										}
									}
								});
					} catch (Exception e) {
						e.printStackTrace();
					}
					handler.postDelayed(this, TIME_DELAY);
				}
			};
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
		 * function to load map. If map is not created it will create it for you
		 * */
		private void initilizeMap() {
			Log.d("de", "de 1");
			if (googleMap == null) {
				this.googleMap = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.map)).getMap();

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
		public boolean onMarkerClick(Marker marker) {
			return false;
		}

		@Override
		public void onInfoWindowClick(Marker arg0) {
			Log.d("infowind", "infowin");
			// TODO Auto-generated method stub

		}

		/**
		 * Updates the Server whether the Driver is available or not.During Ride it
		 * is turned off
		 * 
		 */

		public void onToggleClicked(View view) {
			// Is the toggle on?
			// boolean on = ((CompoundButton)view).isChecked();
			boolean on = toggle.isChecked();

			if (on) {
				Log.d("avail", "on");
				handler.postDelayed(updateTextRunnable, TIME_DELAY);
			} else {
				Log.d("avail", "Off");
				googleMap.clear();
				initilizeMap();
				handler.removeCallbacks(updateTextRunnable);

			}
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			final SharedPreferences prefs = getApplicationContext()
					.getSharedPreferences(DriverMainActivity.class.getSimpleName(),
							Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("availability", toggle.isChecked());
			editor.commit();
		}

		/*
		 * Method to display available Passengers in List Mohammed Althaf A0107629B
		 */
		/**
		 * Navigates the screen to PassengerList screen where the requested
		 * passengers are displayed in a list view
		 * 
		 */

		@SuppressWarnings("deprecation")
		public void PassengerList(View view) {
			Log.d("list", "list" + driverGeo.getLatitude().toString());
			String url = "http://137.132.247.133:8080/taxi360-war/api/request?lat="
					+ driverGeo.getLatitude().toString() + "&log="
					+ driverGeo.getLongitude().toString() + "&distance=1000000";
			try {

				myObject = mapper.readValue(
						Server.getInstance().connect("GET", url).getResponse(),
						new TypeReference<List<Request>>() {
						});
				if (!myObject.isEmpty()) {
					Intent intent = new Intent(context, PassengerListActivity.class);
					intent.putExtra("array", myObject);
					intent.putExtra("mylocation", myLocation);
					
					// intent.putParcelableArrayListExtra("array", (ArrayList<?
					// extends Parcelable>) myObject);
					startActivity(intent);
				} else {
					AlertDialog alert = new AlertDialog(context) {
					};
					alert.setMessage("No Passenger");
					alert.setTitle("Taxi-360");
					alert.setButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// here you can add functions
						}
					});

					alert.show();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void reachedPassenger(View view){
			
			String url = "http://137.132.247.133:8080/taxi360-war/api/ride/reachedNear";
			try {
				
				Server.getInstance().connect("PUT", url, rideresponse);

				// Log.d("id",myObjects.get(0).getPassenger().getId().toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
			reached.setVisibility(View.GONE);
		}
		public Driver driverset() {

			driver = SessionManager.getInstance().getDriver();
			return driver;
		}

		public Ride rideStart(Long id, LatLng loc) {
			ride = new Ride();
			ride.setDriver(driverset());
			Passenger tempPassenger = new Passenger();
			tempPassenger.setId(id);
			ride.setPassenger(tempPassenger);
			ride.setDriverStartLocation(driverGeo);
			writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				String url = "http://137.132.247.133:8080/taxi360-war/api/ride/start";

				rideresponse = mapper.readValue(
						Server.getInstance().connect("POST", url, ride)
								.getResponse(), Ride.class
						);

			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return rideresponse;
		}

		/**
		 * Updates the current location of the driver to the server
		 * 
		 */

		public void updatelocationtoserver() {
			String url = "http://137.132.247.133:8080/taxi360-war/api/availabledriver";
			//writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
			ad = new AvailableDriver();
			ad.setDriver(driver);
			driverGeo = setUpMap();
			ad.setLocation(driverGeo);
			Server.getInstance().connect("PUT", url, ad);
			
			Log.d("id", ad.getDriver().getId() + "driver" + driver.getId());

		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.main, menu);
		    return true;
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		    // Handle item selection
		    switch (item.getItemId()) {
		        case R.id.Logout:
		        	if(Server.getInstance().isConnectedToServer())
		        		SessionManager.getInstance().logoutUser();
		            return true;
		        case R.id.MyPlaces:
		        	

						Intent intent = new Intent(
								context,
								MyPlacesActivity.class);
						startActivity(intent);
					
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
		    }
		}
	}


