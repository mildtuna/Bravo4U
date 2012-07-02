package com.android.bravo4u;

import java.util.ArrayList;
import java.util.Arrays;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class D_sub01_BravoAddressbook extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener
{

	private ListView list_addFamily;
	private Button BackBtn;
	private ArrayList<String> listArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		setTheme(android.R.style.Theme_NoTitleBar);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_sub01_bravoaddressbook);

          
        listArray =new ArrayList<String>();
        serverDbhasAddress();
         
        list_addFamily = (ListView)findViewById(R.id.addressbookList);
	     
        list_addFamily.setAdapter(new ArrayAdapter<String>(this,
	             android.R.layout.simple_list_item_1, listArray));
	
        list_addFamily.setOnItemClickListener(this); 
        
        //뒤로가기 버튼
        BackBtn=(Button)findViewById(R.id.addFamilyBackBtn);
        BackBtn.setOnClickListener(this);
       

     
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
    	long cnt =0;
    	X_BravoDBHandler dbhandler = X_BravoDBHandler.open(this);
    	
    	String insertDBdata[] =listArray.get(position).split("\n");
    	String name = insertDBdata[0];
    	String phone_num = insertDBdata[1];
        
        int cursorCount = dbhandler.select(phone_num);
        if(cursorCount== 0)
        {
        	cnt = dbhandler.insert(name,phone_num);
        	Toast.makeText(this, name + "를 칭찬목록에 추가했습니다.", Toast.LENGTH_LONG).show();
        	
        }else 	Toast.makeText(this, name + "는 이미 목록에 추가 되있습니다.", Toast.LENGTH_LONG).show();
         
        if (cnt == -1) Toast.makeText(this, name + "님이 db에 추가되지 않았습니다.", Toast.LENGTH_LONG).show();

        dbhandler.close();
    }
    
    public void onClick(View v)
    {
    	finish();
    }
    
    
    public void serverDbhasAddress()
    {
    	X_BravoGetAddress getAddress =new X_BravoGetAddress(this);
    	ArrayList<String> groupArray = getAddress.arrangeAddress();
    	
    	// 서버에 있는 주소록(폰번호) 가져오기
        X_BravoWebserver getphonenum = new X_BravoWebserver(this);
        String  allPhonenumStr=getphonenum.getPhonenumData();
        //Toast.makeText(getApplicationContext(), allPhonenumStr, Toast.LENGTH_LONG).show();
        String[]serverDbPhoneArr =allPhonenumStr.split(",");
        
        for(int i=0; i<groupArray.size(); i++)
        {
        	for(int j=0; j<serverDbPhoneArr.length; j++)
        	{
        		String[] array = groupArray.get(i).split("#");
        		
        		//String phone_num = array[array.length-1].substring(1);
        		String phone_num = array[array.length-1];
        		
        		if(phone_num.equals(serverDbPhoneArr[j]))
        		{
        			//서버에있는 폰번호와 현재폰 주소록에있는 번호와 일치한것만 linkedList에 넣는다.
        			String name = array[array.length-2];
        			String phoneNum = array[array.length-1];
        			listArray.add(name+"\n"+phoneNum);
        		}
        	}
        }
        
    }
    
 
}
