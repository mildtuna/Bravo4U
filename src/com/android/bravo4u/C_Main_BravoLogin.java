package com.android.bravo4u;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class C_Main_BravoLogin extends Activity implements View.OnClickListener
{
	EditText phone_num_loginEdit,password_loginEdit;
	Button loginBtn;
	String phone_numStr, passwordStr;
	
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
		phone_numStr = phone_num_loginEdit.getText().toString().trim();
		passwordStr = password_loginEdit.getText().toString().trim();
		
    	if(!phone_numStr.equals("")&& !passwordStr.equals(""))
    	{	
    		//������ ������ ȸ�� ���� ������.
    		X_BravoWebserver loginDataSend =new X_BravoWebserver(this);
    		//����ȣ�� ȸ���˻�
    		String returnLoginData =loginDataSend.sendLoginData(phone_numStr).trim();
    		
    		if(returnLoginData.equals(""))
    		{
    			Toast.makeText(getApplicationContext(), "ȸ���� �ƴϽʴϴ�.", Toast.LENGTH_SHORT).show();
    		}else
    		{
    			String dataArr[] = returnLoginData.split(","); // ��й�ȣ�� ��ġ�ϴ��� �˾ƺ��� ����
    			
    			if(passwordStr.equals(dataArr[1]))
    			{
    				//ȸ���̰� ��� ��ġ�ϸ� ���� ȭ������ ����.
    				Intent intent=new Intent(this, D_Main_BravoMain.class);
    				intent.putExtra("phone_num", phone_numStr);
    				startActivity(intent);
    				finish();
    				
    			}else
    			{
    				Toast.makeText(getApplicationContext(), "��й�ȣ�� ��ġ���� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
    			}	
    		}
    		    		  			
    	}else 
    	{
    		Toast.makeText(getApplicationContext(), "��ĭ�� �Է����ּ���.", Toast.LENGTH_SHORT).show();
    	}
    }
}