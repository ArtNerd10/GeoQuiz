package com.kagera.knowledgepond;

public class Question
{
    private int TextResId;
    private boolean AnswerTrue;

    public boolean isAnswerTrue()
    {
        return AnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue)
    {
        AnswerTrue = answerTrue;
    }

    public int getTextResId()
    {
        return TextResId;
    }

    public void setTextResId(int textResId)
    {
        TextResId = textResId;
    }

    public Question(int ConstructorTextResId, boolean ConstructorAnswerTrue)//Constructor
    {
        TextResId = ConstructorTextResId;
        AnswerTrue = ConstructorAnswerTrue;

        //Generating Getters and Setters
    }
}
