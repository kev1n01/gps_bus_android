package bus.u.gpsudh;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import bus.u.gpsudh.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Bundle dataExtras = getIntent().getExtras();
        double latitude = dataExtras.getDouble("latitude");
        double longitude = dataExtras.getDouble("longitude");
        // Add a marker in Sydney and move the camera
        LatLng my_house = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(my_house).title("Mi jato")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        LatLng bus1 = new LatLng(-9.9139649, -76.2300533);
        mMap.addMarker(new MarkerOptions().position(bus1).title("A4S-W1")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        LatLng bus2 = new LatLng(-9.9219209, -76.2334199);
        mMap.addMarker(new MarkerOptions().position(bus2).title("12V-AS2")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        LatLng hco = new LatLng(-9.9207648, -76.2410843);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hco));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hco, 14.0f));
    }
}