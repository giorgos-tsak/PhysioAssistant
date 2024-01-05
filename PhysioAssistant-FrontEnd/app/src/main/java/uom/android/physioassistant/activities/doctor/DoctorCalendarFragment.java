package uom.android.physioassistant.activities.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.threeten.bp.LocalDate;
import java.util.ArrayList;
import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.activities.patient.PatientActivity;
import uom.android.physioassistant.adapters.DoctorAppointmentAdapter;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorCalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorCalendarFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CalendarView calendarView;
    private TextView noAppointmentsText;
    private RecyclerView recyclerView;
    private DoctorAppointmentAdapter doctorAppointmentAdapter;
    private RelativeLayout appointmentLayout;
    private FloatingActionButton createAppointmentButton;
    private LocalDate selectedDate;
    private Doctor doctor;

    public DoctorCalendarFragment() {
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
    public static DoctorCalendarFragment newInstance(String param1, String param2) {
        DoctorCalendarFragment fragment = new DoctorCalendarFragment();
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
        View view = inflater.inflate(R.layout.fragment_calendar_doctor, container, false);

        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.getNavBar().setVisibility(View.VISIBLE);
        doctor = doctorActivity.getDoctor();

        selectedDate = LocalDate.now();

        DataManager dataManager = new DataManager();
        dataManager.loadAppointmentsByDoctorId(doctor.getAfm());

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
    public void onAppointmentsLoaded(AppointmentsLoadedEvent event){
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        doctor.setAppointments(appointments);
        updateAppointmentAdapter(selectedDate);
        animateLayout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentStatusUpdated(AppointmentUpdatedEvent event){
        if(event.isSuccess()){
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByDoctorId(doctor.getAfm());
        }
    }


    private void initViews(View view) {


        noAppointmentsText = view.findViewById(R.id.noAppointmentsText);

        appointmentLayout = view.findViewById(R.id.appointmentLayout);

        doctorAppointmentAdapter = new DoctorAppointmentAdapter(view.getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        calendarView = view.findViewById(R.id.calendarView2);

        createAppointmentButton = view.findViewById(R.id.createAppointmentButton);
        createAppointmentButton.setColorFilter(ContextCompat.getColor(view.getContext(),R.color.blue));


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = LocalDate.of(year,month+1,dayOfMonth);
                doctorAppointmentAdapter.setDeleteMode(false);
                updateAppointmentAdapter(selectedDate);
                animateLayout();

            }
        });

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorActivity doctorActivity = (DoctorActivity) getActivity();
                doctorActivity.replaceFragment(FragmentType.APPOINTMENT_OPTIONS_DOCTOR_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    private void updateAppointmentAdapter(LocalDate date){

        if(doctor.getAppointmentsByDate(date).size()==0){
            noAppointmentsText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            noAppointmentsText.setVisibility(View.INVISIBLE);
            doctorAppointmentAdapter.setAppointments(doctor.getAppointmentsByDate(date));
            doctorAppointmentAdapter.setOnDeletePressedListener(new DoctorAppointmentAdapter.OnDeletePressedListener() {
                @Override
                public void onDeletePressed(Appointment appointment) {
                    doctorAppointmentAdapter.setDeleteMode(false);
                    DataManager dataManager = new DataManager();
                    dataManager.updateAppointmentStatus(appointment.getId(), AppointmentStatus.DECLINED);
                }
            });
            recyclerView.setAdapter(doctorAppointmentAdapter);
        }
    }


    private void animateLayout() {

        appointmentLayout.setVisibility(View.VISIBLE);

        Animation slideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);

        slideAnimation.setDuration(500);

        appointmentLayout.startAnimation(slideAnimation);
    }


    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        if(doctorAppointmentAdapter.isDeleteMode() && doctor.getAppointmentsByDate(selectedDate).size()>0){
            doctorAppointmentAdapter.setDeleteMode(false);
        }
        else{
            DoctorActivity doctorActivity = (DoctorActivity) navigation;
            doctorActivity.getNavBar().undo();
        }
    }
}