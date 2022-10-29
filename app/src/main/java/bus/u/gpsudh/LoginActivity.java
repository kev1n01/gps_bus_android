package bus.u.gpsudh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import bus.u.gpsudh.user.UserApi;
import bus.u.gpsudh.user.UserModel;
import bus.u.gpsudh.user.UserResponse;
import bus.u.gpsudh.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button btnlogin;
    private TextView link_register;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_GPSUDH);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        validateSession();
        btnlogin.setOnClickListener(view -> LoginUser());
        link_register.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    public void init(){
        btnlogin = findViewById(R.id.btnlogin);
        link_register = findViewById(R.id.link_register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        preferences = getSharedPreferences("preferences",MODE_PRIVATE);
    }

    public boolean validateFields(String username, String passsword){
        if (username.isEmpty() || passsword.isEmpty()){
            Toast.makeText(this,"Los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;//pasa la validacion
    }

    private void validateSession(){
        String username  = preferences.getString("username",null);
        String type  = preferences.getString("type",null);
        int user_id = preferences.getInt("user_id",0);
        Toast.makeText(this, user_id + " " + username + " " + type, Toast.LENGTH_SHORT).show();

        if (username != null && type != null){
            goToMap();
        }
    }

    private void goToMap(){
        Intent intent = new Intent(LoginActivity.this, MapActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void LoginUser() {
        boolean validateUI = validateFields(username.getText().toString(),password.getText().toString());
        if (validateUI){
            UserApi userApi = RetrofitClient.getRetrofitInstance().create(UserApi.class);
            UserModel user = new UserModel(username.getText().toString(),password.getText().toString());
            Call<UserResponse> call = userApi.getUserLogin(user);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                    assert response.body() != null;
                    if(response.body().getStatus().equals("success")){
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("user_id", response.body().getId());
                        editor.putString("username",response.body().getUsername());
                        editor.putString("type",response.body().getType());
                        editor.apply();
                        goToMap();
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("user", response.body().getUsername() +  " " +  response.body().getId()+ " " +  response.body().getType());
                }
                @Override
                public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                    Log.e("user", "onFailure: "+t.getMessage());
                }
            });
        }
    }
    //llamar metodo despues del init en oncreate
    private void validateConnectivityInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            Toast.makeText(this, "Conectado", Toast.LENGTH_SHORT).show();
        }
    }
}