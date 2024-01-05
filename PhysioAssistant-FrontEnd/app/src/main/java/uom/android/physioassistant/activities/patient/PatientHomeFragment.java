package uom.android.physioassistant.activities.patient;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Locale;

import uom.android.physioassistant.R;
import uom.android.physioassistant.adapters.ServiceAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView serviceRecyclerView;
    private TextView showAllText;
    private TextView serviceName;
    private TextView cardDateText;
    private TextView dateText;
    private TextView address;
    private TextView daysLeft;
    private TextView bookNow;
    private CardView cardView;
    private ConstraintLayout noAppointmentLayout;
    private ServiceAdapter serviceAdapter;
    private Patient patient;
    private ArrayList<PhysioAction> physioActions;
    private View view;
    private Handler handler;
    private Runnable eventRunnable;
    private boolean isEventRunning;

    public PatientHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientHomeFragment newInstance(String param1, String param2) {
        PatientHomeFragment fragment = new PatientHomeFragment();
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
        view = inflater.inflate(R.layout.fragment_home,container,false);

        initViews(view);
        startMinuteUpdate();
        checkAppointmentDone();


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        isEventRunning=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        stopMinuteUpdate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentStatusUpdated(AppointmentUpdatedEvent event) {
        if (event.isSuccess()) {
            System.out.println("updated home");
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByPatientId(patient.getAmka());

        } else {
            Log.e("Error", "Failed to update appointment status");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentsLoaded(AppointmentsLoadedEvent event){
        System.out.println("loaded home");
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        patient.setAppointments(appointments);
        updateViews();
    }

    private void checkAppointmentDone() {
        PatientActivity patientActivity = (PatientActivity) getActivity();
        patient = patientActivity.getPatient();

        LocalDateTime dateTime = LocalDateTime.now();
        LocalTime localTime = dateTime.toLocalTime();
        LocalDate localDate = dateTime.toLocalDate();

        DataManager dataManager = new DataManager();
        if(patient.getAcceptedAppointments().size()>0){
            for(Appointment appointment: patient.getAcceptedAppointments()){
                if(localDate.isAfter(appointment.getLocalDate())){
                    dataManager.updateAppointmentStatus(appointment.getId(), AppointmentStatus.DONE);
                }
                else if(localDate.equals(appointment.getLocalDate())){
                    if(localTime.isAfter(appointment.getLocalTime())){
                        dataManager.updateAppointmentStatus(appointment.getId(),AppointmentStatus.DONE);
                    }

                }
            }
        }

        updateViews();

    }

    private void initViews(View view){
        showAllText = view.findViewById(R.id.showAllText);
        cardView = view.findViewById(R.id.cardView);
        noAppointmentLayout = view.findViewById(R.id.noAppointmentLayout);
        serviceName =  view.findViewById(R.id.doctorName);
        cardDateText = view.findViewById(R.id.cardDateText);
        dateText = view.findViewById(R.id.dateText);
        address = view.findViewById(R.id.address);
        daysLeft = view.findViewById(R.id.daysLeft);
        bookNow = view.findViewById(R.id.bookNow);
    }

    private void updateViews(){

        PatientActivity patientActivity = (PatientActivity) getActivity();
        physioActions = patientActivity.getPhysioActions();

        dateText.setText(translateToGreek());
        showAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientActivity patientActivity = (PatientActivity) getActivity();
                patientActivity.getNavBar().transition(FragmentType.CALENDAR_FRAGMENT.getFragment());
            }
        });


        updateNextAppointmentCard();
        initRecyclerView(view);

    }

    private void initRecyclerView(View view){
        serviceAdapter = new ServiceAdapter(view.getContext());
        serviceAdapter.setHomeFragment(this);
        serviceAdapter.setPhysioActions(physioActions);
        serviceRecyclerView = view.findViewById(R.id.servicesRecView);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));
        serviceRecyclerView.setAdapter(serviceAdapter);
    }

    private String translateToGreek(){
        LocalDate localDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("el", "GR"));


        String greekDate = localDate.format(formatter);
        return  greekDate;
    }

    private void updateNextAppointmentCard(){
        PatientActivity patientActivity = (PatientActivity) getActivity();

        if(patient.getAcceptedAppointments().size()>0) {
            Appointment nextAppointment = patient.getAcceptedAppointments().get(0);
            cardView.setVisibility(View.VISIBLE);
            noAppointmentLayout.setVisibility(View.INVISIBLE);

            serviceName.setText(nextAppointment.getPhysioAction().getName());

            cardDateText.setText(nextAppointment.getFormattedDate()+" "+ nextAppointment.getLocalTime());


            long daysDifference = LocalDate.now().until(nextAppointment.getLocalDate(), ChronoUnit.DAYS);
            if(daysDifference>0){
                daysLeft.setText("Απομένουν "+daysDifference+" ημέρες");
            }
            else {
                Duration duration = Duration.between(LocalTime.now(), nextAppointment.getLocalTime());
                long hoursDifference = duration.toHours();
                if(hoursDifference>0){
                    daysLeft.setText("Απομένουν "+ hoursDifference +" ώρες");
                }
                else{
                    long minutesDifference = duration.toMinutesPart()+1;
                    daysLeft.setText("Απομένουν "+minutesDifference+" λεπτά");
                }

            }
        }
        else{
            noAppointmentLayout.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.INVISIBLE);
            bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    patientActivity.getNavBar().transition(FragmentType.CALENDAR_FRAGMENT.getFragment());
                    patientActivity.replaceFragment(FragmentType.APPOINTMENT_OPTIONS_FRAGMENT.getFragment(), R.anim.enter_right_to_left,R.anim.exit_right_to_left);
                }
            });
        }
    }

    public Bundle serviceBundle(PhysioAction physioAction){
        Bundle bundle = new Bundle();
        bundle.putSerializable("physio_action",physioAction);
        return bundle;
    }

    private LocalTime previousTime;
    private void startMinuteUpdate() {
        if(handler==null){
            handler = new Handler();
            previousTime = LocalTime.now();
        }
        if(eventRunnable==null){
            eventRunnable = new Runnable() {
                @Override
                public void run() {
                    LocalTime currentTime = LocalTime.now();
                    int minuteDifference = currentTime.getMinute()-previousTime.getMinute();
                    if(minuteDifference==1 || minuteDifference==-59){
                        previousTime = currentTime;
                        checkAppointmentDone();
                        updateNextAppointmentCard();
                    }

                    handler.postDelayed(this,  1000);
                }
            };
        }

        handler.postDelayed(eventRunnable,  1000);

    }

    private void stopMinuteUpdate() {
        handler.removeCallbacks(eventRunnable);
    }


}