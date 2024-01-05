package uom.android.physioassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.ui.DropDownClickListener;
import uom.android.physioassistant.models.Doctor;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> implements IDropDownAdapter {

    private ArrayList<Doctor> doctors = new ArrayList<>();
    private DropDownClickListener dropDownClickListener;

    private Context context;
    public DoctorAdapter(Context context){
        this.context = context;
    }


    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {

        holder.name.setText(doctors.get(position).getName());
        holder.address.setText(doctors.get(position).getAddress());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dropDownClickListener!=null){
                    dropDownClickListener.onItemClick(doctors.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void setDropDownClickListener(DropDownClickListener dropDownClickListener) {
        this.dropDownClickListener = dropDownClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView name;
        TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.doctorName);
            address = itemView.findViewById(R.id.doctorAddress);
            card = itemView.findViewById(R.id.parent);

        }
    }

}
