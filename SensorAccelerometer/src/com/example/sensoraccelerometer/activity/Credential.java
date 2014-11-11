package com.example.sensoraccelerometer.activity;


import java.util.Set;

import com.example.sensoraccelerometer.classes.BaseID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Credential extends Activity {

	public static void saveCredentialData(Context context, String KEY, String value) {
		SharedPreferences credentialDataPref = context.getSharedPreferences(BaseID.KEY_PREFERENCE, MODE_PRIVATE);
		Editor editorPreference = credentialDataPref.edit();
		editorPreference.putString(KEY, value);
		editorPreference.commit();
	}

	public static String getCredentialData(Context context, String KEY) {
		String valueReturn = "";
		SharedPreferences credentialDataPref = context.getSharedPreferences(BaseID.KEY_PREFERENCE, MODE_PRIVATE);
		valueReturn = credentialDataPref.getString(KEY, "");
		return valueReturn;
	}
	
	public static void clearCredentialData(Context context, String StringKey){
		SharedPreferences credentialDataPref = context.getSharedPreferences(BaseID.KEY_PREFERENCE, MODE_PRIVATE);
		Editor editorPreference = credentialDataPref.edit();
		editorPreference.remove(StringKey);
		editorPreference.commit();
	}
}
