package com.example.sensoraccelerometer.activity;

import com.example.sensoraccelerometer.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class Splashscreen extends Activity{

	private static int TIME = 2000;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_splash);
		
		 new Handler().postDelayed(new Runnable() {
			 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(Splashscreen.this, Register.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, TIME);
	}
	
	@Override public void onBackPressed() { 
		this.finish(); 
		super.onBackPressed(); 
	}
	
}
