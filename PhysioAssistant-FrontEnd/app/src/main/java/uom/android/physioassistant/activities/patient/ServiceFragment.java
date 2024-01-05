package uom.android.physioassistant.activities.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.FragmentNavigation;
import uom.android.physioassistant.activities.OnBackPressedListener;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.ui.FragmentType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment implements OnBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView serviceImage;
    private ImageView backButton;
    private TextView serviceName,serviceDescription,servicePrice;
    private PhysioAction physioAction;
    private String code;
    public ServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
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
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            physioAction = (PhysioAction) bundle.getSerializable("physio_action");
            initViews(view);
        }

        return view;
    }


    private void initViews(View view) {

        serviceName = view.findViewById(R.id.doctorName);
        serviceName.setText(physioAction.getName());

        serviceDescription = view.findViewById(R.id.serviceDescription);
        serviceDescription.setText(physioAction.getDescription());

        servicePrice = view.findViewById(R.id.servicePrice);
        servicePrice.setText(String.valueOf(physioAction.getCostPerSession())+"$");

        serviceImage = view.findViewById(R.id.serviceImage);
        String imageURL = "https://img.freepik.com/premium-photo/spa-arrangement-with-towel-soap-salt_23-2148268482.jpg?w=2000";
        Glide.with(view.getContext()).asBitmap().load(physioAction.getImageURL()).error(R.drawable.ic_failed_to_load_image).into(serviceImage);

        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed(FragmentNavigation navigation) {
        PatientActivity activity = (PatientActivity) navigation;
        activity.replaceFragment(FragmentType.PATIENT_HOME_FRAGMENT.getFragment(), R.anim.fade_in, R.anim.fade_out);
        activity.getNavBar().setVisibility(View.VISIBLE);
    }
}