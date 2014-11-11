package com.mcgrath.rockpaperscissors.events;

import com.mcgrath.rockpaperscissors.main.User;

public class SaveUserInfoEvent
{
	private User mUser;

	public User getUser() {
		return mUser;
	}

	public SaveUserInfoEvent( User aUser )
	{
		mUser = aUser;
	}
	
}