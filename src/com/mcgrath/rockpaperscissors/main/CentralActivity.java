package com.mcgrath.rockpaperscissors.main;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.database.UserDatabaseHelper;
import com.mcgrath.rockpaperscissors.events.BtDeviceFoundEvent;
import com.mcgrath.rockpaperscissors.events.BtDeviceSelectedEvent;
import com.mcgrath.rockpaperscissors.events.BtEnabledEvent;
import com.mcgrath.rockpaperscissors.events.LoadUserEvent;
import com.mcgrath.rockpaperscissors.events.PlayAgainEvent;
import com.mcgrath.rockpaperscissors.events.SaveUserInfoEvent;
import com.mcgrath.rockpaperscissors.events.UserPlayEvent;

import de.greenrobot.event.EventBus;

public class CentralActivity extends FragmentActivity
{
	private User mUser;
	private Character mUsersMoveCode;
	private Character mCPUMoveCode = 'a';
	public UserDatabaseHelper mDBHelper;
	
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private final int REQUEST_ENABLE_BT = 2;
    private final int MESSAGE_READ = 256;
    private BroadcastReceiver mReceiver;
    private final String NAME = "TESTING_PLEASE";
    private static final UUID BLUETOOTH_SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static Handler mHandler;
	
    private AcceptThread mServerThread;
    private ConnectThread mClientThread;
    ConnectedThread mConnectedThread;
    
    
    //--------messages--------------------
    private static final int STARTGAME 		= 1;
    private static final int READY 			= 5;
    private static final int ROCKMOVE 		= 2;
    private static final int PAPERMOVE 		= 3;
    private static final int SCISSORMOVE 	= 4;
    //--------messages--------------------
    
	private enum Pages
	{
		USER_INPUT,
		GAME,
		RESULT,
		BLUETOOTH,
		SETUP
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_central);
		if (savedInstanceState == null)
		{
			switchFrag(  Pages.USER_INPUT  );
		}
		else
		{
			mUser = savedInstanceState.getParcelable("user");
		}

		mDBHelper = new UserDatabaseHelper( this );
        EventBus.getDefault().register( this );
               
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case MESSAGE_READ:
                    	int msgCode = 0;
                    	
                    	ByteBuffer b = ByteBuffer.wrap((byte[]) msg.obj);
                    	msgCode = b.getInt();
                    	
                    	switch( msgCode )
                    	{
                    	case STARTGAME:
                    		switchFrag(Pages.GAME);
                    		break;
                    	case READY:
                    		makeToast("READY");
                    		switchFrag(Pages.RESULT);
                    		
                    		break;
                    	case ROCKMOVE:
                    		makeToast("ROCKMOVE");
                    		mCPUMoveCode = 'r';
                    		break;
                    	case PAPERMOVE:
                    		makeToast("PAPERMOVE");
                    		mCPUMoveCode = 'p';
                    		break;
                    	case SCISSORMOVE:
                    		makeToast("SCISSORMOVE");
                    		mCPUMoveCode = 's';
                    		break;
                		default:
                			makeToast("bad code");
                			break;
                    	}
                    	
