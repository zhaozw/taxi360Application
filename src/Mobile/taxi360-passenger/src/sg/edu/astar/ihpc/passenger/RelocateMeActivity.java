package sg.edu.astar.ihpc.passenger;
/**
The screen allows the user to relocate himself in the map
**/
/** Author :Thilak **/
import java.io.IOException;
import java.util.List;

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.Location;
import sg.edu.astar.ihpc.passenger.entity.Passenger;
import sg.edu.astar.ihpc.passenger.util.GPSTracker;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class RelocateMeActivity extends FragmentActivity {
	private GoogleMap gMap;
	private Geocoder geo;
Passenger passenger;
private LatLng loc;
Context context;
Marker currM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relocate_me);
		
		passenger=(Passenger)(getIntent().getSerializableExtra("Passenger"));
		geo = new Geocoder(this.getBaseContext());
		gMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		gMap.setMyLocationEnabled(true);
		context=this;
		GPSTracker  gps = new GPSTracker(RelocateMeActivity.this);

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
		Location temp=new Location(loc.latitude,loc.longitude);
		passenger.setLocation(temp);
		passenger.setRelocated(true);
		Intent intent = new Intent(context, MainActivity.class);
		
	 	   intent.putExtra("Title",passenger);
	 	   startActivity(intent);
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
				RelocateMeActivity.this.loc = marker.getPosition();
			
			List<android.location.Address> address;
				address = RelocateMeActivity.this.geo.getFromLocation(loc.latitude, loc.longitude,1);
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
