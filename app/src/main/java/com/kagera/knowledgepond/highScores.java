package com.kagera.knowledgepond;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kagera.geoquiz.R;


//I HAVE INHERITED FROM MAINACTIVITY BECAUSE I NEED IT TO CALCULATE SCORES - hope it works
public class highScores extends AppCompatActivity
{
    private TextView mLastScore;
    private TextView mFirstScore;
    private TextView mSecondScore;
    private TextView mThirdScore;
    private ImageButton btnShare;
    private String mShareText;
    private Button mRestart;
    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        mLastScore = findViewById(R.id.last_score);
        mFirstScore = findViewById(R.id.first_score);
        mSecondScore = findViewById(R.id.second_score);
        mThirdScore = findViewById(R.id.third_score);

        final int mlastScore;
        int best1;
        int best2;
        int best3;

        //Load Old_Scores
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        mlastScore = preferences.getInt("lastScore", 0);
        best1 = preferences.getInt("best1", 0);
        best2 = preferences.getInt("best2", 0);
        best3 = preferences.getInt("best3", 0);

        //Compare if there's a highScore
        if (mlastScore > best3)
        {
            best3 = mlastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.apply();
        }
        if (mlastScore > best2)
        {
            int temp = best2;
            best2 = mlastScore;
            best3 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3", best3);
            editor.putInt("best2", best2);
            editor.apply();
        }
        if (mlastScore > best1)
        {
            int temp = best1;
            best1 = mlastScore;
            best2 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best2", best2);
            editor.putInt("best1", best1);
            editor.apply();
        }

        //Display the scores on Each TextView
        mLastScore.setText("LAST SCORE: " + mlastScore + "%");
        mFirstScore.setText("FIRST: " + best1 + "%");
        mSecondScore.setText("SECOND: " + best2 + "%");
        mThirdScore.setText("THIRD: " + best3 + "%");


        //share last score to social media
        btnShare = findViewById(R.id.btn_share);

        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mShareText = "I got " + mlastScore + "%. " + "Play Knowledgepond now and see if you can outperform me! Download Knowledgepond here:";
                Intent mShareProgress = new Intent(Intent.ACTION_SEND);
                mShareProgress.setType("text/plain");
                mShareProgress.putExtra(Intent.EXTRA_SUBJECT, "Knowledgepond");
                mShareProgress.putExtra(Intent.EXTRA_TEXT, mShareText);
                //CHANGE THIS - mShareProgress.putExtra(Intent.EXTRA_COMPONENT_NAME, Login.class);
                startActivity(Intent.createChooser(mShareProgress, "Share Knowledgepond via"));
            }
        });

        //restart questions
        mRestart = findViewById(R.id.restart);
        mRestart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent openSimpleRules = new Intent(highScores.this, simpleRules.class);
                startActivity(openSimpleRules);

            }
        });

        //Logout
        mLogout = findViewById(R.id.btn_logout);
        mLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                //Intent restartIntent = new Intent(MainActivity.class);
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
            }
        });

    }



    @Override
    public void onBackPressed()
    {
        Intent goBack = new Intent(getApplicationContext(), com.kagera.knowledgepond.MainActivity.class);
        startActivity(goBack);
        finish();
    }
}
