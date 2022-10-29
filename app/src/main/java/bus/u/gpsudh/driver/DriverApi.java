package bus.u.gpsudh.driver;

import bus.u.gpsudh.driver.DriverResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DriverApi {

    @POST("apirest/drivers/")
    Call<DriverResponse> RegisterDriver(@Body DriverModel driver);
}
