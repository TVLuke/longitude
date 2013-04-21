/**
 * 
 * Rescources:
 * http://www.androidcompetencycenter.com/2009/06/start-service-at-boot/
 */
package de.lukeslog.longitude.sensing;

import de.lukeslog.longitude.help.LongitudeConstants;
import de.lukeslog.longitude.user.User;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class StartUpReceiver extends BroadcastReceiver
{
	public static SharedPreferences prefs;
	public final static String TAG = LongitudeConstants.TAG;
	public final static String PREFS = LongitudeConstants.PREFS;
	 
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("de.lukeslog.longitude.help.StartUpService");
		context.startService(serviceIntent);
		
		prefs = context.getSharedPreferences(PREFS, 0);
		String uid = prefs.getString("uid_primary", " ");
		if(uid.equals(" "))
		{
			//TODO: no uid is set... this is all useless
		}
		else
		{
			User u = new User(uid);
		}
	}
}
