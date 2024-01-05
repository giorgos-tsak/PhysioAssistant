package uom.android.physioassistant.models;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.Comparator;

public class Appointment implements Serializable,Comparable<Appointment>{
    private Long id;
    private String date;
    private String time;
    private Doctor doctor;
    private Patient patient;
    private PhysioAction physioAction;
    private AppointmentStatus status;


    public Appointment(Long id, String date, String time, AppointmentStatus status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getLocalDate(){
        return LocalDate.parse(date);
    }

    public LocalTime getLocalTime(){
        return LocalTime.parse(time);
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public PhysioAction getPhysioAction() {
        return physioAction;
    }

    public void setPhysioAction(PhysioAction physioAction) {
        this.physioAction = physioAction;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getFormattedDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

        return getLocalDate().format(formatter);
    }

    public String getTimeRange(){
        LocalTime endTime = getLocalTime().plusMinutes(30);
        return getLocalTime()+"-"+endTime;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", physioAction=" + physioAction +
                ", status=" + status +
                '}';
    }

    public static Comparator<Appointment> getDescendingComparator() {
        return Comparator.comparing(Appointment::getLocalDate).reversed()
                .thenComparing(Appointment::getLocalTime).reversed();
    }

    public static Comparator<Appointment> getAscendingComparator() {
        return Comparator.comparing(Appointment::getLocalDate)
                .thenComparing(Appointment::getLocalTime);
    }

    @Override
    public int compareTo(Appointment o) {
        int dateComparison = date.compareTo(o.date);

        if (dateComparison == 0) {
            // If the dates are the same, compare the times
            return time.compareTo(o.time);
        } else {
            return dateComparison;
        }
    }
}
