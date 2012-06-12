package com.android.bravo4u;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class X_BravoMyView extends View 
{
	 int puzleNum=9;
		public X_BravoMyView(Context context)
		{    
			super(context);
		
		}
		
		public X_BravoMyView(Context context, AttributeSet attrs)
		{
			super(context,attrs);
		} 
		
		public X_BravoMyView(Context context, AttributeSet attrs, int defStyle)
		{
			super(context, attrs, defStyle);
		}
		
		public void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			 
			Paint paint = new Paint();
	        paint.setFilterBitmap(true);
			
			Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.bbororo);
			Bitmap bbororo =Bitmap.createScaledBitmap(bitmap1, 300, 300, false);
			//paint.setAlpha(80);
			canvas.drawBitmap(bbororo, 0,0, paint);
			
			
			Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.nemo);
			Bitmap nemo1 =Bitmap.createScaledBitmap(bitmap2, 100, 100, false);
			
			paint.setAlpha(0);
			if(puzleNum>=9)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 0,0, paint);
			
			paint.setAlpha(0);
			if(puzleNum>=8)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 100,0, paint);
			
			paint.setAlpha(0);
			if(puzleNum>=7)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 200,0, paint);
			
			paint.setAlpha(0);
			if(puzleNum>=6)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 0,100, paint);
			
			paint.setAlpha(0);
			if(puzleNum>=5)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 100,100, paint);
			
			paint.setAlpha(0);
			if(puzleNum>=4)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 200,100, paint);

			paint.setAlpha(0);
			if(puzleNum>=3)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 0,200, paint);

			paint.setAlpha(0);
			if(puzleNum>=2)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 100,200, paint);
			
			paint.setAlpha(0);
			if(puzleNum>=1)paint.setAlpha(80);
			canvas.drawBitmap(nemo1, 200,200, paint);
			

			bitmap1.recycle();
			bitmap2.recycle();
		}
}
