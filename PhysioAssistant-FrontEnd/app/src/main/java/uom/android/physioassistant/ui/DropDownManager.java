package uom.android.physioassistant.ui;

import android.view.View;

import java.util.ArrayList;

import uom.android.physioassistant.adapters.IDropDownAdapter;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;

public class DropDownManager {

    private ArrayList<DropDown> dropDowns = new ArrayList<>();
    private Doctor selectedDoctor;
    private PhysioAction selectedService;
    private Patient selectedPatient;

    public DropDownManager(){

    }

    public void handleClicks(){
        for(DropDown dropDown:dropDowns){

            dropDown.getCollapsed().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    resetDropdowns(dropDown);
                    if(!dropDown.isExpanded()){
                        dropDown.setExpanded();
                    }
                    else{
                        dropDown.setCollapsed();
                    }
                }
            });

            IDropDownAdapter adapter = (IDropDownAdapter) dropDown.getAdapter();
            adapter.setDropDownClickListener(new DropDownClickListener() {
                @Override
                public void onItemClick(DropDownItem dropDownItem) {
                    dropDown.setText(dropDownItem.getText());
                    dropDown.setCollapsed();
                    dropDown.setFilled(true);
                    setSelectedOptions(dropDownItem);
                }
            });

        }
    }

    private void setSelectedOptions(DropDownItem dropDownItem){

         if(dropDownItem instanceof Doctor){
            selectedDoctor = (Doctor) dropDownItem;
        }
        else if(dropDownItem instanceof PhysioAction){
            selectedService = (PhysioAction) dropDownItem;
        }
        else if(dropDownItem instanceof Patient){
            selectedPatient = (Patient) dropDownItem;
         }
    }
    private void resetDropdowns(DropDown dropDown){
        for(DropDown dd:dropDowns){
            if(!dd.equals(dropDown)){
                dd.setCollapsed();
            }
        }
    }

    public boolean isAllFilled(){
        for(DropDown dropDown:dropDowns){
            if(!dropDown.isFilled()){
                return false;
            }
        }
        return true;
    }

    public void addDropdown(DropDown dropDown){
        dropDowns.add(dropDown);
    }

    public Doctor getSelectedDoctor() {
        return selectedDoctor;
    }

    public PhysioAction getSelectedService() {
        return selectedService;
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }
}
