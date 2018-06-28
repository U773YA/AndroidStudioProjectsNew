package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG="QuizActivity";

    private static final String KEY_INDEX="index";
    private static final String KEY_ANSWERED="answered";
    private static final String KEY_RIGHT="right";
    private static final String KEY_ARRAY="array";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank=new Question[]{
            new Question(R.string.question_australia,true,false),
            new Question(R.string.question_oceans,true,false),
            new Question(R.string.question_mideast,false,false),
            new Question(R.string.question_africa,false,false),
            new Question(R.string.question_americas,true,false),
            new Question(R.string.question_asia,true,false),
    };

    private int mCurrentIndex=0;
    private int mQuestionsAnswered=0;
    private int mAnswersRight=0;
    private boolean hasAnswered[]=new boolean[mQuestionBank.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState!=null){
            mCurrentIndex=savedInstanceState.getInt(KEY_INDEX,0);
            mQuestionsAnswered=savedInstanceState.getInt(KEY_ANSWERED,0);
            mAnswersRight=savedInstanceState.getInt(KEY_RIGHT,0);
            hasAnswered=savedInstanceState.getBooleanArray(KEY_ARRAY);
            for(int i=0;i<mQuestionBank.length;i++)
                mQuestionBank[i].setHasAnswered(hasAnswered[i]);
        }

        mQuestionTextView=(TextView)findViewById(R.id.question_text_view);

        mTrueButton=(Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionsAnswered++;
                checkAnswer(true);
            }
        });

        mFalseButton=(Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionsAnswered++;
                checkAnswer(false);
            }
        });

        mNextButton=(Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex=(mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();

            }
        });

        updateQuestion();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putInt(KEY_ANSWERED,mQuestionsAnswered);
        savedInstanceState.putInt(KEY_RIGHT,mAnswersRight);
        for(int i=0;i<mQuestionBank.length;i++)
            hasAnswered[i]=mQuestionBank[i].isHasAnswered();
        savedInstanceState.putBooleanArray(KEY_ARRAY,hasAnswered);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() called");
    }

    private void updateQuestion(){
        int question=mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        updateButtons();
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue=mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId=0;

        if(userPressedTrue==answerIsTrue) {
            messageResId = R.string.correct_toast;
            mAnswersRight++;
        }else{
            messageResId=R.string.incorrect_toast;
        }

        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
        mQuestionBank[mCurrentIndex].setHasAnswered(true);
        updateButtons();
    }

    private void updateButtons(){
            mTrueButton.setEnabled(!(mQuestionBank[mCurrentIndex].isHasAnswered()));
            mFalseButton.setEnabled(!(mQuestionBank[mCurrentIndex].isHasAnswered()));
            if(mQuestionsAnswered==mQuestionBank.length) {
                String s1="Score : "+((float)mAnswersRight/mQuestionsAnswered*100)+"%";
                Toast.makeText(QuizActivity.this, s1, Toast.LENGTH_SHORT).show();

            }
    }
}
