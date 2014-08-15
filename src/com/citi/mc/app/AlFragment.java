package com.citi.mc.app;
import com.citi.mc.*;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "WorldReadableFiles", "ShowToast" })
public class AlFragment extends Fragment
{
	private EditText et;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.retrievepassword, container, false); 
		//设置MainTabActivity的flag为false
		MainTabActivity.flag = false;
		ImageView iv = (ImageView)view.findViewById(R.id.cancelalert);
		Button bn = (Button)view.findViewById(R.id.save);
		et = (EditText)view.findViewById(R.id.alertet);		
		iv.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				show(1);
			}
		});
		bn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				SPhelper sp = new SPhelper(getActivity());
				sp.setPassword(et.getText().toString());
				Toast.makeText(getActivity(), sp.getPassword(), 3000).show();
			}
		});
        return view; 
        
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	private void show(int index)
	{
		FragmentManager manager =  getFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		ft.setCustomAnimations(R.anim.push_left_out,R.anim.push_left_zhong);
		Fragment fragment = (Fragment)getFragmentManager().findFragmentById(android.R.id.tabcontent);
		switch (index)
		{
		case 1:
			fragment = new SetFragement();
			ft.replace(android.R.id.tabcontent, fragment);
			break;

		default:
			break;
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); 
        ft.addToBackStack(null);  
        ft.commit();  
	}
}