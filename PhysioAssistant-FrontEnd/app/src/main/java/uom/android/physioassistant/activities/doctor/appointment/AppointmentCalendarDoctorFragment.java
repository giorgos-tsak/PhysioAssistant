package uom.android.physioassistant.activities.doctor.appointment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.adapters.TimeAdapter;
import uom.android.physioassistant.activities.doctor.DoctorActivity;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentCalendarDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentCalendarDoctorFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private TimeAdapter timeAdapter;
    private RelativeLayout timeLayout;
    private TextView dateText;
    private TextView warningText;
    private TextView noTimeText;
    private MaterialButton nextButton;
    private Bundle optionsBundle;
    private Doctor doctor;

    public AppointmentCalendarDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentCalendarDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentCalendarDoctorFragment newInstance(String param1, String param2) {
        AppointmentCalendarDoctorFragment fragment = new AppointmentCalendarDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_appointment_calendar_doctor, container, false);

        optionsBundle = getArguments();
        if(optionsBundle!=null){
            Patient patient = (Patient) optionsBundle.getSerializable("patient");
            doctor = (Doctor) optionsBundle.getSerializable("doctor");
            PhysioAction physioAction = (PhysioAction) optionsBundle.getSerializable("physio_action");
        }

        initViews(view);
        DataManager dataManager = new DataManager();
        dataManager.loadAppointmentsByDoctorId(doctor.getUsername());

        return view;
    }


    public void initViews(View view){

        timeAdapter = new TimeAdapter(view.getContext());

        recyclerView = view.findViewById(R.id.timeRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 3));

        timeLayout = view.findViewById(R.id.timeLayout);

        dateText = view.findViewById(R.id.cardDateText);

        warningText = view.findViewById(R.id.warningText);

        noTimeText = view.findViewById(R.id.noTimeText);

        calendarView = view.findViewById(R.id.calendarView);

        nextButton = view.findViewById(R.id.createPatientButton);
        updateViews();

    }

    private void updateViews(){

        timeAdapter.setTimes(doctor.getAvailableTimeSlots(LocalDate.now()));
        recyclerView.setAdapter(timeAdapter);
        checkValidDate(LocalDate.now());

        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = dateTimeFormatter.format(localDate);
        dateText.setText(formattedDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                timeAdapter.setSelectedButtonPos(-1);
                LocalDate selectedDate  = LocalDate.of(year,month+1,dayOfMonth);
                timeAdapter.setTimes(doctor.getAvailableTimeSlots(selectedDate));
                recyclerView.scrollToPosition(0);
                checkValidDate(selectedDate);
                dateText.setText(formatDate(selectedDate));

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeAdapter.getSelectedTime()!=null){
                    DoctorActivity doctorActivity = (DoctorActivity) getActivity();
                    doctorActivity.replaceFragment(FragmentType.APPOINTMENT_DOCTOR_SUMMARY.getFragment(), R.anim.enter_right_to_left,R.anim.exit_right_to_left,
                            calendarBundle(optionsBundle,convertToLocalDate(dateText.getText().toString()),timeAdapter.getSelectedTime()));


                }
                else{
                    Log.e("Error", "Select time" );
                }

            }
        });
    }
    public void checkAvailableTimes(LocalDate date){
        if(doctor.getAvailableTimeSlots(date).size()==0){
            noTimeText.setVisibility(View.VISIBLE);
            timeLayout.setVisibility(View.INVISIBLE);
        }
        else{
            noTimeText.setVisibility(View.INVISIBLE);
            animateLayout();
        }
    }

    public void checkValidDate(LocalDate date){
        if(date.isBefore(LocalDate.now())){
            timeLayout.setVisibility(View.INVISIBLE);
            warningText.setVisibility(View.VISIBLE);
            noTimeText.setVisibility(View.INVISIBLE);
        }
        else{
            checkAvailableTimes(date);
            warningText.setVisibility(View.INVISIBLE);
        }
    }

    public LocalDate convertToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        LocalDate localDate = LocalDate.parse(date,formatter);
        return localDate;
    }

    public String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private void animateLayout() {

        timeLayout.setVisibility(View.VISIBLE);

        Animation slideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);

        slideAnimation.setDuration(500);

        timeLayout.startAnimation(slideAnimation);

    }

    public Bundle calendarBundle(Bundle optionsBundle, LocalDate date, LocalTime time){
        Bundle bundle = new Bundle();
        bundle.putBundle("options_bundle",optionsBundle);
        bundle.putSerializable("date",date);
        bundle.putSerializable("time",time);
        return  bundle;
    }

    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        DoctorActivity activity = (DoctorActivity) navigation;
        activity.replaceFragment(FragmentType.APPOINTMENT_OPTIONS_DOCTOR_FRAGMENT.getFragment(), R.anim.enter_left_to_right,R.anim.exit_left_to_right);
    }

}