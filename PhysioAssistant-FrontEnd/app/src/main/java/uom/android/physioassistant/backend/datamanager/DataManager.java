package uom.android.physioassistant.backend.datamanager;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uom.android.physioassistant.backend.api.AppointmentApi;
import uom.android.physioassistant.backend.api.DoctorApi;
import uom.android.physioassistant.backend.api.PatientApi;
import uom.android.physioassistant.backend.api.PhysioActionApi;
import uom.android.physioassistant.backend.events.AppointmentCreatedEvent;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.backend.events.DoctorCreatedEvent;
import uom.android.physioassistant.backend.events.DoctorLoadedEvent;
import uom.android.physioassistant.backend.events.DoctorsLoadedEvent;
import uom.android.physioassistant.backend.events.PatientCreatedEvent;
import uom.android.physioassistant.backend.events.PatientLoadedEvent;
import uom.android.physioassistant.backend.events.PatientsLoadedEvent;
import uom.android.physioassistant.backend.events.PhysioActionCreatedEvent;
import uom.android.physioassistant.backend.events.PhysioActionsLoadedEvent;
import uom.android.physioassistant.backend.requests.AppointmentRequest;
import uom.android.physioassistant.backend.requests.CreateDoctorRequest;
import uom.android.physioassistant.backend.requests.CreatePatientRequest;
import uom.android.physioassistant.backend.responses.ErrorResponse;
import uom.android.physioassistant.backend.retrofit.RetrofitService;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;

public class DataManager {

    private RetrofitService retrofitService;
    private PatientApi patientApi;
    private DoctorApi doctorApi;
    private AppointmentApi appointmentApi;
    private PhysioActionApi physioActionApi;

