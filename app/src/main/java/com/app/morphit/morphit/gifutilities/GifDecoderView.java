package com.app.morphit.morphit.gifutilities;

/**
 * Created by leitia on 6/27/2014.
 */
import android.widget.ImageView;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.content.Context;
import android.os.Handler;
import java.lang.Thread;
import android.os.Handler;
import android.util.Log;

public class GifDecoderView extends ImageView {

    private boolean mIsPlayingGif = false;
    private GifDecoder mGifDecoder;
    private Bitmap mTmpBitmap;
    final Handler mHandler = new Handler();
    private String TAG = "GifDecoderView";
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                GifDecoderView.this.setImageBitmap(mTmpBitmap);
            }
        }
    };


    private void playGif(InputStream stream) {
        mGifDecoder = new GifDecoder();
        mGifDecoder.read(stream);
        mIsPlayingGif = true;

        new Thread(new Runnable() {
            public void run() {
                final int n = mGifDecoder.getFrameCount();
                final int ntimes = mGifDecoder.getLoopCount();
                //final int ntimes = 0;
                Log.i(TAG, "n:"+Integer.toString(n));
                Log.i(TAG, "ntimes:"+Integer.toString(ntimes));
                int repetitionCounter = 0;
                do {
                    for (int i = 0; i < n; i++) {
                        mTmpBitmap = mGifDecoder.getFrame(i);
                        final int t = mGifDecoder.getDelay(i);
                        mHandler.post(mUpdateResults);
                        //mTmpBitmap.recycle();
                        try {
                            Thread.sleep(t);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                       // mTmpBitmap.recycle();
                    }
                    if(ntimes != 0) {
                        repetitionCounter ++;
                    }
                }while (mIsPlayingGif && (repetitionCounter < ntimes));
            }
        }).start();
    }




    public GifDecoderView(Context context, InputStream stream) {
        super(context);
        playGif(stream);
    }


}
