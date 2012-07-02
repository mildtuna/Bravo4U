package com.android.bravo4u;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class D_sub03_BravoSelectPhoto extends Activity implements View.OnClickListener
{
	ImageView giftImg;
	Bitmap image_bitmap;
	Button changePhotoBtn;
	
	final int REQ_SELECT=0;
	
	private FileInputStream mFileInputStream = null;
	private URL connectUrl = null;
 
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";	
 
	private int deviceWidth;
	private int deviceHeight;
	private int giftWidth;
	private int giftHeight;
	

	private Bitmap giftBitmap;
	private Canvas canvas;
	private Bitmap background;
	
	
	boolean complete_Flag = false;
	Intent getdataIntent;

	

	
    public void onCreate(Bundle savedInstanceState)
	{
		setTheme(android.R.style.Theme_NoTitleBar);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.d_sub03_bravoselectphoto);
		
	    Display localDisplay = ((WindowManager)getSystemService("window")).getDefaultDisplay();
	    deviceWidth = localDisplay.getWidth();
	    deviceHeight = localDisplay.getHeight();
	    
	    giftWidth = (deviceWidth/10) *8;
	    giftHeight = deviceHeight/2;

		
		giftImg =(ImageView)findViewById(R.id.giftImg);
		
		changePhotoBtn =(Button)findViewById(R.id.changePhotoBtn);
		changePhotoBtn.setOnClickListener(this);

		//목표사진 다운로드
		
		Intent getintent = getIntent();
		String phone_num = getintent.getExtras().get("phone_num").toString();

		X_BravoWebserver server = new X_BravoWebserver(this);
		String imgurl = server.getUrlOnServer(phone_num);
		imgurl = imgurl.replace(" ", "%20");
		
		if(imgurl.contains(",")) imgurl = imgurl.replace(",", "");
		
		//Toast.makeText(getApplicationContext(), imgurl, Toast.LENGTH_SHORT).show();
		
		X_BravoImageDownloader imageDownloader = new X_BravoImageDownloader();
		
		giftBitmap =imageDownloader.download(imgurl, giftImg);
		giftBitmap = Bitmap.createScaledBitmap(giftBitmap, giftWidth, giftHeight, false);
        background = Bitmap.createBitmap(giftWidth, giftHeight, Config.ARGB_8888);
        canvas = new Canvas(background);
        canvas.drawBitmap(giftBitmap, 0, 0, null);
        
        giftImg.setImageBitmap(background);
	}
    

    public void onClick(View v)
    {
	      
    	try {

	    	//사진 읽어오기위한 uri 작성하기.
	    	 Uri uri = Uri.parse("content://media/external/images/media");
	    	 //무언가 보여달라는 암시적 인텐트 객체 생성하기.
	         Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	         //인텐트에 요청을 덛붙인다. 
	         intent.setAction(Intent.ACTION_GET_CONTENT);
	         //모든 이미지
	         intent.setType("image/*");
	         //결과값을 받아오는 액티비티를 실행한다.
	         startActivityForResult(intent, REQ_SELECT);
	         
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    
	 protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
	 {		 

		    try
			{
				//인텐트에 데이터가 담겨 왔다면
				 if(!intent.getData().equals(null))
				 {
				    //해당경로의 이미지를 intent에 담긴 이미지 uri를 이용해서 Bitmap형태로 읽어온다.
    				image_bitmap = Images.Media.getBitmap(getContentResolver(),intent.getData());
    				image_bitmap = Bitmap.createScaledBitmap(image_bitmap, giftWidth, giftHeight, false);
    				giftImg.setImageBitmap(image_bitmap);   
    				
    				getdataIntent = intent;
    				
    				UploadPhoto uploadphoto = new UploadPhoto(this);
    				uploadphoto.execute();
    				
//    				//선택한 이미지의 uri를 읽어온다.   
//    				Uri selPhotoUri = intent.getData();
//    			
//    				//업로드할 서버의 url 주소
//    			    String urlString = "http://210.115.58.140/test7.php";
//    			    //절대경로를 획득한다!!! 중요~
//    			    Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null,null,null,null);
//    			    c.moveToNext();
//    			    //업로드할 파일의 절대경로 얻어오기("_data") 로 해도 된다.
//    			    String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
//    			    Log.e("###파일의 절대 경로###", absolutePath);
//    			    
//    			   //파일 업로드 시작!
//    			   HttpFileUpload(urlString ,"", absolutePath);
				 }
				 
			}catch(FileNotFoundException e) 
			{
				Toast.makeText(getApplicationContext(),"FileNotFoundException" , Toast.LENGTH_SHORT).show();
			    e.printStackTrace();
			    
			}catch(IOException e) 
			{
				Toast.makeText(getApplicationContext(),"IOException" , Toast.LENGTH_SHORT).show();
			    e.printStackTrace();
			    
			}catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),"선물을 선택하시지 않으셨습니다." , Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		
		  
	}
	


	 
	 
	 
	private Boolean HttpFileUpload(String urlString, String params, String fileName) 
	{
		try {
			
			
			mFileInputStream = new FileInputStream(fileName);			
			connectUrl = new URL(urlString);
			Log.d("Test", "mFileInputStream  is " + mFileInputStream);
			
			// open connection 
			HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();			
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			

			
			// write data
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());		
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName+"\"" + lineEnd);
			dos.writeBytes(lineEnd);
			

			
			int bytesAvailable = mFileInputStream.available();
			int maxBufferSize = 1024;

			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			
			byte[] buffer = new byte[bufferSize];
			int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			
			Log.d("Test", "image byte is " + bytesRead);
			
			// read image
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = mFileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
			}	
			
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			// close streams
			Log.e("Test" , "File is written");
			mFileInputStream.close();
			dos.flush(); // finish upload...			
			
			// get response
			int ch;
			InputStream is = conn.getInputStream();
			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 )
			{
				b.append( (char)ch );
			}
			String s=b.toString().trim(); 
			String img_url = "http://14.63.225.38" +s;

			//Log.e("Test", "result = " + img_url);
			
			Intent get_intent01 =getIntent();
