package bus.u.gpsudh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnlogin = findViewById(R.id.btnlogin);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        btnlogin.setOnClickListener(view -> LoginUser());
    }

    private void LoginUser() {
        UserApi userApi = RetrofitClient.getRetrofitInstance().create(UserApi.class);
        UserModel user = new UserModel(username.getText().toString(),password.getText().toString());
        Call<UserResponse> call = userApi.getUserLogin(user);
        call.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                assert response.body() != null;

                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "El usuario y contraseña son obligatorios ", Toast.LENGTH_SHORT).show();
                }else{
                    if(response.body().getStatus().equals("success")){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class)
                                .putExtra("username",response.body().getUsername());
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
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