package uom.android.physioassistant.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uom.android.physioassistant.R;

public class DropDown extends RelativeLayout {

    protected ImageView optionImage;
    protected ImageView arrowImage;
    protected TextView optionText;
    protected RecyclerView recyclerView;
    protected RelativeLayout collapsed;
    protected RecyclerView.Adapter adapter;
    protected boolean isExpanded = false;
    protected boolean filled = false;
    public DropDown(Context context) {
        super(context);
        init(context,null);
    }

    public DropDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DropDown(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        inflate(context, R.layout.dropdown_layout, this);

        optionImage = findViewById(R.id.optionImage);
        optionText  = findViewById(R.id.optionName);
        arrowImage = findViewById(R.id.arrowImage);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        collapsed = findViewById(R.id.dropdownCollapsed);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDown);
            String customText = a.getString(R.styleable.DropDown_optionName);
            int customImage = a.getResourceId(R.styleable.DropDown_optionImage, 0);
            a.recycle();

            optionText.setText(customText);
            if (customImage != 0) {
                optionImage.setImageResource(customImage);
            }
        }
    }

    public void setExpanded(){
        isExpanded = true;
        fadeIn();
        scrollToTop();
        this.bringToFront();
        arrowImage.setImageResource(R.drawable.ic_up_arrow);
    }

    public void setCollapsed(){
        isExpanded = false;
        fadeOut();
        arrowImage.setImageResource(R.drawable.ic_down_arrow);
    }

    public void fadeIn(){
        recyclerView.setVisibility(VISIBLE);
        recyclerView.setAlpha(0);
        recyclerView.animate().alpha(1).setDuration(200).start();
    }

    public void fadeOut(){
        recyclerView.animate().alpha(0).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(GONE);
            }
        }).start();

    }


    public void scrollToTop(){
        recyclerView.scrollToPosition(0);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public RelativeLayout getCollapsed() {
        return collapsed;
    }

    public void setText(String text){
        optionText.setText(text);
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
