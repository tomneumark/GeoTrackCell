package geotrack.bstarsoftware.com.geotrack;

import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by Tom on 2/23/2015.
 */
public class NetworkUtility {
    public static void sendUpdate(ConnectivityManager connectivityManager, Location location, String deviceId) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() ) {
            Log.d("net", "Starting UpdateGPSTask()");
            new UpdateGPSTask().execute(new UpdateMessage(deviceId, location));
        }
        else {
            Log.d("net", "NetworkInfo not available");
        }
    }

    private static class UpdateGPSTask extends AsyncTask<UpdateMessage, Void, Void> {
        @Override
        protected Void doInBackground(UpdateMessage... message) {
            try {
                update(message[0]);
            }
            catch (IOException e) {
                Log.d("net", e.getMessage());
            }
            return null;
        }

        private void update(UpdateMessage message) throws IOException {
            try {
                Log.d("updateGPS", "About to update with message: " + message);
                Socket socket = new Socket("10.0.0.4", 8090);
                ByteBuffer buffer = ByteBuffer.allocate(32);
                buffer.putLong(message.getDeviceId().hashCode());
                buffer.putLong(message.getLocation().getTime());
                buffer.putDouble(message.getLocation().getLatitude());
                buffer.putDouble(message.getLocation().getLongitude());
                socket.getOutputStream().write(buffer.array());
                socket.getOutputStream().flush();
                socket.close();
                Log.d("updateGPS", "Wrote date for message: " + message);
            }
            catch (Exception e) {
                Log.e("updateGPS", e.getMessage());
            }
        }

    }
}
