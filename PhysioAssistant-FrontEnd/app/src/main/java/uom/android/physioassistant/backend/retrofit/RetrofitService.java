package uom.android.physioassistant.backend.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;
    private static String API_URL = "http://192.168.1.8:8080"; //replace with ip address

    public RetrofitService() {
        init();
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.API_URL)   // Specify API url
                .addConverterFactory(GsonConverterFactory.create(new Gson()))   // Specify we will use gson
                .build();
    }


    public Retrofit getRetrofit() {
        return this.retrofit;
    }
}
