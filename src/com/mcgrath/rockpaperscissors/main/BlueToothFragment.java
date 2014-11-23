package com.mcgrath.rockpaperscissors.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.events.BtDeviceFoundEvent;
import com.mcgrath.rockpaperscissors.events.BtDeviceSelectedEvent;
import com.mcgrath.rockpaperscissors.events.BtEnabledEvent;

import de.greenrobot.event.EventBus;


public class BlueToothFragment extends Fragment
{
	
	private ArrayList<BluetoothDevice> mDevices;
	private ArrayAdapter<String> mListAdapter;
	private boolean mBtStarted;
	private BluetoothDevice mSelecteDevice;
	
	@InjectView( R.id.enable_button ) Button mEnableButton;
	@InjectView( R.id.search_button ) Button mSearchButton;
	@InjectView( R.id.btconnection_list ) ListView mConList;
	@InjectView( R.id.connect_button ) Button mConnectButton;
	@InjectView( R.id.server_button ) Button mServerButton;
	@InjectView( R.id.msg_button ) Button mMsg;
	
	public static BlueToothFragment newInstance( Bundle aArgs )
	{
		BlueToothFragment theFrag = new BlueToothFragment();
		return theFrag;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		View theView = inflater.inflate( R.layout.fragment_bluetooth, container, false );

		ButterKnife.inject( this, theView );
		EventBus.getDefault().register(this);

		((CentralActivity) getActivity()).blootoof();

		mDevices = new ArrayList<BluetoothDevice>();
		
		mEnableButton.setOnClickListener( new OnClickListener() 
        {
			
			@Override
			public void onClick(View v)
			{
				CentralActivity theCA = (CentralActivity)getActivity();
				theCA.blootoof();
			}
		});
        
        mSearchButton.setOnClickListener( new OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
//				if( mBtStarted )
//				{
					CentralActivity theCA = (CentralActivity)getActivity();
				
					theCA.startSearch();
//				}
//				else
//				{
//					Toast.makeText(getActivity(), "Enable BT", Toast.LENGTH_SHORT ).show();
//				}
			}
		});
        
        mMsg.setOnClickListener( new OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
//				if( mBtStarted )
//				{
					CentralActivity theCA = (CentralActivity)getActivity();
				
					byte[] theAr = {'a','b','c'};
					theCA.sendMessage(theAr);
//				}
//				else
//				{
//					Toast.makeText(getActivity(), "Enable BT", Toast.LENGTH_SHORT ).show();
//				}
			}
		});
        
        mServerButton.setOnClickListener( new OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
//				if( mBtStarted )
//				{
					CentralActivity theCA = (CentralActivity)getActivity();
				
					theCA.startServer();
//				}
//				else
//				{
//					Toast.makeText(getActivity(), "Enable BT", Toast.LENGTH_SHORT ).show();
//				}
			}
		});
        
        mConnectButton.setOnClickListener( new OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
				if( mSelecteDevice != null )
				{
					EventBus.getDefault().post( new BtDeviceSelectedEvent( mSelecteDevice ));
				}
				
				if( mSelecteDevice == null )
				{
					Toast.makeText(getActivity(), "Select A Device", Toast.LENGTH_SHORT ).show();
				}
				
				
				
			}
		});
        
        mConList.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
              String selectedFromList = (String)( mConList.getItemAtPosition( myItemInt ) );
              mSearchButton = null;
              for (BluetoothDevice bluetoothDevice : mDevices) 
              {
            	if( bluetoothDevice.getName().equals( selectedFromList ) )
            	{
            		mSelecteDevice = bluetoothDevice;
            	}
              }

            }                 
      });
 
        return theView;
	}
	
	public void onEvent( BtEnabledEvent aEvent )
	{
		mBtStarted = aEvent.mEnabled;
		Toast.makeText(getActivity(), "BT Enabled", Toast.LENGTH_SHORT ).show();
	}
	
	public void onEventMainThread( BtDeviceFoundEvent aEvent )
	{
		Toast.makeText( getActivity(), "BT Device Added", Toast.LENGTH_SHORT ).show();
		mDevices.add( aEvent.mDevice );
		
		String[] theDs = new String[mDevices.size()];
		Set<String> deviceSet = new HashSet<String>();
		for( int i = 0; i < mDevices.size(); i++ )
		{
			theDs[i] = mDevices.get(i).getName();
			deviceSet.add(mDevices.get(i).getName());
		}
		mListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, deviceSet.toArray(new String[deviceSet.size()]) );
		mConList.setAdapter(mListAdapter);
		mListAdapter.notifyDataSetChanged();
		
	}
}