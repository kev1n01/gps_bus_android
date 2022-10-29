package bus.u.gpsudh.student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StudentApi {
    @GET("api/studentfilter")
    Call<List<StudentResponse>> studentByUserId(@Query("user_id") int user_id);

    @POST("apirest/students/")
    Call<StudentResponse> RegisterStudent(@Body StudentModel student);

    @PUT("apirest/students/{id}/")
    Call<StudentModel> UpdateCoordenates(@Path ("id") int id,@Body StudentModel student);

}
