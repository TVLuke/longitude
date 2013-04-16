/**
 * 
 * Rescources:
 * http://www.androidcompetencycenter.com/2009/06/start-service-at-boot/
 */
package de.lukeslog.longitude.sensing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartUpReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("de.lukeslog.longitude.sensing.LocationSensing");
		context.startService(serviceIntent);
	}
}
