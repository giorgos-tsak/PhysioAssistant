package uom.android.physioassistant.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {


    private ArrayList<Appointment> appointments = new ArrayList<>();
    private Context context;
    private boolean deleteMode = false;
    private boolean current = false;
    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }
    private OnItemClickListener onItemClickListener;

    public AppointmentAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item,parent,false);
        return new AppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(deleteMode && current){
            holder.statusImage.setVisibility(View.INVISIBLE);
            holder.deleteImage.setVisibility(View.VISIBLE);
        }
        else{
            holder.statusImage.setVisibility(View.VISIBLE);
            holder.deleteImage.setVisibility(View.INVISIBLE);
        }

        String imageURL = "https://img.freepik.com/premium-photo/spa-arrangement-with-towel-soap-salt_23-2148268482.jpg?w=2000";
        Glide.with(context).asBitmap().load(appointments.get(position).getPhysioAction().getImageURL()).error(R.drawable.ic_failed_to_load_image).into(holder.serviceImage);
        holder.serviceName.setText(appointments.get(position).getPhysioAction().getName());
        holder.date.setText(formatDate(appointments.get(position).getLocalDate()));
        holder.time.setText(appointments.get(position).getLocalTime().toString());
        holder.address.setText(appointments.get(position).getDoctor().getName());
        holder.statusImage.setImageResource(setStatusImage(appointments.get(position).getStatus()));
        holder.cost.setText(appointments.get(position).getPhysioAction().getCostPerSession()+"$");
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setDeleteMode(true);
                return true;
            }
        });
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(appointments.get(position));

            }
        });

    }
    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public int setStatusImage(AppointmentStatus appointmentStatus){
        switch (appointmentStatus){
            case PENDING:
                return  R.drawable.ic_pending;
            case DECLINED:
                return R.drawable.ic_declined;
            case ACCEPTED:
            case DONE:
                return R.drawable.ic_done;
        }
        return -1;
    }

    public String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void showDeleteDialog(Appointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Διαγραφή");
        builder.setMessage("Σίγουρα θες να ακυρώσεις την κράτηση");

        builder.setPositiveButton("Διαγραφή", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onItemClickListener != null) {
                    setDeleteMode(false);
                    onItemClickListener.onItemClick(appointment);
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Ακύρο", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    public void setDeleteMode(boolean deleteMode) {
        this.deleteMode = deleteMode;
        notifyDataSetChanged();
    }

    public boolean isDeleteMode() {
        return deleteMode;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{


        RelativeLayout parent;
        ImageView serviceImage;
        ImageView statusImage;
        ImageView deleteImage;
        TextView serviceName;
        TextView address;
        TextView date;
        TextView time;
        TextView cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parentLayout);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            statusImage = itemView.findViewById(R.id.statusImage);
            serviceName = itemView.findViewById(R.id.doctorName);
            address = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            cost = itemView.findViewById(R.id.price);
            deleteImage = itemView.findViewById(R.id.deleteImage);
        }
    }

}
