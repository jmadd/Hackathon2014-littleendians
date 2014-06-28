package com.app.morphit.morphit.morph;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.morphit.morphit.R;

/**
 * Created by madjared on 6/28/2014.
 */
public class MorphInstructions extends Activity {

    private Button okbtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_morphit_instructions);
        okbtn = (Button)findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MorphInstructions.this, MorphActivity.class));
            }
        });
    }
}
