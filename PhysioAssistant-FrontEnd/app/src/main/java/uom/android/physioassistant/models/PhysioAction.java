package uom.android.physioassistant.models;

import java.io.Serializable;

import uom.android.physioassistant.ui.DropDownItem;

public class PhysioAction implements Serializable, DropDownItem {

    private String code;
    private String name;
    private String description;
    private double costPerSession;
    private String imageURL;

    public PhysioAction(String code, String name, String description, double costPerSession,String imageURL) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.costPerSession = costPerSession;
        this.imageURL = imageURL;
    }


    public String getFormattedCost(){
        if(costPerSession % 1 == 0){
            return String.format("%.0f$",costPerSession);
        }
        return String.format("%.2f$", costPerSession).replace(",", ".");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCostPerSession() {
        return costPerSession;
    }
    public void setCostPerSession(double costPerSession) {
        this.costPerSession = costPerSession;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public String toString() {
        return "PhysioAction{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", costPerSession=" + costPerSession +
                '}';
    }

    @Override
    public String getText() {
        return this.name;
    }
}
