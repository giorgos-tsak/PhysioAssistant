package uom.android.physioassistant.activities.doctor.appointment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import uom.android.physioassistant.MainActivity;
import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.activities.doctor.DoctorActivity;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.AppointmentCreatedEvent;
import uom.android.physioassistant.backend.events.AppointmentUpdatedEvent;
import uom.android.physioassistant.backend.events.AppointmentsLoadedEvent;
import uom.android.physioassistant.backend.requests.AppointmentRequest;
import uom.android.physioassistant.models.Appointment;
import uom.android.physioassistant.models.AppointmentStatus;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.Patient;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentSummaryDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentSummaryDoctorFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView nameText,addressText,serviceText,costText,dateText,timeText;
    private MaterialButton bookButton;
    private Bundle bundle;
    private Doctor doctor;
    private Patient patient;
    private PhysioAction service;

    public AppointmentSummaryDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentSummaryDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentSummaryDoctorFragment newInstance(String param1, String param2) {
        AppointmentSummaryDoctorFragment fragment = new AppointmentSummaryDoctorFragment();
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
        View view =  inflater.inflate(R.layout.fragment_appointment_summary, container, false);


        bundle = getArguments();
        if(bundle!=null){
            initViews(view);
        }

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
    public void onAppointmentCreated(AppointmentCreatedEvent event){
        Appointment appointment = event.getAppointment();
        DataManager dataManager = new DataManager();
        dataManager.updateAppointmentStatus(appointment.getId(),AppointmentStatus.ACCEPTED);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentUpdated(AppointmentUpdatedEvent event){
        if(event.isSuccess()){
            DataManager dataManager = new DataManager();
            dataManager.loadAppointmentsByDoctorId(doctor.getAfm());
        }
        else{
            Log.e("ERROR", "onAppointmentUpdated: falied to update Appointment status" );
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointmentsLoaded(AppointmentsLoadedEvent event){
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) event.getAppointments();
        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.getDoctor().setAppointments(appointments);
        showSuccessDialog();
        returnToCalendar();
    }

    public void initViews(View view){

        Bundle optionsBundle = bundle.getBundle("options_bundle");
        patient = (Patient) optionsBundle.getSerializable("patient");
        doctor = (Doctor) optionsBundle.getSerializable("doctor");
        service = (PhysioAction) optionsBundle.getSerializable("physio_action");
        LocalDate date = (LocalDate) bundle.getSerializable("date");
        LocalTime time = (LocalTime) bundle.getSerializable("time");


        nameText = view.findViewById(R.id.physioName);
        nameText.setText(doctor.getName());

        addressText = view.findViewById(R.id.address);
        addressText.setText(doctor.getAddress());

        serviceText = view.findViewById(R.id.doctorName);
        serviceText.setText(service.getName());

        dateText = view.findViewById(R.id.date);
        dateText.setText(formatDate(date));

        timeText = view.findViewById(R.id.time);
        timeText.setText(time.toString());

        costText = view.findViewById(R.id.cost);
        costText.setText(service.getFormattedCost());

        bookButton = view.findViewById(R.id.bookButton);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager dataManager = new DataManager();
                AppointmentRequest appointmentRequest = new AppointmentRequest(doctor.getAfm(),patient.getAmka(),service.getCode(),date.toString(),time.toString());
                dataManager.createAppointment(appointmentRequest);
            }
        });


    }

    private void returnToCalendar(){
        DoctorActivity doctorActivity = (DoctorActivity) getActivity();
        doctorActivity.replaceFragment(FragmentType.DOCTOR_CALENDAR_FRAGMENT.getFragment(), R.anim.fade_in,R.anim.fade_out);

    }
    private  String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private void showSuccessDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setTitle("Επιτυχία")
                .setMessage("Το ραντεβού προγραμματίστηκε επιτυχώς!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        DoctorActivity activity = (DoctorActivity) navigation;
        activity.replaceFragment(FragmentType.APPOINTMENT_CALENDAR_DOCTOR_FRAGMENT.getFragment(), R.anim.enter_left_to_right,R.anim.exit_left_to_right);
    }

}