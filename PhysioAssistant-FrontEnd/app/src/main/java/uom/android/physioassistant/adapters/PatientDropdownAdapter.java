package uom.android.physioassistant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.ui.DropDownClickListener;

public class PatientDropdownAdapter extends RecyclerView.Adapter<PatientDropdownAdapter.ViewHolder> implements IDropDownAdapter {

    private ArrayList<Patient> patients;
    private DropDownClickListener dropDownClickListener;

    @NonNull
    @Override
    public PatientDropdownAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item,parent,false);
        return new PatientDropdownAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientDropdownAdapter.ViewHolder holder, int position) {
        holder.name.setText(patients.get(position).getName());
        holder.address.setText(patients.get(position).getAddress());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownClickListener != null) {
                    dropDownClickListener.onItemClick(patients.get(position));
                }
            }
        });
    }
    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setDropDownClickListener(DropDownClickListener dropDownClickListener) {
        this.dropDownClickListener = dropDownClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView parent;
        TextView name;
        TextView address;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
        }
    }

}
