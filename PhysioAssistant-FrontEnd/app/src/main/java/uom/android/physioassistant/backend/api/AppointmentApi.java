package uom.android.physioassistant.backend.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uom.android.physioassistant.backend.requests.AppointmentRequest;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;

public interface AppointmentApi {

    @GET("/appointments")
    Call<List<Appointment>> getAllAppointments();

    @GET("/appointments/patient")
    Call<List<Appointment>> getAppointmentsByPatientId(@Query("pid") String patientId);
    @GET("/appointments/doctor")
    Call<List<Appointment>> getAppointmentsByDoctorId(@Query("did") String doctorId);

    @GET("/appointments/{id}")
    Call<Appointment> getAppointmentsById(@Path("id") Long id);

    @GET("/appointments/{patientId}/{doctorId}")
    Call<List<Appointment>> getAppointmentsForPatientWithDoctor(@Path("patientId") String patientId, @Path("doctorId") String doctorId);

    @PUT("/appointments/update/status")
    Call<ResponseBody> updateAppointmentStatus(@Query("aid") Long appointmentId, @Query("status")AppointmentStatus status);

    @POST("/appointments/create")
    Call<Appointment> createAppointment(@Body AppointmentRequest appointmentRequest);


}
