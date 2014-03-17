//Used to check the Current location
//Author:Hemanth
package sg.edu.astar.ihpc.passenger.entity;

import android.app.Application;
import android.content.Context;

public class CurrentLocation extends Application{
Location location;


//Constructor
public CurrentLocation() {
	super();
	
	

}

public Location getLocation() {
	return location;
}

public void setLocation(double lat ,double lon) {
	this.location = new Location(lat, lon);
}


}
