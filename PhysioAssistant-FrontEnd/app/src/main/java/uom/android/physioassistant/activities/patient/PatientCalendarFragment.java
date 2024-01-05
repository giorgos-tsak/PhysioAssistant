package uom.android.physioassistant.activities.patient;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;

import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.adapters.AppointmentAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.R;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientCalendarFragment extends Fragment implements OnBackPressedListener, AppointmentAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView appointmentRecyclerView;
    private AppointmentAdapter appointmentAdapter;
    private TextView historyButton,currentButton;
    private TextView noAppointmentsText;
    private FloatingActionButton createAppointmentButton;
    private Patient patient;

    public PatientCalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientCalendarFragment newInstance(String param1, String param2) {
        PatientCalendarFragment fragment = new PatientCalendarFragment();
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

        View view = inflater.inflate(R.layout.fragment_calendar,container,false);

        PatientActivity patientActivity = (PatientActivity) getActivity();
        patientActivity.getNavBar().setVisibility(View.VISIBLE);
        patient = patientActivity.getPatient();

        DataManager dataManager = new DataManager();
        dataManager.loadAppointmentsByPatientId(patient.getAmka());

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

    @Override
        public void onItemClick(Appointment appointment) {
        Long appointmentId = appointment.getId();
        AppointmentStatus status = AppointmentStatus.DECLINED;
        DataManager dataManager = new DataManager();
        dataManager.updateAppointmentStatus(appointmentId, status);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentStatusUpdated(AppointmentUpdatedEvent event) {
        if (event.isSuccess()) {
            System.out.println("updated calendar");
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByPatientId(patient.getAmka());
        } else {
            Log.e("Error", "Failed to update appointment status");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentsLoaded(AppointmentsLoadedEvent event){
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        patient.setAppointments(appointments);
        appointmentAdapter.setAppointments(patient.getCurrentAppointments());
        if(patient.getCurrentAppointments().size()==0){
            noAppointmentsText.setVisibility(View.VISIBLE);
            appointmentRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void checkAppointmentDone() {
        LocalDateTime dateTime = LocalDateTime.now();
        LocalTime localTime = dateTime.toLocalTime();
        LocalDate localDate = dateTime.toLocalDate();

        DataManager dataManager = new DataManager();

        for(Appointment appointment: patient.getCurrentAppointments()){
            if(localDate.isAfter(appointment.getLocalDate())){
                if(appointment.getStatus().equals(AppointmentStatus.ACCEPTED)){
                    dataManager.updateAppointmentStatus(appointment.getId(),AppointmentStatus.DONE);
                }
                else if(appointment.getStatus().equals(AppointmentStatus.PENDING)){
                    dataManager.updateAppointmentStatus(appointment.getId(),AppointmentStatus.DECLINED);
                }
            }
            else if(localDate.equals(appointment.getLocalDate())){
                if(localTime.isAfter(appointment.getLocalTime())){
                    if(appointment.getStatus().equals(AppointmentStatus.ACCEPTED)){
                        dataManager.updateAppointmentStatus(appointment.getId(),AppointmentStatus.DONE);
                    }
                    else if(appointment.getStatus().equals(AppointmentStatus.PENDING)){
                        dataManager.updateAppointmentStatus(appointment.getId(),AppointmentStatus.DECLINED);
                    }
                }
            }
        }
    }

    private  void initViews(View view){

        createAppointmentButton = view.findViewById(R.id.createAppointmentButton);

        currentButton = view.findViewById(R.id.currentButton);
        historyButton = view.findViewById(R.id.historyButton);

        appointmentAdapter = new AppointmentAdapter(view.getContext());
        appointmentAdapter.setOnItemClickListener(this);
        noAppointmentsText = view.findViewById(R.id.noAppointmentsText);

        appointmentRecyclerView = view.findViewById(R.id.appointmentRecyclerView);
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        appointmentRecyclerView.setAdapter(appointmentAdapter);

        setCurrentPressed(view);

        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentPressed(view);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHistoryPressed(view);

            }
        });

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientActivity patientActivity = (PatientActivity) getActivity();
                patientActivity.replaceFragment(FragmentType.APPOINTMENT_OPTIONS_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);

            }
        });

    }

    private void setHistoryPressed(View view){

        historyButton.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
        historyButton.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.blue));

        currentButton.setTextColor(ContextCompat.getColor(view.getContext(),R.color.blue));
        currentButton.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.white));

        if(patient.getHistoryAppointments().size()>0){
            appointmentAdapter.setCurrent(false);
            appointmentAdapter.setAppointments(patient.getHistoryAppointments());
            appointmentRecyclerView.setVisibility(View.VISIBLE);
            noAppointmentsText.setVisibility(View.INVISIBLE);
        }
        else{
            noAppointmentsText.setVisibility(View.VISIBLE);
            appointmentRecyclerView.setVisibility(View.INVISIBLE);
        }

    }

    private void setCurrentPressed(View view){
        currentButton.setTextColor(ContextCompat.getColor(view.getContext(),R.color.white));
        currentButton.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.blue));

        historyButton.setTextColor(ContextCompat.getColor(view.getContext(),R.color.blue));
        historyButton.setBackgroundColor(ContextCompat.getColor(view.getContext(),R.color.white));

        checkAppointmentDone();
        if(patient.getCurrentAppointments().size()>0){
            appointmentAdapter.setCurrent(true);
            appointmentAdapter.setDeleteMode(false);
            appointmentRecyclerView.setVisibility(View.VISIBLE);
            noAppointmentsText.setVisibility(View.INVISIBLE);
            appointmentAdapter.setAppointments(patient.getCurrentAppointments());
        }
        else{
            noAppointmentsText.setVisibility(View.VISIBLE);
            appointmentRecyclerView.setVisibility(View.INVISIBLE);
        }
    }




    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        if(appointmentAdapter.isDeleteMode() && patient.getCurrentAppointments().size()>0){
            appointmentAdapter.setDeleteMode(false);
        }
        else{
            PatientActivity patientActivity = (PatientActivity) navigation;
            patientActivity.getNavBar().undo();
        }
    }

}