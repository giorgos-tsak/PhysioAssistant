package uom.android.physioassistant.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import uom.android.physioassistant.R;

public class NavItem extends ConstraintLayout {

    private NavButton navButton;
    private TextView navText;

    public NavItem(@NonNull Context context) {
        super(context);
    }

    public NavItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NavItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPressed(){
        navButton.setPressed();
        navText.setTextColor(ContextCompat.getColor(getContext(), R.color.button_pressed));
    }
    public void setIdle(){
        navButton.setIdle();
        navText.setTextColor(ContextCompat.getColor(getContext(),R.color.button_idle));
    }

    public void setNavButton(NavButton navButton) {
        this.navButton = navButton;
    }

    public void setNavText(TextView navText) {
        this.navText = navText;
    }

    public NavButton getNavButton() {
        return navButton;
    }
}
