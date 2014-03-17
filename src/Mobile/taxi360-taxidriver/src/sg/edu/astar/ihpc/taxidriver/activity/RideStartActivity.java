package sg.edu.astar.ihpc.taxidriver.activity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;


import sg.edu.astar.ihpc.taxidriver.entity.Location;
import sg.edu.astar.ihpc.taxidriver.entity.Ride;
import sg.edu.astar.ihpc.taxidriver.utils.DirectionsJSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import sg.edu.astar.ihpc.taxidriver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 *  Activity which is used to display Ride start Screen 
 *  Once the driver has accepted a request,with the direction between the passenger 
 *  and driver
 *  Mohammed Althaf
	 * A0107629
 * */
public class RideStartActivity extends Activity {
	JSONArray requests = null;
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private Location myLocation;
	private ObjectMapper mapper = new ObjectMapper();
	private Handler handler=new Handler();
	private Ride ride;
	private CompoundButton toggle ;
	private Button rides;
	private AlertDialog.Builder alertDialogBuilder;
	private Context context;
	private Location driverGeo;
	
	
	/**
	 * Loads the RideStartScreen with the Passenger 
	 * and Driver Markers on screen and 
	 * the direction Map between them 
	 * 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride_start);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy); 
		    
		}
		setTitle("Start the ride!!");
		context=this;
		
		initilizeMap();
		this.toggle= (CompoundButton) findViewById(R.id.toggleAvail);
		toggle.setEnabled(false);
		this.driverGeo= setUpMap();
        ride=(Ride)(getIntent().getSerializableExtra("ride"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(ride.getPassengerStartLocation().getLatitude(),ride.getPassengerStartLocation().getLongitude())).title("accepted"));
        handler.post(updateTextRunnable);
	}

	Runnable updateTextRunnable=new Runnable(){  
		  public void run() {
			  String url = getDirectionsUrl(new LatLng(ride.getDriverStartLocation().getLatitude(),ride.getDriverStartLocation().getLongitude()), new LatLng(ride.getPassengerStartLocation().getLatitude(),ride.getPassengerStartLocation().getLongitude()));
			  
              DownloadTask downloadTask = new DownloadTask();

              // Start downloading json data from Google Directions API
              downloadTask.execute(url);
			  handler.postDelayed(this, 10000);
		  }};
	 public void ridestarted(View view) {
		 	Intent intent = new Intent(this, RideActivity.class);
	 	   	intent.putExtra("ride", ride);
	 	   	startActivity(intent);
	 	   	finish();
				
	 }
	 /**
		 * Fetches the Directions between Passenger 
		 * and Driver Locations from google API
		 * 
		 */
					
	 public String getDirectionsUrl(LatLng origin,LatLng dest){
		 
	        // Origin of route
	        String str_origin = "origin="+origin.latitude+","+origin.longitude;
	 
	        // Destination of route
	        String str_dest = "destination="+dest.latitude+","+dest.longitude;
	 
	        // Sensor enabled
	        String sensor = "sensor=false";
	 
	        // Building the parameters to the web service
	        String parameters = str_origin+"&"+str_dest+"&"+sensor;
	 
	        // Output format
	        String output = "json";
	 
	        // Building the url to the web service
	        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
	 
	        return url;
	    }
	 
	    /** 
	     * A method to download json data from url 
	     * */
	    public String downloadUrl(String strUrl) throws IOException{
	        String data = "";
	        InputStream iStream = null;
	        HttpURLConnection urlConnection = null;
	        try{
	            URL url = new URL(strUrl);
	 
	            // Creating an http connection to communicate with url
	            urlConnection = (HttpURLConnection) url.openConnection();
	 
	            // Connecting to url
	            urlConnection.connect();
	 
	            // Reading data from url
	            iStream = urlConnection.getInputStream();
	 
	            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
	 
	            StringBuffer sb = new StringBuffer();
	 
	            String line = "";
	            while( ( line = br.readLine()) != null){
	                sb.append(line);
	            }
	 
	            data = sb.toString();
	 
	            br.close();
	 
	        }catch(Exception e){
	            Log.d("Exception while downloading url", e.toString());
	        }finally{
	            iStream.close();
	            urlConnection.disconnect();
	        }
	        return data;
	    }
	 
