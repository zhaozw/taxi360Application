//This Class is used for checking the connectivity with the internet and used for all the HTTP requests send
//Author:Hemanth
package sg.edu.astar.ihpc.passenger.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class CheckConnectivity extends BroadcastReceiver {
	Context context;

	// This is called whenever there is a lost connection
	@Override
	public void onReceive(Context context, Intent arg1) {

		boolean isConnected = arg1.getBooleanExtra(
				ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		if (isConnected) {
			Toast.makeText(context, "Internet Connection Lost",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG)
					.show();
		}
	}

	// Used to check whether the network is available
	public static boolean isNetworkAvailable(Context context) {
		return ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo() != null;
	}

	// Used to show the alert window whenever error occurs
	public void alertshow(Context context) {
		AlertDialogBuilder.alertshow(context, "Error",
				"Internet Connection lost cannot render your request", "OK");
	}

	// Used to check whether able to connect to server
	public boolean isConnectedToServer() {
		return Server.getInstance().isConnectedToServer();
	}

}
