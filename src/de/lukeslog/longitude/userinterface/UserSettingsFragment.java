package de.lukeslog.longitude.userinterface;

import de.lukeslog.longitude.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class UserSettingsFragment extends PreferenceFragment
{


	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragmented_preferences);
    }



}
