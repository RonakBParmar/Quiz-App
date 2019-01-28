package com.webstarts.sahilsalma.quizApp;

public class Questions{
    // These are the placeholders for the question resource id and the correct answer
    private String mQuestionID;
    private String mAnswer;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private String mOption4;

    // This is the constructor that will be called when a new quiz question is created.
    public Questions(String questionResourceID, String option1, String option2, String option3,
                     String option4, String answer) {
        mQuestionID = questionResourceID;
        mOption1 = option1;
        mOption2 = option2;
        mOption3 = option3;
        mOption4 = option4;
        mAnswer = answer;
    }

    // This method gives us access to info stored in the (private) question id.
    public String getQuestionID() {
        return mQuestionID;
    }

    // This method gives us access to info stored in the (private) mAnswer.
    public String isAnswer() {
        return mAnswer;
    }

    public String getOption1() { return mOption1;}

    public String getOption2() { return mOption2; }

    public String getOption3() { return mOption3; }

    public String getOption4() { return mOption4; }

}

