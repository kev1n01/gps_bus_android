package bus.u.gpsudh.user;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("api/login")
    Call<UserResponse> getUserLogin(@Body UserModel user);

    @POST("apirest/users/")
    Call<UserResponse> RegisterUser(@Body UserModel user);


}
