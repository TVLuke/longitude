/**
 *
 * References
 * https://docs.google.com/document/pub?id=19nQzvKP-CVLd7_VrpwnHfl-AE9fjbJySowONZZtNHzw
 *
 * Big Thanks to 
 * malcooke at http://stackoverflow.com/questions/5167273/in-eclipse-unable-to-reference-an-android-library-project-in-another-android-pr
 */
package de.lukeslog.longitude.usierinterface;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;

import de.lukeslog.longitude.R;
import de.lukeslog.longitude.constants.LongitudeConstants;
import de.lukeslog.longitude.database.LocationDatabase;
import de.lukeslog.longitude.sensing.LocationSensing;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainMap extends Activity implements
ActionBar.OnNavigationListener {

	GoogleMap map;
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	public SharedPreferences prefs;
	public final static String PREFS = LongitudeConstants.PREFS;
	public final static String TAG = LongitudeConstants.TAG;
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_map);
	    
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		Intent startsensing = new Intent(this, LocationSensing.class);
		startService(startsensing);
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),}), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_map, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) 
	{
		if(position==0)
		{
		    
		    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		    LocationDatabase ldb = new LocationDatabase(this);
		    ldb.getRow("tvluke");
		    Log.d(TAG, "from Map. Location="+LocationDatabase.lati+", "+LocationDatabase.longi);
		    Marker hamburg = map.addMarker(new MarkerOptions().position(new LatLng(LocationDatabase.lati, LocationDatabase.longi)).title("Hamburg"));
		    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocationDatabase.lati, LocationDatabase.longi), 15));
		    
		}
		if(position==1)
		{
			
		}
		return false;
	}
}
