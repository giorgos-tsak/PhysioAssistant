package uom.android.physioassistant.backend.requests;


import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class AppointmentRequest {

    String doctorId;
    String patientId;
    String physioActionId;
    String date;
    String time;

    public AppointmentRequest(String doctorId, String patientId, String physioActionId,String date, String time) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.physioActionId = physioActionId;
        this.date = date;
        this.time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhysioActionId() {
        return physioActionId;
    }

    public void setPhysioActionId(String physioActionId) {
        this.physioActionId = physioActionId;
    }

    @Override
    public String toString() {
        return "AppointmentRequest{" +
                "doctorId='" + doctorId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", physioActionId='" + physioActionId + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
