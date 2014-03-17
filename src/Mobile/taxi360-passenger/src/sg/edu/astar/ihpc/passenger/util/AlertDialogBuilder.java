package sg.edu.astar.ihpc.passenger.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogBuilder {

	@SuppressWarnings("deprecation")
	public static void alertshow(Context context,String title, String message, String buttonValue){
		
		AlertDialog alertDialog1 = new AlertDialog.Builder(
	            context).create();

	    // Setting Dialog Title
	    alertDialog1.setTitle(title);
	    //alertDialog1.setTitle("Error");

	    // Setting Dialog Message
	    alertDialog1.setMessage(message);
	    //alertDialog1.setMessage("Internet Connection lost cannot render your request");

	    

	    // Setting OK Button
	    alertDialog1.setButton(buttonValue, new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            
	        }
	    });

	    // Showing Alert Message
	    alertDialog1.show();
	}
	
}
