package com.mcgrath.rockpaperscissors.main;

import java.nio.ByteBuffer;
import java.util.Random;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.events.PlayAgainEvent;

import de.greenrobot.event.EventBus;


public class ResultFragment extends Fragment
{
	private User mUser;
	private Character mUsersMoveCode;
	private Character mCpuMoveCode;
	
	@InjectView( R.id.user_name ) TextView mUserName;
	@InjectView( R.id.useravatar ) ImageView mAvatarImage;
	@InjectView( R.id.cpuavatar ) ImageView mCpuAv;
	@InjectView( R.id.user_move ) TextView mUserMove;
	@InjectView( R.id.cpu_move) TextView mCpuMove;
	@InjectView( R.id.playagain) Button mPlayAgainButton;
	@InjectView( R.id.result ) TextView mResult;
	@InjectView( R.id.record ) TextView mRecord;
	
	public static ResultFragment newInstance( Bundle aArgs )
	{
		ResultFragment theFrag = new ResultFragment();
		if( aArgs != null )
		{
			theFrag.mUser = (User) aArgs.get("user");
			theFrag.mUsersMoveCode = aArgs.getChar( "user_move" );
			theFrag.mCpuMoveCode = aArgs.getChar( "cpumove" );
		}
		return theFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//setRetainInstance(true);
		if( savedInstanceState != null )
		{
			mUser = (User) savedInstanceState.get("user");
		    mUsersMoveCode = savedInstanceState.getChar("user_move");
		    mCpuMoveCode = savedInstanceState.getChar("cpumove");
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		 View theView = inflater.inflate(R.layout.result_frag, container, false);
		 ButterKnife.inject( this, theView );
		 
		 mUserName.setText( mUser.getName() );

	     if( mUser.getIsFemale() )
	     {
	      	 mAvatarImage.setImageResource(R.drawable.favatards);
	     }
	     else
	     {
	    	 mAvatarImage.setImageResource(R.drawable.mavatards);
	     }
	     
	     mUserMove.setText( getMoveText( mUsersMoveCode ) );
	     
	     mCpuMove.setText( getMoveText( mCpuMoveCode ) );
	     decideGame( mUsersMoveCode, mCpuMoveCode );
		 ButterKnife.inject( this, theView );
		 mPlayAgainButton.setOnClickListener( new OnClickListener() 
		 {
			
			@Override
			public void onClick(View v) 
			{
				EventBus.getDefault().post( new PlayAgainEvent() );
			}
		});
		 
		 return theView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("user", mUser );
		outState.putChar("user_move", mUsersMoveCode );
		outState.putChar("cpumove", mCpuMoveCode );
		super.onSaveInstanceState(outState);
	}
	
	private void decideGame( char userMove, char cpuMove )
	{
		if( userMove == cpuMove )
		{
			mResult.setText( getString( R.string.youtie ) );
		}
		else if( userMove == 'p' && cpuMove == 's' ||
			userMove == 'r' && cpuMove == 'p' ||
			userMove == 's' && cpuMove == 'r' )
		{
			mResult.setText( getString( R.string.youlose ) );
			mUser.setLosses( mUser.getLosses() + 1 );

		}
		else
		{
			mResult.setText( getString( R.string.youwin ) );
			mUser.setWins( mUser.getWins() + 1 );
		}
		
		CentralActivity theCA = (CentralActivity)getActivity();
		theCA.sendMessage(getBytes(5));
		
		
		 mRecord.setText( "W:" + mUser.getWins() + " L:" + mUser.getLosses() );
		 
	}
	
	byte[] getBytes( int aIn )
    {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt( aIn );
		return b.array();
    }
	
	private String getMoveText( char aMove )
	{
		switch( aMove )
		{
		case 'p': return "Paper";
		case 'r': return "Rock";
		case 's': return "Scissors";
		}
		return "Paper";
	}
	
}