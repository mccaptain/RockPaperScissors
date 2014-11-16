package com.mcgrath.rockpaperscissors.main;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelHelper
{

    public static byte[] getBytes(Parcelable parceable) 
    {
        Parcel theParcel = Parcel.obtain();
        parceable.writeToParcel(theParcel, 0);
        byte[] theBytes = theParcel.marshall();
        theParcel.recycle();
        return theBytes;
    }
 
    public static <T extends Parcelable> T getParcel(byte[] aBytes, Parcelable.Creator<T> creator)
    {
        Parcel theParcel = getParcel(aBytes);
        return creator.createFromParcel(theParcel);
    }
 
    public static Parcel getParcel(byte[] aBytes) {
        Parcel theParcel = Parcel.obtain();
        theParcel.unmarshall(aBytes, 0, aBytes.length);
        theParcel.setDataPosition(0);
        return theParcel;
    }
	
}
