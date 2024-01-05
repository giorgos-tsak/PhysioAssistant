package uom.android.physioassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.ui.DropDownClickListener;
import uom.android.physioassistant.models.PhysioAction;

public class ServiceDropDownAdapterAdapter extends RecyclerView.Adapter<ServiceDropDownAdapterAdapter.ViewHolder> implements IDropDownAdapter {

    private Context context;
    private DropDownClickListener dropDownClickListener;
    private ArrayList<PhysioAction> services = new ArrayList<>();

    public ServiceDropDownAdapterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceDropDownAdapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_dropdown_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceDropDownAdapterAdapter.ViewHolder holder, int position) {


        holder.serviceName.setText(services.get(position).getName());
        holder.servicePrice.setText(services.get(position).getFormattedCost());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownClickListener != null) {
                    dropDownClickListener.onItemClick(services.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void setServices(ArrayList<PhysioAction> services) {
        this.services = services;
    }

    public void setDropDownClickListener(DropDownClickListener dropDownClickListener) {
        this.dropDownClickListener = dropDownClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CardView card;
        TextView serviceName;
        TextView servicePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.parent);
            serviceName = itemView.findViewById(R.id.doctorName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
        }
    }

}
