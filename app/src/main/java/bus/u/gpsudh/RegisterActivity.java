package bus.u.gpsudh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bus.u.gpsudh.student.StudentApi;
import bus.u.gpsudh.student.StudentModel;
import bus.u.gpsudh.student.StudentResponse;
import bus.u.gpsudh.user.UserApi;
import bus.u.gpsudh.user.UserModel;
import bus.u.gpsudh.user.UserResponse;
import bus.u.gpsudh.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText rg_username, rg_password;
    private Button rg_button;
    private TextView link_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);
        init();
        rg_button.setOnClickListener(view -> RegisterUser());
        link_login.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));
    }

    public void init(){
        rg_button = findViewById(R.id.rg_button);
        rg_username = findViewById(R.id.rg_username);
        rg_password = findViewById(R.id.rg_password);
        link_login = findViewById(R.id.link_login);
    }

    public boolean validateFields(String username, String passsword){
        if (username.isEmpty() || passsword.isEmpty()){
            Toast.makeText(this,"Los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return  false;
        }else if(username.length()>30 || passsword.length()>8){
            Toast.makeText(this,"Ingrese usuario o contrasena valido (usuario min 30 y contrasena min 8 caracteres)", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;//pasa la validacion
    }

    private void RegisterUser() {
        boolean validateUI = validateFields(rg_username.getText().toString(),rg_password.getText().toString());
        if (validateUI){
            UserApi userApi = RetrofitClient.getRetrofitInstance().create(UserApi.class);
            UserModel user = new UserModel(rg_username.getText().toString(),rg_password.getText().toString());
            Call<UserResponse> call = userApi.RegisterUser(user);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                    assert response.body() != null;
                    RegisterStudent(response.body().getId());

                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    Log.e("user", response.body().getUsername() +  " " +  response.body().getId()+ " " +  response.body().getType());
                }

                @Override
                public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                    Log.e("user", "onFailure: "+t.getMessage());
                }
            });
        }
    }

    private void RegisterStudent(int user_id) {
        StudentApi studentApi = RetrofitClient.getRetrofitInstance1().create(StudentApi.class);
        StudentModel student = new StudentModel(user_id);
        Call<StudentResponse> studentResponseCall = studentApi.RegisterStudent(student);
        studentResponseCall.enqueue(new Callback<StudentResponse>() {
            @Override
            public void onResponse(@NonNull Call<StudentResponse> call, @NonNull Response<StudentResponse> response) {
                assert response.body() != null;
                Log.e("student", String.valueOf(response.body().getUser_id()));
            }

            @Override
            public void onFailure(@NonNull Call<StudentResponse> call, @NonNull Throwable t) {
                Log.e("student", "onFailure: "+t.getMessage());
            }
        });
    }
}