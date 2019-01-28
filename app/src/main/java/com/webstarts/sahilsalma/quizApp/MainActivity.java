package com.webstarts.sahilsalma.quizApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity{

    // Member variables accessible in all the methods of the MainActivity:
    Button mOption1;        //Connects to button 1 in options
    Button mOption2;        //Connects to button 1 in options
    Button mOption3;        //Connects to button 1 in options
    Button mOption4;        //Connects to button 1 in options
    Button mStart;          //Connects to start button on first screen
    Button mClose;          //Connects to close button on first screen
    Button mBack;           //Connects to back button on review screen
    TextView mQuestionTextView; //Connects to Question TextView
    TextView mCompleted;        //Connects to text view that shows questions completed
    TextView mReview;           //Connects to text view that shows review
    ProgressBar mProgressBar;   //Connects to progress bar on the bottom of screen
    ProgressBar mTimer;         //Connects to progress bar on top of screen
    CountDownTimer mCountDownTimer; //sets up count down timer
    EditText mStudentNumber;    //Connects to student number edit text on first screen
    int mIndex;                 //Index variable for questions
    int mScore;                 //Variable to keep track of score
    int qComp = 0;              //Variable to keep track of questions
    Questions[] mQuestionBank = {}; //setUp empty questionBank
    final int timeDecrease = -1;    //sets timer decrease value

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setQuestionBank();
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("IndexKey");
        } else {
            mIndex = 0;
        }

        mStudentNumber = findViewById(R.id.studentNumber);  //sets mStudentNumber to student number

        //Forms AlertDialog for event in which the user doesn't enter student number
        final AlertDialog.Builder noStudentNumber = new AlertDialog.Builder(this);
        noStudentNumber.setTitle("Invalid Input");
        noStudentNumber.setCancelable(false);
        noStudentNumber.setMessage("Please enter student number");
        noStudentNumber.setPositiveButton("OK", null);

        mStart = findViewById(R.id.start);      //sets mStart to start button
        mClose = findViewById(R.id.close);      //sets mClose to close button

        //Click listener which sets text in edit text to blank
        mStudentNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mStudentNumber.getText().toString().equals(getResources().getString(R.string.StudentNumber)))
                    mStudentNumber.setText("");
            }
        });

        //Click listener for start button, checks if value in student number edit text is not initial
        //value or blank, if condition is true then starts the app else show student numebr alert
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStudentNumber.getText().toString().equals(getResources().getString(R.string.StudentNumber))==false&&
                        mStudentNumber.getText().toString().equals("")==false) {
                    start();
                    //sets elements on first screen to invisible
                    mStart.setVisibility(View.INVISIBLE);
                    mClose.setVisibility(View.INVISIBLE);
                    mStudentNumber.setVisibility(View.INVISIBLE);
                }
                else{
                    noStudentNumber.show();
                }
            }
        });
        //ClickListener for close button, closes app
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void review(){
        String y = "";
        for (int i = 0;i<mQuestionBank.length;i++){
            y = y + mQuestionBank[i].getQuestionID()+"\nAnswer: "+mQuestionBank[i].isAnswer()+"\n\n";
        }
        mReview.setText(y);
        mBack.setVisibility(View.VISIBLE);
        mReview.setVisibility(View.VISIBLE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver();
            }
        });
    }
    //Forms arrays for Question, options and correct from resources
    //Users values in arrays to form a question bank
    private void setQuestionBank() {
        String[] Ques = getResources().getStringArray(R.array.Question);
        String[] Option1 = getResources().getStringArray(R.array.Option1);
        String[] Option2 = getResources().getStringArray(R.array.Option2);
        String[] Option3 = getResources().getStringArray(R.array.Option3);
        String[] Option4 = getResources().getStringArray(R.array.Option4);
        String[] correctAns = getResources().getStringArray(R.array.correct_answer);
        mQuestionBank = new Questions[Ques.length];
        for (int i = 0; i < Ques.length; i++) {
            mQuestionBank[i] = new Questions(Ques[i], Option1[i], Option2[i],
                    Option3[i], Option4[i], correctAns[i]);
        }
    }

    /*
     *   sets object in screen 2 to visible, calls countDown, initialize and selectAnswer
     *   starts timer, sets mTimer to 100 and progressBar to 0 and displays 0/10 qs completed
     */
    private void start(){
        countDown();
        initialize();
        selectAnswer();
        //mQuestionTextView.setVisibility(View.VISIBLE);
        mOption1.setVisibility(View.VISIBLE);
        mOption2.setVisibility(View.VISIBLE);
        mOption3.setVisibility(View.VISIBLE);
        mOption4.setVisibility(View.VISIBLE);
        mQuestionTextView.setVisibility(View.VISIBLE);
        mReview.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTimer.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.INVISIBLE);
        mCompleted.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        mTimer.setProgress(100);
        mCountDownTimer.start();
        mCompleted.setText("Completed: "+qComp+"/10");
    }

    // sets countDown timer for 100 seconds, ticks every 1 second, values of mTimer is increased
    // by timeDecrease (-1) on every tick, calls gameOver on finish
    private void countDown(){
        mCountDownTimer = new CountDownTimer(100000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimer.incrementProgressBy(timeDecrease);
            }
            @Override
            public void onFinish() {
                gameOver();
            }
        };
    }

    //Initializes variables by connecting them to their respective resources (more details available above)
    //Sets question text and options text and initializes other values
    private void initialize() {
        mOption1 = findViewById(R.id.option1);
        mOption2 = findViewById(R.id.option2);
        mOption3 = findViewById(R.id.option3);
        mOption4 = findViewById(R.id.option4);
        mCompleted = findViewById(R.id.completed);
        mQuestionTextView = findViewById(R.id.QuestionText);
        mProgressBar = findViewById(R.id.progress_bar);
        mTimer = findViewById(R.id.timer);
        mBack = findViewById(R.id.back);
        mReview = findViewById(R.id.review);
        mTimer.setProgress(100);
        mIndex = 0;
        mScore = 0;
        mCompleted.setText("Completed: "+qComp+"/10");
        mQuestionTextView.setText(mQuestionBank[mIndex].getQuestionID());
        mOption1.setText(mQuestionBank[mIndex].getOption1());
        mOption2.setText(mQuestionBank[mIndex].getOption2());
        mOption3.setText(mQuestionBank[mIndex].getOption3());
        mOption4.setText(mQuestionBank[mIndex].getOption4());

    }

    //Method sets click listeners for option buttons, and calls checkAnswer and updateQuestion from
    //the button clicked. The button id is passed to checkAnswer
    private void selectAnswer(){
        mOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(mOption1.getText().toString());
                updateQuestion();
            }
        });

        mOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(mOption2.getText().toString());
                updateQuestion();
            }
        });

        mOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(mOption3.getText().toString());
                updateQuestion();
            }
        });

        mOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(mOption4.getText().toString());
                updateQuestion();
            }
        });
    }

    //checks if the value passed in parameter is equal to correct answer from question object
    //if yes score is increased by 1
    private void checkAnswer(String userSelection) {

        String correctAnswer = mQuestionBank[mIndex].isAnswer();
        if (userSelection.equals(correctAnswer)) {
            mScore += 1;
        }
    }

    /* This method updates the question by getting questions object from question bank.
     * it sets the question text and option values, also increments progress bar
     * if the first question is repeated (mIndex == 0) then calls gameOver
     * Updates value for question completed and displays it
     */
    private void updateQuestion() {
        // This takes the modulus. Not a division.
        mIndex = (mIndex + 1) % mQuestionBank.length;
        qComp ++;
        mCompleted.setText("Completed: "+qComp+"/10");
        if(mIndex == 0) {
            gameOver();
        }
        //mQuestion = mQuestionBank[mIndex];
        mQuestionTextView.setText(mQuestionBank[mIndex].getQuestionID());
        mOption1.setText(mQuestionBank[mIndex].getOption1());
        mOption2.setText(mQuestionBank[mIndex].getOption2());
        mOption3.setText(mQuestionBank[mIndex].getOption3());
        mOption4.setText(mQuestionBank[mIndex].getOption4());
        mProgressBar.incrementProgressBy((int) Math.ceil(100.0 / mQuestionBank.length));
    }

    // This callback is received when the screen is rotated so we can save the 'state'
    // of the app in a 'bundle'.
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt("IndexKey", mIndex);
    }

    /*Method create an alert with following attributes
    * Two buttons on alert gives option to close application or restart it (calls start/finish)
    * sets all object on screen to invisible, cancels timer and deletes its instance
    * resets score and question number, displays the alert
    */
    private void gameOver(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Quiz Completed: "+mStudentNumber.getText()  );
        alert.setCancelable(false);
        alert.setMessage("You scored " + mScore + " points!");

        alert.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                start();
            }
        });

        alert.setNeutralButton("Close Application", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.setPositiveButton("Review", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                review();
            }
        });
        mQuestionTextView.setVisibility(View.INVISIBLE);
        mOption1.setVisibility(View.INVISIBLE);
        mOption2.setVisibility(View.INVISIBLE);
        mOption3.setVisibility(View.INVISIBLE);
        mOption4.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mTimer.setVisibility(View.INVISIBLE);
        mBack.setVisibility(View.INVISIBLE);
        mReview.setVisibility(View.INVISIBLE);
        mCountDownTimer = null;
        mCompleted.setVisibility(View.INVISIBLE);
        qComp = 0;
        alert.show();
    }
}



