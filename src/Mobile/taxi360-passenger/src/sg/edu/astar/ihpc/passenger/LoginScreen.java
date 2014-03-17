package sg.edu.astar.ihpc.passenger;

import sg.edu.astar.ihpc.passenger.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class LoginScreen extends Activity {
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_screen, menu);
		return true;
	}

	public void register(View v) {

		Intent intent = new Intent(context, RegistrationActivity.class);

		startActivity(intent);
	}

	public void login(View v) {
		Intent intent = new Intent(context, LoginActivity.class);
		startActivity(intent);
	}

	public void loginviafacebook(View v) {
		Intent intent = new Intent(context, LoginView.class);
		startActivity(intent);
	}
}