package com.mcgrath.rockpaperscissors.events;

import android.bluetooth.BluetoothDevice;


public class BtDeviceSelectedEvent
{
	public BluetoothDevice mDevice;
	public BtDeviceSelectedEvent( BluetoothDevice aDevice )
	{
		mDevice = aDevice;
	}
	
}