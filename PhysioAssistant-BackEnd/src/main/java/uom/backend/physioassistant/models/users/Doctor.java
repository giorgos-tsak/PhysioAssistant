package uom.backend.physioassistant.models.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "doctor")
public class Doctor extends User{
    @Id
    @Column(nullable = false)
    private String afm;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "doctor_patient",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private List<Patient> patients = new ArrayList<>();

    public Doctor(String afm,String username, String password,String name,String address){
        this.afm = afm;
        this.setUsername(username);
        this.setPassword(password);
        this.name = name;
        this.address = address;
    }


}
