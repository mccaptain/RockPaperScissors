package com.mcgrath.rockpaperscissors.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.events.PlayAgainEvent;

import de.greenrobot.event.EventBus;


public class SetupFragment extends Fragment
{
	
	@InjectView( R.id.single_button ) Button mSingleButton;
	@InjectView( R.id.multi_button ) Button mMultiButton;
	
	public static SetupFragment newInstance( Bundle aArgs )
	{
		SetupFragment theFrag = new SetupFragment();
		return theFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		EventBus.getDefault().unregister(this);
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onDestroy() 
	{
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		View theView = inflater.inflate( R.layout.fragment_setup, container, false );

		ButterKnife.inject( this, theView );

		mSingleButton.setOnClickListener( new OnClickListener() 
        {
			
			@Override
			public void onClick(View v)
			{
				CentralActivity theCA = (CentralActivity)getActivity();
				EventBus.getDefault().post( new PlayAgainEvent() );
			}
		});
        
        mMultiButton.setOnClickListener( new OnClickListener()
        {
			
			@Override
			public void onClick(View v)
			{
				CentralActivity theCA = (CentralActivity)getActivity();
			
				theCA.gotoBluetooth();
			}
		});
        
        return theView;
	}
	
}