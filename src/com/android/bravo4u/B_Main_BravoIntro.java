package com.android.bravo4u;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
		
		// 로그인 상태 알아본다.
		
		SharedPreferences pref = getSharedPreferences("LogIn",0);
		// 처음에 아무상태도 없을때 로그인 상태를 넣어준다  로그인=1 로그아웃 =0
		if(pref.getInt("LoginState", 1000)==1)
        {
        	SharedPreferences sharedpref = getSharedPreferences("PhoneNumber",0);
        	String phone_numStr= sharedpref.getString("phone_num", "");

			Intent intent=new Intent(B_Main_BravoIntro.this, D_Main_BravoMain.class);
			intent.putExtra("phone_num", phone_numStr);
			startActivity(intent);
			//finish();
        }
		
	
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
