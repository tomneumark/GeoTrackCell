package geotrack.bstarsoftware.com.geotrack;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import geocell.bstarsoftware.com.geotrack.R;

import static android.widget.Toast.makeText;


public class StartActivity extends Activity implements LocationListener {

    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private ConnectivityManager connectivityManager;

    private Button startButton;
    private TextView phoneIdTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView timeTextView;

    private boolean isStarted = false;
    private String deviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Instantiate references to services
        telephonyManager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //Initialize variables
        deviceId = telephonyManager.getDeviceId();

        //Setup GUI
        startButton = (Button)findViewById(R.id.startButton);
        phoneIdTextView = (TextView)findViewById(R.id.phoneId);
        phoneIdTextView.setText("Cell Phone Id: " + deviceId);
        latitudeTextView = (TextView)findViewById(R.id.latitude);
        longitudeTextView = (TextView)findViewById(R.id.longitude);
        timeTextView = (TextView)findViewById(R.id.time);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            makeText(this, "You selected the menu", Toast.LENGTH_LONG);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStartClicked(View view) {
        if (this.isStarted) {
            startButton.setText("Start");
            this.isStarted = false;
            locationManager.removeUpdates(this);
        }
        else {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            locationManager.requestLocationUpdates(getMinTime(), 0f, criteria, this, null);
            startButton.setText("Stop");
            this.isStarted = true;
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d("gps", location.toString());
        latitudeTextView.setText("Lat: " + location.getLatitude());
        longitudeTextView.setText("Long: " + location.getLongitude());
        timeTextView.setText("Time: " + location.getTime());
        NetworkUtility.sendUpdate(connectivityManager, location, deviceId);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }public long getMinTime() {
        return 2000L;
    }


}
