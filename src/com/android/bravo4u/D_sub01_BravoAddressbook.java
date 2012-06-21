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
	private String phonelist ;
	private ArrayList<String> groupArray ;
	private ListView list_addFamily;
	private Button BackBtn;
	private ArrayList<String> listArray;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_sub01_bravoaddressbook);

        //주소록 리스트로 옮기기
        phonelist =  getContacts();
        String[] phone_array =phonelist.split("q");
        groupArray =new ArrayList<String>();
        groupArray.addAll(Arrays.asList(phone_array));
        //String str ="";
        
        for(int i=0; i<groupArray.size(); i++)
        {
        	for(int j=0; j<groupArray.size(); j++)
        	{
        		if(i!=j)
        		{
        			if(groupArray.get(i).equals(groupArray.get(j))||groupArray.get(i).contains(groupArray.get(j)))
        			{
            			if((j+1) != groupArray.size())
            			{
            				groupArray.remove(j);
            			}	
        			}
        		}
        		
        	}
        }
        
        for(int i=0; i<groupArray.size();i++)
        {
        	if(!groupArray.get(i).contains("#"))
        	{
        		groupArray.remove(i);
        	}
        }
        
//        for(int i=0; i<groupArray.size(); i++)
//        {
//        	str += ""+ i + groupArray.get(i)+"\n\n";
//        }
          
        listArray =new ArrayList<String>();
        serverDbhasAddress();
         
        list_addFamily = (ListView)findViewById(R.id.addressbookList);
	     
        list_addFamily.setAdapter(new ArrayAdapter<String>(this,
	             android.R.layout.simple_list_item_1, listArray));
	
        list_addFamily.setOnItemClickListener(this); 
        
        //뒤로가기 버튼
        BackBtn=(Button)findViewById(R.id.addFamilyBackBtn);
        BackBtn.setOnClickListener(this);
       
//        AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);
//		alertDlg.setTitle("폰리스트");
//		alertDlg.setMessage(str);
//		alertDlg.setNegativeButton("확인", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
//		});
//		alertDlg.show();
     
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
        	
        }else 	Toast.makeText(this, name + "님은 이미 추가 되있습니다.", Toast.LENGTH_LONG).show();
         
        if (cnt == -1) Toast.makeText(this, name + "님이 db에 추가되지 않았습니다.", Toast.LENGTH_LONG).show();

        dbhandler.close();
    }
    
    public void onClick(View v)
    {
    	finish();
    }
    
    public void serverDbhasAddress()
    {
    	// 서버에 있는 주소록(폰번호) 가져오기
        X_BravoWebserver getphonenum = new X_BravoWebserver(this);
        String  allPhonenumStr=getphonenum.getPhonenumData();
        String[]serverDbPhoneArr =allPhonenumStr.split(",");
        
        for(int i=0; i<groupArray.size(); i++)
        {
        	for(int j=0; j<serverDbPhoneArr.length; j++)
        	{
        		String[] array = groupArray.get(i).split("#");
        		//Log.e("주소록 잘못됐어", ""+groupArray.get(i));
        		//Log.e("주소록 잘못됐어", ""+array[array.length-1]);
        		
        		String phone_num = array[array.length-1].substring(1);
        		
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
    
    public String getContacts() 
    {

        //주소록 정보를 담을 문자열 객체
        StringBuffer sb = new StringBuffer();
        
        //getContentResolver() 메소드로 ContentResolver를 얻은 후에  query 메소드를 사용해서 주소록 정보를 요청한다.
        //이렇게 얻은 주소록 결과 집합은 Cursor를 통해서 랜덤하게 접근할 수 있다.
        Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        
        //Cursor의 moveToNext() 메소드를 사용해서 주소록을 검색한다.
        while (contacts.moveToNext())
        {
            
            //저장된 이름과 성을 구한다.
            String displayName = contacts.getString(contacts
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        
            sb.append("" + displayName).append("#");

            //주소록 아이디를 구한다.
            //이 아이디는 전화번호 목록과 이메일 목록을 찾을 때 사용한다.
            String contactId = contacts.getString(contacts
                    .getColumnIndex(ContactsContract.Contacts._ID));
            
            //전화번호 저장 여부를 구한다.
            //저장되어 있다면 "1"이 반환되며 그렇지 않다면 "0"이 반환된다.
            String hasPhoneNumber = contacts.getString(contacts
                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            
 

            //전화번호가 저장되어 있다면
            if (hasPhoneNumber.equals("1")) 
            {
            	
                //전화번호 Cursor를 생성한다.
                //이때 contactId를 사용해서 전화번호 목록을 구한다.
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null);
                
                
                
                //전화번호가 있을 때까지 계속 반복하면서 전화번호를 구한다.
                while (phones.moveToNext()) 
                {
                    String phoneNumber = phones.getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    sb.append(phoneNumber).append("q");
                }
                
                
                //전화번호 Cursor를 닫는다.
                phones.close();
            }
           


        }

        //주소록 Cursor를 닫는다.
        contacts.close();
        
        return sb.toString();
    }  
}
