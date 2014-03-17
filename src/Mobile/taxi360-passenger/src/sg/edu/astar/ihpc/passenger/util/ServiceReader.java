package sg.edu.astar.ihpc.passenger.util;

import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;


// Remember, setContext must be the first thing called, better to add it in MainActivity.
public class ServiceReader {

	private static Context context = null;
	private Properties properties;
	private static ServiceReader instance = null;

	public static synchronized ServiceReader getInstance() {
		if (instance == null) {
			instance = new ServiceReader();
			try {
				instance.properties.load(context.getAssets().open("WebService"));
			} catch (IOException e) {			
				Log.d("ServiceReader", e.toString());
			}
		}
		return instance;
	}
	
	private ServiceReader(){
		this.properties=null;
	}

	public static void setContext(Context c) {
		if (context == null)
			context = c;
	}

	public Context getContext() {
		return context;
	}
	
	public String getServerAddress(){
		return this.properties.getProperty("ServerAddress");
	}
	
	public String getService(String name){
		return getServerAddress()+this.properties.getProperty(name);
	}

}
