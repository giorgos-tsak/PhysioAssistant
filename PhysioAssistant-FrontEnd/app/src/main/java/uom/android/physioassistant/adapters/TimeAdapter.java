package uom.android.physioassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;

import uom.android.physioassistant.R;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private ArrayList<LocalTime> times;
    private Context context;
    private int selectedButtonPos=-1;
    private LocalTime selectedTime;
    public TimeAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.button.setText(times.get(position).toString());

        if(position==selectedButtonPos){
            setButtonPressed(holder.button);
        }
        else{
            setButtonIdle(holder.button);
        }


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonPressed(holder.button);
                selectedButtonPos = position;
                selectedTime = times.get(position);
                notifyDataSetChanged();
            }
        });
    }

    public void setButtonPressed(Button button){
        button.setTextColor(ContextCompat.getColor(context,R.color.blue));
        button.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
    }

    public void setButtonIdle(Button button){
        button.setTextColor(ContextCompat.getColor(context,R.color.white));
        button.setBackgroundColor(ContextCompat.getColor(context,R.color.blue));
    }

    public void resetAdapter(ArrayList<LocalTime> times){
        selectedButtonPos=-1;
        selectedTime=null;
        setTimes(times);
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public void setTimes(ArrayList<LocalTime> times) {
        this.times = times;
        notifyDataSetChanged();
    }


    public LocalTime getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedButtonPos(int selectedButtonPos) {
        this.selectedButtonPos = selectedButtonPos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialButton button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.timeButton);

        }
    }

}