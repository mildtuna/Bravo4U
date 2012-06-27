package com.android.bravo4u;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

/**
 * This helper class download images from the Internet and binds those with the provided ImageView.
 *
 * <p>It requires the INTERNET permission, which should be added to your application's manifest
 * file.</p>
 *
 * A local cache of downloaded images is maintained internally to improve performance.
 * 의역 > 다운로드된 이미지들의 로컬캐시는 내부적으로 개선된 퍼포먼스로 유지된다.
 */

public class X_BravoImageDownloader 
{
    private static final String LOG_TAG = "ImageDownloader";

    public enum Mode { NO_ASYNC_TASK, NO_DOWNLOADED_DRAWABLE, CORRECT }
    private Mode mode = Mode.NO_ASYNC_TASK;
    
    /**
     * Download the specified image from the Internet and binds it to the provided ImageView. The
     * binding is immediate if the image is found in the cache and will be done asynchronously(비동기로)
     * otherwise. A null bitmap will be associated(연관있다) to the ImageView if an error occurs.
     *
     * @param url The URL of the image to download.
     * @param imageView The ImageView to bind the downloaded image to.
     */
    
    public Bitmap download(String url, ImageView imageView) 
    {
        resetPurgeTimer();
        
        //해당 url과 일치하는 로컬캐시에있는 비트맵 가져오기  
        Bitmap bitmap = getBitmapFromCache(url);

        if (bitmap == null) //로컬캐시에 비트맵이 없다면 
        {
        	// 인터넷에서 다운로드 받는다.
            Bitmap getbitmap = forceDownload(url, imageView); 
            
            return getbitmap;
            
        } else //로컬캐시에 비트맵이 있다면 
        {
            cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap); 
        }
        
