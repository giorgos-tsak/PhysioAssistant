package uom.backend.physioassistant.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uom.backend.physioassistant.models.PhysioAction;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CreateVisitRequest {
    private Long appointmentId;
    private Collection<PhysioAction> services;
}

