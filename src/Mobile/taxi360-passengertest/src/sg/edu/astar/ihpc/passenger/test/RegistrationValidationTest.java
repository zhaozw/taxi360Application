package sg.edu.astar.ihpc.passenger.test;

import sg.edu.astar.ihpc.passenger.MainActivity;
import sg.edu.astar.ihpc.passenger.OTP;
import sg.edu.astar.ihpc.passenger.Passenger;
import sg.edu.astar.ihpc.passenger.RegisterPhoneActivity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationValidationTest extends
    android.test.ActivityUnitTestCase<RegisterPhoneActivity> {

  private int buttonId;
  private int buttonId2;
  private int text1;
  private int text2;
  private int error;
  private RegisterPhoneActivity activity;

  public RegistrationValidationTest() {
    super(RegisterPhoneActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Passenger passenger=new Passenger();
    passenger.setName("test");
    passenger.setPassengerid("345");
    
    Intent intent = new Intent(getInstrumentation().getTargetContext(),
    		RegisterPhoneActivity.class);
    intent.putExtra("Title",passenger);
    startActivity(intent,null,null);
    activity = getActivity();
    
  }
  
  public void testLayout() {
	    buttonId = sg.edu.astar.ihpc.passenger.R.id.register;
	    assertNotNull(activity.findViewById(buttonId));
	    Button view = (Button) activity.findViewById(buttonId);
	    assertEquals("Send OTP",view.getText().toString());
	    
	    
	  }
  public void testRequest(){
	  buttonId = sg.edu.astar.ihpc.passenger.R.id.register;
	  buttonId2 = sg.edu.astar.ihpc.passenger.R.id.validate;
	  
	  text1=sg.edu.astar.ihpc.passenger.R.id.number;
	    assertNotNull(activity.findViewById(buttonId));
	    Button view = (Button) activity.findViewById(buttonId);
	    Button view2 = (Button) activity.findViewById(buttonId2);
	    EditText phonenumber=(EditText) activity.findViewById(text1);
	    assertEquals("Send OTP",view.getText().toString());
	    assertTrue(view.isEnabled());
	    assertFalse(view2.isEnabled());
	    view.callOnClick();
	    assertEquals("Register",view2.getText().toString());
	    assertTrue(view.isEnabled());
	    assertFalse(view2.isEnabled());
	    phonenumber.setText("93970135");
	    view.callOnClick();
	    assertEquals("Register",view2.getText().toString());
	    assertFalse(view.isEnabled());
	    assertTrue(view2.isEnabled());
	    
	  
  }
  
  public void testvalidate(){
	  buttonId = sg.edu.astar.ihpc.passenger.R.id.register;
	  buttonId2 = sg.edu.astar.ihpc.passenger.R.id.validate;
	  
	  text1=sg.edu.astar.ihpc.passenger.R.id.number;
	  
	  text2=sg.edu.astar.ihpc.passenger.R.id.otp;
	  error=sg.edu.astar.ihpc.passenger.R.id.error;
	    assertNotNull(activity.findViewById(buttonId));
	    Button view = (Button) activity.findViewById(buttonId);
	    Button view2 = (Button) activity.findViewById(buttonId2);
	    EditText phonenumber=(EditText) activity.findViewById(text1);
	    EditText otp=(EditText) activity.findViewById(text2);
	    TextView errors=(TextView)activity.findViewById(error);
	    assertEquals("Send OTP",view.getText().toString());
	    assertTrue(view.isEnabled());
	    assertFalse(view2.isEnabled());
	    view.callOnClick();
	    assertEquals("Register",view2.getText().toString());
	    assertTrue(view.isEnabled());
	    assertFalse(view2.isEnabled());
	    phonenumber.setText("93970135");
	    view.callOnClick();
	    assertEquals("Register",view2.getText().toString());
	    assertFalse(view.isEnabled());
	    assertTrue(view2.isEnabled());
	    view2.callOnClick();
	    assertEquals("Invalid OTP.Please retry", errors.getText().toString());
	    Log.d("otpnew",activity.otp.getOtp().toString());
	    
	    
	  
  }
  
  

} 

