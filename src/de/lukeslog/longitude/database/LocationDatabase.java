/**
 * Database asbtraction class for users location. The database ontains all locations from all users (including the user itself)
 * 
 * Struture
 * *****************************************************************************************************
 * entryID | timestamp | userid | latitude | longitude | accuracy | speed | checkinlocation | distance |
 * *****************************************************************************************************  
 * 
 * Resources:
 * http://www.anotherandroidblog.com/2010/08/04/android-database-tutorial/4/
 * http://www.sqlite.org/datatype3.html
 * https://bitbucket.org/TVLuke/soundofthecity/src/5a6d3c107c64/src/de/uni_luebeck/itm/soundofthecity/api/SoundOfTheCityConstants.java?at=master
 */
package de.lukeslog.longitude.database;

import de.lukeslog.longitude.constants.LongitudeConstants;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocationDatabase 
{

	public SharedPreferences prefs;
	Context context;
	public final static String PREFS = LongitudeConstants.PREFS;
	public final static String TAG = LongitudeConstants.TAG;
	
	public static double lati=0.0;
	public static double longi=0.0;
	
	private SQLiteDatabase db;
	
	//info for the user location table
	public static String TABLE_USER_LOCATION = "UserLocationTable";
	public static String TABLE_USER_LOCATION_ROW_ID = "entry_id";
	public static String TABLE_USER_LOCATIOn_ROW_TIMESTAMP = "timestamp";
	public static String TABLE_USER_LOCATIOn_ROW_USER_ID = "user_id";
	public static String TABLE_USER_LOCATIOn_ROW_LATITUDE = "latitude";
	public static String TABLE_USER_LOCATIOn_ROW_LONGITUDE = "longitude";
	public static String TABLE_USER_LOCATIOn_ROW_ALTITUDE = "altitude";
	public static String TABLE_USER_LOCATIOn_ROW_ACCURACY = "accuracy";
	public static String TABLE_USER_LOCATIOn_ROW_SPEED = "speed";
	public static String TABLE_USER_LOCATIOn_ROW_CHECKED = "checkedinlocation";
	public static String TABLE_USER_LOCATIOn_ROW_DISTANCE = "distance";
	public static String TABLE_USER_LOCATION_CREATE =
			"CREATE TABLE IF NOT EXISTS " + TABLE_USER_LOCATION + 
			" ( "+TABLE_USER_LOCATION_ROW_ID+" integer primary key autoincrement, " +
			TABLE_USER_LOCATIOn_ROW_TIMESTAMP +" integer, " +	
			TABLE_USER_LOCATIOn_ROW_USER_ID +" text, " +
			TABLE_USER_LOCATIOn_ROW_LATITUDE+" real, " +
			TABLE_USER_LOCATIOn_ROW_LONGITUDE+" real, " +
			TABLE_USER_LOCATIOn_ROW_ALTITUDE+" real, " +
			TABLE_USER_LOCATIOn_ROW_ACCURACY+" integer, " +
			TABLE_USER_LOCATIOn_ROW_SPEED+" integer, " +
			TABLE_USER_LOCATIOn_ROW_CHECKED+" text, " +
			TABLE_USER_LOCATIOn_ROW_DISTANCE+" integer)" +
			";";
	
	public LocationDatabase(Context context)
    {
		this.context = context;
        Log.i(TAG, "createDatabase called");
        OpenHelper openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
    }
	
	public void addRow(long timestamp, String user_id, double latitude, double longitude, double altitude, int accuracy, int speed, String checked_in, int distance)
	{
		ContentValues values = new ContentValues();
		values.put(TABLE_USER_LOCATIOn_ROW_TIMESTAMP, timestamp);
		values.put(TABLE_USER_LOCATIOn_ROW_USER_ID, user_id);
		values.put(TABLE_USER_LOCATIOn_ROW_LATITUDE, latitude);
		values.put(TABLE_USER_LOCATIOn_ROW_LONGITUDE, longitude);
		values.put(TABLE_USER_LOCATIOn_ROW_ALTITUDE, altitude);
		values.put(TABLE_USER_LOCATIOn_ROW_ACCURACY, accuracy);
		values.put(TABLE_USER_LOCATIOn_ROW_SPEED, speed);
		values.put(TABLE_USER_LOCATIOn_ROW_CHECKED, checked_in);
		values.put(TABLE_USER_LOCATIOn_ROW_DISTANCE, distance);
		
		try
		{
			db.insert(TABLE_USER_LOCATION, null, values);
		}
		catch(Exception e)
		{
			Log.e("DB ERROR", e.toString());
			e.printStackTrace();
		}
	}
	
	public void getRow(String username)
	{
		Log.d(TAG, "databse getRow"+username);
		 Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER_LOCATION +" WHERE "+TABLE_USER_LOCATIOn_ROW_USER_ID+"='"+username+"';", null);
		 cursor.moveToLast();
		 long timestamp = cursor.getLong(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_TIMESTAMP));
		 Log.d(TAG, "timestamp="+timestamp );
    	 String user_id = cursor.getString(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_USER_ID));
    	 double latitude = cursor.getDouble(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_LATITUDE));
    	 double longitude = cursor.getDouble(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_LONGITUDE));
    	 double altitude = cursor.getDouble(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_ALTITUDE));
    	 int accuracy = cursor.getInt(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_ACCURACY));
    	 int speed = cursor.getInt(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_SPEED));
    	 String checked = cursor.getString(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_CHECKED));
    	 int distance = cursor.getInt(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_DISTANCE));
    	 lati=latitude;
    	 longi=longitude;
    	 Log.d(TAG, "location="+lati+", "+longi );
         if (cursor.moveToFirst()) 
         {
                 do 
                 {
                	 //long timestamp = cursor.getLong(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_TIMESTAMP));
                	 //String user_id = cursor.getString(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_USER_ID));
                	 //double latitude = cursor.getDouble(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_LATITUDE));
                	 //double longitude = cursor.getDouble(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_LONGITUDE));
                	 //double altitude = cursor.getDouble(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_ALTITUDE));
                	 //int accuracy = cursor.getInt(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_ACCURACY));
                	 //int speed = cursor.getInt(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_SPEED));
                	 //String checked = cursor.getString(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_CHECKED));
                	 //int distance = cursor.getInt(cursor.getColumnIndex(TABLE_USER_LOCATIOn_ROW_DISTANCE));
                     //    String name = cursor.getString(cursor.getColumnIndex("name"));
                     //    String desc = cursor.getString(cursor.getColumnIndex("description"));
                     //    String path = cursor.getString(cursor.getColumnIndex("path"));
                     //    long date = cursor.getLong(cursor.getColumnIndex("date"));
                     //    int la = cursor.getInt(cursor.getColumnIndex("latitude"));
                     //    int lo = cursor.getInt(cursor.getColumnIndex("longitude"));                             
                     //    String tag = cursor.getString(cursor.getColumnIndex("tag"));
                     //    list.add(new SoundSample(name, desc, new GeoPoint(la, lo), new Date(date), path, tag)); 
                 } 
                 while (cursor.moveToNext());
         }
         if (cursor != null && !cursor.isClosed()) cursor.close();
	}
	
	public class OpenHelper extends SQLiteOpenHelper
    {

            OpenHelper(Context context) 
            {
                    super(context, LongitudeConstants.DATABASE_NAME, null,  LongitudeConstants.DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db)
            {
                    Log.i(TAG, "onCreate Database");
                    db.execSQL(TABLE_USER_LOCATION_CREATE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
            {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_LOCATION);                
                    onCreate(db);
            }
    }
	
}
