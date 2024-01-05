package uom.backend.physioassistant.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uom.backend.physioassistant.models.appointment.Appointment;

import java.util.Collection;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "visit")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToMany
    @JoinTable(
            name = "visit_physioactions",
            joinColumns = @JoinColumn(name = "visit_id"),
            inverseJoinColumns = @JoinColumn(name = "physioaction_code")
    )
    private Collection<PhysioAction> physioActions;

    @Column(nullable = false)
    private double totalPrice;

    public Visit(Appointment appointment, Collection<PhysioAction> physioActions) {
        this.appointment = appointment;
        this.physioActions = physioActions;
        this.calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        double totalPrice = 0.0;
        if (physioActions != null) {
            for (PhysioAction physioAction : physioActions) {
                totalPrice += physioAction.getCostPerSession();
            }
        }
        this.totalPrice = totalPrice;
    }

}