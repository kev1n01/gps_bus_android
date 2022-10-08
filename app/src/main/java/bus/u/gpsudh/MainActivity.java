package bus.u.gpsudh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    //references to id elements interface
    TextView latitude, longitude;
    Button btn_showMap;
    //location request: config file for all setting related to FusedLocationProviderClient
    LocationRequest locationRequest;
    //API google service location
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get elementos ui into variables value
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        btn_showMap = findViewById(R.id.btn_showMap);

        //set all properties of LocationRequest
        locationRequest = LocationRequest.create()
                .setInterval(1000 * 30)
                .setFastestInterval(1000 * 5)
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setMaxWaitTime(100);

        btn_showMap.setOnClickListener(view -> {
            longitude.getText();
            Intent i = new Intent(MainActivity.this, MapActivity.class )
                    .putExtra("latitude", Double.parseDouble(latitude.getText().toString()))
                    .putExtra("longitude", Double.parseDouble(longitude.getText().toString()))
                    ;
            startActivity(i);
        });
        //Logic for updating coordinates with switch or button manually
//        sw_gps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (sw_gps.isChecked()){
//                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                }else{
//                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//                }
//            }
//        });
        updateCoordinates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateCoordinates();
            } else {
                Toast.makeText(this, "Esta app requiere de permisos", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateCoordinates(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, this::updateVariableValues);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateVariableValues(Location location) {
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
//        accurary.setText(String.valueOf(location.getAccuracy()));

//        if(location.hasAltitude()){
//            altitude.setText(String.valueOf(location.getAltitude()));
//        }else{
//            altitude.setText("No habilitado");
//        }
//        if(location.hasSpeed()){
//            speed.setText(String.valueOf(location.getSpeed()));
//        }else{
//            speed.setText("No habilitado");
//        }
    }
}