//	    	String phone_num = get_intent01.getExtras().get("phone_num").toString().substring(1);
	    	String phone_num = get_intent01.getExtras().get("phone_num").toString();
			
			X_BravoWebserver server = new X_BravoWebserver(this);
			String result = server.ImgUpdateOnServer(phone_num,img_url);
			
//	    	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
			dos.close();
			complete_Flag = true;
			
			return true;
			
		} catch (Exception e) {
			Log.d("Test", "exception " + e.getMessage());
			return false;
			// TODO: handle exception
		}		
	}
	
	private class UploadPhoto extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		private ProgressDialog progressDialog = null;

		public UploadPhoto(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(context);
			progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("Image Uploading. Please wait...");
			progressDialog.show();
		}
		
		// 고고고

		@Override
		protected Boolean doInBackground(Void... unused) {
			
			//선택한 이미지의 uri를 읽어온다.   
			Uri selPhotoUri = getdataIntent.getData();
		
			//업로드할 서버의 url 주소
		    String urlString = "http://14.63.225.38/test7.php";
		    //절대경로를 획득한다!!! 중요~
		    Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null,null,null,null);
		    c.moveToNext();
		    //업로드할 파일의 절대경로 얻어오기("_data") 로 해도 된다.
		    String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
		    Log.e("###파일의 절대 경로###", absolutePath);
		    
		   //파일 업로드 시작!
			return HttpFileUpload(urlString ,"", absolutePath);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if (result) {
				Toast.makeText(context, "이미지 업로드가 완료됐습니다 .", Toast.LENGTH_SHORT).show();
	        	
			} else {
				Toast.makeText(context, "이미지가 업로드 됐습니다.", Toast.LENGTH_SHORT).show();
			}
		}
	}
}




