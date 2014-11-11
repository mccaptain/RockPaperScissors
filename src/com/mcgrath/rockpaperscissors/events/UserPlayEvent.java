package com.mcgrath.rockpaperscissors.events;

import com.mcgrath.rockpaperscissors.main.User;

public class UserPlayEvent
{
	private User mUser;
	private Character mMove;
	
	public User getUser() {
		return mUser;
	}

	public Character getMove()
	{
		return mMove;
	}
	
	public UserPlayEvent( User aUser, Character aMove )
	{
		mUser = aUser;
		mMove = aMove;
	}
	
	
}