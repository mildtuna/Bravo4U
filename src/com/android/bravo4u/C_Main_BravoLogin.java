package com.android.bravo4u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class C_Main_BravoLogin extends Activity implements View.OnClickListener
{
	EditText phone_num_loginEdit,password_loginEdit;
	Button loginBtn;
	String phone_numStr, passwordStr;
	
	ConnectivityManager connectManger;
	NetworkInfo networkinfo;
	
	
    public void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_NoTitleBar);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.c_main_bravologin);
		
		phone_num_loginEdit =(EditText)findViewById(R.id.phone_num_loginEdit);
		password_loginEdit =(EditText)findViewById(R.id.password_loginEdit);
		loginBtn =(Button)findViewById(R.id.loginBtn);
		
		loginBtn.setOnClickListener(this);
		
	}
    
    public void onClick(View v)
    {

    	
    	
    	// 네트워크 연결 관리자의 핸들을 얻는다.
		connectManger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// 기본 모바일 네트워크 연결자(3g)관련 정보를 얻는다.
		networkinfo = connectManger
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobieConn = networkinfo.isConnected();
		// WiFi관련 정보를 얻는다.
		networkinfo = connectManger
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = networkinfo.isConnected();

		if (isMobieConn || isWifiConn) 
		{
			phone_numStr = phone_num_loginEdit.getText().toString().trim();
			passwordStr = password_loginEdit.getText().toString().trim();
			
	    	if(!phone_numStr.equals("")&& !passwordStr.equals(""))
	    	{	
	    		//서버로 가입할 회원 정보 보낸다.
	    		X_BravoWebserver loginDataSend =new X_BravoWebserver(this);
	    		//폰번호로 회원검색
	    		String returnLoginData =loginDataSend.sendLoginData(phone_numStr).trim();
	    		
	    		if(returnLoginData.equals(""))
	    		{
	    			Toast.makeText(getApplicationContext(), "회원이 아니십니다.", Toast.LENGTH_SHORT).show();
	    		}else
	    		{
	    			String dataArr[] = returnLoginData.split(","); // 비밀번호가 일치하는지 알아보기 위해
	    			
	    			if(passwordStr.equals(dataArr[1]))
	    			{
	    				SharedPreferences pref = getSharedPreferences("LogIn",0);
			        	SharedPreferences.Editor edit = pref.edit();
			        	edit.putInt("LoginState", 1);
			        	edit.commit();
	    				
	    				//회원이고 비번 일치하면 메인 화면으로 간다.
	    				Intent intent=new Intent(C_Main_BravoLogin.this, D_Main_BravoMain.class);
	    				intent.putExtra("phone_num", phone_numStr);
	    				startActivity(intent);
	    				
	    				finish();
	    				
	    			}else
	    			{
	    				Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
	    			}	
	    		}
	    		    		  			
	    	}else 
	    	{
	    		Toast.makeText(getApplicationContext(), "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
	    	}
		}else
		{
			Toast.makeText(getApplicationContext(), "네트워크를 연결해주세요",Toast.LENGTH_SHORT).show();
		}	
		
    }
    
    @Override   
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {    
        if(keyCode == KeyEvent.KEYCODE_BACK) 
        {
        	finish();
        	Intent intent = new Intent(C_Main_BravoLogin.this,B_Main_BravoIntro.class);
        	startActivity(intent);    
        }
        return false;    
    }

}
