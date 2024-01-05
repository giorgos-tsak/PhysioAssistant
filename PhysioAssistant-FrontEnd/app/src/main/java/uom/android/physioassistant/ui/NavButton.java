package uom.android.physioassistant.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NavButton extends androidx.appcompat.widget.AppCompatImageView {

    private ButtonType buttonType;
    public NavButton(@NonNull Context context) {
        super(context);
    }

    public NavButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NavButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setPressed(){
        this.setImageResource(buttonType.getPressed());
    }
    public void setIdle(){
        this.setImageResource(buttonType.getIdle());
    }

    public ButtonType getButtonType() {
        return buttonType;
    }

    public Fragment getFragment(){
        return buttonType.getFragment();
    }

    public void setButtonType(ButtonType buttonType) {
        this.buttonType = buttonType;
    }
}
