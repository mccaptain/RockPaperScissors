package com.mcgrath.rockpaperscissors.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.example.rockpaperscissors.R;
import com.mcgrath.rockpaperscissors.events.LoadUserEvent;
import com.mcgrath.rockpaperscissors.events.SaveUserInfoEvent;

import de.greenrobot.event.EventBus;


public class UserInputFragment extends Fragment
{
	@InjectView( R.id.userName ) EditText mUserNameEditbox;
	@InjectView( R.id.userAge ) NumberPicker mUserAgePicker;
	@InjectView( R.id.check_sex_female ) CheckBox mFemaleSexCheckBox;
	@InjectView( R.id.input_user_button ) Button mInputButton;
	@InjectView( R.id.user_spinner ) Spinner mUserSpinner;
	@InjectView( R.id.load_user_button ) Button mLoadButton;
	
	public static UserInputFragment newInstance( Bundle aArgs )
	{
		UserInputFragment theFrag = new UserInputFragment();
		if( aArgs != null )
		{
			//set user info?
		}
		return theFrag;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		 View theView = inflater.inflate(R.layout.user_input_frag, container, false);

		 ButterKnife.inject( this, theView );
		 mUserAgePicker.setMinValue( 0 );
		 mUserAgePicker.setMaxValue( 100 );
		 mUserAgePicker.setValue( 2 );
		 
		 mInputButton.setOnClickListener( new OnClickListener() 
		 {
			
			@Override
			public void onClick(View v) 
			{
				User theUser = new User( mUserNameEditbox.getText().toString(),
										 mFemaleSexCheckBox.isChecked(),
										 mUserAgePicker.getValue(),
										 0, 0 );

				EventBus.getDefault().post( new SaveUserInfoEvent( theUser) );
			}
		});
		 
		 ArrayList<String> theNames = ((CentralActivity)getActivity()).mDBHelper.getUsers( ((CentralActivity)getActivity()).mDBHelper.getReadableDatabase() );
		 if( theNames.size() > 0 )
		 {
			 ArrayAdapter<String> theAd = new ArrayAdapter<String>( getActivity(), android.R.layout.simple_spinner_item, theNames );
			 mUserSpinner.setAdapter( theAd );
			 mLoadButton.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					EventBus.getDefault().post( new LoadUserEvent( mUserSpinner.getSelectedItem().toString() ) );					
				}
			});
		 }
		 else
		 {
			 mUserSpinner.setVisibility( View.GONE );
			 mLoadButton.setVisibility( View.GONE );
		 }
		 return theView;
	}
	
}