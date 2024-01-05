package uom.android.physioassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.textservice.TextInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

import uom.android.physioassistant.R;

public class DoctorNavBar extends NavBar{

    private TextView notificationsCount;
    public DoctorNavBar(Context context) {
        super(context);
        init(context);
    }

    public DoctorNavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DoctorNavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context){

        inflate(context, R.layout.doctor_nav_bar_layout,this);

        NavButton homeButton = findViewById(R.id.homeButton);
        homeButton.setButtonType(ButtonType.DOCTOR_HOME);
        homeButton.setPressed();

        NavButton patientsButton = findViewById(R.id.patientsButton);
        patientsButton.setButtonType(ButtonType.DOCTOR_PATIENTS);

        NavButton calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setButtonType(ButtonType.DOCTOR_CALENDAR);

        NavButton notificationsButton = findViewById(R.id.notificationsButton);
        notificationsButton.setButtonType(ButtonType.DOCTOR_NOTIFICATIONS);

        TextView homeText = findViewById(R.id.homeNavText);
        TextView patientText = findViewById(R.id.patientNavText);
        TextView calendarText = findViewById(R.id.calendarNavText);
        TextView notificationText = findViewById(R.id.notificationNavText);

        NavItem homeItem = findViewById(R.id.homeLayout);
        NavItem patientItem = findViewById(R.id.patientsLayout);
        NavItem calendarItem = findViewById(R.id.calendarLayout);
        NavItem notificationsItem = findViewById(R.id.notificationLayout);

        homeItem.setNavButton(homeButton);
        homeItem.setNavText(homeText);
        homeItem.setPressed();

        patientItem.setNavButton(patientsButton);
        patientItem.setNavText(patientText);

        calendarItem.setNavButton(calendarButton);
        calendarItem.setNavText(calendarText);

        notificationsItem.setNavButton(notificationsButton);
        notificationsItem.setNavText(notificationText);
        notificationsCount = findViewById(R.id.notificationsCount);

        backStack = new Stack<>();

        currentButton = homeItem;

        buttons = new ArrayList<>();
        buttons.add(homeItem);
        buttons.add(patientItem);
        buttons.add(calendarItem);
        buttons.add(notificationsItem);

    }

    public void updateNotificationsCount(String num){
        notificationsCount.setText(num);
    }
    public void hideNotificationsCount(){
        notificationsCount.setVisibility(INVISIBLE);
    }

    public void showNotificationsCount(){
        notificationsCount.setVisibility(VISIBLE);
    }



}
