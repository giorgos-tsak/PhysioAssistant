package uom.backend.physioassistant.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uom.backend.physioassistant.dtos.requests.CreatePatientRequest;
import uom.backend.physioassistant.exceptions.AlreadyAddedException;
import uom.backend.physioassistant.models.users.Doctor;
import uom.backend.physioassistant.models.users.Patient;
import uom.backend.physioassistant.repositories.DoctorRepository;
import uom.backend.physioassistant.repositories.PatientRepository;
import uom.backend.physioassistant.utils.AccountGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PatientService patientService;
    private final PatientRepository patientRepository;


    public DoctorService(DoctorRepository doctorRepository, PatientService patientService,PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
        this.patientRepository = patientRepository;
    }

    public Collection<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getById(String id) {
        Optional<Doctor> foundDoctor = this.doctorRepository.findById(id);

        if (foundDoctor.isEmpty())
            throw new EntityNotFoundException("Doctor with id: " + id + " not found.");

        return foundDoctor.get();
    }

    public Doctor getDoctorByUsername(String username){
        Optional<Doctor> foundDoctor = doctorRepository.findByUsername(username);
        if(foundDoctor.isEmpty()){
            throw new EntityNotFoundException("Doctor with username: "+username+" not found");
        }

        return foundDoctor.get();
    }

    public List<Patient> getAllPatientsByDoctorId(String doctorId) {
        Optional<Doctor> foundDoctor = doctorRepository.findById(doctorId);
        if(foundDoctor.isEmpty()){
            throw new EntityNotFoundException("Doctor with id: "+doctorId+" not found");
        }
        return foundDoctor.get().getPatients();
    }

    public Optional<Doctor> getOptionalById(String id) {
        return this.doctorRepository.findById(id);
    }

    public void deleteById(String id) {
        Doctor foundDoctor = this.getById(id);

        this.doctorRepository.delete(foundDoctor);
    }

    public Doctor createDoctor(Doctor doctor) {
        String givenAFM = doctor.getAfm();
        Optional<Doctor> foundDoctor = this.doctorRepository.findById(givenAFM);

        if (foundDoctor.isPresent())
            throw new AlreadyAddedException("Ο γιατρός με ΑΦΜ: " + givenAFM + " υπάρχει ήδη.");

        String giveUsername = doctor.getUsername();
        foundDoctor = this.doctorRepository.findByUsername(giveUsername );
        if(foundDoctor.isPresent()){
            throw new AlreadyAddedException("Το username "+giveUsername+" υπάρχει ήδη");
        }

        return doctorRepository.save(doctor);
    }

    public Patient createPatient(String doctorId,CreatePatientRequest patientRequest){
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);

        if (optionalDoctor.isEmpty()) {
            throw new EntityNotFoundException("Doctor with afm: " + doctorId + " not found.");
        }
        Doctor doctor = optionalDoctor.get();

        String amka = patientRequest.getAmka();
        Optional<Patient> optionalPatient = patientRepository.findById(amka);

        Patient patient;

        if(optionalPatient.isEmpty()){
            patient = patientService.createPatient(patientRequest);
        }
        else{
            patient = optionalPatient.get();
            for(Patient patient1: doctor.getPatients()){
                if(patient1.getAmka()==patient.getAmka()){
                    throw new AlreadyAddedException("Patient with amka: "+patient.getAmka()+" already added");
                }
            }
        }
        patient.getDoctors().add(doctor);
        patientRepository.save(patient);

        doctor.getPatients().add(patient);
        doctorRepository.save(doctor);

        return patient;

    }


}
