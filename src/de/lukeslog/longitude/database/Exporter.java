/**
 * This class provides a abstract class for all exporters. An exporter has the task of creating a file of certain properties from the cursor.
 * 
 * This class defines what kind of exports from the database have to be supported and already provides the 
 * method to get the correct cursor for the method.
 */
package de.lukeslog.longitude.database;

import android.app.Service;
import android.database.Cursor;

public abstract class Exporter extends Service{

	private Cursor getLocationFromUserCursor(String username)
	{
		LocationDatabase ldb = new LocationDatabase(this);
		return ldb.getLocationHistoryFromUser(username);
	}
	
	public abstract void ExportLocationFromUser(String username);
}
