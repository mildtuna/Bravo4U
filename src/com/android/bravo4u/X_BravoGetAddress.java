package com.android.bravo4u;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class X_BravoGetAddress 
{
	Context mContext;
	
	
	public X_BravoGetAddress(Context context)
	{
		mContext = context;
	}
	
	public ArrayList<String> arrangeAddress()
	{
		String phonelist =  getContacts();
		ArrayList<String> groupArray = new ArrayList<String>();
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
		
		return groupArray;
	}
	
	public String getContacts() 
    {

        //주소록 정보를 담을 문자열 객체
        StringBuffer sb = new StringBuffer();
        
        //getContentResolver() 메소드로 ContentResolver를 얻은 후에  query 메소드를 사용해서 주소록 정보를 요청한다.
        //이렇게 얻은 주소록 결과 집합은 Cursor를 통해서 랜덤하게 접근할 수 있다.
        Cursor contacts = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        
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
                Cursor phones = mContext.getContentResolver().query(
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
