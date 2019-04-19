package com.kagera.knowledgepond;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kagera.geoquiz.R;

public class Login extends AppCompatActivity
{
    TextView mUsername;
    TextView mPassword;
    Button mLogin;
    TextView mRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = new DatabaseHelper(this);
        mUsername = (TextView) findViewById(R.id.txt_username);
        mPassword = (TextView) findViewById(R.id.txt_password);
        mLogin = (Button) findViewById(R.id.btn_login);
        mRegister = (TextView) findViewById(R.id.txt_register);

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(Login.this,registerActivity.class);
                startActivity(registerIntent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String user = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                Boolean res = db.checkUserAndPassword(user,password);
                if (res == true)
                {
                    Toast.makeText(Login.this, "Successfully Logged in!", Toast.LENGTH_SHORT).show();
                    Intent knowledgePond = new Intent(Login.this,simpleRules.class);
                    startActivity(knowledgePond);
                }
                else
                {
                    Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
