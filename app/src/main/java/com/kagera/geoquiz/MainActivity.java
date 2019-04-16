package com.kagera.geoquiz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity
{
    private int mCurrentIndex = 0;
    private int myScore = 0;
    //saving data after autorotation
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    //end of saving data after autorotation
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button btnRestart;
    //add new feature - progress bar
    private ProgressBar mProgressBar;
    private TextView mProgressBarTextView;

    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

            private Question[] mQuestionBank = new Question[]
            {
                new Question(R.string.question_kenya, true),
                new Question(R.string.question_oceans,true),
                new Question(R.string.question_mideast,false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia,true)
            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "***ON CREATE CALLED***");


        //questionIsOutOf();

        //The progressBar

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBarTextView = (TextView) findViewById(R.id.loadingCompleteTextView);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (mProgressStatus < 100)
                {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(200);
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (mCurrentIndex != 0)
                        {
                            questionIsOutOf();
                        }
                        mProgressBarTextView.setVisibility(View.VISIBLE);
                        calculateScore();
                        mTrueButton.setEnabled(false);
                        mFalseButton.setEnabled(false);
                        mNextButton.setEnabled(false);
                        mPreviousButton.setEnabled(false);
                    }
                });
            }
        }).start();
        //End ProgressBar

        //Setting a question on the textview
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //final ViewGroup transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        //final TextView mQuestionTextView = (TextView) transitionsContainer.findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        //once autorotate happens, save the state of app
        if(savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        //end

        //Listener
        //TRUE BUTTON
        mTrueButton = (Button) findViewById(R.id.true_button);//Getting references to widgets
        //Setting Listeners
        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Anonymous Inner Classes
                /*Toast top_Toast = Toast.makeText(MainActivity.this,"correct_toast", Toast.LENGTH_SHORT);
                top_Toast.setGravity(Gravity.CENTER,0,0);*/
                disableButton();
                checkAnswer(true);
            }
        });

        //FALSE BUTTON
        mFalseButton = (Button) findViewById((R.id.false_button));//Getting references to widgets
        //Setting Listeners
        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Anonymous Inner Class
                /*Toast top_Toast = Toast.makeText(MainActivity.this, "incorrect_toast", Toast.LENGTH_SHORT);
                top_Toast.setGravity(Gravity.CENTER,0,0);*/
                disableButton();
                checkAnswer(false);
            }

        });


        //NEXT BUTTON
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        //
        //final ImageButton mNextButton = (ImageButton) transitionsContainer.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {

            //boolean visible;

            @Override
            public void onClick(View v)
            {
                //TransitionManager.beginDelayedTransition(transitionsContainer);
                //visible = true;
                //mQuestionTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
                //Calculate and end quiz
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length; //Explain this? INCREMENT & CHECK IF VALUE IS GREATER THAN LENGTH OF ARRAY. DIVIDE BOTH A[3] BY Y = ANS IS THE REMAINDER
                //check if i'm in the last question
                if (mCurrentIndex != 0)
                {
                    questionIsOutOf();
                    enableButton();
                    updateQuestion();
                }
                else
                {
                    calculateScore();
                    mNextButton.setEnabled(false);
                    mPreviousButton.setEnabled(false);
                }
            }
        });
        updateQuestion();

        //PREVIOUS BUTTON with some animation
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        //ImageButton mPreviousButton = (ImageButton) transitionsContainer.findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                //Stop Going back when index of Array is 0
                if(mCurrentIndex == 0)
                {
                    Toast.makeText(getApplicationContext(),"End of Previous Question",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    enableButton();
                    updateQuestion();
                }
            }
        });
        updateQuestion();


        //RESTART BUTTON--Understand how it works later-Too sleepy
        btnRestart = findViewById(R.id.restart);
        btnRestart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
            }
        });
    }



    //Checking Answer If it is correct - Referencing Question Class
    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if(userPressedTrue ==  answerIsTrue)
        {
            messageResId = R.string.correct_toast;
            myScore++;//increment myScore by 1
        }
        else
        {
            messageResId = R.string.incorrect_toast;
        }
        makeText(this, messageResId, LENGTH_SHORT).show();
    }

    //Do calculation of Questions
    private void calculateScore()
    {

        int correctAnswers = 100 * myScore / mQuestionBank.length;
        Toast.makeText(getApplicationContext(),"END OF QUIZ!", Toast.LENGTH_LONG).show();// + "Score: " + correctAnswers + " %", Toast.LENGTH_LONG).show();
        //mProgressBarTextView.setText(correctAnswers);
        mProgressBarTextView.setText("Your Score: " + correctAnswers + " %");
/*
        //Just display text in the correct place Please

        if (mCurrentIndex == mQuestionBank.length - 1)
        {
            Toast finishedQuiz = Toast.makeText(getApplicationContext(),"END OF QUIZ :)",Toast.LENGTH_LONG);
            finishedQuiz.setGravity(Gravity.TOP | Gravity.TOP, 8,40);
            finishedQuiz.show();
        }
*/
    }
    //Challenge: Prevent repeating Answers?
    private void enableButton()
    {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void disableButton()
    {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }


    //ADD NEW FEATURE OF ANIMATION - That code should've been here

    private void updateQuestion()  //Set Button as Private so that it can't be modified
    {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

    }

    //Setting No of Questions
    private void questionIsOutOf()
    {
       if (mCurrentIndex != 0)
       {
           mProgressBarTextView.setText("Question: " + mCurrentIndex + " of " + mQuestionBank.length);
       }
    }
    //End


    //LEARN HOW ACTIVITY LIFECYCLE WORKS
    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(TAG,"***ON START CALLED***");
    }

    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "***ON RESUME CALLED***");
    }

    public void onPause()
    {
        super.onPause();
        Log.d(TAG,"***ON PAUSE CALLED***");
    }

    public  void onStop()
    {
        super.onStop();
        Log.d(TAG,"***ON STOP CALLED***");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG,"***ON DESTROY CALLED***");
    }
}