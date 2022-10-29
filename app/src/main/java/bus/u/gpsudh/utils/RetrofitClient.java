package bus.u.gpsudh.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static Retrofit retrofit;
    public static Retrofit retrofit1;

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://gbusapi.up.railway.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit;
    }

    public static Retrofit getRetrofitInstance1(){
        if (retrofit1 == null){
            retrofit1 = new Retrofit.Builder()
                    .baseUrl("https://gbusapi.up.railway.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit1;
    }
}
