package uom.android.physioassistant.activities.doctor;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.adapters.NotificationAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.Doctor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private TextView noRequestsText;
    private Doctor doctor;
    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctor = doctorActivity.getDoctor();

        initViews(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentsUpdated(AppointmentUpdatedEvent event){
        if(event.isSuccess()){
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByDoctorId(doctor.getAfm());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentsLoaded(AppointmentsLoadedEvent event){
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        doctor.setAppointments(appointments);
        populateAdapter();
    }

    private void initViews(View view){


        noRequestsText = view.findViewById(R.id.noRequestsText);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DataManager dataManager = new DataManager();
        dataManager.loadAppointmentsByDoctorId(doctor.getAfm());

        adapter = new NotificationAdapter(view.getContext());


    }

    private void populateAdapter(){


        ArrayList<Appointment> appointments = doctor.getPendingAppointments();
        if(appointments.size()>0){
            adapter.setAppointments(appointments);
            adapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Appointment appointment) {
                    DataManager dataManager = new DataManager();
                    dataManager.updateAppointmentStatus(appointment.getId(),appointment.getStatus());
                }
            });
            noRequestsText.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);

        }
        else{
            recyclerView.setVisibility(View.INVISIBLE);
            noRequestsText.setVisibility(View.VISIBLE);
        }
    }


}