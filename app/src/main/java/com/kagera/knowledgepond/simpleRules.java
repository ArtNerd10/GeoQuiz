package com.kagera.knowledgepond;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kagera.geoquiz.R;

public class simpleRules extends AppCompatActivity
{

    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_rules);

        btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent startApp = new Intent(simpleRules.this,MainActivity.class);
                startActivity(startApp);
            }
        });

    }
}
