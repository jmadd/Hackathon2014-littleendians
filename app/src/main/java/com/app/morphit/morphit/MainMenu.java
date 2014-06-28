package com.app.morphit.morphit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.morphit.morphit.morph.DoodleActivity;
import com.app.morphit.morphit.morph.DrawingBoard;
import com.app.morphit.morphit.morph.MorphActivity;
import com.app.morphit.morphit.morph.SubdividePoints;


public class MainMenu extends Activity {

    private ImageButton eraseBtn;
    private DrawingBoard drawingBoard;
    private ImageButton currPaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final Context ctx = this;
       /* Button.OnClickListener handler = new Button.OnClickListener(){
            public void onClick(View v){
                switch(v.getId()){

                    case R.id.buttonMorph:
                        startActivity(new Intent(ctx, MorphActivity.class));
                    case R.id.buttonDoodle:
                        startActivity(new Intent(ctx, DoodleActivity.class));
                }
            }
        };*/




        Button morphButton = (Button) findViewById(R.id.buttonMorph);
        morphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx, MorphActivity.class));
            }
        });
        Button doodleButton = (Button) findViewById(R.id.buttonDoodle);
        doodleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ctx, DoodleActivity.class));
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

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
