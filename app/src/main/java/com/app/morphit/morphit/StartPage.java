package com.app.morphit.morphit;

/**
 * Created by bulbul on 6/28/2014.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.app.morphit.morphit.gifutilities.GifDecoderView;

import java.io.InputStream;



public class StartPage extends Activity {
    final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        InputStream is = getResources().openRawResource(R.drawable.startpage);
        GifDecoderView view = new GifDecoderView(this, is);
        setContentView(view);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(StartPage.this, MainMenu.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter_left, R.anim.exit_left);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
