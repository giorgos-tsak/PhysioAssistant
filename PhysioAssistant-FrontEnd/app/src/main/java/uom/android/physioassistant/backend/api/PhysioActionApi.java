package uom.android.physioassistant.backend.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uom.android.physioassistant.models.PhysioAction;

public interface PhysioActionApi {

    @GET("/api/services")
    Call<List<PhysioAction>> getPhysioActions();

    @GET("/api/services/{id}")
    Call<PhysioAction> getServiceByCode(@Path("id") String code);

    @POST("/api/services/create")
    Call<PhysioAction> createPhysioAction(@Body PhysioAction physioAction);


}
