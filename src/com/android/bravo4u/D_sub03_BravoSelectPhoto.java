package com.android.bravo4u;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class D_sub03_BravoSelectPhoto extends Activity implements View.OnClickListener
{
	ImageView giftImg;
	Bitmap image_bitmap;
	Button changePhotoBtn;
	
    int REQ_CODE_SELECT_IMAGE= 1;
    
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
		  Intent intent=new Intent(Intent.ACTION_PICK);
		  intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		  intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	      startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(requestCode ==REQ_CODE_SELECT_IMAGE)
    	{
    		
    		if(resultCode == Activity.RESULT_OK)
    		{
    			try
    			{
    				image_bitmap = Images.Media.getBitmap(getContentResolver(),data.getData());
    				image_bitmap = Bitmap.createScaledBitmap(image_bitmap, giftWidth, giftHeight, false);
    				giftImg.setImageBitmap(image_bitmap);
    				
    			}catch(FileNotFoundException e)
    			{
    				e.printStackTrace();
    			}catch(IOException e)
    			{
    				e.printStackTrace();
    			}
    		}
    		
    	}
    }
}
