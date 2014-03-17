package sg.edu.astar.ihpc.passenger;
/**
 * Attempts to sign in with the facebook fragment
 **/

/** Author :Hemanth **/
import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.util.CheckConnectivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
//import com.google.android.gms.maps.CameraUpdateFactory;
public class LoginView extends FragmentActivity {
	private FacebookFragment facebookFragment;
	private static final String TAG = "facebookFragment";
	CheckConnectivity check;
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    context=this;
check=new CheckConnectivity();
CheckConnectivity.isNetworkAvailable(this);
if (!CheckConnectivity.isNetworkAvailable(this)&&(!check.isConnectedToServer())){
	
	AlertDialog alertDialog1 = new AlertDialog.Builder(
            this).create();

    // Setting Dialog Title
    alertDialog1.setTitle("Error");

    // Setting Dialog Message
    alertDialog1.setMessage("Internet Connection lost cannot render your request");

    

    // Setting OK Button
    alertDialog1.setButton("OK", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
        	((Activity) context).finish();
            dialog.cancel();
            
        }
    });

    // Showing Alert Message
    alertDialog1.show();
	}
	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	    	facebookFragment = new FacebookFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, facebookFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	    	facebookFragment = (FacebookFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

