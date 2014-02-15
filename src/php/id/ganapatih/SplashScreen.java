package php.id.ganapatih;

import helper.peopleData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        peopleData p = new peopleData(getApplicationContext());
        
        if(p.isLogin()){
        	startActivity(new Intent(SplashScreen.this, Home.class));
        	finish();
        }else{
        	setContentView(R.layout.activity_splash_screen);
        	
			Thread timer = new Thread() {
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						startActivity(new Intent(SplashScreen.this,
								Welcome.class));
						finish();
					}
				}
			};
			timer.start();
        }
    }   
}
