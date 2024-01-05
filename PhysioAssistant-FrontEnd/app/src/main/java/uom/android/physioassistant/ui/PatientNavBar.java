package uom.android.physioassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;

import uom.android.physioassistant.R;

public class PatientNavBar extends NavBar {

    public PatientNavBar(Context context) {
        super(context);
        init(context);
    }

    public PatientNavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PatientNavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context){

        inflate(context,R.layout.patient_nav_bar_layout,this);

        NavButton homeButton = findViewById(R.id.homeButton);
        homeButton.setButtonType(ButtonType.PATIENT_HOME);
        homeButton.setPressed();

        NavButton calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setButtonType(ButtonType.PATIENT_CALENDAR);

        TextView homeText = findViewById(R.id.homeNavText);
        TextView calendarText = findViewById(R.id.calendarNavText);

        NavItem homeItem = findViewById(R.id.homeLayout);
        NavItem calendarItem = findViewById(R.id.calendarLayout);

        homeItem.setNavButton(homeButton);
        homeItem.setNavText(homeText);

        calendarItem.setNavButton(calendarButton);
        calendarItem.setNavText(calendarText);

        backStack = new Stack<>();

        currentButton = homeItem;

        buttons = new ArrayList<>();
        buttons.add(homeItem);
        buttons.add(calendarItem);

    }

}