        return bitmap;
    }

    /*
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
       private void forceDownload(String url, ImageView view) {
          forceDownload(url, view, null);
       }
     */

    /**
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
     */
    private Bitmap forceDownload(String url, ImageView imageView) 
    {
        // State sanity(분): url is guaranteed to never be null in DownloadedDrawable and cache keys.
        if (url == null) 
        {
            imageView.setImageDrawable(null);
            return null; 
        }

        
        if (cancelPotentialDownload(url, imageView)) 
        {
            switch (mode) 
            {
                case NO_ASYNC_TASK:
                    Bitmap bitmap = downloadBitmap(url);
                    addBitmapToCache(url, bitmap);
                    if(imageView != null)
                    {
                    	imageView.setImageBitmap(bitmap);
                    }
                    return bitmap;
                    //break;

                case NO_DOWNLOADED_DRAWABLE:
                    imageView.setMinimumHeight(156);
                    BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
                    task.execute(url);
                    Log.d("스위치02","된다");
                    break;

                case CORRECT:
                    task = new BitmapDownloaderTask(imageView);
                    DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
                    imageView.setImageDrawable(downloadedDrawable);
                    imageView.setMinimumHeight(156);
                    Log.d("스위치03","된다");
                    task.execute(url);
                    break;
            }
            
            
       }
        
        return null;
    }

    /**
     * Returns true if the current download has been canceled or if there was no download in
     * progress on this image view.
     * Returns false if the download in progress deals with the same url. The download is not
     * stopped in that case.
     */
    private static boolean cancelPotentialDownload(String url, ImageView imageView) 
    {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) 
        {
            String bitmapUrl = bitmapDownloaderTask.url;
            
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) 
            {
                bitmapDownloaderTask.cancel(true);
                
            } else 
            {
                // The same URL is already being downloaded.
                return false;
            }
        }
        
        return true;
    }

    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) 
    {
        if (imageView != null) 
        {
            Drawable drawable = imageView.getDrawable();
            
            if (drawable instanceof DownloadedDrawable) 
            {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

   public Bitmap downloadBitmap(String url) 
    {
        //final int IO_BUFFER_SIZE = 4 * 1024;
	    final int TARGET_SIZE      = 1280;
        url = url.trim();

 
        // AndroidHttpClient is not allowed to be used from the main thread
        final HttpClient client = (mode == Mode.NO_ASYNC_TASK) ? new DefaultHttpClient() :
        	new DefaultHttpClient();
        final HttpGet getRequest = new HttpGet(url);

        try 
        {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode != HttpStatus.SC_OK) 
            {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            
            if (entity != null) 
            {
                InputStream inputStream = null;
                
                try 
                {
//                    inputStream = entity.getContent();
//                    // return BitmapFactory.decodeStream(inputStream);
//                    // Bug on slow connections, fixed in future release.
//                    return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                    
                	inputStream = entity.getContent();
                	
                	BitmapFactory.Options options = new BitmapFactory.Options();
                	options.inJustDecodeBounds = true;
                	
                	BitmapFactory.decodeStream(inputStream,null,options);
                	
                	options.inSampleSize =4; // 요요요 요부분!!  이미지 줄여주는 부분이다 2의 배수로 해주면 좋다. 
                							//	숫자가 클수록  큰이미지가  더 작게 줄어든다.
                	
                	options.inJustDecodeBounds = false;
                	inputStream.close();
                	
                	//다시 연결해줘야한다. Inputstream을 다시 이용 못해서라는데 왜 다시 이용 못하는지는 모르겠다 
                    HttpClient client_ = new DefaultHttpClient();
                    HttpGet getRequest_ = new HttpGet(url);
                    HttpResponse response_ = client_.execute(getRequest_);
                    HttpEntity entity_ = response_.getEntity();
                    InputStream inputStream_ = entity_.getContent();
                	return BitmapFactory.decodeStream(new FlushedInputStream(inputStream_),null,options);
                	
                } finally 
                {
                    if (inputStream != null) 
                    {
                        inputStream.close();
                    }
                    
                    entity.consumeContent();
                }
            }
            
        } catch (IOException e) 
        {
            getRequest.abort();
            Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
        } catch (IllegalStateException e) 
        {
            getRequest.abort();
            Log.w(LOG_TAG, "Incorrect URL: " + url);
        } catch (Exception e) 
        {
            getRequest.abort();
            Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
        } finally 
        {
            if ((client instanceof DefaultHttpClient)) 
            {
                ((DefaultHttpClient) client).clearRequestInterceptors();
            }
        }
        return null;
    }

    
    //----------------------------------------------------------
    //
    //				FlushedInputStream 클래스 	
    //
    //----------------------------------------------------------
    
    

    
    /*
     * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
     */
// 이 거는 이미지를 다운받을때 너무 오래 걸려서 익셉션이 나지 않도록 해주는 거다, 대충 그렇게 알고있다 ㅋ
    static class FlushedInputStream extends FilterInputStream  
    {
        public FlushedInputStream(InputStream inputStream) 
        {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException 
        {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) 
            {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) 
                {
                    int b = read();
                    
                    if (b < 0) 
                    {
                        break;  // we reached EOF
                        
                    } else 
                    {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
    
    
    //----------------------------------------------------------
    //
    //				BitmapDownloaderTask 클래스 	
    //
    //----------------------------------------------------------    
    
  

    /**
     * The actual AsyncTask that will asynchronously download the image.
     */
    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> 
    {
        private String url;
        private final WeakReference<ImageView> imageViewReference;

        public BitmapDownloaderTask(ImageView imageView) 
        {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(String... params) 
        {
            url = params[0];
            return downloadBitmap(url);
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) 
        {
            if (isCancelled()) 
            {
                bitmap = null;
            }

            addBitmapToCache(url, bitmap);

            if (imageViewReference != null) 
            {
                ImageView imageView = imageViewReference.get();
                BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                // Change bitmap only if this process is still associated with it
                // Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
                if ((this == bitmapDownloaderTask) || (mode != Mode.CORRECT)) 
                {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }//class BitmapDownloaderTask


    //----------------------------------------------------------
    //
    //				DownloadedDrawable 클래스 	
    //
    //----------------------------------------------------------       
    
    
    
    /**
     * A fake Drawable that will be attached to the imageView while the download is in progress.
     *
     * <p>Contains a reference to the actual download task, so that a download task can be stopped
     * if a new binding is required, and makes sure that only the last started download process can
     * bind its result, independently of the download finish order.</p>
     */
    
    static class DownloadedDrawable extends ColorDrawable 
    {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) 
        {
            super(Color.BLACK);
            bitmapDownloaderTaskReference =
                new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() 
        {
            return bitmapDownloaderTaskReference.get();
        }
    }// class downloadeddrawable

    
    
    
    
    
    
    
    
    public void setMode(Mode mode) 
    {
        this.mode = mode;
        clearCache();
    }

    
    /*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
     * Garbage Collector.
     */
    
    private static final int HARD_CACHE_CAPACITY = 10;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

    // Hard cache, with a fixed maximum capacity and a life duration
    private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
        
    	@Override
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) 
        {
            if (size() > HARD_CACHE_CAPACITY) 
            {
                // Entries push-out of hard reference cache are transferred to soft reference cache
                sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
                
            } else
                return false;
        }
    };

    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = 
    		new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

    private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() 
    {
        public void run() 
        {
            clearCache();
        }
    };

    /**
     * Adds this bitmap to the cache.
     * @param bitmap The newly downloaded bitmap.
     */
    private void addBitmapToCache(String url, Bitmap bitmap) 
    {
        if (bitmap != null) 
        {
            synchronized (sHardBitmapCache) 
            {
                sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    /**
     * @param url The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private Bitmap getBitmapFromCache(String url) 
    {
        // First try the hard reference cache
        synchronized (sHardBitmapCache) 
        {
            final Bitmap bitmap = sHardBitmapCache.get(url);
            if (bitmap != null) 
            {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardBitmapCache.remove(url);
                sHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
        
        if (bitmapReference != null) 
        {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) 
            {
                // Bitmap found in soft cache
                return bitmap;
                
            } else 
            {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(url);
            }
        }

        return null;
    }
 
    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public void clearCache() 
    {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    private void resetPurgeTimer() 
    {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }

}
