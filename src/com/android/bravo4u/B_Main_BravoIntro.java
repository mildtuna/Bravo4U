package com.android.bravo4u;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class B_Main_BravoIntro extends Activity implements View.OnClickListener
{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_NoTitleBar);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.b_main_bravointro);

		Button gotoJoinUsActBtn =(Button)findViewById(R.id.gotoJoinUsActBtn);
		Button gotoLoginActBtn =(Button)findViewById(R.id.gotoLoginActBtn);
		
		gotoJoinUsActBtn.setOnClickListener(this);
		gotoLoginActBtn.setOnClickListener(this);
		
	}
    
    public void onClick(View v)
    {
    	switch(v.getId()) 
    	{
    		case R.id.gotoJoinUsActBtn:
    			Intent intent =new Intent(this,B_sub01_BravoJoinus.class);
    			startActivity(intent);
    			break;
    		case R.id.gotoLoginActBtn:
    			intent =new Intent(this,C_Main_BravoLogin.class);
    			startActivity(intent);
    			break;
    	}
    }
}
