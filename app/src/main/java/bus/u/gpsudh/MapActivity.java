package bus.u.gpsudh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;


import bus.u.gpsudh.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    // Estado del Settings de verificación de permisos del GPS
    private static final int REQUEST_CHECK_SETTINGS = 102;
    FusedLocationProviderClient fusedLocationClient;
    // La clase LocationCallback se utiliza para recibir notificaciones de FusedLocationProviderApi
    // cuando la ubicación del dispositivo ha cambiado o ya no se puede determinar.
    LocationCallback mlocationCallback;
    // La clase LocationSettingsRequest.Builder extiende un Object
    // y construye una LocationSettingsRequest.
    LocationSettingsRequest.Builder builder;
    // La clase LocationRequest sirve para  para solicitar las actualizaciones
    // de ubicación de FusedLocationProviderApi
    LocationRequest mLocationRequest;
    // Marcador para la ubicación del usuario
    Marker marker;
    // Mapa de Google
    private  String username;
    private String type;
    private int user_id;
    private int student_id;
    LocationManager locationManager;
    private GoogleMap mMap;
    private SharedPreferences preferences;
    private Button btnlogout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMapBinding binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        getCoordinateDevice();
        btnlogout.setOnClickListener(view -> closeSession());

        username  = preferences.getString("username", null);
        type  = preferences.getString("type",null);
        user_id = preferences.getInt("user_id",0);
        Toast.makeText(this, user_id + " " + username + " " + type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) { mMap = googleMap; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // Se cumplen todas las configuraciones de ubicación.
                // La aplicación envía solicitudes de ubicación del usuario.
                startUpdateLocation();
            } else {
                checkLocationSetting(builder);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    
    public void init(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        btnlogout = findViewById(R.id.btnlogout);
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
    //Relizar petición post la API para el guardado de la coordenadas, según el tipo de usuario
    private void saveDataForTypeUser(double lat, double lng) {
        if (type.equals("student")){
            
        }
    }

    private void getCoordinateDevice() {

        // verifica si el opcion de ubicacion esta activo
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLastLocation();
            // Con LocationCallback enviamos notificaciones de la ubicación del usuario
            mlocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    // Cuando obtenemos la coordenadas de ubicación del usuario, agregamos
                    // un marcador para la ubicación del usuario con el método agregarMarcador()
                    for (Location location : locationResult.getLocations()) {
                        saveDataForTypeUser(location.getLatitude(),location.getLongitude());
                        addMark(location.getLatitude(), location.getLongitude(), username);
                        Log.e("Coordenates user: ", location.toString());
                    }
                }
            };

            // Obtenemos actualizaciones de la ubicación del usuario
            mLocationRequest = createLocationRequest();

            // Construimos un LocationSettingsRequest
            builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);

            // Verificamos la configuración de los permisos de ubicación
            checkLocationSetting(builder);
        }else{
            //sino muestra una alerta de que
            showAlertMessageLocationDisable();
        }
    }

    private void closeSession(){

        preferences.edit().clear().apply();
        goToLogin();
    }

    private void goToLogin(){
        Intent intent = new Intent(MapActivity.this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void showAlertMessageLocationDisable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ubicación del dispositivo desactivado,activalo primero para continuar :3");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
            LatLng coordenatesHCO = new LatLng(-9.9109609,-76.2407916);
            CameraUpdate cameraHCO = CameraUpdateFactory.newLatLngZoom(coordenatesHCO, 14);
            mMap.animateCamera(cameraHCO);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    
    private void addMark(double lat, double lng, String title) {
        LatLng coordinates = new LatLng(lat, lng);
        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinates, 16);
        if (marker != null) marker.remove();
        marker = mMap.addMarker(new MarkerOptions().position(coordinates).title(title));
        mMap.animateCamera(myLocation);
    }

    private void getLastLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // muestra una ventana o Dialog en donde el usuario debe
                // dar permisos para el uso del GPS de su dispositivo.
                requestDialogPermissionGps();
            }
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setSmallestDisplacement(30);
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void checkLocationSetting(LocationSettingsRequest.Builder builder) {
        builder.setAlwaysShow(true);
        // Dentro de la variable 'cliente' iniciamos LocationServices, para los servicios de ubicación
        SettingsClient cliente = LocationServices.getSettingsClient(this);
        // Creamos una task o tarea para verificar la configuración de ubicación del usuario
        Task<LocationSettingsResponse> task = cliente.checkLocationSettings(builder.build());
        // Adjuntamos OnSuccessListener a la task o tarea
        task.addOnSuccessListener(this, locationSettingsResponse -> {
            // Si la configuración de ubicación es correcta,
            // se puede iniciar solicitudes de ubicación del usuario
            startUpdateLocation();
        });
        // Adjuntamos addOnCompleteListener a la task para gestionar si la tarea se realiza correctamente
        task.addOnCompleteListener(task1 -> {
            try {
                LocationSettingsResponse response = task1.getResult(ApiException.class);
                // En try podemos hacer 'algo', si la configuración de ubicación es correcta,
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // La configuración de ubicación no está satisfecha.
                        // Le mostramos al usuario un diálogo de confirmación de uso de GPS.
                        try {
                            // Transmitimos a una excepción resoluble.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Mostramos el diálogo llamando a startResolutionForResult()
                            // y es verificado el resultado en el método onActivityResult().
                            resolvable.startResolutionForResult(MapActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignora el error.
                        } catch (ClassCastException e) {
                            // Ignorar, aca podría ser un error imposible.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // La configuración de ubicación no está satisfecha
                        // podemos hacer algo.
                        break;
                }
            }
        });
    }

    public void startUpdateLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        // Obtenemos la ubicación más reciente
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mlocationCallback, null);
    }

    private void requestDialogPermissionGps(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }
}