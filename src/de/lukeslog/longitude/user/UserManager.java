/**
 * Keeping a list of users and stopring data for runtime.
 * 
 */
package de.lukeslog.longitude.user;

import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wareninja.opensource.gravatar4android.Gravatar;
import com.wareninja.opensource.gravatar4android.GravatarDefaultImage;
import com.wareninja.opensource.gravatar4android.GravatarRating;
import com.wareninja.opensource.gravatar4android.GravatarResponseData;
import com.wareninja.opensource.gravatar4android.common.CONSTANTS;
import com.wareninja.opensource.gravatar4android.common.GenericRequestListener;

import de.lukeslog.longitude.database.LocationDatabase;
import de.lukeslog.longitude.help.LongitudeConstants;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

public class UserManager extends Service implements OnSharedPreferenceChangeListener
{

	private static HashMap<String, User> userlist = new HashMap<String, User>();
	public final static String TAG = LongitudeConstants.TAG;
	public static SharedPreferences defaultprefs;
	public static SharedPreferences prefs;
	public final static String PREFS = LongitudeConstants.PREFS;
	@Override
	
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() 
	{
		Log.d(TAG, "UserManager - OnCreate");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.d(TAG, "UserManager - onStartCommand");
		//now get the roster from its xmpp account
		//create those users too...
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() 
	{
        stopSelf();
        super.onDestroy();
	}
	
	public static void addUser(String uid, User u)
	{
		Log.d(TAG, "UserManager addUser()");
		if(userlist.containsKey(uid))
		{
			//Replace
			Log.d(TAG, "already containes "+u.getUID());
			userlist.put(uid, u);
			//TODO: not sure if replace is ok here...
		}
		else
		{
			Log.d(TAG, "adding "+u.getUID());
			userlist.put(uid, u);
		}
	}

	public static void deleteUser(String uid)
	{
		userlist.remove(uid);
	}
	
	public static User getUser(String uid)
	{
		//TODO: get the username from the preferences
		Log.d(TAG, "getUser() uid="+uid);
		Log.d(TAG, "userlist "+userlist.size());
		
		if(userlist.containsKey(uid))
		{
			Log.d(TAG, "found");
			return userlist.get(uid);
		}
		else
		{
			return null;
		}
	}
	
	public static void setPosition (String uid, LatLng p)
	{
		if(userlist.containsKey(uid))
		{
			userlist.get(uid).setPosition(p);
		}
		else
		{
			//TODO: Notification? Error? Not Sure.
		}
		
	}
	
	//TODO: This Method is Bulshit
	private void getGravatars()
	{
	    Gravatar gravatar = new Gravatar();
	    gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
	    gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
	    final String email="lukeslog@googlemail.com";
	    gravatar.downloadGravatarImage("lukeslog@googlemail.com", new GenericRequestListener() 
	    {
			@Override
			public void onComplete_wBundle(final Bundle params) 
			{
				super.onComplete_wBundle(params);
				Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() 
				{
	                public void run() 
	                {
	                	processImageResponse(params, email);
	                }
				});
			}		
	    });
	}
	
public void processImageResponse(Bundle response, String uid) {
    	
    	GravatarResponseData gravatarResponseData = (GravatarResponseData)response.getSerializable("gravatarResponseData");
	    
    	if(gravatarResponseData.getStatus()==1) 
    	{
    		Bitmap avatar = BitmapFactory.decodeByteArray(gravatarResponseData.getImageData(), 0, gravatarResponseData.getImageData().length);
    	}
    }

@Override
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) 
{
	if(key.equals("edittext_username"))
	{
		String uid = prefs.getString("uid_primary", " ");
		if(uid.equals(" "))//not yet any primary user set
		{
			String n=sharedPreferences.getString("edittext_username", " ");
			User u = new User(n);
			addUser(n, u);
		}
		else
		{
			deleteUser(uid);
			String nextuid=sharedPreferences.getString("edittext_username", " ");
			User u = new User(nextuid);
			addUser(nextuid, u);
			Editor edit = prefs.edit();
			edit.putString("uid_primary", nextuid);
			edit.commit();
		}
	}
	
}

}
