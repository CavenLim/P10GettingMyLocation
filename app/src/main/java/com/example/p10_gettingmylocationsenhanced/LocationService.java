package com.example.p10_gettingmylocationsenhanced;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileWriter;

public class LocationService extends Service {
    boolean started;

    LocationRequest locationRequest = LocationRequest.create();

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                Location data = locationResult.getLastLocation();
                double lat = data.getLatitude();
                double lng = data.getLongitude();
                String msg = "Lat:" + lat + " Lng:"+ lng;
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();




                LatLng newloc = new LatLng(data.getLatitude(), data.getLongitude());

                try {
                    String folderLocation_I = getFilesDir().getAbsolutePath() + "/Folder";
                    File targetFile_I = new File(folderLocation_I, "location.txt");
                    FileWriter writer_I = new FileWriter(targetFile_I, true); writer_I.write(newloc.latitude+","+newloc.longitude + "\n");
                    writer_I.flush();
                    writer_I.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to write!", Toast.LENGTH_LONG).show();
                    e.printStackTrace(); }




            }

        };



    };



    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (started == false) {
            started = true;

            checkPermission();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(3000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setSmallestDisplacement(0);
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient( getApplicationContext());

            client.requestLocationUpdates(locationRequest, mLocationCallback, null);

        } else {
            Log.d("MyService", "Service is still running");
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public boolean checkPermission(){

        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public void onDestroy() {
        Log.d("MyService", "Service exited");
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient( getApplicationContext());

        client.removeLocationUpdates(mLocationCallback);

        super.onDestroy();
    }
}