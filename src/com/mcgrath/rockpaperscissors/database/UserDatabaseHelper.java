package com.mcgrath.rockpaperscissors.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mcgrath.rockpaperscissors.database.DatabaseContract.UserEntry;
import com.mcgrath.rockpaperscissors.main.User;

public class UserDatabaseHelper extends SQLiteOpenHelper
{
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "USER_DBe.db";
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_USER_ENTRIES =
	    "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
	    UserEntry._ID + " INTEGER PRIMARY KEY," +
	    UserEntry.COLUMN_NAME_USER_ID + TEXT_TYPE + COMMA_SEP +
	    UserEntry.COLUMN_NAME_AGE + " INTEGER" + COMMA_SEP +
	    UserEntry.COLUMN_NAME_SEX + " INTEGER" + COMMA_SEP +
	    UserEntry.COLUMN_NAME_WIN + " INTEGER" + COMMA_SEP +
	    UserEntry.COLUMN_NAME_LOSS + " INTEGER" + " )";

	private static final String SQL_DELETE_ENTRIES =
	    "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

	public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	public long saveUser( SQLiteDatabase aDb, User aUser )
	{
		ContentValues values = new ContentValues();
		values.put(UserEntry.COLUMN_NAME_USER_ID, aUser.getName());
		values.put(UserEntry.COLUMN_NAME_AGE, aUser.getAge());
		values.put(UserEntry.COLUMN_NAME_SEX, aUser.getIsFemale() ? 1 : 0 );
		values.put(UserEntry.COLUMN_NAME_WIN, 0 );
		values.put(UserEntry.COLUMN_NAME_LOSS, 0 );
		
		long newRowId;
		newRowId = aDb.insert(
		         UserEntry.TABLE_NAME,
		         UserEntry.COLUMN_NAME_NULLABLE,
		         values);
		return newRowId;
	}
	
	public void updateUserWins( SQLiteDatabase aDb, User aUser )
	{
		ContentValues values = new ContentValues();

		values.put(UserEntry.COLUMN_NAME_WIN, aUser.getWins()  );
		values.put(UserEntry.COLUMN_NAME_LOSS, aUser.getLosses() );
		
		aDb.update( UserEntry.TABLE_NAME, values, UserEntry.COLUMN_NAME_USER_ID +"=?", new String[] {aUser.getName()} );
	}
	
	public ArrayList<String> getUsers( SQLiteDatabase aDb )
	{
		String theQuery = "SELECT " + UserEntry.COLUMN_NAME_USER_ID + " FROM " + UserEntry.TABLE_NAME;
		Cursor c = aDb.rawQuery( theQuery, null );
		ArrayList<String> theNames = new ArrayList<String>();
		if (c.moveToFirst()) {
			do {
				theNames.add( c.getString(0));
			} while (c.moveToNext());
		}
		return theNames;
	}
	
	public User getUserWithName( SQLiteDatabase aDb, String aName )
	{
		String theQuery = "SELECT * FROM " + UserEntry.TABLE_NAME + " WHERE " + UserEntry.COLUMN_NAME_USER_ID + "= \"" + aName + "\"";
		Cursor c = aDb.rawQuery( theQuery, null );
		User theUser = new User("Fake", false, 1, 1, 1 );
		if( c.moveToFirst() )
		{
			theUser = new User( c.getString(1), c.getInt(3) == 1 ? true : false, c.getInt(2), c.getInt(4), c.getInt(5) );
		}
		return theUser;
	}
	
	@Override
	public void onCreate( SQLiteDatabase aDb )
	{
		aDb.execSQL(SQL_CREATE_USER_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//no op
	}
}


