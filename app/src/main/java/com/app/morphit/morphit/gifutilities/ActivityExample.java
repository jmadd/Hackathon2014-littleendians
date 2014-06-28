package com.app.morphit.morphit.gifutilities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.ArrayList;
import android.os.Environment;
import java.io.File;
import android.content.ContextWrapper;
import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.media.MediaScannerConnection;

import com.app.morphit.morphit.R;

public class ActivityExample extends Activity {

    private String TAG = "GifPlayer";
    private int duration = 300;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ArrayList<Bitmap> bms = new ArrayList<Bitmap>();
//        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.flower1);
//        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.flower2);
//        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.flower3);
//        bms.add(bm1);
//        bms.add(bm2);
//        bms.add(bm3);


        // SAVE IT
        //ContextWrapper cw = new ContextWrapper(this);
        //File directory = cw.getDir("media", Context.MODE_PRIVATE);
        Log.i(TAG, Environment.getExternalStorageState());
        File saveDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Morphit/");
        if(!saveDir.exists())
            saveDir.mkdir();
        Log.i(TAG, Boolean.toString(saveDir.exists()));

        String gifName = "/flower_test.gif";
        String gifPath =saveDir.getAbsolutePath()+gifName;
        Log.i(TAG, gifPath);

       // generateGif(bms, gifPath);

        // LOAD IT
        try{
            InputStream is = new FileInputStream(gifPath);
            GifDecoderView view = new GifDecoderView(this, is);
            setContentView(view);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void generateGif(ArrayList<Bitmap> bms, String gitPath){
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(1);
            e.start(gitPath);
            for(Bitmap bm: bms){
                e.setDelay(duration);
                e.addFrame(bm);
            }
            e.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Generate Bitmap");
    }





//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//       // getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
