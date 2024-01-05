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

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Patient patient);
    }

    private OnItemClickListener onItemClickListener;
    private ArrayList<Patient> patients;

    @NonNull
    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item,parent,false);
        return new PatientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.ViewHolder holder, int position) {


        holder.name.setText(patients.get(position).getName());
        holder.address.setText(patients.get(position).getAddress());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(patients.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
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
