package sg.edu.astar.ihpc.passenger;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.Location;
import sg.edu.astar.ihpc.passenger.entity.Passenger;
import sg.edu.astar.ihpc.passenger.util.GPSTracker;
import sg.edu.astar.ihpc.passenger.util.Server;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BookActivity extends FragmentActivity {
	private GoogleMap gMap;
	private Geocoder geo;
Passenger passenger;
private LatLng loc;
Context context;
String url;
Marker currM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book);
		passenger=(Passenger)(getIntent().getSerializableExtra("Passenger"));
		geo = new Geocoder(this.getBaseContext());
		gMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		gMap.setMyLocationEnabled(true);
		context=this;
		url="http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=en&latlng=";
		GPSTracker  gps = new GPSTracker(BookActivity.this);

        // check if GPS enabled       
       
		final LatLng curr = new LatLng(gps.getLatitude(), gps.getLongitude());
		 currM = gMap.addMarker(new MarkerOptions()
		                          .position(curr)
		                          .draggable(true));
		gMap.setOnMarkerDragListener(new MarkerDragListener());
		loc = curr;
		  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	/**
	 The fuction allows the user to confirm once the location is set
	 **/
	public void relocate(View v) {
		//loc=currM.getPosition();
		
		
url=url+Double.toString(loc.latitude)+","+Double.toString(loc.longitude);
			String reversegeocode=Server.getInstance()
					.connect("GET", url).getResponse();
			Log.d("reverse geocode",reversegeocode);
			try {
				JSONObject jsonObject = new JSONObject(reversegeocode);
				
				Log.d("reverse adress",jsonObject.getJSONObject("results").getString("formatted_address"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Location temp=new Location(loc.latitude,loc.longitude);
		Bundle conData = new Bundle();
	    conData.putString("param_result", "Clementi");
	    Intent intent = new Intent();
	    intent.putExtras(conData);
	    setResult(RESULT_OK, intent);
	    finish();
	}

	/**
	 The listner is called once the marker is moved
	 **/
	class MarkerDragListener implements OnMarkerDragListener {

		@Override
		public void onMarkerDrag(Marker marker) {
			
		}
		
		
		@Override
		public void onMarkerDragEnd(Marker marker) {
			try {
				BookActivity.this.loc = marker.getPosition();
			
			List<android.location.Address> address;
				address = BookActivity.this.geo.getFromLocation(loc.latitude, loc.longitude,1);
				marker.setTitle(address.get(0).getLocality());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}

	
		
		
		@Override
		public void onMarkerDragStart(Marker marker) {
			
		}
		
	}

}
