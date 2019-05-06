package com.kagera.knowledgepond;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;


import com.kagera.geoquiz.R;

import java.util.Arrays;
import java.util.Collections;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity
{
    private int mCurrentIndex = 0;
    private int numberOfCorrectAnswers = 0;
    private int numberOfIncorrectAnswers = 0;
    private int mySCore;
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
    private int questionNumber = 1;
    private int mProgressStatus = 0;
    private ImageButton btnShare;
    private Button mshowScore;
    public String mShareText;
    private LinearLayout show_Score_LinearLayout;
    //int mcorrectAnswers = 0;

    //int getmShareText2 = calculateScore();





    private Handler mHandler = new Handler();



            private Question[] mQuestionBank = new Question[]
            {
                new Question(R.string.question_kenya, true),
                new Question(R.string.question_oceans,true),
                new Question(R.string.question_mideast,false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia,true),
                new Question(R.string.question_snowwhite, false),
                    new Question(R.string.question_random1, true),
                    new Question(R.string.question_random2, true),
                    new Question(R.string.question_random3, false),
                    new Question(R.string.question_random4, false),
            };




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "***ON CREATE CALLED***");

        //shuffle questions
        shuffleQuestions();


        //MediaPlayer
        final MediaPlayer mTimerSound = MediaPlayer.create(getApplicationContext(),R.raw.game_show);

        mTimerSound.start();
        //End of media player declaration




        //questionIsOutOf();
        //mProgressBarTextView.setText("Question: " + questionNumber + " of " + mQuestionBank.length);
        //The progressBar

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBarTextView = (TextView) findViewById(R.id.loadingCompleteTextView);
        mProgressBarTextView.setText("Question: " + questionNumber + " of " + mQuestionBank.length);
        //Test

        //
        //int correctAnswers;
        //correctAnswers = 100 * myScore / mQuestionBank.length;



        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (mProgressStatus < 100)
                {
                    mProgressStatus++;
                    SystemClock.sleep(300);
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mProgressBar.setProgress(mProgressStatus);
                            //questionIsOutOf();
                        }
                    });
                }
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        mProgressBarTextView.setVisibility(View.VISIBLE);
                        show_Score_LinearLayout.setVisibility(View.VISIBLE);
                        mshowScore.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "End of Round 1. Click on 'SUMMARY'", Toast.LENGTH_LONG).show();
                        mTimerSound.stop();
                        mTrueButton.setEnabled(false);
                        mFalseButton.setEnabled(false);

                        //mNextButton.setEnabled(false);
                        //mPreviousButton.setEnabled(false);
                    }
                });
            }
        }).start();
        //End ProgressBar



        //calculating correct answers


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

                checkAnswer(true);



                //Test If True Will go To NEXT QUESTION
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length; //Explain this? INCREMENT & CHECK IF VALUE IS GREATER THAN LENGTH OF ARRAY. DIVIDE BOTH A[3] BY Y = ANS IS THE REMAINDER

                //check if i'm in the last question
                if (mCurrentIndex != 0)
                {
                    //questionIsOutOf();
                    updateQuestion();
                    questionIsOutOf();
                    //mshowScore.setEnabled(false);
                }
                else
                {

                    //Toast.makeText(getApplicationContext(),"CLICK ON SUMMARY TO SEE HOW YOU PERFORMED", Toast.LENGTH_LONG).show();
                    //change this - TEST MAJOR
                    //mProgressBarTextView.setText(calculateScore(mcorrectAnswers));
                    //mNextButton.setEnabled(false);
                    //mPreviousButton.setEnabled(false);
                    notifyUserQuizIsFinished();
                    mTimerSound.stop();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "End of Round 1. Click on 'SUMMARY'", Toast.LENGTH_LONG).show();
                    //mshowScore.setEnabled(true);
                    mshowScore.setVisibility(View.VISIBLE);
                    show_Score_LinearLayout.setVisibility(View.VISIBLE);

                    //calculateScore(mySCore);
                }
                //END
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
                checkAnswer(false);

                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length; //Explain this? INCREMENT & CHECK IF VALUE IS GREATER THAN LENGTH OF ARRAY. DIVIDE BOTH A[3] BY Y = ANS IS THE REMAINDER
                //correctAnswers = 100 * myScore / mQuestionBank.length;
                //check if i'm in the last question
                if (mCurrentIndex != 0)
                {
                    //questionIsOutOf();
                    updateQuestion();
                    questionIsOutOf();
                    //mshowScore.setEnabled(false);
                }
                else
                {

                    //mProgressBarTextView.setText(calculateScore(mcorrectAnswers));
                    //Toast.makeText(getApplicationContext(),"CLICK ON SUMMARY TO SEE HOW YOU PERFORMED", Toast.LENGTH_LONG).show();
                    //mProgressBarTextView.setText(calculateScore(mcorrectAnswers));
                    //mNextButton.setEnabled(false);
                    //mPreviousButton.setEnabled(false);
                    //calculateScore(mcorrectAnswers);
                    notifyUserQuizIsFinished();
                    mTimerSound.stop();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "End of Round 1. Click on 'SUMMARY'", Toast.LENGTH_LONG).show();
                    //mshowScore.setEnabled(true);
                    //calculateScore(mySCore);
                    mshowScore.setVisibility(View.VISIBLE);
                    show_Score_LinearLayout.setVisibility(View.VISIBLE);
                }
            }

        });

        //Social Media Share

        /*
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mShareText = "I got " + calculateScore(mySCore) + "%. " + "Play Knowledgepond now and see if you can outperform me! Download Knowledgepond here:";
                Intent mShareProgress = new Intent(Intent.ACTION_SEND);
                mShareProgress.setType("text/plain");
                mShareProgress.putExtra(Intent.EXTRA_SUBJECT, "Knowledgepond");
                mShareProgress.putExtra(Intent.EXTRA_TEXT, mShareText);
                //CHANGE THIS - mShareProgress.putExtra(Intent.EXTRA_COMPONENT_NAME, Login.class);
                startActivity(Intent.createChooser(mShareProgress, "Share Knowledgepond via"));
            }
        });
        */
        //End


        //NEXT BUTTON
        //mNextButton = (ImageButton) findViewById(R.id.next_button);
        //
        //final ImageButton mNextButton = (ImageButton) transitionsContainer.findViewById(R.id.next_button);

        //mNextButton.setOnClickListener(new View.OnClickListener()
        //{

            //boolean visible;

            //@Override
           // public void onClick(View v)
            //{
                //TransitionManager.beginDelayedTransition(transitionsContainer);
                //visible = true;
                //mQuestionTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
                //Calculate and end quiz
                //CHANGE -- mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length; //Explain this? INCREMENT & CHECK IF VALUE IS GREATER THAN LENGTH OF ARRAY. DIVIDE BOTH A[3] BY Y = ANS IS THE REMAINDER
                //check if i'm in the last question
                /*CHANGEif (mCurrentIndex != 0)
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
                }END CHANGE*/
           // }
      //  });
       // updateQuestion();

        //PREVIOUS BUTTON with some animation
       // mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        //ImageButton mPreviousButton = (ImageButton) transitionsContainer.findViewById(R.id.previous_button);
        /*mPreviousButton.setOnClickListener(new View.OnClickListener()
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

        */


        //SHOW SCORE-SUMMARY
        mshowScore = findViewById(R.id.btn_showScores);
        show_Score_LinearLayout = findViewById(R.id.show_score_layout);
        show_Score_LinearLayout.setVisibility(View.INVISIBLE);
        mshowScore.setVisibility(View.INVISIBLE);
        mshowScore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mTimerSound.stop();
                //Intent highScore = new Intent(MainActivity.this, highScores.class);
                //startActivity(highScore);

                    SharedPreferences preferences = getSharedPreferences("PREFS",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("lastScore", calculateScore(mySCore));
                    editor.apply();

                    Intent highScores = new Intent(MainActivity.this, com.kagera.knowledgepond.highScores.class);
                    startActivity(highScores);
                    finish();
            }
        });
        //END

        //RESTART BUTTON--Understand how it works later-Too sleepy
        /*
        btnRestart = findViewById(R.id.restart);
        btnRestart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent restartIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                //Intent restartIntent = new Intent(MainActivity.class);
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
                mTimerSound.stop();
            }
        });
        */
    }


    //SHUFFLE QUESTIONS
    public void shuffleQuestions()
    {
        Collections.shuffle(Arrays.asList(mQuestionBank));
    }

    //Checking Answer If it is correct - Referencing Question Class
    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        MediaPlayer mCorrectSound = MediaPlayer.create(getApplicationContext(),R.raw.gameshow_correct);
        MediaPlayer mIncorrectSound = MediaPlayer.create(getApplicationContext(),R.raw.incorrect);
        //int correctAnswers = 100 * myScore / mQuestionBank.length;


        int messageResId = 0;

        if(userPressedTrue ==  answerIsTrue)
        {
            messageResId = R.string.correct_toast;
            numberOfCorrectAnswers++;//increment myScore by 1
            mCorrectSound.start();
        }
        else
        {
            numberOfIncorrectAnswers++;
            messageResId = R.string.incorrect_toast;
            mIncorrectSound.start();
        }
        makeText(this, messageResId, LENGTH_SHORT).show();

        /*if ((numberOfCorrectAnswers + numberOfIncorrectAnswers) == mQuestionBank.length)
        {
            int percentageOfScore;
            percentageOfScore = ((numberOfCorrectAnswers / mQuestionBank.length) * 100);
            Toast.makeText(MainActivity.this, getString(percentageOfScore), Toast.LENGTH_SHORT).show();
        }*/
    }

    public int calculateScore(int percentageOfScore)
    {


            percentageOfScore = 100 * numberOfCorrectAnswers / mQuestionBank.length;
            //String msg = "Your Score = " + percentageOfScore;
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        mySCore = percentageOfScore;
        return mySCore;
    }

    //Do calculation of Questions
    //public int calculateScore(int correctAnswers)
    //{

      //correctAnswers = 100 * myScore / mQuestionBank.length;
      //Toast.makeText(getApplicationContext(),"END OF QUIZ!", Toast.LENGTH_LONG).show();// + "Score: " + correctAnswers + " %", Toast.LENGTH_LONG).show();
        //mProgressBarTextView.setText(correctAnswers);
       // mProgressBarTextView.setText("Your Score: " + correctAnswers + " %");
/*
        //Just display text in the correct place Please

        if (mCurrentIndex == mQuestionBank.length - 1)
        {
            Toast finishedQuiz = Toast.makeText(getApplicationContext(),"END OF QUIZ :)",Toast.LENGTH_LONG);
            finishedQuiz.setGravity(Gravity.TOP | Gravity.TOP, 8,40);
            finishedQuiz.show();
        }
    mProgressBarTextView.setText(calculateScore(mcorrectAnswers));
*/

   // mcorrectAnswers = correctAnswers;
    //return mcorrectAnswers;
        //mProgressBarTextView.setText(mcorrectAnswers);
   // }
    //Challenge: Prevent repeating Answers?
    private void enableButton()
    {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        //mshowScore.setEnabled(true);
    }

    private void disableButton()
    {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void notifyUserQuizIsFinished()
    {
        if (questionNumber == mQuestionBank.length)
        {
            disableButton();

        }
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
        questionNumber++;
        mProgressBarTextView.setText("Question: " + questionNumber + " of " + mQuestionBank.length);

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