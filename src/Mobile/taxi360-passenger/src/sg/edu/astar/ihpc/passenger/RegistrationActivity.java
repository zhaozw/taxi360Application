package sg.edu.astar.ihpc.passenger;

/**
 The Screen  allows the user to register himself to the application
 **/
/** Author :Thilak **/

import sg.edu.astar.ihpc.passenger.R;
import sg.edu.astar.ihpc.passenger.entity.Passenger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class RegistrationActivity extends Activity {
	ProgressDialog dialog;
	TextView name;
	TextView email;
	TextView pass;
	TextView repass;

	Passenger passenger;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		dialog = new ProgressDialog(this);
		name = (TextView) findViewById(R.id.name);
		email = (TextView) findViewById(R.id.email);
		pass = (TextView) findViewById(R.id.password);
		repass = (TextView) findViewById(R.id.repassword);

		passenger = new Passenger();
		context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * The function allows the registration of the user along with validation of
	 * the fields
	 **/
	public void register_listener(View v) {
		email.setError(null);
		pass.setError(null);
		name.setError(null);
		repass.setError(null);

		final String mEmail = email.getText().toString();
		String mPassword = pass.getText().toString();
		final String mName = name.getText().toString();
		final String mRepass = repass.getText().toString();

		if (TextUtils.isEmpty(mName)) {
			name.setError("Name is required");
			name.requestFocus();
		} else if (TextUtils.isEmpty(mEmail)) {
			email.setError("Email is required");
			email.requestFocus();
		} else if (!mEmail.contains("@")) {
			email.setError("Invalid email id");
			email.requestFocus();
		} else if (TextUtils.isEmpty(mPassword)) {
			pass.setError("Password is required");
			pass.requestFocus();
		} else if (TextUtils.isEmpty(mRepass)) {
			repass.setError("please confirm password");
			repass.requestFocus();
		} else if (!mPassword.equals(mRepass)) {
			repass.setError("Password mismatch");
			repass.requestFocus();
		} else {

			passenger.setEmailid(mEmail + "," + mRepass);

			passenger.setName(mName);
			passenger.setPassword(mRepass);

			Intent intent = new Intent(context, RegisterPhoneActivity.class);

			passenger.setRelocated(false);
			intent.putExtra("Title", passenger);
			startActivity(intent);
		}

	}

	void show() {
		dialog.setMessage("Registering for you");
		dialog.show();
	}

	void hide() {
		dialog.dismiss();
	}
}
