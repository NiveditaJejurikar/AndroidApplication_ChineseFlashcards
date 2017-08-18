package com.example.niveditajejurikar.chinesequiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.lang.String;
import java.util.*;

public class PlayQuiz extends AppCompatActivity {

    //global constant that determines length of quiz
    public static final int QUIZ_LENGTH = 10;

    //variables that determine question
    public static int[] quizWords;
    public static int onWord;
    public static int tWI;
    public static int qType;
    public static int qMode;
    public static boolean contGame;

    //variables to track user performance
    public static int correctCounter;
    public static int incorrectCounter;
    public static int totalAnswered;
    public static boolean FirstOptionCorrect = false;
    public static boolean SecondOptionCorrect = false;

    //variables to record time
    public static long startTime = 0;
    public static long endTime = 0;
    public static long elapsed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_quiz);

        //retrieve the intent and user selections
        Intent intent = getIntent();

        qType = intent.getIntExtra("quizType", MainMenu.quizType);
        qMode = intent.getIntExtra("quizMode", MainMenu.quizMode);

        Log.i("-------", ""+qMode );

        if(qMode==1)
            contGame = true;
        else
            contGame = false;

        totalAnswered = 0;
        incorrectCounter = 0;
        totalAnswered = 0;

        //select words
        selectWords();

        //initialize question and option buttons
        setTestWord();
        setOptions1();
        setOptions2();

        //set range of number picker that allows user to rate interest on a word
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(10);

        //record the time that the quiz was started
        startTime = (System.currentTimeMillis());
    }
    //selectWord will pick words to quiz
    public void selectWords()
    {
        quizWords = new int[QUIZ_LENGTH];

        //code that selects quizWords based on user interest and past success
        //begin at beginning of dictionary
        int q = 0;
        for( int i = 0; i < MainMenu.wordCount && q < QUIZ_LENGTH; i++ )
        {

            //check if word is "known"
            boolean includeWord = true;
            boolean wordKnown = false;
            Word thisWord = MainMenu.theDictionary[i];
            int aTally = 0;
            int bTally = 0;
            int cTally = 0;

            for( int a = 0; a < 8; a++)
            {
                if( thisWord.charAcc[a] )
                    aTally++;
            }
            for( int b = 0; b < 8; b++)
            {
                if( thisWord.pinyinAcc[b] )
                    bTally++;
            }
            for( int c = 0; c < 8; c++)
            {
                if( thisWord.englishAcc[c] )
                    cTally++;
            }

            if( (aTally > 3) || (bTally > 3) || (cTally > 3) )
                wordKnown = true;

            //if the word is known, it must have not been tested within the last month
            if( wordKnown )
            {
                long oneMonth = (long) 2e+9;
                long lastTest = thisWord.lastTest[qType];
                if( (startTime - lastTest) > oneMonth )
                    includeWord = true;
                else
                    includeWord = false;
            }

            //check that user interest in the word is greater than 4
            if( includeWord )
                if( thisWord.interestLevel < 5 )
                    includeWord = false;

            //if still includeWord, insert into quizWords
            if( includeWord )
            {
                quizWords[q] = i;
                q++;
            }

        }

        //code randomly select quizWords
//        for ( int i = 0; i < QUIZ_LENGTH; i++ )
//        {
//            Random randy = new Random();
//            int randomWord = randy.nextInt(MainMenu.wordCount);
//            quizWords[i] = randomWord;
//        }

        //begin iterator at the beginning of list
        onWord = 0;
        tWI = quizWords[onWord];
    }

    //setTestWord displays the word being tested
    public void setTestWord()
    {
        //set testingWord to qType value of definitions array
        TextView testingWord = (TextView) findViewById(R.id.tW);
        testingWord.setText(MainMenu.theDictionary[tWI].definitions[qType]);

        //record the testing time of this word
        MainMenu.theDictionary[tWI].lastTest[qType] = startTime;
    }

    //setOptions1 fills in the first set of radio button options with choices
    //for the meaning of the selected word
    public void setOptions1()
    {
        int defType = (qType+1)%3;
        //array is not initialized
        //somehing is wrong w definitions



        Random rando = new Random();
        int randomWord = rando.nextInt(MainMenu.wordCount);
        int randomWord2 = rando.nextInt(MainMenu.wordCount);
        int randomWord3 = rando.nextInt(MainMenu.wordCount);

        TextView Option1 = (TextView) findViewById(R.id.rb1);
        TextView Option2 = (TextView) findViewById(R.id.rb2);
        TextView Option3 = (TextView) findViewById(R.id.rb3);
        TextView Option4 = (TextView) findViewById(R.id.rb4);

        String[] options = {MainMenu.theDictionary[tWI].definitions[defType],
                MainMenu.theDictionary[randomWord].definitions[defType],
                MainMenu.theDictionary[randomWord2].definitions[defType],
                MainMenu.theDictionary[randomWord3].definitions[defType]};

        Random randy = new Random();
        int startOn = randy.nextInt(4);

        Option1.setText(options[startOn]);
        Option2.setText(options[(startOn+1)%4]);
        Option3.setText(options[(startOn+2)%4]);
        Option4.setText(options[(startOn+3)%4]);
    }

    //setOptions2 fills in the second set of radio button options with choices
    //for the meaning of the selected word
    public void setOptions2()
    {
        int defType2 = (qType+2)%3;

        Random rando = new Random();
        int randomWord = rando.nextInt(MainMenu.wordCount);
        int randomWord2 = rando.nextInt(MainMenu.wordCount);
        int randomWord3 = rando.nextInt(MainMenu.wordCount);

        TextView Option1 = (TextView) findViewById(R.id.rb5);
        TextView Option2 = (TextView) findViewById(R.id.rb6);
        TextView Option3 = (TextView) findViewById(R.id.rb7);
        TextView Option4 = (TextView) findViewById(R.id.rb8);

        String[] options = {MainMenu.theDictionary[tWI].definitions[defType2],
                MainMenu.theDictionary[randomWord].definitions[defType2],
                MainMenu.theDictionary[randomWord2].definitions[defType2],
                MainMenu.theDictionary[randomWord3].definitions[defType2]};

        Random randy = new Random();
        int startOn = randy.nextInt(4);

        Option1.setText(options[startOn]);
        Option2.setText(options[(startOn+1)%4]);
        Option3.setText(options[(startOn+2)%4]);
        Option4.setText(options[(startOn+3)%4]);
    }

    //this section is called when user selects one of the first options
    //the function compares the chosen answer to the correct answer
    public void onFirstOptionSelected ( View view )
    {
        boolean checked = ((RadioButton) view).isChecked();
        int defType = (qType+1)%3;
        String correctAnswer = MainMenu.theDictionary[tWI].definitions[defType];

        switch (view.getId()) {
            case R.id.rb1:
                if (checked)
                {
                    String answer = ((RadioButton)view).getText().toString();
                    if (answer == correctAnswer)
                        FirstOptionCorrect = true;
                    else
                        FirstOptionCorrect = false;
                }

                break;
            case R.id.rb2:
                if (checked)
                {
                    String answer = ((RadioButton)view).getText().toString();
                    if (answer == correctAnswer)
                        FirstOptionCorrect = true;
                    else
                        FirstOptionCorrect = false;
                }
                break;
            case R.id.rb3:
                if (checked)
                {
                    String answer = ((RadioButton)view).getText().toString();
                    if (answer == correctAnswer)
                        FirstOptionCorrect = true;
                    else
                        FirstOptionCorrect = false;
                }
                break;
        }
    }

    //pretty much same as the above function, only the user
    //is selecting the chinese character here
    public void onSecondOptionSelected (View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        int defType = (qType+2)%3;
        String correctAnswer = MainMenu.theDictionary[tWI].definitions[defType];

        switch (view.getId()) {
            case R.id.rb4:
                if (checked)
                {
                    String answer = ((RadioButton)view).getText().toString();
                    if (answer == correctAnswer)
                        SecondOptionCorrect = true;
                    else
                        SecondOptionCorrect = false;
                }
                break;
            case R.id.rb5:
                if (checked)
                {
                    String answer = ((RadioButton)view).getText().toString();
                    if (answer == correctAnswer)
                        SecondOptionCorrect = true;
                    else
                        SecondOptionCorrect = false;
                }

                break;
            case R.id.rb6:
                if (checked)
                {
                    String answer = ((RadioButton)view).getText().toString();
                    if (answer == correctAnswer)
                        SecondOptionCorrect = true;
                    else
                        SecondOptionCorrect = false;
                }
                break;
        }

    }

    //onSubmit controls game play and when to move to the activity
    //it passes and updates the relevant variables, updates the total number of questions answered, the total
    //it also re populates the radio buttons while the quiz continues
    public void onSubmit(View view)
    {
        boolean moveOn = false;

        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        MainMenu.theDictionary[tWI].interestLevel = np.getValue();

        //update total answered, number correct, and number incorrect
        totalAnswered++;
        if ((FirstOptionCorrect ) && (SecondOptionCorrect )) {
            correctCounter++;
            //set word to invalid index value and store success
            quizWords[onWord] = MainMenu.wordCount+2;
            int placeHold = MainMenu.theDictionary[tWI].accTrackers[qType];
            if( qType== 0 )
                MainMenu.theDictionary[tWI].charAcc[placeHold] = true;
            else if( qType == 1 )
                MainMenu.theDictionary[tWI].pinyinAcc[placeHold] = true;
            else if( qType == 2 )
                MainMenu.theDictionary[tWI].englishAcc[placeHold] = true;
        }
        else
            incorrectCounter++;

        //move correct accTracker to the next index
        MainMenu.theDictionary[tWI].accTrackers[qType] = (MainMenu.theDictionary[tWI].accTrackers[qType]+1)%8;

        //quiz mode determines when quiz is over
        if( contGame )
        {
            //stopping condition is all words have been answered correctly
            if( correctCounter == QUIZ_LENGTH )
                moveOn = true;
        }
        else
        {
            //stopping condition is end of one round of quiz has been reached
            if( onWord == QUIZ_LENGTH-1 )
                moveOn = true;
        }

        //if quiz isn't over, reset question
        if( !moveOn )
        {
            //advance onWord and refresh tWI
            onWord = (onWord+1)%QUIZ_LENGTH;
            while( quizWords[onWord] > MainMenu.wordCount )
                onWord = (onWord+1)%QUIZ_LENGTH;
            tWI = quizWords[onWord];

            //re initialize to false for the next question
            FirstOptionCorrect = false;
            SecondOptionCorrect = false;

            //update the target view and option buttons so the quiz loads another word
            setTestWord();
            setOptions1();
            setOptions2();

            //unselect the radio buttons
            RadioGroup FirstGroup = (RadioGroup) findViewById(R.id.rg1);
            FirstGroup.clearCheck();
            RadioGroup SecondGroup = (RadioGroup) findViewById(R.id.rg2);
            SecondGroup.clearCheck();
        }
        //otherwise move on to the activity
        else if ( moveOn )
        {
            //calculate time elapsed and store
            endTime = (System.currentTimeMillis());
            elapsed = endTime - startTime;

            Intent intent = new Intent(this, ResultPage.class);
            intent.putExtra("total", totalAnswered);
            intent.putExtra("correct", correctCounter);
            intent.putExtra("incorrect", incorrectCounter);
            intent.putExtra("duration", elapsed);

            startActivity(intent);
            finish();
        }
    }


}
