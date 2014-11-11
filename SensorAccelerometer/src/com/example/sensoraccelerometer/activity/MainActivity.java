package com.example.sensoraccelerometer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.sensoraccelerometer.R;
import com.example.sensoraccelerometer.classes.BaseID;

import android.os.Environment;

public class MainActivity extends Activity implements SensorEventListener,
		LocationListener {

	File root = Environment.getExternalStorageDirectory();
	File save1;
	String nameUser;
	ArrayList<String> tampung = new ArrayList<String>();
	Double latData, longData;
	Double latNew=-0.0, longNew=-0.0;

	// ==========================================
	double pJalan = 0.49862259;
	double pDuduk = 0.187327824;
	double pBerdiri = 0.314049587;
	double rXJalan = 2.27206301;
	double rYJalan = 10.24030494;
	double rZJalan = 2.106537725;
	double vXJalan = 1.021679418;
	double vYJalan = 1.794924741;
	double vZJalan = 2.301824894;
	double rXDuduk = 4.424428388;
	double rYDuduk = -0.309781528;
	double rZDuduk = 10.59473688;
	double vXDuduk = 0.765874846;
	double vYDuduk = 0.391099654;
	double vZDuduk = 0.27693526;
	double rXBerdiri = 1.058237148;
	double rYBerdiri = 9.461801055;
	double rZBerdiri = 3.978054439;
	double vXBerdiri = 0.449834651;
	double vYBerdiri = 0.341032646;
	double vZBerdiri = 0.537109722;

	double pXDuduk;
	double pYDuduk;
	double pZDuduk;
	double pXBerdiri;
	double pYBerdiri;
	double pZBerdiri;
	double pXJalan;
	double pYJalan;
	double pZJalan;
	double pnDuduk;
	double pnBerdiri;
	double pnJalan;
	String posisi;
	// ==========================================
	// Accelerometer X, Y, and Z values
	private TextView accelXValue;
	private TextView accelYValue;
	private TextView accelZValue;

	private TextView latValue;
	private TextView longValue;

	private String simpan;
	private int count;
	private float x;
	private float y;
	private float z;
	private SimpleDateFormat dateFormat;
	private Date date;
	private String waktu;

	private SensorManager sensorManager = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get a reference to a SensorManager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.activity_main);

		nameUser = Credential.getCredentialData(getApplicationContext(),
				BaseID.KEY_NAME);

		/* Use the LocationManager class to obtain GPS locations */
		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				this);

		count = 0;
		x = 0;
		y = 0;
		z = 0;

		dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH:mm:ss");
		date = new Date();
		waktu = dateFormat.format(date);

		// Capture accelerometer related view elements
		accelXValue = (TextView) findViewById(R.id.accel_x_value);
		accelYValue = (TextView) findViewById(R.id.accel_y_value);
		accelZValue = (TextView) findViewById(R.id.accel_z_value);

		latValue = (TextView) findViewById(R.id.latValue);
		longValue = (TextView) findViewById(R.id.longValue);

		// Initialize accelerometer related view elements
		accelXValue.setText("0.00");
		accelYValue.setText("0.00");
		accelZValue.setText("0.00");

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater
						.from(MainActivity.this);

				View promptView = layoutInflater.inflate(
						R.layout.activity_dialog, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						MainActivity.this);

				// set prompts.xml to be the layout file of the
				// alertdialog builder
				alertDialogBuilder.setView(promptView);

				final EditText namaFile = (EditText) promptView
						.findViewById(R.id.namaFile);
				Log.e("FILE", "MASUUUUK");
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Save",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// get user input and set it to
										// result
										if (namaFile.getText().toString()
												.trim().equals("")) {
											namaFile.setError("Field is required!");
										} else {
											Log.e("FILE", "MASUK ELSE SAVE");
											try {

												File directory = new File(root
														+ "/sensor/");
												/*
												 * if (!directory.exists()){
												 * directory.mkdirs(); } else
												 * Log.d("error",
												 * "dir. already exists");
												 */
												Log.e("FILE", namaFile
														.getText().toString());
												save1 = new File(directory,
														namaFile.getText()
																.toString()
																+ ".csv");
												FileOutputStream fOut = new FileOutputStream(
														save1);
												String header = "Waktu" + ","
														+ "Acc X" + ","
														+ "Acc Y" + ","
														+ "Acc Z" + "\n";
												fOut.write(header.getBytes());
												fOut.write(simpan.getBytes());
												fOut.flush();
												fOut.close();
												Toast.makeText(
														getApplicationContext(),
														"File Saved",
														Toast.LENGTH_SHORT)
														.show();
												simpan = "";
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
										simpan = "";
									}
								});

				// create an alert dialog
				AlertDialog alertD = alertDialogBuilder.create();
				alertD.show();
			}
		});

		Button kirim = (Button) findViewById(R.id.buttonKirim);
		kirim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(tampung.size()>0){
					new DataAsyncTask().execute();
					Log.e("TES", "MASUK");
				}else
					Log.e("TES", "GAK MASUK");
				// TODO Auto-generated method stub
				/*
				 * Intent intent = new Intent(MainActivity.this,
				 * DataInput.class);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * MainActivity.this.startActivity(intent);
				 */

				/*String name;
				try {
					name = URLEncoder.encode(Credential.getCredentialData(
							getApplicationContext(), BaseID.KEY_NAME), "UTF-8");
					String URL;
					Log.e("URLBEFORE", tampung.toString());
					// for(int i=0;i<1;i++){
					// URL = tampung.get(0);
					String nama = URLEncoder.encode(nameUser, "UTF-8");
					String latitut = URLEncoder.encode("13.2323", "UTF-8");
					String longitut = URLEncoder.encode("2.23131", "UTF-8");
					String status1 = URLEncoder.encode("duduk", "UTF-8");
					String timestamp = URLEncoder.encode("jam", "UTF-8");
					URL = "http://riset.ajk.if.its.ac.id/klp3/index.php/getdata/get?name="
							+ nama
							+ "&latitude="
							+ latitut
							+ "&longitude="
							+ longitut
							+ "&status="
							+ status1
							+ "&timestamp="
							+ timestamp;
					Log.e("DATAAA", URL);
					HttpClient Client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(URL);
					String SetServerString = "";
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					SetServerString = Client.execute(httpget, responseHandler);
					// }
					tampung.clear();
					Log.e("URLAFTER", "YEAH");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		});
	}

	// This method will update the UI on new sensor events
	public void onSensorChanged(SensorEvent sensorEvent) {
		synchronized (this) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				accelXValue.setText(Float.toString(sensorEvent.values[0]));
				accelYValue.setText(Float.toString(sensorEvent.values[1]));
				accelZValue.setText(Float.toString(sensorEvent.values[2]));

				x += sensorEvent.values[0];
				y += sensorEvent.values[1];
				z += sensorEvent.values[2];
				count++;
				if (count > 5) {
					x /= 5;
					y /= 5;
					z /= 5;

					// ========================================================
					pXDuduk = (1 / Math.sqrt(2 * 3.14 * vXDuduk))
							* Math.exp(-Math.pow(x - rXDuduk, 2)
									/ (2 * vXDuduk));
					pYDuduk = (1 / Math.sqrt(2 * 3.14 * vYDuduk))
							* Math.exp(-Math.pow(y - rYDuduk, 2)
									/ (2 * vYDuduk));
					pZDuduk = (1 / Math.sqrt(2 * 3.14 * vZDuduk))
							* Math.exp(-Math.pow(z - rZDuduk, 2)
									/ (2 * vZDuduk));

					pXJalan = (1 / Math.sqrt(2 * 3.14 * vXJalan))
							* Math.exp(-Math.pow(x - rXJalan, 2)
									/ (2 * vXJalan));
					pYJalan = (1 / Math.sqrt(2 * 3.14 * vYJalan))
							* Math.exp(-Math.pow(y - rYJalan, 2)
									/ (2 * vYJalan));
					pZJalan = (1 / Math.sqrt(2 * 3.14 * vZJalan))
							* Math.exp(-Math.pow(z - rZJalan, 2)
									/ (2 * vZJalan));

					pXBerdiri = (1 / Math.sqrt(2 * 3.14 * vXBerdiri))
							* Math.exp(-Math.pow(x - rXBerdiri, 2)
									/ (2 * vXBerdiri));
					pYBerdiri = (1 / Math.sqrt(2 * 3.14 * vYBerdiri))
							* Math.exp(-Math.pow(y - rYBerdiri, 2)
									/ (2 * vYBerdiri));
					pZBerdiri = (1 / Math.sqrt(2 * 3.14 * vZBerdiri))
							* Math.exp(-Math.pow(z - rZBerdiri, 2)
									/ (2 * vZBerdiri));

					pnDuduk = pXDuduk * pYDuduk * pZDuduk * pDuduk;
					pnBerdiri = pXBerdiri * pYBerdiri * pZBerdiri * pBerdiri;
					pnJalan = pXJalan * pYJalan * pZJalan * pJalan;

					if (pnDuduk > pnBerdiri && pnDuduk > pnJalan) {
						posisi = "duduk";
					}

					else if (pnBerdiri > pnDuduk && pnBerdiri > pnJalan) {
						posisi = "berdiri";
					}

					else {
						posisi = "berjalan";
					}
					// ========================================================

					/*
					 * simpan += nameUser+waktu + "," + Float.toString(xAxis) +
					 * "," + Float.toString(yAxis) + "," + Float.toString(zAxis)
					 * + "\n";
					 */
					
					if (latData != null && longData != null) {
						if(!latNew.equals(latData)&&!longNew.equals(longData)){
							latNew = latData;
							longNew = longData;
							tampung.add("http://riset.ajk.if.its.ac.id/klp3/index.php/getdata/get?name="
								+ nameUser
								+ "&latitude="
								+ latData
								+ "&longitude="
								+ longData
								+ "&status="
								+ posisi + "&timestamp=" + waktu);
						}
					}

					count = 0;
					x = 0;
					y = 0;
					z = 0;
				}
			}
		}
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		// ...and the orientation sensor
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// Unregister the listener
		sensorManager.unregisterListener(this);
		super.onStop();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latValue.setText(Double.toString(location.getLatitude()).toString());
		longValue.setText(Double.toString(location.getLongitude()).toString());

		latData = location.getLatitude();
		longData = location.getLongitude();

		// Toast.makeText(MainActivity.this,
		// Double.toString(location.getLatitude())+","+Double.toString(location.getLongitude()),
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		// Toast.makeText(MainActivity.this,
		// "Provider status changed",Toast.LENGTH_LONG).show();
		Log.d("Latitude", "status");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		// Toast.makeText(MainActivity.this,"Provider enabled by the user. GPS turned on",Toast.LENGTH_LONG).show();
		Log.d("Latitude", "enable");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		// Toast.makeText(MainActivity.this,"Provider disabled by the user. GPS turned off",Toast.LENGTH_LONG).show();
		Log.d("Latitude", "disable");
	}

	private class DataAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(MainActivity.this);
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
				HttpClient Client = new DefaultHttpClient();
				for(int i=0;i<tampung.size();i++){
					HttpGet httpget = new HttpGet(tampung.get(i));
					String SetServerString = "";
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					SetServerString = Client.execute(httpget, responseHandler);
				}
				// Log.e("SERVERSTRING",SetServerString);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}

		protected void onPostExecute(String result) {
			pDialog.dismiss();
			Toast.makeText(getApplicationContext(), "Data Sent",Toast.LENGTH_SHORT).show();
			Intent i = new Intent(getApplicationContext(), Register.class);
			startActivity(i);
		}
	}
}