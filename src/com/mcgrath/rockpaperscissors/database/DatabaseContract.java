package com.mcgrath.rockpaperscissors.database;

import android.provider.BaseColumns;

public final class DatabaseContract
{
	public DatabaseContract(){}

    /* Inner class that defines the table contents */
    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user_table";
        public static final String COLUMN_NAME_USER_ID = "userid";
        public static final String COLUMN_NAME_AGE = "age";
        public static final String COLUMN_NAME_SEX = "sex";
        public static final String COLUMN_NAME_WIN = "win";
        public static final String COLUMN_NAME_LOSS = "loss";
		public static final String COLUMN_NAME_NULLABLE = null;
	}
    
}