	    /**
	     * 
	     * Fetches data from url passed
	     *
	     */
	    private class DownloadTask extends AsyncTask<String, Void, String>{
	 
	        // Downloading data in non-ui thread
	        @Override
	        protected String doInBackground(String... url) {
	 
	            // For storing data from web service
	            String data = "";
	 
	            try{
	                // Fetching the data from web service
	                data = downloadUrl(url[0]);
	            }catch(Exception e){
	                Log.d("Background Task",e.toString());
	            }
	            return data;
	        }
	 
	        // Executes in UI thread, after the execution of
	        // doInBackground()
	        @Override
	        protected void onPostExecute(String result) {
	            super.onPostExecute(result);
	 
	            ParserTask parserTask = new ParserTask();
	 
	            // Invokes the thread for parsing the JSON data
	            parserTask.execute(result);
	        }
	    }
	 
	    /**
	     *  A class to parse the Google Places in JSON format 
	     *  
	     *  */
	    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
	 
	        // Parsing the data in non-ui thread
	        @Override
	        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
	 
	            JSONObject jObject;
	            List<List<HashMap<String, String>>> routes = null;
	 
	            try{
	                jObject = new JSONObject(jsonData[0]);
	                DirectionsJSONParser parser = new DirectionsJSONParser();
	 
	                // Starts parsing data
	                routes = parser.parse(jObject);
	            }catch(Exception e){
	                e.printStackTrace();
	            }
	            return routes;
	        }
	 
	        // Executes in UI thread, after the parsing process
	        @Override
	        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
	            ArrayList<LatLng> points = null;
	            PolylineOptions lineOptions = null;
	            MarkerOptions markerOptions = new MarkerOptions();
	 
	            // Traversing through all the routes
	            for(int i=0;i<result.size();i++){
	                points = new ArrayList<LatLng>();
	                lineOptions = new PolylineOptions();
	 
	                // Fetching i-th route
	                List<HashMap<String, String>> path = result.get(i);
	 
	                // Fetching all the points in i-th route
	                for(int j=0;j<path.size();j++){
	                    HashMap<String,String> point = path.get(j);
	 
	                    double lat = Double.parseDouble(point.get("lat"));
	                    double lng = Double.parseDouble(point.get("lng"));
	                    LatLng position = new LatLng(lat, lng);
	 
	                    points.add(position);
	                }
	 
	                // Adding all the points in the route to LineOptions
	                lineOptions.addAll(points);
	                lineOptions.width(2);
	                lineOptions.color(Color.RED);
	            }
	 
	            // Drawing polyline in the Google Map for the i-th route
	            googleMap.addPolyline(lineOptions);
	        } 
	    
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
		 * Gets the current Location of the driver and
		 * zooms the camera to focus on the Driver Location 
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
	         android.location.Location driverLocation= locationManager.getLastKnownLocation(provider);

	        //set map type
	        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

	        // Get latitude of the current location
	        double latitude = driverLocation.getLatitude();

	        // Get longitude of the current location
	        double longitude = driverLocation.getLongitude();
	        
	        

	        // Create a LatLng object for the current location
	        this.myLocation = new Location(latitude, longitude);      

	        // Show the current location in Google Map        
	        googleMap.moveCamera(CameraUpdateFactory.newLatLng((new LatLng(latitude, longitude))));

	        // Zoom in the Google Map
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
	       //googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("driver").visible(false));
	        //return latLng;
	        
	        Log.d("driver", myLocation.getLatitude().toString());
	        return myLocation;
	    }
	 
}
