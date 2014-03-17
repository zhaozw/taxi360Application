package sg.edu.astar.ihpc.taxidriver.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import sg.edu.astar.ihpc.taxidriver.R;
import sg.edu.astar.ihpc.taxidriver.entity.Location;
import sg.edu.astar.ihpc.taxidriver.utils.PlaceJSONParser;
import sg.edu.astar.ihpc.taxidriver.utils.PlacesAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyPlacesActivity extends Activity{
	AutoCompleteTextView atvPlaces;
	PlacesTask placesTask;
    ParserTask parserTask;
	private ArrayList<Location> myPlaces;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        myPlaces=new ArrayList<Location>();
        myPlaces.add(new Location(1.31129099987447,103.763887910172));
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.header);
        atvPlaces.setThreshold(1);
        atvPlaces.addTextChangedListener(new TextWatcher() {
        	 
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    
        PlacesAdapter adapter = new PlacesAdapter(this, R.layout.activity_myplaces,myPlaces);
        ListView requestView = (ListView)findViewById(R.id.list);
        requestView.setAdapter(adapter);
       
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.addplaces, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    
	        case R.id.add:
	        	Intent intent=new Intent(this,AutoPopulateLocationActivity.class);
	        	startActivity(intent);
	        	
	        	/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            // Get the layout inflater
	            LayoutInflater inflater = this.getLayoutInflater();

	            // Inflate and set the layout for the dialog
	            // Pass null as the parent view because its going in the dialog layout
	            //builder.setView(R.layout.activity_autopopulate, null);
	            builder.setMessage("You are heading to ?:");
	     
	             // Use an EditText view to get user input.
	             final EditText input = new EditText(this);
	             
	             builder.setView(input);
	     
	            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	     
	                @Override
	                public void onClick(DialogInterface dialog, int whichButton) {
	                    String value = input.getText().toString();
	                    
	                    return;
	                }
	            });
	     
	            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	     
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    return;
	                }
	            });
	            
	            builder.show();*/
	            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	//A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
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
	// Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{
 
        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";
 
            // Obtain browser key from https://code.google.com/apis/console
            String key = "AIzaSyARMPXQPO0q7cle8epyN6lBHO82RTi9ubk";
 
            String input="";
 
            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
 
            // place type to be searched
            String types = "types=geocode";
 
            // Sensor enabled
            String sensor = "sensor=false";
 
            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;
 
            // Output format
            String output = "json";
 
            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
 
            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            // Creating ParserTask
            parserTask = new ParserTask();
 
            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
 
        JSONObject jObject;
 
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
 
            List<HashMap<String, String>> places = null;
 
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
 
            try{
                jObject = new JSONObject(jsonData[0]);
 
                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }
 
        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
 
            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };
 
            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
 
            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }
}

