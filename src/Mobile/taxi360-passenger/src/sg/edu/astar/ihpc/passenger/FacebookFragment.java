/*This checks for the facebook login part of the application
/** Author :Hemanth **/
package sg.edu.astar.ihpc.passenger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.Passenger;
import sg.edu.astar.ihpc.passenger.util.CheckConnectivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class FacebookFragment extends Fragment {
	private static final String TAG = "FacebookFragment";
	private Session s1;
	Passenger passenger;
	LoginButton authButton;
	CheckConnectivity connectivity;
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		view = inflater.inflate(R.layout.activitymain, container, false);
		authButton = (LoginButton) view.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.callOnClick();
		connectivity = new CheckConnectivity();
		authButton.setReadPermissions(Arrays.asList("email", "user_status"));

		return view;
	}

	/* This is called whenever the session parameters changes for facebook */
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		s1 = session;
		final Context context = this.getActivity();
		if (state.isOpened()) {
			LoginButton btn = (LoginButton) view.findViewById(R.id.authButton);
			btn.setVisibility(View.GONE);
			System.out.println("Token=" + session.getAccessToken());
			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							// If the response is successful
							if (s1 == Session.getActiveSession()) {
								if (user != null) {
									Log.d("username", user.getName());
									passenger = createUser(user);
									if (isUserregistered(passenger)) {
										Log.d("insideif", passenger.getName());
										passenger.setRelocated(false);
										Intent intent = new Intent(context,
												MainActivity.class);
										intent.putExtra("Title", passenger);
										startActivity(intent);
									} else {
										Intent intent = new Intent(context,
												RegisterPhoneActivity.class);
										Log.d("insidenewif",
												passenger.getName());
										passenger.setRelocated(false);
										intent.putExtra("Title", passenger);
										startActivity(intent);
									}
								}
							}
							if (response.getError() != null) {

								Log.d("username", response.getError()
										.toString());
								// Handle errors, will do so later.
							}
						}
					});
			request.executeAsync();

			Log.i(TAG, "Logged in...");
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	// These are the methods helping in implementing facebook session

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private UiLifecycleHelper uiHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	// This method is used to check whether the user is already registered in
	// the server
	public boolean isUserregistered(Passenger pass) {
		String responses = "false";
		String URL = "http://137.132.247.133:8080/taxi360-war/api/passenger/accesscontrol/login";
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
																				// Limit
		HttpResponse response = null;
		JSONObject json = new JSONObject();

		try {
			HttpPost post = new HttpPost(URL);
			json.put("name", pass.getName());
			json.put("emailid", pass.getEmailid());
			json.put("registrationtype", pass.getRegistrationtype());
			StringEntity se = new StringEntity(json.toString());
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post);
			StringBuilder stringBuilder = new StringBuilder();
			/* Checking response */
			if (response != null) {
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				in.close();// Get the data in the entity

				responses = stringBuilder.toString();
				Log.d("Insideis registeredrespone", responses);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		if ((response.getStatusLine().getStatusCode()) == HttpStatus.SC_OK)
			this.getActivity().finish();

		if (responses.equals("0")) {

			return false;
		} else {
			passenger.setId(Long.parseLong(responses));
			return true;
		}

	}

	// This is used to create the passenger object on login
	private Passenger createUser(GraphUser user) {
		// TODO Auto-generated method stub
		Passenger temp = new Passenger();
		temp.setName(user.getName());
		temp.setRegistrationtype("facebook");

		temp.setUsername(user.getUsername());
		try {
			if (user.asMap().get("email").toString() != null)
				temp.setEmailid(user.asMap().get("email").toString());
		} catch (Exception e) {
			Log.d("error", e.getMessage().toString());
		}
		Log.d("insidecreate", temp.getName());
		return temp;
	}

}