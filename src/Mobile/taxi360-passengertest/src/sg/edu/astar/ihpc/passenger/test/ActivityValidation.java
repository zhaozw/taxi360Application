package sg.edu.astar.ihpc.passenger.test;

import sg.edu.astar.ihpc.passenger.LoginView;
import sg.edu.astar.ihpc.passenger.MainActivity;
import sg.edu.astar.ihpc.passenger.Passenger;
import android.content.Intent;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.widget.Button;

public class ActivityValidation extends
    android.test.ActivityUnitTestCase<MainActivity> {

  private int buttonId;
  private MainActivity activity;

  public ActivityValidation() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Passenger passenger=new Passenger();
    passenger.setName("test");
    Intent intent = new Intent(getInstrumentation().getTargetContext(),
    		MainActivity.class);
    intent.putExtra("Title",passenger);
    startActivity(intent,null,null);
    activity = getActivity();
    
  }
  
  public void testLayout() {
	    buttonId = sg.edu.astar.ihpc.passenger.R.id.button1;
	    assertNotNull(activity.findViewById(buttonId));
	    Button view = (Button) activity.findViewById(buttonId);
	    assertEquals("Need Taxi",view.getText().toString());
	    
	    
	  }
  public void testRequest(){
	  buttonId = sg.edu.astar.ihpc.passenger.R.id.button1;
	    assertNotNull(activity.findViewById(buttonId));
	    Button view = (Button) activity.findViewById(buttonId);
	    assertEquals("Need Taxi",view.getText().toString());
	    view.callOnClick();
	    assertEquals("Cancel Request",view.getText().toString());
	    
	  
  }
  
  

} 
