package com.mcgrath.rockpaperscissors.main;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.database.UserDatabaseHelper;
import com.mcgrath.rockpaperscissors.events.LoadUserEvent;
import com.mcgrath.rockpaperscissors.events.PlayAgainEvent;
import com.mcgrath.rockpaperscissors.events.SaveUserInfoEvent;
import com.mcgrath.rockpaperscissors.events.UserPlayEvent;

import de.greenrobot.event.EventBus;

public class CentralActivity extends FragmentActivity
{
	private User mUser;
	private Character mUsersMoveCode;
	public UserDatabaseHelper mDBHelper;
	private BluetoothAdapter mBluetoothAdapter;
	private BroadcastReceiver mReceiver;
	private int REQUEST_ENABLE_BT = 2;
	
	private enum Pages
	{
		USER_INPUT,
		GAME,
		RESULT
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_central);
		if (savedInstanceState == null)
		{
			switchFrag( getFrag( Pages.USER_INPUT ) );
		}
		else
		{
			mUser = savedInstanceState.getParcelable("user");
		}

		mDBHelper = new UserDatabaseHelper( this );
        EventBus.getDefault().register( this );
        
        blootoof();
        if(mBluetoothAdapter != null)
        {
        	discoverBTs();
        	mBluetoothAdapter.startDiscovery();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.central, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void switchFrag( Fragment aFragment )
	{
		getSupportFragmentManager().beginTransaction()
			.replace( R.id.container, aFragment )
			.commit();
	}
	
	private Fragment getFrag( Pages aPage )
	{
		switch( aPage )
		{
		case USER_INPUT:
			Fragment theUInputFrag = (Fragment)UserInputFragment.newInstance(null);
			return theUInputFrag;
		case GAME:
			Bundle theBundle = new Bundle();
			theBundle.putParcelable("user", mUser);
			Fragment theGameFrag = (Fragment)GameFragment.newInstance( theBundle );
			return theGameFrag;
		case RESULT:
			Bundle theBundlee = new Bundle();
			theBundlee.putParcelable("user", mUser);
			theBundlee.putChar("move", mUsersMoveCode );
			Fragment theResultFrag = (Fragment)ResultFragment.newInstance( theBundlee );
			return theResultFrag;
		}
		return null;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("user", mUser);
		if( mUsersMoveCode != null )
		outState.putChar("move", mUsersMoveCode );
		super.onSaveInstanceState(outState);
	}
	
	public void onEvent( SaveUserInfoEvent aEvent )
	{
		mUser = aEvent.getUser();
		mDBHelper.saveUser( mDBHelper.getWritableDatabase(), mUser );
		switchFrag( getFrag( Pages.GAME ) );
	}
	
	public void onEvent( LoadUserEvent aEvent )
	{
		mUser = mDBHelper.getUserWithName( mDBHelper.getReadableDatabase(), aEvent.getUserID() );
		switchFrag( getFrag( Pages.GAME ) );
	}
	
	public void onEvent( UserPlayEvent aEvent )
	{
		//record user stats
		mUsersMoveCode = aEvent.getMove();
		switchFrag( getFrag( Pages.RESULT ) );
	}
	
	public void onEvent( PlayAgainEvent aEvent )
	{
		switchFrag( getFrag( Pages.GAME ) );
		if( mUser != null )
		{
			mDBHelper.updateUserWins( mDBHelper.getWritableDatabase(), mUser );
		}
	}

	@Override
	protected void onDestroy()
	{
		mDBHelper.close();
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	public void blootoof()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) 
		{
		    // Device does not support Bluetooth
		}
		
		
		if (!mBluetoothAdapter.isEnabled())
		{
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2)
	{
		if( arg0 == REQUEST_ENABLE_BT  &&  arg1 == RESULT_OK )
		{
			Toast.makeText( this, "Fuck yeah BT", Toast.LENGTH_SHORT).show();
		}
		else
		{
			super.onActivityResult(arg0, arg1, arg2);
		}
		
	}
	
	private void discoverBTs()
	{
		// Create a BroadcastReceiver for ACTION_FOUND
		mReceiver = new BroadcastReceiver() 
		{
		    public void onReceive(Context context, Intent intent)
		    {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action))
		        {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		            Toast.makeText( CentralActivity.this, device.getName() + "\n" + device.getAddress().toString(), Toast.LENGTH_SHORT ).show();
		        }
		    }
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	}
}
