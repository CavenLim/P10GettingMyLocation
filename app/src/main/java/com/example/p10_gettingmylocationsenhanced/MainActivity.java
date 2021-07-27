package com.example.p10_gettingmylocationsenhanced;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {
    private GoogleMap map;
    Button btnStartDetec,btnStopDetect,btnCheckRecords;
    ToggleButton btnMusic;
    TextView tvLat,tvLong;
    LatLng lastLoc;
    Marker central;


    FusedLocationProviderClient client ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMusic =  findViewById(R.id.toggleButton);
        btnStartDetec = (Button) findViewById(R.id.btnStartLocDetect);
        btnStopDetect = (Button) findViewById(R.id.btnStopLocDetect);
        btnCheckRecords = (Button) findViewById(R.id.btnCheckRecords);

        tvLat = (TextView) findViewById(R.id.tvLatitude);
        tvLong = (TextView) findViewById(R.id.tvLongitude);
        client =  LocationServices.getFusedLocationProviderClient(this);

        String folderLocation = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Folder";
        File folder = new File(folderLocation);
        if (folder.exists() == false) {
            boolean result = folder.mkdir();
            if (result == true) {
                Log.d("File Read/Write", "Folder created");
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);

        int permissionCheck = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
           Toast.makeText(getApplicationContext(),"No permission",Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0 );

        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap ;

                if (map != null){
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                checkPermission();

                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            map.clear();
                            tvLat.setText(location.getLatitude()+"");
                            tvLong.setText(location.getLongitude()+"");

                            lastLoc =  new LatLng( location.getLatitude(),location.getLongitude());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLoc,11));
                            central = map.addMarker(new MarkerOptions()
                                    .position(lastLoc)
                                    .title("Last Location")
                                    .snippet("user's last location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                            );
                        }
                        else{
                            String msg = "No Last Known Location found";
                            Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Toast.makeText(MainActivity.this,marker.getTitle(),Toast.LENGTH_SHORT).show();


                        return false;
                    }
                });

                UiSettings ui = map.getUiSettings();

                ui.setCompassEnabled(true);
                ui.setZoomControlsEnabled(true);


                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PermissionChecker.PERMISSION_GRANTED){
                    map.setMyLocationEnabled(true);

                }
                else{
                    Log.e("Gmap-Permission","Gps access has not been granted");
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0 );
                }



            }
        });
        btnStartDetec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Intent i = new Intent(MainActivity.this, LocationService.class);
                startService(i);


            }
        });
        btnStopDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LocationService.class);
                stopService(i);

            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnMusic.isChecked()){
                    btnMusic.setTextOn("Music Off");
                    Intent intent =  new Intent(MainActivity.this,MyService.class);
                    startService(intent);
                }
                else{
                    btnMusic.setTextOff("Music On");
                    Intent intent =  new Intent(MainActivity.this,MyService.class);
                    stopService(intent);
                }

            }
        });
        btnCheckRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MainActivity.this,ListTracked.class);
                startActivity(newIntent);
            }
        });

    }
    public boolean checkPermission(){

        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0 );
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

            return false;
        }
    }
}