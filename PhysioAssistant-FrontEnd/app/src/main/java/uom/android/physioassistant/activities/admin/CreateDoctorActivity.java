package uom.android.physioassistant.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uom.android.physioassistant.R;
import uom.android.physioassistant.backend.api.DoctorApi;
import uom.android.physioassistant.backend.retrofit.RetrofitService;
import uom.android.physioassistant.models.Doctor;

public class CreateDoctorActivity extends AppCompatActivity {
    private EditText physioCenterNameInput;
    private EditText addressInput;
    private EditText afmInput;
    private EditText usernameInput;
    private EditText passwordInput;
    private TextView errorMsg;
    private RetrofitService retrofitService;
    private DoctorApi doctorApi;
    private Button cancelBtn;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity layout using the corresponding XML file
        setContentView(R.layout.activity_create_doctor);
        init();

        this.retrofitService = new RetrofitService();
        this.doctorApi = retrofitService.getRetrofit().create(DoctorApi.class);
    }

    // Initialize ui components
    private void init() {
        this.errorMsg = findViewById(R.id.create_doctor_msg);

        this.physioCenterNameInput = findViewById(R.id.doctor_name_input);
        this.addressInput = findViewById(R.id.address_input);
        this.afmInput = findViewById(R.id.afm_input);
        this.usernameInput = findViewById(R.id.doctor_username_input);
        this.passwordInput = findViewById(R.id.doctor_pass_input);

        this.cancelBtn = findViewById(R.id.cancel_btn);
        this.addBtn = findViewById(R.id.add_service_btn);
    }

    // This method is called when the user clicks on the cancel button
    public void cancelBtnClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Επιβεβαίωση Ακύρωσης")
                .setMessage("Τα δεδομένα δεν αποθηκεύτηκαν. Είστε σίγουροι ότι θέλετε να ακυρώσετε;")
                .setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("ΟΧΙ", null)
                .show();
    }

    // This method is called when the user clicks on the add button
    public void addBtnClicked(View view) {
        Log.i("CreateServiceActivity", "Attempting to add new doctor ");

        // Get the input text from the three EditText views
        String doctorName = String.valueOf(this.physioCenterNameInput.getText());
        String address = String.valueOf(this.addressInput.getText());
        String afm = String.valueOf(this.afmInput.getText());
        String username = String.valueOf(this.usernameInput.getText());
        String password = String.valueOf(this.passwordInput.getText());

        // Create the Doctor object to send to the backend
        Doctor new_doctor = new Doctor(doctorName, address, afm, username, password);

        if (detailsAreProvided(doctorName, address, afm, username, password)) {
            callTheApiToAddDoctor(new_doctor);
            Log.i("CreateServiceActivity", "Added Doctor: " + new_doctor);
        }
        else {
            showMessage("Ελλειπή Στοιχεία Γιατρού", R.color.error_red);
        }

    }

    private void callTheApiToAddDoctor(Doctor doctor) {
        this.doctorApi.createDoctor(doctor)
                .enqueue(new Callback<Doctor>() {
                    @Override
                    public void onResponse(Call<Doctor> call, Response<Doctor> response) {
                        Doctor addedDoctor = response.body();

                        if (response.isSuccessful()) {
                            Log.i("Create Doctor", "Doctor added");

                            showMessage("Το Φυσικοθεραπευτήριο αποθηκεύτηκε!", R.color.success_green);
                            Toast.makeText(getApplicationContext(), "Το Φυσικοθεραπευτήριο αποθηκεύτηκε!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else if (response.code() == 409 ){
                            showMessage("Το Φυσικοθεραπευτήριο με Username: " + doctor.getAfm() + " υπάρχει ήδη.", R.color.error_red);
                        }
                        else {
                            showMessage("Το Φυσικοθεραπευτήριο δεν αποθηκεύτηκε.", R.color.error_red);
                        }
                    }

                    @Override
                    public void onFailure(Call<Doctor> call, Throwable t) {
                        showMessage("Σφάλμα Σύνδεσης. Προσπαθήστε ξανά αργότερα.", R.color.error_red);
                    }
                });
    }

    private void showMessage(String message, int color) {
        errorMsg.setText(message);
        errorMsg.setVisibility(View.VISIBLE);

        int textColor = ContextCompat.getColor(this, color);
        errorMsg.setTextColor(textColor);

        errorMsg.postDelayed(() -> errorMsg.setVisibility(View.INVISIBLE), 3000); // 3 seconds delay
    }

    private boolean detailsAreProvided(String name, String address, String afm, String username, String password) {
        return !TextUtils.isEmpty(name) &&
                !TextUtils.isEmpty(address) &&
                !TextUtils.isEmpty(afm) &&
                !TextUtils.isEmpty(username) &&
                !TextUtils.isEmpty(password);
    }
}