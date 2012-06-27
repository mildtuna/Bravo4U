package com.android.bravo4u;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class A_Main_BravoLogo extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_NoTitleBar);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main_brovologo);

		thread.start();


	}


	Thread thread = new Thread(new Runnable(){

		public void run()
		{
			try{
				Thread.sleep(1200);// 로고1. 2초 보여주고 바로 메인 액티비티로 넘어간다
	
					Intent intent =new Intent(A_Main_BravoLogo.this,B_Main_BravoIntro.class);
					startActivity(intent);
					finish();				

				Thread.interrupted();

			}catch(InterruptedException e){}

		}
	});
}