package com.android.bravo4u;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
			try{
				//인텐트에 데이터가 담겨 왔다면
				if(!intent.getData().equals(null)){

				    //해당경로의 이미지를 intent에 담긴 이미지 uri를 이용해서 Bitmap형태로 읽어온다.
    				image_bitmap = Images.Media.getBitmap(getContentResolver(),intent.getData());
    				image_bitmap = Bitmap.createScaledBitmap(image_bitmap, giftWidth, giftHeight, false);
    				giftImg.setImageBitmap(image_bitmap);
				   
				}
			}catch(FileNotFoundException e) {
			    e.printStackTrace();
			}catch(IOException e) {
			    e.printStackTrace();
			}
			//선택한 이미지의 uri를 읽어온다.   
			Uri selPhotoUri = intent.getData();
		
			//업로드할 서버의 url 주소
		    String urlString = "http://210.115.58.140/test7.php";
		    //절대경로를 획득한다!!! 중요~
		    Cursor c = getContentResolver().query(Uri.parse(selPhotoUri.toString()), null,null,null,null);
		    c.moveToNext();
		    //업로드할 파일의 절대경로 얻어오기("_data") 로 해도 된다.
		    String absolutePath = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
		    Log.e("###파일의 절대 경로###", absolutePath);
		    
		   //파일 업로드 시작!
		   HttpFileUpload(urlString ,"", absolutePath);
		  
	    }
	 
		private void HttpFileUpload(String urlString, String params, String fileName) 
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
				while( ( ch = is.read() ) != -1 ){
					b.append( (char)ch );
				}
				String s=b.toString().trim(); 
				

				Log.e("Test", "result = " + s);
				Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
				//mEdityEntry.setText(s);
				dos.close();			
				
			} catch (Exception e) {
				Log.d("Test", "exception " + e.getMessage());
				// TODO: handle exception
			}		
		}
}
