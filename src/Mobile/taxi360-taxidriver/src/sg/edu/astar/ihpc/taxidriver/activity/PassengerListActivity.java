package sg.edu.astar.ihpc.taxidriver.activity;

import java.util.ArrayList;
import java.util.HashMap;


import sg.edu.astar.ihpc.taxidriver.entity.Location;
import sg.edu.astar.ihpc.taxidriver.entity.Request;
import sg.edu.astar.ihpc.taxidriver.entity.Ride;
import sg.edu.astar.ihpc.taxidriver.utils.ListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import sg.edu.astar.ihpc.taxidriver.R;
import com.google.android.gms.maps.model.LatLng;

/**
 *  Activity which is used to display the 
 *  list of passengers requested for a Cab in a List View
 *  Mohammed Althaf
	 * A0107629
 * */
public class PassengerListActivity extends Activity{
	private ArrayList<Request> myObjects;
	private DriverMainActivity drivermain;
	private Ride rideresponse;
	
	/**
	 * Loading the Passenger List Screen
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myObjects=(ArrayList<Request>) this.getIntent().getSerializableExtra("array");
        ListAdapter adapter = new ListAdapter(this, R.layout.activity_passengerlist,myObjects);
        ListView requestView = (ListView)findViewById(R.id.list);
        requestView.setAdapter(adapter);
	}
	
	/**
	 * When driver accepts a request ,
	 * passenger is notified that the request
	 * is accepted and the ride is started
	 * 
	 */
		public void sendnotification(View v){
		Request selected=(Request)v.getTag();
		Log.d("selected", selected.getPassenger().getId().toString());
		drivermain=new DriverMainActivity();
		rideresponse=drivermain.rideStart(selected.getPassenger().getId(), new LatLng(selected.getLocation().getLatitude(), selected.getLocation().getLongitude()));
		rideresponse.setPassengerStartLocation((selected.getLocation()));
		rideresponse.setDriverStartLocation((Location)this.getIntent().getSerializableExtra("mylocation"));
		Intent intent = new Intent(this, RideStartActivity.class);
 	   	intent.putExtra("ride", rideresponse);
 	   	startActivity(intent);
 	   	finish();
		}

}
