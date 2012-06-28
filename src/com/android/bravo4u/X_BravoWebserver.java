package com.android.bravo4u;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

public class X_BravoWebserver 
{
	Context mContext;

	public X_BravoWebserver(Context context) {
		mContext = context;
	}
	
	
	//--------------------------------------------------------------------
	//
	//					회원가입데이터 서버로 보낼때
	//
	//--------------------------------------------------------------------
	
	public String sendJoinusData(String name, String phone_num, String password, String bravo_num, String img_url, String promise_person) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과 출력
			 returnStr = sendData01(name, phone_num, password,bravo_num,img_url,promise_person);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData01(String name, String phone_num, String password, String bravo_num, String img_url,String promise_person)
			throws ClientProtocolException, IOException 
	{
		
		HttpPost request = makeHttpPost01(name, phone_num, password,bravo_num, img_url,promise_person,"http://210.115.58.140/test2.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		Log.e("sendData01", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost01(String $name, String $phone_num,String $password,String $bravo_num, String $img_url,String $promise_person, String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("name", $name));
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		nameValue.add(new BasicNameValuePair("password", $password));
		nameValue.add(new BasicNameValuePair("bravo_num", $bravo_num));
		nameValue.add(new BasicNameValuePair("img_url", $img_url));
		nameValue.add(new BasicNameValuePair("promise_person", $promise_person));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost01", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					로그인데이터 서버로 보낼때
	//
	//--------------------------------------------------------------------
	
	public String sendLoginData(String phone_num) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData02(phone_num);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData02(String phone_num)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost02(phone_num,"http://210.115.58.140/test3.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData02", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost02(String $phone_num, String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost02", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					모든 회원번호  서버에서 받아올 때
	//
	//--------------------------------------------------------------------
	
	public String getPhonenumData() 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData03();
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData03()
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost03("http://210.115.58.140/test4.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData03", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost03(String $url) 
	{
		HttpPost request = new HttpPost($url);
		Log.e("makeHttpPost03", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					칭찬 개수 받아올때
	//
	//--------------------------------------------------------------------
	
	
	public String getComplimentNumData(String phone_num, String sendingpz) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData04(phone_num, sendingpz);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData04(String phone_num,String sendingpz)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost04(phone_num,sendingpz,"http://210.115.58.140/test5.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData04", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost04(String $phone_num, String $sendingpz ,String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		nameValue.add(new BasicNameValuePair("sendingpz", $sendingpz));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost04", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					데이터베이스에서 회원삭제
	//
	//--------------------------------------------------------------------
	
	
	public String deleteDataonServer(String phone_num) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData05(phone_num);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData05(String phone_num)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost05(phone_num,"http://210.115.58.140/test6.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData05", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost05(String $phone_num,String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost05", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					이미지 Url 업데이트
	//
	//--------------------------------------------------------------------
	
	
	public String ImgUpdateOnServer(String phone_num, String update_url) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData06(phone_num, update_url);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData06(String phone_num, String update_url)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost06(phone_num,update_url,"http://210.115.58.140/test8.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData06", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost06(String $phone_num,String $update_url,String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		nameValue.add(new BasicNameValuePair("update_url", $update_url));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost06", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					ImageUrl받아오기
	//
	//--------------------------------------------------------------------
	
	
	public String getUrlOnServer(String phone_num) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData07(phone_num);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData07(String phone_num)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost07(phone_num,"http://210.115.58.140/test9.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData07", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost07(String $phone_num,String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost07", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					선물사주기로 약속한 사람 번호 업데이트
	//
	//--------------------------------------------------------------------
	
	
	public String promisePersonUpdateOnServer(String phone_num, String update_promise_person) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData08(phone_num, update_promise_person);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData08(String phone_num, String update_promise_person)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost08(phone_num,update_promise_person,"http://210.115.58.140/test10.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData08", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost08(String $phone_num,String $update_promise_person,String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		nameValue.add(new BasicNameValuePair("update_promise_person", $update_promise_person));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost08", "메서드 호출");
		return request;

	}
	
	//--------------------------------------------------------------------
	//
	//					선물사주기로한사람 누구인지 번호 받아오기
	//
	//--------------------------------------------------------------------

	public String getPromisePersonOnServer(String phone_num) 
	{
		String returnStr= null;
		try {
			// 데이터를 웹서버에 보내고 받아온 결과를 출력
			 returnStr = sendData09(phone_num);
			

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnStr;
	}

	
	private String sendData09(String phone_num)
			throws ClientProtocolException, IOException 
	{
		HttpPost request = makeHttpPost09(phone_num,"http://210.115.58.140/test11.php");

		HttpClient client = new DefaultHttpClient();

		ResponseHandler<String> reshandler = new BasicResponseHandler();
		String result = client.execute(request, reshandler);
		
		Log.e("sendData09", result);
		
		return result;
	}
	
	private HttpPost makeHttpPost09(String $phone_num,String $url) 
	{
		HttpPost request = new HttpPost($url);
		Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
		nameValue.add(new BasicNameValuePair("phone_num", $phone_num));
		request.setEntity(makeEntity(nameValue));

		Log.e("makeHttpPost09", "메서드 호출");
		return request;

	}
	

	private HttpEntity makeEntity(Vector<NameValuePair> $nameValue) {
		HttpEntity result = null;

		try {
			result = new UrlEncodedFormEntity($nameValue);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Log.e("makeEntitiy", "메서드 불림");

		return result;
	}
}
