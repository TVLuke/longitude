package de.lukeslog.longitude.help;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StartUpService extends Service
{
	public final static String TAG = LongitudeConstants.TAG;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		Log.d(TAG, "StartUpService - OnCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.d(TAG, "StartUpService - onStartCommand");
		
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("de.lukeslog.longitude.sensing.LocationSensing");
		startService(serviceIntent);
		
		Intent broadcasterintent = new Intent();
		broadcasterintent.setAction("de.lukeslog.longitude.sensing.Broadcaster");
		startService(broadcasterintent);
		
		Intent xmppintent = new Intent();
		xmppintent.setAction("de.lukeslog.longitude.sharing.XMPPWrapper");
		startService(xmppintent);
		
		return START_NOT_STICKY;
	}
	
}
