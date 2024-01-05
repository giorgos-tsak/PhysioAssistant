package uom.android.physioassistant.backend.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import uom.android.physioassistant.backend.requests.LoginRequest;
import uom.android.physioassistant.backend.responses.LoginResponse;

public interface AuthenticationApi {
    @POST("/api/admin/login")
    Call<LoginResponse> adminLogin(@Body LoginRequest request);

    @POST("/api/patients/login")
    Call<LoginResponse> patientLogin(@Body LoginRequest request);

    @POST("/api/doctors/login")
    Call<LoginResponse> doctorLogin(@Body LoginRequest request);
}
