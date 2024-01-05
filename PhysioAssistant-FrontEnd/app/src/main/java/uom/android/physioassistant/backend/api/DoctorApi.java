package uom.android.physioassistant.backend.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uom.android.physioassistant.activities.doctor.CreatePatientFragment;
import uom.android.physioassistant.backend.requests.CreateDoctorRequest;
import uom.android.physioassistant.backend.requests.CreatePatientRequest;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;

public interface DoctorApi {
    @POST("api/doctors/create")
    Call<Doctor> createDoctor(@Body Doctor doctor);

    @GET("/api/doctors")
    Call<List<Doctor>> getDoctors();

    @GET("/api/doctors/username/{username}")
    Call<Doctor> getDoctorByUsername(@Path("username") String username);

    @GET("/api/doctors/{doctorId}/patients")
    Call<List<Patient>> getAllPatientsByDoctorId(@Path("doctorId") String doctorId);

    @POST("/api/doctors/create-patient")
    Call<Patient> createPatient(@Query("doctorId") String doctorId, @Body CreatePatientRequest patientRequest);

    @POST("/api/doctors/create")
    Call<Doctor> createDoctor(@Body CreateDoctorRequest doctorRequest);

}
