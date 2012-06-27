package com.android.bravo4u;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class D_Main_BravoMain extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, TabHost.OnTabChangeListener
{
	TabHost tabHost;
	ListView groupTabList;
	ArrayList<String> firstTablistArray;
	ArrayList<String> phone_numArray;
	
	Button firstTabEditBtn, firstTabCompleteBtn, firstTabAddGroupBtn, firstTabDeleteBtn; 
	Button secondTabSelectPhotoBtn,secondTabInfoBtn,secondTabLogoutBtn,secondTabMemberCancellationBtn;
	
	ConnectivityManager connectManger;
	NetworkInfo networkinfo;
	boolean isMobieConn;
	boolean isWifiConn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		setTheme(android.R.style.Theme_NoTitleBar);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.d_main_bravomain);
		

		// ------------------------- 탭 설정 -----------------------------
		tabHost = (TabHost) findViewById(R.id.tabhost);
		
		tabHost.setup();

		TabSpec tabSpec1 = tabHost.newTabSpec("Tab1").setIndicator("");
		tabSpec1.setContent(R.id.tab1);
		tabHost.addTab(tabSpec1);

		TabSpec tabSpec2 = tabHost.newTabSpec("Tab2").setIndicator("");
		tabSpec2.setContent(R.id.tab2);
		tabHost.addTab(tabSpec2);

		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(this);
		
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_01_a);
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_02_a);

		
		//--------------------------db에 내정보 넣기------------------------
		
    	X_BravoDBHandler dbhandler = X_BravoDBHandler.open(this);
    	Intent intent =getIntent();
    	String phone_num = intent.getExtras().get("phone_num").toString();
        int cursorCount = dbhandler.select(phone_num);
        if(cursorCount== 0)
        {
        	dbhandler.insert("나",phone_num);
        	
        }else 	Toast.makeText(this, "회원님은 이미 db에추가 되있습니다.", Toast.LENGTH_LONG).show();
 

        dbhandler.close();
        
        //-------------- 로그인 자동으로 해줄때 phonnum 값 intent 대신 pref로 넘겨주기 위해 pref에 값 넣는다 ----------
        
		Intent get_intent01 =getIntent();
    	String phone_numstr = get_intent01.getExtras().get("phone_num").toString();
    	
    	SharedPreferences pref = getSharedPreferences("PhoneNumber",0);
    	SharedPreferences.Editor edit = pref.edit();
    	edit.putString("phone_num", phone_numstr);
    	edit.commit();
    	
    	//---------------------와이파이 및 3g 상태 점검 ----------------------------
    	
    	networkState();
        
		//--------------------------- 첫번째탭 ----------------------------
		
		firstTabEditBtn =(Button)findViewById(R.id.firstTabEditBtn);
		firstTabCompleteBtn =(Button)findViewById(R.id.firstTabCompleteBtn);
		firstTabAddGroupBtn =(Button)findViewById(R.id.firstTabAddGroupBtn);
		firstTabDeleteBtn =(Button)findViewById(R.id.firstTabDeleteBtn);

		firstTabEditBtn.setOnClickListener(this);
		firstTabCompleteBtn.setOnClickListener(this);
		firstTabAddGroupBtn.setOnClickListener(this);
		firstTabDeleteBtn.setOnClickListener(this);

		firstTabCompleteBtn.setVisibility(firstTabCompleteBtn.GONE);
		firstTabDeleteBtn.setVisibility(firstTabDeleteBtn.GONE);

	    firstTablistArray = new ArrayList<String>(); 
	    phone_numArray = new ArrayList<String>();
		groupTabList =(ListView)findViewById(R.id.groupTabList);
		
		//------------------------- 두번째 탭 --------------------------------
	
		secondTabSelectPhotoBtn =(Button)findViewById(R.id.secondTabSelectPhotoBtn);
		secondTabInfoBtn =(Button)findViewById(R.id.secondTabInfoBtn);
		secondTabLogoutBtn =(Button)findViewById(R.id.secondTabLogoutBtn);
		secondTabMemberCancellationBtn =(Button)findViewById(R.id.secondTabMemberCancellationBtn);
		
		secondTabSelectPhotoBtn.setOnClickListener(this);
		secondTabInfoBtn.setOnClickListener(this);
		secondTabLogoutBtn.setOnClickListener(this);
		secondTabMemberCancellationBtn.setOnClickListener(this);
		
	}
    public void onResume()
    {
    	super.onResume();
    	firstTab();
    }
	
	public void onClick(View v)
	{
		// 첫번째탭에 있는 버튼들
		switch(v.getId())
		{
			case R.id.firstTabEditBtn:
				firstTabEditBtn.setVisibility(firstTabEditBtn.GONE);
				firstTabCompleteBtn.setVisibility(firstTabCompleteBtn.VISIBLE);
				firstTabAddGroupBtn.setVisibility(firstTabAddGroupBtn.GONE);
				firstTabDeleteBtn.setVisibility(firstTabDeleteBtn.VISIBLE);
				
				firstTab();
				break;
			case R.id.firstTabCompleteBtn:
				firstTabEditBtn.setVisibility(firstTabEditBtn.VISIBLE);
				firstTabCompleteBtn.setVisibility(firstTabCompleteBtn.GONE);
				firstTabAddGroupBtn.setVisibility(firstTabAddGroupBtn.VISIBLE);
				firstTabDeleteBtn.setVisibility(firstTabDeleteBtn.GONE);
				
				firstTab();
				break;
			case R.id.firstTabAddGroupBtn:
				
				networkState();
				
				if (isMobieConn || isWifiConn) 
				{
					Intent intent =new Intent(D_Main_BravoMain.this,D_sub01_BravoAddressbook.class);
					startActivity(intent);
				}else
				{
					Toast.makeText(getApplicationContext(), "네트워크를 연결해주세요",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.firstTabDeleteBtn:
				
				deleteListCheckItem();
				break;
		}
		
		
		// 두번째 탭에 있는 버튼들
		switch(v.getId())
		{
			case R.id.secondTabSelectPhotoBtn:
				
				networkState();
				
				if (isMobieConn || isWifiConn) 
				{
			    	X_BravoDBHandler dbhandler = X_BravoDBHandler.open(this);
	
					Intent get_intent =getIntent();
			    	String phone_num = get_intent.getExtras().get("phone_num").toString();
			        int cursorCount = dbhandler.select(phone_num);
			        if(cursorCount!= 0)
			        {
						Intent get_intent01 =getIntent();
				    	String phone_numstr = get_intent01.getExtras().get("phone_num").toString();
				    	
						Intent intent =new Intent(D_Main_BravoMain.this,D_sub03_BravoSelectPhoto.class);
						intent.putExtra("phone_num", phone_numstr);
						startActivity(intent);
			        }else
			        {
			        	Toast.makeText(this, "회원이 아니셔서 선물을 선택할 수 없습니다.", Toast.LENGTH_LONG).show();
			        }
			        dbhandler.close();
				}else
				{
					Toast.makeText(getApplicationContext(), "네트워크를 연결해주세요",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.secondTabMemberCancellationBtn:
				
				networkState();

				if (isMobieConn || isWifiConn) 
				{
				
			    	X_BravoDBHandler dbhandler02 = X_BravoDBHandler.open(this);
	
					Intent get_intent02 =getIntent();
			    	String phone_num02 = get_intent02.getExtras().get("phone_num").toString();
			        int cursorCount02 = dbhandler02.select(phone_num02);
			        if(cursorCount02!= 0)
			        {
			        	dbhandler02.deleteAll();
						SharedPreferences pref = getSharedPreferences("LogIn",0);
			        	SharedPreferences.Editor edit = pref.edit();
			        	edit.putInt("LoginState", 0);
			        	edit.commit();
	
			        	Toast.makeText(this, "회원님의 데이터가 db에서 삭제 되었습니다.", Toast.LENGTH_LONG).show();
			        	
			        }else 	Toast.makeText(this, "회원님은 db에없는 회원이십니다.", Toast.LENGTH_LONG).show();
			        dbhandler02.close();
			        
			        phone_num02 = phone_num02.substring(1);
			        
			        X_BravoWebserver server = new X_BravoWebserver(this);
			        String str =server.deleteDataonServer(phone_num02);
			        
			        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
		        
				}else
				{
					Toast.makeText(getApplicationContext(), "네트워크를 연결해주세요",Toast.LENGTH_SHORT).show();
				}
		        
				break;
				
			case R.id.secondTabLogoutBtn:
				SharedPreferences pref = getSharedPreferences("LogIn",0);
	        	SharedPreferences.Editor edit = pref.edit();
	        	edit.putInt("LoginState", 0);
	        	edit.commit();
	        	Toast.makeText(this, "로그아웃되셨습니다.", Toast.LENGTH_LONG).show();
	        	
	        	Intent intent = new Intent(D_Main_BravoMain.this,B_Main_BravoIntro.class);
	        	startActivity(intent);
	        	
	        	finish();
				break;
		}
	}
	
    public void onItemClick(AdapterView<?> listview, View v, int position, long id) 
    {
    	networkState();
    	
		if (isMobieConn || isWifiConn) 
		{
	    	if(firstTabEditBtn.getVisibility() == firstTabEditBtn.VISIBLE)
	    	{
	    		String phone_num = phone_numArray.get(position);
	    		String name = firstTablistArray.get(position);
	    		
	    		Intent intent =new Intent(this, D_sub02_BravoAboutGift.class);
	    		intent.putExtra("phone_num",phone_num);
	    		intent.putExtra("name", name);
	    		startActivity(intent);
	    	}
		}else
		{
			Toast.makeText(getApplicationContext(), "네트워크를 연결해주세요",Toast.LENGTH_SHORT).show();
		}
    	
    }
    
    public void deleteListCheckItem()
    {
    	X_BravoDBHandler dbhandler= X_BravoDBHandler.open(this);
    	
		long[] checklist = groupTabList.getCheckItemIds();
		for(int i=0;i<checklist.length;i++)
		{
			int position =(int)checklist[i];
			if(position != 0) dbhandler.deleteRecord(phone_numArray.get(position));
		}

		dbhandler.close();
		firstTab();
    }
	
	
	public void firstTab()
	{
		X_BravoDBHandler dbhandler= X_BravoDBHandler.open(this);

		   String dbdataAll = dbhandler.selectAll();

		   if(dbdataAll.contains("\n"))
		   {
			   String[] splbyRecode= dbdataAll.split("\n");
			   firstTablistArray = new ArrayList<String>(); 
			   phone_numArray = new ArrayList<String>();
			   
			   for(int i=0; i<splbyRecode.length; i++)
			   {
				   String[] splbyColum = splbyRecode[i].split(",");
				   String name = splbyColum[0];
				   String phone_num = splbyColum[1];
				   
				   firstTablistArray.add(name);
				   phone_numArray.add(phone_num);  
			   }
		   }else
		   { 
			   
			   firstTablistArray.removeAll(firstTablistArray);
			   phone_numArray.removeAll(phone_numArray);
			   
			   LinearLayout layout =(LinearLayout)findViewById(R.id.firstTabLayout);
			   layout.setVisibility(layout.GONE);
			   groupTabList.setVisibility(groupTabList.GONE);

		   }
		   
		   
		   if(firstTabEditBtn.getVisibility() == firstTabEditBtn.VISIBLE)
		   {
			   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					   android.R.layout.simple_list_item_1,firstTablistArray);
			   groupTabList.setAdapter(adapter);
			   groupTabList.setClickable(true);
			   groupTabList.setOnItemClickListener(this);
		   
		   }else if(firstTabEditBtn.getVisibility() == firstTabEditBtn.GONE)
		   {
			   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					   android.R.layout.simple_list_item_multiple_choice,firstTablistArray);
			   
			   groupTabList.setAdapter(adapter);
//	           list_familyTab.setItemsCanFocus(false);
			   groupTabList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		   }


		   //Log.i("퍼스트탭", firstTablistArray.get(0)+firstTablistArray.get(1)+firstTablistArray.get(2));
		   dbhandler.close();
	       
	}
	
	public void networkState()
	{
		connectManger = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		networkinfo = connectManger.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		isMobieConn = networkinfo.isConnected();
		networkinfo = connectManger.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		isWifiConn = networkinfo.isConnected();
	}
	
    public void onTabChanged(String tabId)
    {

    	if(tabId.equals("Tab1"))
    	{
    		firstTab();
    		tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_01_a);
            tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_02_a);
    	}else if(tabId.equals("Tab2"))
    	{
    		tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_01_b);
            tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_02_b);
    	}
    }

}
