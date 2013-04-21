package de.lukeslog.longitude.sharing;

import org.jivesoftware.smack.XMPPException;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import de.lukeslog.longitude.help.LongitudeConstants;
import de.lukeslog.xmpplibrary.XMPPChat;
import de.lukeslog.xmpplibrary.XMPPService;

public class XMPPWrapper extends Service implements XMPPChat  
{

	public final static String TAG = LongitudeConstants.TAG;
	
	@Override
	public void newMessage(String from, String body) 
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "new message"+from+body);
		try 
		{
			XMPPService.send("deinemudda", "tvluke@jabber.org", "deinemudda");
		} 
		catch (IllegalStateException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (XMPPException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		super.onCreate();
		Log.d(TAG, "XMPP-Wraper on Create");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "XMPP-Wraper onStart");
		SharedPreferences prefs = getSharedPreferences("xmppPreferences", 0); 
		//XMPPService.connect(prefs, "jabber.org", "5222", "jabber.org", "tvluke", "XXXX", "r1", this);
		XMPPService.connect(prefs, "talk.google.com", 5222, "gmail", "lukeslog@gmail.com", "XXXX", "longitude", this);
		return Service.START_STICKY;
	}

}
