package sg.edu.astar.ihpc.taxidriver.activity;

import sg.edu.astar.ihpc.taxidriver.R;
import sg.edu.astar.ihpc.taxidriver.entity.Ride;
import sg.edu.astar.ihpc.taxidriver.utils.Server;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

public class RatingActivity extends Activity {

	/**
	 * @param args
	 */
	private Ride ride;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rating);
		context = this;
		
	}

	/** The function helps in storing the rating for the ride **/
	public void saveRating(View view) {

		ride = (Ride) (getIntent().getSerializableExtra("ride"));
		RatingBar mBar = (RatingBar) findViewById(R.id.rating);
		ride.setPassengerRating((double) (mBar.getRating()));
		String url = "http://137.132.247.133:8080/taxi360-war/api/ride/passengerRating";
		if (Server.getInstance().connect("POST", url, ride).getCode()==0) {
			Intent intent = new Intent(context, DriverMainActivity.class);
			
			startActivity(intent);
			finish();
		} else {
			
		}

	}

	

}
