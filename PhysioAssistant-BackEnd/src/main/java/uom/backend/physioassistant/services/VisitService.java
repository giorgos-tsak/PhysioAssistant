package uom.backend.physioassistant.services;

import org.springframework.stereotype.Service;
import uom.backend.physioassistant.dtos.requests.CreateVisitRequest;
import uom.backend.physioassistant.models.PhysioAction;
import uom.backend.physioassistant.models.Visit;
import uom.backend.physioassistant.models.appointment.Appointment;
import uom.backend.physioassistant.repositories.VisitRepository;

import java.util.Collection;
import java.util.List;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final AppointmentService appointmentService;

    public VisitService(VisitRepository visitRepository, AppointmentService appointmentService) {
        this.visitRepository = visitRepository;
        this.appointmentService = appointmentService;
    }

    public Collection<Visit> getAllVisits() {
        return this.visitRepository.findAll();
    }

    public Collection getVisitsByPatientId(String patientId) {
        return this.visitRepository.findAllByPatientId(patientId);
    }

    public Visit createVisit(CreateVisitRequest visitRequest) {
        // FInd the visit appointment
        Long appointmentId = visitRequest.getAppointmentId();

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        List<PhysioAction> services = (List<PhysioAction>) visitRequest.getServices();

        Visit visit = new Visit(appointment, services);

        return this.visitRepository.save(visit);
    }
}
