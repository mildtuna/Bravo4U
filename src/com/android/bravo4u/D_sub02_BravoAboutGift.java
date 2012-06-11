package com.android.bravo4u;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class D_sub02_BravoAboutGift extends Activity implements View.OnClickListener
{

	X_BravoMyView myview;
	Button sendbtn;
	X_BravoWebserver getcomplimentNum;
	
    public void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_NoTitleBar);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.d_sub02_bravoaboutgift);

        myview=(X_BravoMyView)findViewById(R.id.Myviewxml);
        sendbtn =(Button)findViewById(R.id.sendBtn);
        sendbtn.setOnClickListener(this);
        
        getcomplimentNum =new X_BravoWebserver(this);
        
        sendData(0);
	}
    
    public void sendData(int sendingnum)
    {
      
		Intent intent =getIntent();
		String str1= intent.getExtras().get("phone_num").toString();
		String phone_num = str1.substring(1);
		
		String sendingpz = Integer.toString(sendingnum);
		
		String str = getcomplimentNum.getComplimentNumData(phone_num, sendingpz);
		int num =Integer.parseInt(str);
		myview.puzleNum = num;
		myview.invalidate();

    }

    public void onClick(View v)
    {
    	sendData(1);		
    }
}
