/**
 * 
 * Code based on work done for https://bitbucket.org/TVLuke/soundofthecity
 */
package de.lukeslog.longitude.sensing;

import org.joda.time.DateTime;

import de.lukeslog.longitude.constants.LongitudeConstants;
import de.lukeslog.longitude.database.LocationDatabase;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationSensing extends Service
{
	public SharedPreferences prefs;
	public final static String PREFS = LongitudeConstants.PREFS;
	public final static String TAG = LongitudeConstants.TAG;
	
	float[] speedarray = new float[LongitudeConstants.LOCATION_SPEEDARRAY_SIZE];
	
	LocationManager lm;
	
	private static boolean isGpsListening = false;
	private static boolean isNetworkListening = false;
	private static boolean isPassiveListening = false;
	
    private Location lastlocation;
    private DateTime lasttime;
    
	@Override
	public IBinder onBind(Intent arg0) 
	{
		return null;
	}

	@Override
	public void onCreate() 
	{
		Log.d(TAG, "LocationSensing - OnCreate");
		prefs = getSharedPreferences(PREFS, 0);
		for (int i = 0; i < speedarray.length; i++) 
		{
			speedarray[i] = 0.0f;
		}
	}
	    
	boolean isSpeedarrayFull() 
	{
		Log.d(TAG, "LocationSensing - isSpeedarrayFull");
		boolean temp = true;
		for (int i = 0; i < speedarray.length; i++) 
		{
			temp &= speedarray[i] != 0.0f;  
		}
		return temp;
	}
	    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		Log.d(TAG, "LocationSensing - onStartCommand");
		if(lm==null)
		{
			lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
		registerListeners();
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() 
	{
		Log.d(TAG, "LocationSensing - onDestroy");
        unregisterListeners();
        stopSelf();
        super.onDestroy();
	}
	 
	private void registerListeners()
	{
		Log.d(TAG, "LocationSensing - registerListeners");
		if (! isGpsListening)
		{
			try 
			{
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, LongitudeConstants.LOCATION_FREQUENCY*1000, 5, locationListenerGps);
			} 
			catch (IllegalArgumentException e) 
			{
				isGpsListening = false;
			}
		}
		if (! isNetworkListening)
		{
			try 
			{
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LongitudeConstants.LOCATION_FREQUENCY*1000, 5, locationListenerNetwork);
	                
			} 
			catch (IllegalArgumentException e) 
			{
	                        isNetworkListening = false;
			}
		}
		if (! isPassiveListening)
		{
			try 
			{
				lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListenerPassive);
			} 
			catch (IllegalArgumentException e) 
			{
				isPassiveListening = false;
			}
		}
	}
	 
	private void unregisterListeners()
	{
			Log.d(TAG, "LocationSensing - unregisterListeners");
	        lm.removeUpdates(locationListenerGps);
	        lm.removeUpdates(locationListenerNetwork);
	        lm.removeUpdates(locationListenerPassive);
	}
	    
	LocationListener locationListenerGps = new LocationListener() 
	{

	                @Override
	                public void onLocationChanged(Location arg0) 
	                {
	                        Log.i(TAG,"gps based location");
	                        onNewLocation(arg0);    
	                }

	                @Override
	                public void onProviderDisabled(String arg0) 
	                {
	                        isGpsListening = false; 
	                }

	                @Override
	                public void onProviderEnabled(String provider) 
	                {
	                        isGpsListening = true;          
	                }

	                @Override
	                public void onStatusChanged(String provider, int status, Bundle extras) 
	                {

	                	registerListeners();
	                }
	        
	};
	    
	LocationListener locationListenerPassive = new LocationListener() 
	{

	                @Override
	                public void onLocationChanged(Location arg0) 
	                {
	                        Log.i(TAG,"passive based location");
	                        onNewLocation(arg0);
	                }

	                @Override
	                public void onProviderDisabled(String arg0) 
	                {
	                        isPassiveListening = false;
	                }

	                @Override
	                public void onProviderEnabled(String provider) 
	                {
	                        isPassiveListening = true;
	                }

	                @Override
	                public void onStatusChanged(String provider, int status, Bundle extras) 
	                {
	                	registerListeners();
	                }
	};
	    
	LocationListener locationListenerNetwork = new LocationListener() 
	{

	                @Override
	                public void onLocationChanged(Location arg0) 
	                {
	                        Log.i(TAG,"network based location");
	                        onNewLocation(arg0);
	                }

	                @Override
	                public void onProviderDisabled(String arg0) 
	                {
	                        isNetworkListening = false;
	                }

	                @Override
	                public void onProviderEnabled(String provider) 
	                {
	                        isNetworkListening = true;
	                }

	                @Override
	                public void onStatusChanged(String provider, int status, Bundle extras) 
	                {
	                	registerListeners();
	                }       
	};
	    
	private void onNewLocation(Location location)
	{
		if(!hasGoodAccuracy(location))
		{
			Log.i(TAG, "location not good enough.");
			return;
		} 
		else 
		{
			Log.d(TAG, "New accurate location recieved");
		}
		DateTime dt = new DateTime();
		if (lasttime == null)
		{
			lastlocation = location;
			lasttime = dt;
	    }
		float distance = location.distanceTo(lastlocation);
        float timepassed = (float)( dt.getMillis()-lasttime.getMillis() ) / 1000.0f;
    	float speedsum=0.0f;        
        Log.d(TAG,"distance is: "+distance);
        Log.d(TAG,"timepassed is: "+timepassed);
        //only if speed calculation is useful
        if (distance > 1 && timepassed > 1) 
        {
        	float speed = distance/timepassed;
        	// convert to km/h
        	speed=speed*3.6f;
        	Log.d(TAG,"speed is: "+speed);
                
        	// move SpeedArray up
        	for(int i=0; i<speedarray.length-1; i++)
        	{
        		speedarray[speedarray.length-(i+1)]=speedarray[speedarray.length-(i+2)];
        	}
        	speedarray[0]=speed;
        	// sum up speed...
        	for(int i=0; i<speedarray.length; i++)
        	{
        		speedsum+=speedarray[i];
        	}
        	//... and divide by array size => speed is in speedsum
        	speedsum=speedsum/(float)LongitudeConstants.LOCATION_SPEEDARRAY_SIZE;
                
        	//First speed is not useful since first location is not measured
        	Log.d(TAG,"speedsum is: "+speedsum);
	    }
        //Location and Speed to be put into a database
        LocationDatabase ldb = new LocationDatabase(this);
        int speed = (int) speedsum;
        ldb.addRow(dt.getMillis(), "tvluke", location.getLatitude(), location.getLongitude(),location.getAltitude(), (int)location.getAccuracy(), speed, " ", 0);
	}

	    
	private boolean hasGoodAccuracy(Location location)
	{
		if (location.getAccuracy() == 0.0f || location.getLatitude() == 0.0 || location.getLongitude() == 0.0)
		{
			return false;
		}
        return location.getAccuracy() < LongitudeConstants.LOCATION_ACCURACY;
    }

}
