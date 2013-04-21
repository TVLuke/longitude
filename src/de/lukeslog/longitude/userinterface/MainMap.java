/**
 *
 * References
 * https://docs.google.com/document/pub?id=19nQzvKP-CVLd7_VrpwnHfl-AE9fjbJySowONZZtNHzw
 *
 * Big Thanks to 
 * malcooke at http://stackoverflow.com/questions/5167273/in-eclipse-unable-to-reference-an-android-library-project-in-another-android-pr
 */
package de.lukeslog.longitude.userinterface;

import java.lang.reflect.Field;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;
import com.wareninja.opensource.gravatar4android.Gravatar;
import com.wareninja.opensource.gravatar4android.GravatarDefaultImage;
import com.wareninja.opensource.gravatar4android.GravatarRating;
import com.wareninja.opensource.gravatar4android.GravatarResponseData;
import com.wareninja.opensource.gravatar4android.common.CONSTANTS;
import com.wareninja.opensource.gravatar4android.common.GenericRequestListener;

import de.lukeslog.longitude.R;
import de.lukeslog.longitude.database.LocationDatabase;
import de.lukeslog.longitude.help.LongitudeConstants;
import de.lukeslog.longitude.help.StartUpService;
import de.lukeslog.longitude.sensing.LocationSensing;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;


public class MainMap extends Activity implements
ActionBar.OnNavigationListener {

	GoogleMap map;
	Activity mActivity;
	public SharedPreferences prefs;
	public final static String TAG = LongitudeConstants.TAG;
	public final static String PREFS = LongitudeConstants.PREFS;
	
	MapPopulator mappop = new MapPopulator();
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
	    mActivity = this;
	    prefs = getSharedPreferences(PREFS, 0);
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		//start the startup service
		Intent serviceIntent = new Intent(this, StartUpService.class);
		startService(serviceIntent);
		
		//TODO: check if uid is set
		String uid = prefs.getString("uid_primary", " ");
		if(uid.equals(" "))
		{
			//Start an activity to set the uid and the metadata.
		}
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2),}), this);
		Log.d(TAG, "ok... populate the map");
		
		mappop = new MapPopulator();
		mappop.run();
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
		Log.d(TAG, "menu position="+position);
		Log.d(TAG, "menu id="+id);
		if(position==0)
		{
		    
		    Gravatar gravatar = new Gravatar();
		    gravatar.setRating(GravatarRating.GENERAL_AUDIENCES);
		    gravatar.setDefaultImage(GravatarDefaultImage.IDENTICON);
		    String email="lukeslog@googlemail.com";
	        gravatar.downloadGravatarImage("lukeslog@googlemail.com", new GenericRequestListener() 
	        {
				@Override
				public void onComplete_wBundle(final Bundle params) 
				{
					super.onComplete_wBundle(params);

					mActivity.runOnUiThread(new Runnable() 
					{
		                public void run() 
		                {
		                	processImageResponse(params);
		                }
					});
				}		
	        });
		    
		}
		if(position==1)
		{
			
		}
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Log.d(TAG, ""+item.getTitle());
		Intent i = new Intent(this, PreferenceWithHeaders.class);
		startActivity(i);
		return false;
	}
	
    public void processImageResponse(Bundle response) {
    	
    	GravatarResponseData gravatarResponseData = (GravatarResponseData)response.getSerializable("gravatarResponseData");
    	
    	//if(CONSTANTS.DEBUG)Log.d(TAG, "gravatarResponseData->" + gravatarResponseData);

    	//ImageView gravatarImage = (ImageView)findViewById(R.id.img_gravatar);
    	//TextView resultText = (TextView)findViewById(R.id.tv_result);
    	
	    LocationDatabase ldb = new LocationDatabase(this);
	    ldb.getRow("tvluke");
	    //Log.d(TAG, "from Map. Location="+LocationDatabase.lati+", "+LocationDatabase.longi);
	    
    	if(gravatarResponseData.getStatus()==1) 
    	{
    		Bitmap avatar = BitmapFactory.decodeByteArray(gravatarResponseData.getImageData(), 0, gravatarResponseData.getImageData().length);
    		Marker postition = map.addMarker(new MarkerOptions()
    		.position(new LatLng(LocationDatabase.lati, LocationDatabase.longi))
    		.title("lukeslog@googlemail.com")
    		.icon(BitmapDescriptorFactory.fromBitmap(avatar)));
    	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LocationDatabase.lati, LocationDatabase.longi), 15));
        	    
    	}
    	else 
    	{
    		Log.e(TAG, "das war wohl nichts");
    	}
    }
	
	class MapPopulator implements Runnable
	{

		 private Handler handler = new Handler();
		 public static final int delay = 1000;
			
		@Override
		public void run() 
		{

			String uid = prefs.getString("uid_primary", " ");
			//LatLng myposition = me.getPosition();
			//TODO: Load Map View
			//TODO: Find Position of the user
			//TODO: Find Position of his friends
			//TODO: Load picture for each friend, and use defaiult pic if non there.
			//TODO: Diplay all the friends on the map
			//handler.removeCallbacks(this);
			//handler.postDelayed(this, delay);
		}
		
		public void onResume() 
		{
            handler.postDelayed(this, delay);
		}

		public void onPause() 
		{
            handler.removeCallbacks(this);
		}
	}
}
