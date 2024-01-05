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

import java.util.ArrayList;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.backend.datamanager.DataManager;
import uom.android.physioassistant.backend.events.DoctorCreatedEvent;
import uom.android.physioassistant.backend.events.DoctorsLoadedEvent;
import uom.android.physioassistant.backend.events.PhysioActionCreatedEvent;
import uom.android.physioassistant.backend.events.PhysioActionsLoadedEvent;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.PhysioAction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateServiceFragment extends Fragment  implements ClearEditTexts{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText codeText;
    private EditText nameText;
    private EditText descText;
    private EditText costText;
    private EditText imageURLText;
    private TextView codeWarning;
    private TextView nameWarning;
    private TextView descWarning;
    private TextView costWarning;
    private MaterialButton createButton;
    private ArrayList<PhysioAction> physioActions;
    private View view;

    public CreateServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateServiceFragment newInstance(String param1, String param2) {
        CreateServiceFragment fragment = new CreateServiceFragment();
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
        view = inflater.inflate(R.layout.fragment_create_service, container, false);

        AdminActivity activity = (AdminActivity) getActivity();
        physioActions = activity.getPhysioActions();

        activity.setButtonIdle(activity.getDoctorButton());
        activity.setButtonPressed(activity.getPhysioActionButton());

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
    public void onPhysioActionCreated(PhysioActionCreatedEvent event){
        PhysioAction physioAction = event.getPhysioAction();
        showServiceCreatedDialog(view.getContext());
        clearEditTexts();
        DataManager dataManager = new DataManager();
        dataManager.loadPhysioActions();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPhysioActionsLoaded(PhysioActionsLoadedEvent event){
        physioActions = (ArrayList<PhysioAction>) event.getPhysioActions();
    }


    private void initViews(View view) {

        codeText = view.findViewById(R.id.codeEditText);
        nameText = view.findViewById(R.id.nameEditText);
        descText = view.findViewById(R.id.descEditText);
        costText = view.findViewById(R.id.costEditText);
        imageURLText = view.findViewById(R.id.urlEditText);

        codeWarning = view.findViewById(R.id.codeWarning);
        nameWarning = view.findViewById(R.id.nameWarning);
        descWarning = view.findViewById(R.id.descWarning);
        costWarning = view.findViewById(R.id.costWarning);

        createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Click");
                if(isAllFilled()){
                    PhysioAction physioAction = new PhysioAction(codeText.getText().toString(),nameText.getText().toString(),
                            descText.getText().toString(),Double.parseDouble(costText.getText().toString()),imageURLText.getText().toString());
                    if(!codeAlreadyExist(physioAction.getCode())){
                        DataManager dataManager = new DataManager();
                        dataManager.createPhysioAction(physioAction);
                    }
                }
            }
        });
    }

    private void showServiceCreatedDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Επιτυχία")
                .setMessage("Η υπηρεσία δημιουργήθηκε επιτυχώς")
                .setPositiveButton("OK", null)
                .show();
    }

    private boolean codeAlreadyExist(String code) {
        for(PhysioAction physioAction: physioActions){
            if(code.equals(physioAction.getCode())){
                codeWarning.setVisibility(View.VISIBLE);
                codeWarning.setText("*Η υπηρεσία με κωδικό: "+code+" υπάρχει ήδη");
                return true;
            }
        }
        return false;
    }

    private boolean isAllFilled(){
        String code = codeText.getText().toString();
        String name = nameText.getText().toString();
        String desc = descText.getText().toString();
        String cost = costText.getText().toString();
        boolean allFilled = true;
        setAllInvisible();

        if(code.equals("")){
            codeWarning.setText("*Εισάγεται κωδικό");
            codeWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(name.equals("")){
            nameWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(desc.equals("")){
            descWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }
        if(cost.equals(""))
        {
            costWarning.setVisibility(View.VISIBLE);
            allFilled = false;
        }

        return  allFilled;
    }


    private void setAllInvisible(){
        codeWarning.setVisibility(View.GONE);
        nameWarning.setVisibility(View.GONE);
        descWarning.setVisibility(View.GONE);
        costWarning.setVisibility(View.GONE);
    }

    @Override
    public void clearEditTexts() {
        codeText.setText("");
        nameText.setText("");
        descText.setText("");
        costText.setText("");
        imageURLText.setText("");

    }
}