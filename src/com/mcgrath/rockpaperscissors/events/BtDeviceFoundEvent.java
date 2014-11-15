package com.mcgrath.rockpaperscissors.events;

import android.bluetooth.BluetoothDevice;


public class BtDeviceFoundEvent
{
	public BluetoothDevice mDevice;
	public BtDeviceFoundEvent( BluetoothDevice aDevice )
	{
		mDevice = aDevice;
	}
	
}