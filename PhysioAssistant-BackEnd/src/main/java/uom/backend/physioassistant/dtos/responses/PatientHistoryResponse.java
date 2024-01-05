package uom.backend.physioassistant.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uom.backend.physioassistant.models.Visit;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PatientHistoryResponse {
    private Collection<Visit> visits;
    private double totalMoneySpent;
}
