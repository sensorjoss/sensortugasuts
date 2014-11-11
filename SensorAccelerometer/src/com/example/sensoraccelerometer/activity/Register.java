package com.example.sensoraccelerometer.activity;

import com.example.sensoraccelerometer.R;
import com.example.sensoraccelerometer.activity.Credential;
import com.example.sensoraccelerometer.classes.BaseID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity {

	private EditText editName;
	private Button start;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_register);
		
		editName = (EditText) findViewById(R.id.editName);
		start = (Button) findViewById(R.id.buttonStart);
		
		start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (editName.getText().toString().trim().equals("")) {
					editName.setError("Lengkapi dahulu " + editName.getHint());
				}
				else{
					Credential.saveCredentialData(getApplicationContext(),BaseID.KEY_NAME, editName.getText().toString());
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
				}
			}
		});
	}
}
