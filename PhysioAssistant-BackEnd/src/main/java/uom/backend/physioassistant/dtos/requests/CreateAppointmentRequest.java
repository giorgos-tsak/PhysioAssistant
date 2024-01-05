package uom.backend.physioassistant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CreateAppointmentRequest {
    String doctorId;
    String patientId;
    String physioActionId;
    LocalDate date;
    LocalTime time;
}
