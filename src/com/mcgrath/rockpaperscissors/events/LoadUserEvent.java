package com.mcgrath.rockpaperscissors.events;


public class LoadUserEvent
{
	private String mName;
	
	public String getUserID() {
		return mName;
	}

	public LoadUserEvent( String aName )
	{
		mName = aName;
	}
	
	
}