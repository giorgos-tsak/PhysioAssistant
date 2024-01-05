package uom.android.physioassistant.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import uom.android.physioassistant.R;

public interface FragmentNavigation {

    public void replaceFragment(Fragment fragment, int enterAnimation, int exitAnimation);
    public void replaceFragment(Fragment fragment, int enterAnimation, int exitAnimation, Bundle bundle);



}
