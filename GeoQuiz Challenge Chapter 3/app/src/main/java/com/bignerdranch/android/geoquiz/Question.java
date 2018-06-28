package com.bignerdranch.android.geoquiz;

/**
 * Created by Utteya on 3/5/2018.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mHasAnswered;

    public Question(int textResId,boolean answerTrue, boolean hasAnswered){
        mTextResId=textResId;
        mAnswerTrue=answerTrue;
        mHasAnswered=hasAnswered;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isHasAnswered() {
        return mHasAnswered;
    }

    public void setHasAnswered(boolean hasAnswered) {
        mHasAnswered = hasAnswered;
    }
}