    public DataManager() {
        retrofitService = new RetrofitService();
        patientApi = retrofitService.getRetrofit().create(PatientApi.class);
        doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);
        appointmentApi = retrofitService.getRetrofit().create(AppointmentApi.class);
        physioActionApi = retrofitService.getRetrofit().create(PhysioActionApi.class);
        doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);

    }


    public void loadPatients(){
        patientApi.getPatients().enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if(response.isSuccessful()){
                    ArrayList<Patient> patients = (ArrayList<Patient>) response.body();
                    EventBus.getDefault().post(new PatientsLoadedEvent(patients));
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {

            }
        });
    }

    public void loadPatientById(String id){
        patientApi.getPatientById(id).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    Patient patient = response.body();
                    EventBus.getDefault().post(new PatientLoadedEvent(patient));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error", "failed to load patient by id"+t);
            }
        });
    }

    public void loadPatientByUsername(String username){
        patientApi.getPatientByUsername(username).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    Patient patient = response.body();
                    EventBus.getDefault().post(new PatientLoadedEvent(patient));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error", "failed to load patient by id"+t);
            }
        });
    }

    public void loadDoctors(){
        doctorApi.getDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    List<Doctor> doctors = response.body();
                    EventBus.getDefault().post(new DoctorsLoadedEvent(doctors));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e("Error", "failed to load doctors"+t);
            }
        });
    }

    public void loadDoctorByUsername(String username){
        doctorApi.getDoctorByUsername(username).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    Doctor doctor = response.body();
                    EventBus.getDefault().post(new DoctorLoadedEvent(doctor));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Log.e("Error", "failed to load doctor"+t);
            }
        });
    }

    public void loadDoctorsByPatientId(String patientId){
        patientApi.getAllDoctorsByPatientId(patientId).enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    List<Doctor> doctors = response.body();
                    EventBus.getDefault().post(new DoctorsLoadedEvent(doctors));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e("Error", "failed to load doctors"+t);
            }
        });
    }

    public void createDoctor(Doctor doctor){
        doctorApi.createDoctor(doctor).enqueue(new Callback<Doctor>() {
            @Override
            public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                if (response.isSuccessful()) {
                    System.out.println("response "+doctor);
                    EventBus.getDefault().post(new DoctorCreatedEvent(doctor));
                }
            }

            @Override
            public void onFailure(Call<Doctor> call, Throwable t) {
                Log.e("Error", "failed to create doctor"+t);
            }
        });

    }

    public void createPatient(String doctorId, CreatePatientRequest patientRequest){
        doctorApi.createPatient(doctorId,patientRequest).enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if (response.isSuccessful()) {
                    Patient patient = response.body();
                    System.out.println("response "+patient);
                    EventBus.getDefault().post(new PatientCreatedEvent(patient));
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Log.e("Error", "failed to create patient"+t);
            }
        });
    }

    public void loadAllPatientByDoctorId(String doctorId){
        doctorApi.getAllPatientsByDoctorId(doctorId).enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if (response.isSuccessful()) {
                    List<Patient> patients = response.body();
                    EventBus.getDefault().post(new PatientsLoadedEvent((ArrayList<Patient>) patients));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                Log.e("Error", "failed to load doctor"+t);
            }
        });
    }

    public void loadAppointments() {
        appointmentApi.getAllAppointments().enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    List<Appointment> appointments = response.body();
                    EventBus.getDefault().post(new AppointmentsLoadedEvent(appointments));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.e("Error", "failed to load Appointments"+t);
            }
        });
    }

    public void loadAppointmentsByPatientId(String id){
        appointmentApi.getAppointmentsByPatientId(id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    List<Appointment> appointments = response.body();
                    EventBus.getDefault().post(new AppointmentsLoadedEvent(appointments));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.e("Error", "failed to load patient by id"+t);
            }
        });
    }

    public void loadAppointmentsByDoctorId(String id){
        appointmentApi.getAppointmentsByDoctorId(id).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    List<Appointment> appointments = response.body();
                    EventBus.getDefault().post(new AppointmentsLoadedEvent(appointments));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.e("Error", "failed to load appointments by doctor id"+t);
            }
        });
    }

    public void loadAppointmentsForPatientWithDoctor(String patientId,String doctorId){
        appointmentApi.getAppointmentsForPatientWithDoctor(patientId,doctorId).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    List<Appointment> appointments = response.body();
                    EventBus.getDefault().post(new AppointmentsLoadedEvent(appointments));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                Log.e("Error", "failed to load appointments by doctor id"+t);
            }
        });
    }

    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status){
        appointmentApi.updateAppointmentStatus(appointmentId,status).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    EventBus.getDefault().post(new AppointmentUpdatedEvent(true));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Error", "failed to load patient by id"+t);
            }
        });
    }

    public void createAppointment(AppointmentRequest appointmentRequest){
        appointmentApi.createAppointment(appointmentRequest).enqueue(new Callback<Appointment>() {
            @Override
            public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                if (response.isSuccessful()) {
                    Appointment appointment = response.body();
                    EventBus.getDefault().post(new AppointmentCreatedEvent(appointment));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Appointment> call, Throwable t) {
                Log.e("Error", "failed to create Appointment"+t);
            }
        });
    }

    public void loadPhysioActions(){
        physioActionApi.getPhysioActions().enqueue(new Callback<List<PhysioAction>>() {
            @Override
            public void onResponse(Call<List<PhysioAction>> call, Response<List<PhysioAction>> response) {
                if (response.isSuccessful()) {
                    List<PhysioAction> physioActions = response.body();
                    EventBus.getDefault().post(new PhysioActionsLoadedEvent(physioActions));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<PhysioAction>> call, Throwable t) {
                Log.e("Error", "failed to load PhysioActions"+t);
            }
        });
    }

    public void createPhysioAction(PhysioAction physioAction){
        physioActionApi.createPhysioAction(physioAction).enqueue(new Callback<PhysioAction>() {
            @Override
            public void onResponse(Call<PhysioAction> call, Response<PhysioAction> response) {
                if (response.isSuccessful()) {
                    PhysioAction createdPhysioAction = response.body();
                    System.out.println(createdPhysioAction);
                    EventBus.getDefault().post(new PhysioActionCreatedEvent(createdPhysioAction));
                } else {
                    printErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PhysioAction> call, Throwable t) {
                Log.e("Error", "failed to create PhysioAction"+t);
            }
        });
    }



    public void printErrorMessage(ResponseBody responseBody){
        try {
            String errorBodyString = responseBody.string();
            JSONObject errorJson = new JSONObject(errorBodyString);
            String errorMessage = errorJson.getString("message");
            Log.e("Error", "on response: " +errorMessage);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseErrorResponse(ResponseBody responseBody){
        try {
            String errorBodyString = responseBody.string();
            JSONObject errorJson = new JSONObject(errorBodyString);
            String errorMessage = errorJson.getString("message");
            return errorMessage;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
