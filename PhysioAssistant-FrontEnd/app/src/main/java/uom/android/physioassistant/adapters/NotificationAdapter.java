package uom.android.physioassistant.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.doctor.NotificationsFragment;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Appointment> appointments;

    public interface OnItemClickListener{
        void onItemClick(Appointment appointment);
    }

    private OnItemClickListener onItemClickListener;

    public NotificationAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        Appointment currentAppointment = appointments.get(position);
        holder.message.setText(currentAppointment.getPatient().getName()+" έστειλε αίτημα για ραντεβού στις "
                +currentAppointment.getFormattedDate()+" "+currentAppointment.getTime()+" για "+currentAppointment.getPhysioAction().getName());

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    showAcceptDialog(appointments.get(position));
                }
            }
        });
        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    showDeclineDialog(appointments.get(position));
                }
            }
        });

    }

    private void showAcceptDialog(Appointment appointment) {
        // Create a custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Αποδοχή")
                .setMessage("Θες σίγουρα να αποδεχτείς το αίτημα;")
                .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appointment.setStatus(AppointmentStatus.ACCEPTED);
                        onItemClickListener.onItemClick(appointment);
                    }
                })
                .setNegativeButton("Όχι", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel accept action
                    }
                })
                .show();
    }

    private void showDeclineDialog(Appointment appointment) {
        // Create a custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Απόρριψη")
                .setMessage("Θες σίγουρα να απορρίψεις το αίτημα;")
                .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        appointment.setStatus(AppointmentStatus.DECLINED);
                        onItemClickListener.onItemClick(appointment);
                    }
                })
                .setNegativeButton("Όχι", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel accept action
                    }
                })
                .show();
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }


    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView message;
        MaterialButton acceptButton;
        MaterialButton declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);

        }
    }

}
