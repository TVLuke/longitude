package de.lukeslog.longitude.sensing;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import de.lukeslog.longitude.help.LongitudeConstants;

public class Broadcaster  extends Service implements OnSharedPreferenceChangeListener
{
	public static SharedPreferences prefs;
	public final static String TAG = LongitudeConstants.TAG;
	public final static String PREFS = LongitudeConstants.PREFS;
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
	{
		Log.d(TAG, "stuff changed "+key);
		prefs = getSharedPreferences(PREFS, 0);
		//Find out what data type this is
		try
		{
			int x = sharedPreferences.getInt(key, 0);
			Editor edit = prefs.edit();
			edit.putInt(key, x);
			edit.commit();
			return;
		}
		catch(Exception e)
		{
			//it was not an int
			try
			{
				long x = sharedPreferences.getLong(key, 0);
				Editor edit = prefs.edit();
				edit.putLong(key, x);
				edit.commit();
			}
			catch(Exception e2)
			{
				//it was no longh
				try
				{
					float x = sharedPreferences.getFloat(key, 0.0f);
					Editor edit = prefs.edit();
					edit.putFloat(key, x);
					edit.commit();
				}
				catch(Exception ex)
				{
					//and not a float
					try
					{
						String x = sharedPreferences.getString(key, "");
						Editor edit = prefs.edit();
						edit.putString(key, x);
						edit.commit();
					}
					catch(Exception exc)
					{
						//neither a string
						try
						{
							Boolean x = sharedPreferences.getBoolean(key, false);
							Editor edit = prefs.edit();
							edit.putBoolean(key, x);
							edit.commit();
						}
						catch(Exception exce)
						{
							//it should have been a boolean...
						}
					}
				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		Log.d(TAG, "Broadcaster - OnCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.d(TAG, "Broadcaster - onStartCommand");
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		return START_STICKY;
	}
	
	
	
}
