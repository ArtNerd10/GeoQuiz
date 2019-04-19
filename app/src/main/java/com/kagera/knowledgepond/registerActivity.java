package com.kagera.knowledgepond;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kagera.geoquiz.R;

public class registerActivity extends AppCompatActivity
{
    DatabaseHelper db;
    TextView mUsername;
    TextView mPassword;
    TextView mConfirmPassword;
    Button mRegister;
    TextView mLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        mUsername = (TextView) findViewById(R.id.txt_username);
        mPassword = (TextView) findViewById(R.id.txt_password);
        mConfirmPassword = (TextView) findViewById(R.id.txt_confirmPassword);
        mRegister = (Button) findViewById(R.id.btn_register);
        mLogin = (TextView) findViewById(R.id.txt_login);

        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent LoginIntent = new Intent(registerActivity.this,Login.class);
                startActivity(LoginIntent);
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String user = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();

                if (password.equals(confirmPassword))
                {
                    long val = db.addUser(user,password);
                    if (val > 0)
                    {
                        Toast.makeText(registerActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent moveToLogin = new Intent(registerActivity.this, Login.class);
                        startActivity(moveToLogin);
                    }
                    else
                    {
                        Toast.makeText(registerActivity.this, "Registration Error!", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(registerActivity.this, "Passwords do not match. Please retype the passwords!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
