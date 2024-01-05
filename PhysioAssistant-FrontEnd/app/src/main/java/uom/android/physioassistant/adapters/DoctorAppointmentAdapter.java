package uom.android.physioassistant.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder> {


    public interface OnDeletePressedListener{
        void onDeletePressed(Appointment appointment);
    }

    private ArrayList<Appointment> appointments;
    private boolean isDeleteMode;
    private Context context;
    private OnDeletePressedListener onDeletePressedListener;

    public DoctorAppointmentAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointment_item,parent,false);
        return new DoctorAppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.ViewHolder holder, int position) {


        holder.patientName.setText(appointments.get(position).getPatient().getName());
        holder.serviceName.setText(appointments.get(position).getPhysioAction().getName());
        holder.time.setText(appointments.get(position).getTimeRange());

        if (isDeleteMode && appointments.get(position).getStatus().equals(AppointmentStatus.ACCEPTED)) {
            holder.deleteImage.setVisibility(View.VISIBLE);
            handleDeleteButton(holder.deleteImage,appointments.get(position));
        }
        else{
            holder.deleteImage.setVisibility(View.INVISIBLE);
        }

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setDeleteMode(true);
                return true;
            }
        });

    }

    public void setDeleteMode(boolean mode){
        isDeleteMode = mode;
        notifyDataSetChanged();
    }

    private void handleDeleteButton(ImageView deleteImage,Appointment appointment){
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(appointment);
            }
        });
    }

    private void showDeleteDialog(Appointment appointment) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Διαγραφή");
        builder.setMessage("Σίγουρα θες να ακυρώσεις το ραντεβού;");
        builder.setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(onDeletePressedListener!=null){
                    onDeletePressedListener.onDeletePressed(appointment);
                }

            }
        });
        builder.setNegativeButton("'Οχι", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    public void setOnDeletePressedListener(OnDeletePressedListener onDeletePressedListener) {
        this.onDeletePressedListener = onDeletePressedListener;
    }

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        CardView parent;
        TextView patientName;
        TextView serviceName;
        TextView time;
        ImageView deleteImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            patientName = itemView.findViewById(R.id.patientName);
            serviceName = itemView.findViewById(R.id.serviceName);
            time = itemView.findViewById(R.id.timeText);
            deleteImage = itemView.findViewById(R.id.deleteImage);

        }
    }

}
