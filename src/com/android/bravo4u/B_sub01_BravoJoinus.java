package com.android.bravo4u;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class B_sub01_BravoJoinus extends Activity implements View.OnClickListener 
{
	EditText name_EditText, phone_num_EditText, password_EditText;
	String nameStr, phone_numStr, passwordStr;

	ConnectivityManager connectManger;
	NetworkInfo networkinfo;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b_sub01_bravojoinus);

		name_EditText = (EditText) findViewById(R.id.name_EditText);
		phone_num_EditText = (EditText) findViewById(R.id.phone_num_EditText);
		password_EditText = (EditText) findViewById(R.id.password_EditText);

		Button joinUsBtn = (Button) findViewById(R.id.joinUsBtn);
		joinUsBtn.setOnClickListener(this);

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

			nameStr = name_EditText.getText().toString().trim();
			phone_numStr = phone_num_EditText.getText().toString().trim();
			passwordStr = password_EditText.getText().toString().trim();

			int first_bravo_num = 25;
			String bravo_numStr = Integer.toString(first_bravo_num);
			String img_url = "http://210.115.58.140/upload/defaultimg.jpg";
			String promise_person = "nobody";

			if (!nameStr.equals("") && !phone_numStr.equals("")&& !passwordStr.equals("")) 
			{
				// 서버로 가입할 회원 정보 보낸다.
				X_BravoWebserver clientDataSend = new X_BravoWebserver(this);
				
				clientDataSend.sendJoinusData(nameStr, phone_numStr,
						passwordStr, bravo_numStr, img_url, promise_person);

				Toast.makeText(getApplicationContext(), "축하합니다. 회원가입되셨습니다~!",
						Toast.LENGTH_SHORT).show();
				finish();
				Intent intent = new Intent(B_sub01_BravoJoinus.this,
						B_Main_BravoIntro.class);
				startActivity(intent);

			} else 
			{
				Toast.makeText(getApplicationContext(), "빈칸을 입력해주세요.",
						Toast.LENGTH_SHORT).show();
			}

		} else
		{
			Toast.makeText(getApplicationContext(), "네트워크를 연결해주세요",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			finish();
			Intent intent = new Intent(B_sub01_BravoJoinus.this,
					B_Main_BravoIntro.class);
			startActivity(intent);
		}
		return false;
	}
}
