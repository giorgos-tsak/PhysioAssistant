package uom.android.physioassistant.activities.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.DoctorCreatedEvent;
import uom.android.physioassistant.backend.events.DoctorsLoadedEvent;
import uom.android.physioassistant.backend.responses.ErrorResponse;
import uom.android.physioassistant.models.Admin;
import uom.android.physioassistant.models.Doctor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateDoctorFragment extends Fragment implements ClearEditTexts{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText nameText;
    private EditText addressText;
    private EditText afmText;
    private EditText usernameText;
    private EditText passwordText;
    private TextView nameWarning;
    private TextView addressWarning;
    private TextView afmWarning;
    private TextView usernameWarning;
    private TextView passwordWarning;
    private MaterialButton createButton;
    private ArrayList<Doctor> doctors;

    private View view;
    public CreateDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateDoctorFragment newInstance(String param1, String param2) {
        CreateDoctorFragment fragment = new CreateDoctorFragment();
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
        view = inflater.inflate(R.layout.fragment_create_doctor, container, false);

        AdminActivity activity = (AdminActivity) getActivity();
        doctors = activity.getDoctors();

        activity.setButtonPressed(activity.getDoctorButton());
        activity.setButtonIdle(activity.getPhysioActionButton());


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
    public void onDoctorCreated(DoctorCreatedEvent event){
        Doctor doctor = event.getDoctor();
        showDoctorCreatedDialog(view.getContext());
        clearEditTexts();
        DataManager dataManager = new DataManager();
        dataManager.loadDoctors();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoctorsLoaded(DoctorsLoadedEvent event){
        doctors = (ArrayList<Doctor>) event.getDoctors();
    }



    private void initViews(View view) {

        nameText = view.findViewById(R.id.nameEditText);
        addressText = view.findViewById(R.id.addressEditText);
        afmText = view.findViewById(R.id.afmEditText);
        usernameText = view.findViewById(R.id.usernameEditText);
        passwordText = view.findViewById(R.id.passwordEditText);

        nameWarning = view.findViewById(R.id.nameWarning);
        addressWarning = view.findViewById(R.id.addressWarning);
        afmWarning = view.findViewById(R.id.afmWarning);
        usernameWarning = view.findViewById(R.id.usernameWarning);
        passwordWarning = view.findViewById(R.id.passwordWarning);

        createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFilled()){
                    Doctor doctor = new Doctor(usernameText.getText().toString(),passwordText.getText().toString(),afmText.getText().toString(),
                            nameText.getText().toString(),addressText.getText().toString());
                    if(!alreadyExists(doctor)){
                        DataManager dataManager = new DataManager();
                        dataManager.createDoctor(doctor);
                    }
                }
            }
        });

    }

    private void showDoctorCreatedDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Επιτυχία")
                .setMessage("Το φυσικοθεραπευτήριο δημιουργήθηκε επιτυχώς")
                .setPositiveButton("OK", null)
                .show();
    }

    private boolean alreadyExists(Doctor doctor){

        afmAlreadyExists(doctor.getAfm());
        usernameAlreadyExist(doctor.getUsername());



        return afmAlreadyExists(doctor.getAfm()) || usernameAlreadyExist(doctor.getUsername());
    }

    private boolean afmAlreadyExists(String afm) {
        for(Doctor doctor:doctors){
            if(afm.equals(doctor.getAfm())){
                afmWarning.setVisibility(View.VISIBLE);
                afmWarning.setText("*Το φυσικοθεραπευτήριο με ΑΦΜ: "+afm+" υπάρχει ήδη");
                return true;
            }
        }
        return false;
    }

    private boolean usernameAlreadyExist(String username) {
        for(Doctor doctor:doctors){
            if(username.equals(doctor.getUsername())){
                usernameWarning.setVisibility(View.VISIBLE);
                usernameWarning.setText("*Το username: "+username+" υπάρχει ήδη");
                return true;
            }
        }
        return false;
    }

    private boolean isAllFilled(){
        String name = nameText.getText().toString();
        String address = addressText.getText().toString();
        String afm = afmText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        boolean allFilled = true;
        setAllInvisible();

        if(name.equals("")){
            nameWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(address.equals("")){
            addressWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(afm.equals(""))
        {
            afmWarning.setText("*Εισάγεται ΑΦΜ");
            afmWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(username.equals("")){
            allFilled = false;
            usernameWarning.setText("*Εισάγεται Username");
            usernameWarning.setVisibility(View.VISIBLE);
        }
        if(password.equals("")){
            allFilled = false;
            passwordWarning.setVisibility(View.VISIBLE);
        }

        return  allFilled;
    }

    private void setAllInvisible(){
        nameWarning.setVisibility(View.GONE);
        addressWarning.setVisibility(View.GONE);
        afmWarning.setVisibility(View.GONE);
        usernameWarning.setVisibility(View.GONE);
        passwordWarning.setVisibility(View.GONE);
    }

    @Override
    public void clearEditTexts() {
        System.out.println("Cleared Doctor");
        nameText.setText("");
        addressText.setText("");
        afmText.setText("");
        usernameText.setText("");
        passwordText.setText("");
    }
}