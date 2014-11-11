package com.mcgrath.rockpaperscissors.main;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{

	private String mName;
	private boolean mIsFemale;
	private int mAge;
	private int mWins;
	private int mLosses;
	
	public User( String aName, boolean aIsFemale, int aAge, int aWins, int aLosses )
	{
		mName = aName;
		mIsFemale = aIsFemale;
		mAge = aAge;
		mWins = aWins;
		mLosses = aLosses;
	}
	
	public String getName()
	{
		return mName;
	}
	
	public void setName(String aString) 
	{
		this.mName = aString;
	}
	
	public boolean getIsFemale() 
	{
		return mIsFemale;
	}
	
	public void setIsFemale(boolean mSex)
	{
		this.mIsFemale = mSex;
	}
	
	public int getAge()
	{
		return mAge;
	}
	
	public void setAge(int mAge) 
	{
		this.mAge = mAge;
	}
	
	public int getWins()
	{
		return mWins;
	}
	
	public void setWins(int mWins) 
	{
		this.mWins = mWins;
	}
	
	public int getLosses()
	{
		return mLosses;
	}
	
	public void setLosses(int mLosses) 
	{
		this.mLosses = mLosses;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString( mName );
		dest.writeInt( mIsFemale ? 1 : 0 );
		dest.writeInt( mAge );
		dest.writeInt( mWins );
		dest.writeInt( mLosses );
	}
	
	public static User readUser( Parcel aIn )
	{
		return new User( aIn.readString(), aIn.readInt() == 1 ? true : false, aIn.readInt(), aIn.readInt() , aIn.readInt() );
	}
	  
	public static final User.Creator<User> CREATOR  = new User.Creator<User>()
	{
		public User createFromParcel(Parcel in) 
		{
			return readUser(in);
		}

		public User[] newArray(int size)
		{
			return new User[size];
		}
	};
	
}
