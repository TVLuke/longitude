package de.lukeslog.longitude.userinterface;

import java.util.List;

import de.lukeslog.longitude.R;
import de.lukeslog.longitude.help.LongitudeConstants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

public class PreferenceWithHeaders extends PreferenceActivity
{

	public static SharedPreferences defaultprefs;
	public static SharedPreferences prefs;
	public final static String TAG = LongitudeConstants.TAG;
	public final static String PREFS = LongitudeConstants.PREFS;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
	}
	
    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) 
    {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    
}
