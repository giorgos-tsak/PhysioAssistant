package uom.android.physioassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import uom.android.physioassistant.activities.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private Button adminLoginBtn;
    private Button doctorLoginBtn;
    private Button patientLogintBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize button views
        adminLoginBtn = findViewById(R.id.admin_btn);
        doctorLoginBtn = findViewById(R.id.doctor_btn);
        patientLogintBtn = findViewById(R.id.patient_btn);
    }

    public void handleClick(View view) {
        Button clickedButton = (Button) view;
        if (clickedButton == this.adminLoginBtn) {
            Log.i("Main", "Admin Login button clicked.");
            Intent adminLoginIntent = new Intent(this, LoginActivity.class);
            startActivity(adminLoginIntent);
        }
        if (clickedButton == this.doctorLoginBtn) {
            Log.i("Main", "Doctor Login");
        }
        if (clickedButton == this.patientLogintBtn) {
            Log.i("Main", "Patient Login");
        }
    }
}