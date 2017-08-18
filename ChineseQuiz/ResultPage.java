package com.example.niveditajejurikar.chinesequiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import java.io.*;
import java.lang.String;
import java.util.*;
import java.io.FileOutputStream;

public class ResultPage extends AppCompatActivity implements Serializable{

    public static int wordsKnown = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int totalansweredint;
        int totalcorrectint;
        int totalincorrectint;
        long elapsedlong;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        doSave();

        Intent intent = getIntent();

        totalansweredint = intent.getIntExtra("total", PlayQuiz.totalAnswered);
        totalcorrectint = intent.getIntExtra("correct", PlayQuiz.correctCounter);
        totalincorrectint = intent.getIntExtra("incorrect", PlayQuiz.incorrectCounter);
        elapsedlong = intent.getLongExtra("duration", PlayQuiz.elapsed);
        elapsedlong = elapsedlong/1000;

        getWordsKnown();

        String totalAnswered = String.valueOf(totalansweredint);
        String totalCorrect = String.valueOf(totalcorrectint);
        String totalIncorrect = String.valueOf(totalincorrectint);
        String wordsKnownString = String.valueOf(wordsKnown);
        String timeElapsed = String.valueOf(elapsedlong)+"s";

        TextView TotalResponses = (TextView) findViewById(R.id.tv1);
        TextView Correct = (TextView) findViewById(R.id.tv2);
        TextView Incorrect = (TextView) findViewById(R.id.tv3);
        TextView Known = (TextView) findViewById(R.id.tv4);
        TextView Duration = (TextView) findViewById(R.id.tv5);

        //set results number values
        TotalResponses.setText(totalAnswered);
        Correct.setText(totalCorrect);
        Incorrect.setText(totalIncorrect);
        Known.setText(wordsKnownString);
        Duration.setText(timeElapsed);
    }

    //return back to Main Menu page
    public void returnToMain( View view )
    {
        //Intent intent = new Intent(this, MainMenu.class);
        //startActivity(intent);
        finish();
    }

    public void getWordsKnown()
    {
        //program counts word as known if the player got
        //at least one of the three quiz types correct in at least four of the past eight times
        //testing that type
        for( int i = 0; i < MainMenu.wordCount; i++ )
        {
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
                wordsKnown++;
        }
    }

    //doSave will save dictionary to file before moving to next activity
    public void doSave()
    {
        try
        {
            File file = new File(MainMenu.filePath);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            int index = 0;
            while( index < MainMenu.wordCount )
            {
                Word w = MainMenu.theDictionary[index];
                oos.writeObject(w);
                index++;
            }

            fos.close();
        }
        catch(Exception e){ Log.i( "------", "error="+e ); }
    }

}
