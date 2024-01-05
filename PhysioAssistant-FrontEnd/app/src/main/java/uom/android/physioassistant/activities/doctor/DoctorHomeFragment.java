package uom.android.physioassistant.activities.doctor;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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
import uom.android.physioassistant.activities.patient.PatientActivity;
import uom.android.physioassistant.adapters.ServiceAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView showAllText;
    private TextView serviceName;
    private TextView cardDateText;
    private TextView dateText;
    private TextView daysLeft;
    private TextView bookNow;
    private TextView totalPatients;
    private TextView totalAppointments;
    private TextView profitText;
    private CardView cardView;
    private ConstraintLayout noAppointmentLayout;
    private Doctor doctor;
    private ArrayList<PhysioAction> physioActions;
    private Handler handler;
    private Runnable eventRunnable;
    private boolean isEventRunning;

    public DoctorHomeFragment() {
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
    public static DoctorHomeFragment newInstance(String param1, String param2) {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_doctor, container, false);



        view = inflater.inflate(R.layout.fragment_home_doctor,container,false);

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctor = doctorActivity.getDoctor();

        initViews(view);
        startMinuteUpdate();
        checkAppointmentDone();
        doctor.calculateMonthlyProfit();

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
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByDoctorId(doctor.getAfm());

        } else {
            Log.e("Error", "Failed to update appointment status");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentsLoaded(AppointmentsLoadedEvent event){
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        doctor.setAppointments(appointments);
        updateProfit();
        updateViews();
    }

    private void updateProfit(){
        double profit = doctor.calculateMonthlyProfit();
        profitText.setText(profit+"$");

    }

    private void checkAppointmentDone() {

        LocalDateTime dateTime = LocalDateTime.now();
        LocalTime localTime = dateTime.toLocalTime();
        LocalDate localDate = dateTime.toLocalDate();

        DataManager dataManager = new DataManager();
        if(doctor.getCurrentAppointments().size()>0){
            for(Appointment appointment: doctor.getCurrentAppointments()){
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
        serviceName =  view.findViewById(R.id.serviceName);
        cardDateText = view.findViewById(R.id.cardDateText);
        dateText = view.findViewById(R.id.dateText);
        daysLeft = view.findViewById(R.id.daysLeft);
        bookNow = view.findViewById(R.id.bookNow);
        totalAppointments = view.findViewById(R.id.totalAppointments);
        totalPatients = view.findViewById(R.id.totalPatients);
        profitText=view.findViewById(R.id.profitText);

    }

    private void updateViews(){

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        physioActions = doctorActivity.getPhysioActions();

        dateText.setText(translateToGreek());

        totalPatients.setText(doctor.getPatients().size()+"");
        totalAppointments.setText(doctor.getCurrentAppointments().size()+"");

        showAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctorActivity.getNavBar().transition(FragmentType.DOCTOR_CALENDAR_FRAGMENT.getFragment());
            }
        });

        updateProfit();
        updateNextAppointmentCard();

    }


    private String translateToGreek(){
        LocalDate localDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", new Locale("el", "GR"));


        String greekDate = localDate.format(formatter);
        return  greekDate;
    }

    private void updateNextAppointmentCard(){
        DoctorActivity doctorActivity = (DoctorActivity) getActivity();


        if(doctor.getCurrentAppointments().size()>0) {

            Appointment nextAppointment = doctor.getCurrentAppointments().get(0);
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
                    doctorActivity.getNavBar().transition(FragmentType.DOCTOR_CALENDAR_FRAGMENT.getFragment());
                    doctorActivity.replaceFragment(FragmentType.APPOINTMENT_OPTIONS_DOCTOR_FRAGMENT.getFragment(), R.anim.enter_right_to_left,R.anim.exit_right_to_left);
                }
            });
        }
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
                    /*DataManager dataManager = new DataManager();
                    dataManager.loadAppointmentsByDoctorId(doctor.getAfm());*/
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