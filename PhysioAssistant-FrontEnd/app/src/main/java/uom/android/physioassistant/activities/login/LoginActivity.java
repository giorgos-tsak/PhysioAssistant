package uom.android.physioassistant.activities.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uom.android.physioassistant.R;
import uom.android.physioassistant.activities.admin.AdminActivity;
import uom.android.physioassistant.activities.doctor.DoctorActivity;
import uom.android.physioassistant.activities.patient.PatientActivity;
import uom.android.physioassistant.backend.api.AuthenticationApi;
import uom.android.physioassistant.backend.requests.LoginRequest;
import uom.android.physioassistant.backend.responses.LoginResponse;
import uom.android.physioassistant.backend.retrofit.RetrofitService;
import uom.android.physioassistant.models.Doctor;
import uom.android.physioassistant.models.PhysioAction;
import uom.android.physioassistant.models.User;
import uom.android.physioassistant.models.UserType;

/**
 * This activity is responsible for the user login screen.
 * It handles user input and sends the data to the backend API for validation.
 * Once validated, the user is redirected to the appropriate screen based on their user type.
 */
public class LoginActivity extends AppCompatActivity {
    EditText usernameInput;
    EditText passwordInput;
    TextView errorMsg;
    Button loginBtn;
    Spinner selectUserType;
    RetrofitService retrofitService;
    AuthenticationApi authenticationApi;
    String selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        // Initialize RetrofitService and AuthenticationApi for making API calls
        this.retrofitService = new RetrofitService();
        this.authenticationApi = retrofitService.getRetrofit().create(AuthenticationApi.class);

        Intent intent = getIntent();

        configureSpinner();

        // Set onClickListener for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin(view);
            }
        });
    }

    // Helper method to initialize components
    public void initComponents() {
        // Find the views from the layout file and assign them to instance variables
        this.usernameInput = findViewById(R.id.username_input);
        this.passwordInput = findViewById(R.id.password_input);
        this.errorMsg = findViewById(R.id.error_msg);
        this.loginBtn = findViewById(R.id.login_btn);
        this.selectUserType = (Spinner) findViewById(R.id.select_user_spinner);
    }

    // Helper method to configure spinner
    public void configureSpinner() {
        // Set on selected listener
        this.selectUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Get the selected item from the spinner and assign it to an instance variable
                String selectedItem = selectUserType.getSelectedItem().toString();

                // Make sure its not the default value and then add the value to the selectedUser attribute
                if(!selectedItem.equalsIgnoreCase("--Επιλέξτε τύπο Χρήστη--"))
                    selectedUser = selectedItem;
                else
                    selectedUser = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Method to handle login button click
    public void handleLogin(View view) {
        // Retrieve the entered username and password
        String username = String.valueOf(usernameInput.getText());
        String password = String.valueOf(passwordInput.getText());

        Log.i("Login", "Attempting to login.");

        // Make sure the user provided a user type
        if (this.selectedUser == null)
            showErrorMessage("Παρακαλώ επιλέξτε τύπο χρήστη.");

        else {
            // Check if username and password were provided
            if (credentialsProvided(username, password)){
                // Create a LoginRequest object with the entered credentials
                LoginRequest loginRequest = new LoginRequest(username, password);

                // Call the appropriate login API method based on the selected user type
                this.loginBasedOnSelectedUser(loginRequest);
            }
            else {
                showErrorMessage("Παρακαλώ εισάγετε Username και Password.");
            }
        }

    }

    // Method to call the appropriate login API method based on the selected user type
    private void loginBasedOnSelectedUser(LoginRequest loginRequest) {
        // Check which user type was selected and call the corresponding loginUser method
        if (selectedUserIs(UserType.ADMIN))
            loginUser(AdminActivity.class, this.authenticationApi.adminLogin(loginRequest));

        else if (selectedUserIs(UserType.PATIENT))
            loginUser(PatientActivity.class, this.authenticationApi.patientLogin(loginRequest));

        else if (selectedUserIs(UserType.DOCTOR))
            loginUser(DoctorActivity.class, this.authenticationApi.doctorLogin(loginRequest));
    }

    private boolean selectedUserIs(UserType userType) {
        return this.selectedUser.equalsIgnoreCase(userType.toString());
    }

    // Helper method to make sure the credential input fields are not empty
    private boolean credentialsProvided(String givenUsername, String givenPass) {
        return !TextUtils.isEmpty(givenUsername) && !TextUtils.isEmpty(givenPass);
    }

    // This method shows an error message to the user for a short amount of time.
    private void showErrorMessage(String message) {
        errorMsg.setText(message);
        errorMsg.setVisibility(View.VISIBLE);
        errorMsg.postDelayed(() -> errorMsg.setVisibility(View.INVISIBLE), 3000); // 3 seconds delay
    }

    // This method sends an API request to login the user and starts a new activity if the login is successful.
    private void loginUser(final Class<? extends AppCompatActivity> activityClass, final Call<LoginResponse> apiMethod) {
        apiMethod.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.i("Login", "Api call done.");   // Log that the API call was completed

                System.out.println(response.body());
                // Get the login response from the API call
                LoginResponse loginResponse = response.body();

                // Check if login is successful
                if (response.isSuccessful()) {
                    Log.i("Login", "Login Successful");

                    // Create a new Intent for the next activity
                    User user = response.body().getUser();
                    Intent next_activity = new Intent(LoginActivity.this, activityClass);
                    next_activity.putExtra("user",user);
                    startActivity(next_activity);
                } else {
                    Log.i("Login", "Login Failed");

                    // Show an error message to ui
                    showErrorMessage("Λάθος username και/ή password.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // Log that the API call failed
                Log.w("Login Error", "Api call failed");
                // Log the error message
                Log.e("Error", t.getMessage());
                showErrorMessage("Σφάλμα Σύνδεσης. Προσπαθήστε ξανά αργότερα.");
            }
        });
    }

}