                        break;

                }
            }
        };
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.central, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
       return super.onOptionsItemSelected(item);
	}
	
	private void switchFrag( Pages page )
	{

		Fragment fragment = getSupportFragmentManager().findFragmentByTag(page.toString());
		if(fragment == null){
			getSupportFragmentManager().beginTransaction()
			.replace( R.id.container, getFrag(page), page.toString())
			.commit();
		}
		else if(fragment != null && !fragment.isVisible()){
			getSupportFragmentManager().beginTransaction()
			.replace( R.id.container, getFrag(page), page.toString())
			.commit();
		}
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
			theBundlee.putChar( "user_move", mUsersMoveCode );
			theBundlee.putChar( "cpumove", mCPUMoveCode );
			Fragment theResultFrag = (Fragment)ResultFragment.newInstance( theBundlee );
			return theResultFrag;
		case BLUETOOTH:
			Fragment theBtFrag = (Fragment)BlueToothFragment.newInstance( new Bundle() );
			return theBtFrag;
		case SETUP:
			Fragment theSetupFrag = (Fragment)SetupFragment.newInstance( new Bundle() );
			return theSetupFrag;		
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
		switchFrag(Pages.GAME);
	}
	
	public void onEvent( LoadUserEvent aEvent )
	{
		mUser = mDBHelper.getUserWithName( mDBHelper.getReadableDatabase(), aEvent.getUserID() );
		switchFrag(  Pages.SETUP );
	}
	
	public void onEvent( UserPlayEvent aEvent )
	{
		//record user stats
		
		mUsersMoveCode = aEvent.getMove();
		switch( mUsersMoveCode )
		{
		case 'r':
			sendMessage(getBytes(ROCKMOVE));
			break;
		case 'p':
			sendMessage(getBytes(PAPERMOVE));
			break;
		case 's':
			sendMessage(getBytes(SCISSORMOVE));
			break;
		default:
			break;
		}
		
		if(!mCPUMoveCode.equals('a')){
			switchFrag( Pages.RESULT );
		}
		
		
	}
	
	public void onEvent( PlayAgainEvent aEvent )
	{
		switchFrag(  Pages.GAME );
		mCPUMoveCode = 'a';
		if( mUser != null )
		{
			mDBHelper.updateUserWins( mDBHelper.getWritableDatabase(), mUser );
		}
	}
	
	public void onEventMainThread( BtDeviceSelectedEvent aEvent )
	{
		mDevice = aEvent.mDevice;
		startClient();
		switchFrag(  Pages.GAME  );
	}
	
	public void gotoBluetooth()
	{
		switchFrag( Pages.BLUETOOTH );
	}

	@Override
	protected void onDestroy()
	{
		mDBHelper.close();
		if( mReceiver != null )
		{
			unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}
	
	public void blootoof()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) 
		{
		    return;
		}
		
		
		if (!mBluetoothAdapter.isEnabled())
		{
		    Intent enableBtIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
		    startActivityForResult( enableBtIntent, REQUEST_ENABLE_BT );
		}
		setupBtFoundReciever();
		mBluetoothAdapter.startDiscovery();
		
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2)
	{
		if( arg0 == REQUEST_ENABLE_BT  &&  arg1 == RESULT_OK )
		{
			makeToast( "Heck yeah BT" );
			EventBus.getDefault().post(new BtEnabledEvent(true));
		}
		else
		{
			super.onActivityResult(arg0, arg1, arg2);
		}
		
	}
	
    public void setupBtFoundReciever()
    {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    BluetoothDevice aDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    makeToast( "Found " + aDevice.getName() );
                    EventBus.getDefault().post( new BtDeviceFoundEvent(aDevice));
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }
	
    public void makeToast( String aString )
    {
        //Toast.makeText( CentralActivity.this, aString, Toast.LENGTH_SHORT ).show();
    }
	
    public void sendMessage(byte[] aMessage)
    {
        mConnectedThread.write( aMessage );
    }

    public void startSearch()
    {
        if( mBluetoothAdapter != null )
        {
            makeToast( "Starting search" );
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
        }
    }

    public void startServer()
    {
        makeDiscoverable();
        mServerThread = new AcceptThread();
        mServerThread.start();
    }

    public void startClient()
    {
		mClientThread = new ConnectThread( mDevice );
		mClientThread.start();
		sendMessage(getBytes(STARTGAME));
    }
    
    byte[] getBytes( int aIn )
    {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt( aIn );
		return b.array();
    }
    
    public void makeDiscoverable()
    {
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent); 
    }
    
    public void manageConnectedSocket( BluetoothSocket aSocket )
    {
        mConnectedThread = new ConnectedThread( aSocket );
        mConnectedThread.start();
    }
    
    //server code
    private class AcceptThread extends Thread
    {
        private final BluetoothServerSocket mmServerSocket;
     
        public AcceptThread()
        {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try 
            {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, BLUETOOTH_SPP_UUID);
            } 
            catch (IOException e)
            {
                
            }
            mmServerSocket = tmp;
        }
     
        @Override
        public void run()
        {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) 
            {
                try 
                {
                    socket = mmServerSocket.accept();
                } catch (IOException e)
                {
                    break;
                }
                // If a connection was accepted
                if (socket != null) 
                {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    try
                    {
                        mmServerSocket.close();
                    }
                    catch( IOException e )
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
     
        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() 
        {
            try 
            {
                mmServerSocket.close();
            }
            catch (IOException e)
            {
                
            }
        }
    }
    //server code   
    
    //client code
    private class ConnectThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
     
        public ConnectThread( BluetoothDevice device ) 
        {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
     
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try 
            {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(BLUETOOTH_SPP_UUID);
            } 
            catch (IOException e)
            {
                
            }
            mmSocket = tmp;
        }
     
        @Override
        public void run() 
        {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();
     
            try
            {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } 
            catch (IOException connectException) 
            {
                // Unable to connect; close the socket and get out
                try
                {
                    mmSocket.close();
                }
                catch (IOException closeException)
                {
                    
                }
                return;
            }
     
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }
     
        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() 
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e)
            { 
                
            }
        }
    }
    //client code
    
    //socket management
    private class ConnectedThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
     
        public ConnectedThread( BluetoothSocket socket )
        {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
     
            // Get the input and output streams, using temp objects because
            // member streams are final
            try 
            {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            }
            catch (IOException e)
            {
                
            }
     
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
     
        @Override
        public void run() 
        {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
     
            // Keep listening to the InputStream until an exception occurs
            while (true) 
            {
                try
                {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                }
                catch (IOException e) 
                {
                    break;
                }
            }
        }
     
        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes)
        {
            try
            {
                mmOutStream.write(bytes);
            }
            catch (IOException e)
            {
            }
        }
     
        /* Call this from the main activity to shutdown the connection */
        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e)
            {
                
            }
        }
    }
    //socket management
}
