package com.mcgrath.rockpaperscissors.main;

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

import com.anupcowkur.wheelmenu.WheelMenu;
import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.events.UserPlayEvent;

import de.greenrobot.event.EventBus;


public class GameFragment extends Fragment
{
	private User mUser;

	private enum move
	{
		ROCK,
		PAPER,
		SCISSORS
	}
	
	private move mMove;

	@InjectView( R.id.user_name ) TextView mUserNameLabel;
	@InjectView( R.id.user_entry ) TextView mUserEntryLabel;
	@InjectView( R.id.wheelMenu ) WheelMenu mWheel;
	@InjectView( R.id.set_move_button ) Button mPlayButton;
	@InjectView( R.id.useravatar ) ImageView mAvatarImage;
	
	public static GameFragment newInstance( Bundle aArgs )
	{
		GameFragment theFrag = new GameFragment();
		if( aArgs != null )
		{
			theFrag.setUser( (User) aArgs.getParcelable("user") );
		}
		return theFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		//setRetainInstance(true);
		if( savedInstanceState != null )
		{
			mUser = savedInstanceState.getParcelable("user");
		}
		super.onCreate( savedInstanceState );
	}
	
	public void setUser( User aUser )
	{
		mUser = aUser;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		View theView = inflater.inflate( R.layout.game_frag, container, false );

		ButterKnife.inject( this, theView );
		
        mUserNameLabel.setText( mUser.getName() );
        mWheel.setDivCount(12);
        mWheel.setWheelImage(R.drawable.rps_wheel);
        
        if( mUser.getIsFemale() )
        {
        	mAvatarImage.setImageResource(R.drawable.favatards);
        }
        else
        {
        	mAvatarImage.setImageResource(R.drawable.mavatards);
        }
        
        mWheel.setWheelChangeListener(new WheelMenu.WheelChangeListener() {
        	@Override
        	public void onSelectionChange(int selectedPosition) {
        	      switch( selectedPosition )
        	      {
        	      case 0: case 1: case 2: case 3:
        	    	  mUserEntryLabel.setText("ROCK");
        	    	  mMove = move.ROCK;
        	    	  break;
        	      case 4: case 5: case 6: case 7:
        	    	  mUserEntryLabel.setText( "PAPER" );
        	    	  mMove = move.PAPER;
        	    	  break;
        	      default:
        	    	  mUserEntryLabel.setText("SCISSCORS");
        	    	  mMove = move.SCISSORS;
        	    	  break;
        	      }
        	    }
        	});
        
        mPlayButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				EventBus.getDefault().post( new UserPlayEvent(mUser, getMove( mMove ) ) );
			}
		});
  	    mUserEntryLabel.setText( "PAPER" );
  	    mMove = move.PAPER;
        return theView;
	}
	
	private Character getMove( move aMove )
	{
		switch( aMove )
		{
			case ROCK: return 'r';
			case PAPER: return 'p';
			default: return 's';
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("user", mUser);
		super.onSaveInstanceState(outState);
	}
}