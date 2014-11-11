package com.example.sensoraccelerometer.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.example.sensoraccelerometer.R;
import com.example.sensoraccelerometer.classes.BaseID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DataInput extends Activity{
	private EditText name,latitude,longitude,status,tgl;
	private Button send;
	Context context;
	//private static String url = "http://tugasakhir015.vv.si/sensor/index.php/getdata/get";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datainput);
        
        JSONParser jParser = new JSONParser();
        
        latitude = (EditText) findViewById(R.id.editLat);
        longitude = (EditText) findViewById(R.id.editLong);
        status = (EditText) findViewById(R.id.editStatus);
        name = (EditText) findViewById(R.id.editName);
        tgl = (EditText) findViewById(R.id.editTanggal);
        send = (Button) findViewById(R.id.buttonKirim);
        
        name.setText(Credential.getCredentialData(getApplicationContext(), BaseID.KEY_NAME));
        send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DataAsyncTask().execute();
			}
		});
        
	}
	
	private class DataAsyncTask extends AsyncTask<String, Void, String>{
		ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(DataInput.this);
			pDialog.setMessage("Please wait...");
			pDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			InputStream inputStream = null;
	        String result = "";
	        // URLEncode user defined data
	        
            String name;
			try {
				name = URLEncoder.encode(Credential.getCredentialData(getApplicationContext(), BaseID.KEY_NAME),"UTF-8");
				 String latitut   = URLEncoder.encode(latitude.getText().toString(), "UTF-8");
		         String longitut   = URLEncoder.encode(longitude.getText().toString(), "UTF-8");
		         String status1    = URLEncoder.encode(status.getText().toString(), "UTF-8");
		         String timestamp    = URLEncoder.encode(tgl.getText().toString(), "UTF-8");
		         String URL = "http://riset.ajk.if.its.ac.id/klp3/index.php/getdata/get?name="+name+"&latitude="+latitut+"&longitude="+longitut+"&status="+status1+"&timestamp="+timestamp;
		         HttpClient Client = new DefaultHttpClient();
		         HttpGet httpget = new HttpGet(URL);
		         String SetServerString = "";
		         ResponseHandler<String> responseHandler = new BasicResponseHandler();
                 SetServerString = Client.execute(httpget, responseHandler);
                 //Log.e("SERVERSTRING",SetServerString);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
	        return result;
	 
		}
		
		protected void onPostExecute(String result)
		{
			pDialog.dismiss();
			Toast.makeText(getApplicationContext(), "Data Sent", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(),Register.class);
			startActivity(i);
		}
	}
}
