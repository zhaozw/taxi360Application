package sg.edu.astar.ihpc.taxidriver.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import sg.edu.astar.ihpc.taxidriver.R;
import sg.edu.astar.ihpc.taxidriver.entity.Location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlacesAdapter extends ArrayAdapter<Location>{
	private List<Location> request;
	private int layoutResourceId;
	private Context context;
	private Geocoder geocoder;
	
	private List<Address> addresses;
	public PlacesAdapter(Context context, int layoutResourceId, List<Location> request) {
		super(context, layoutResourceId, request);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.request = request;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RequestHolder holder = null;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);
		holder = new RequestHolder();
		holder.request = request.get(position);
		holder.name = (TextView)row.findViewById(R.id.des);
		row.setTag(holder);
		setupItem(holder);
		return row;
	}
private void setupItem(RequestHolder holder) {
		
		try {
			geocoder = new Geocoder(this.context,Locale.getDefault());
			addresses = geocoder.getFromLocation(holder.request.getLatitude(),holder.request.getLongitude(), 1);
			holder.name.setText(addresses.get(0).getAddressLine(0).toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	public static class RequestHolder {
		Location request;
		TextView name;
	}
}
