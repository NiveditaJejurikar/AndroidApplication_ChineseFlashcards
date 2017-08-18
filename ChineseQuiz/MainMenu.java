package com.example.niveditajejurikar.chinesequiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.util.Log;
import android.widget.RadioGroup;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.*;
import java.net.URLConnection;

public class MainMenu extends AppCompatActivity {

    static public Word[] theDictionary;
    static public int wordCount = 0;
    public static String dictionaryFileName = "dictionary.txt";
    public static String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //receive intent if this page activates after user takes quiz
        Intent intent = getIntent();

        theDictionary = new Word[100];
        wordCount = 0;

        //String str = "Alpha, Beta, Theta";

        //thread to load dictionary
        Thread read = new Thread(
                new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            filePath = getFilesDir().getPath() + dictionaryFileName;
                            //load existing dictionary
                            boolean dL = doLoad();

                            //for a manual reset of the dictionary, comment in:
                            //dL = false;

                            if( !dL )
                            {
                                String str = "NIVU";
                                //Log.i("BEFORE &&&&&&&&&", str);
                                //read online dictionary and load all new words into dictionary file
                                URL url = new URL("http://people.cs.georgetown.edu/~bk620/chidi.txt");
                                BufferedReader in = new BufferedReader (new InputStreamReader(url.openStream()));




                                int index = 0;
                                //Log.i("&&&&&&&&&&&&&&&&&", str);

                                while ((str = in.readLine()) !=null)
                                {
                                    //Log.i("*****",str);
                                    theDictionary[index] = new Word();
                                    theDictionary[index].definitions = new String[3];
                                    theDictionary[index].definitions = str.split(",");
                                    theDictionary[index].charAcc = new boolean[8];
                                    theDictionary[index].pinyinAcc = new boolean[8];
                                    theDictionary[index].englishAcc = new boolean[8];
                                    theDictionary[index].accTrackers = new int[3];
                                    theDictionary[index].accTrackers[0] = 0;
                                    theDictionary[index].accTrackers[1] = 0;
                                    theDictionary[index].accTrackers[2] = 0;
                                    theDictionary[index].lastTest = new long[3];
                                    theDictionary[index].interestLevel = 5;
                                    // Log.i("*****",theDictionary[index].definitions[0]+theDictionary[index].definitions[1]+theDictionary[index].definitions[2]);

                                    index++;
                                    wordCount++;
                                }
                                in.close();
                            }
                        }
                        catch(Exception e)
                        {

                        }
                    }
                });

        read.start();
    }


    //doLoad loads existing dictionary file into the Array
    public boolean doLoad()
    {
        boolean loaded = false;
        theDictionary = new Word[100];
        wordCount = 0;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            loaded = true;
        }
        catch(Exception e) {}

        try {
            Word w = (Word) (ois.readObject());
            while (w != null) {
                theDictionary[wordCount] = w;
                wordCount++;
                w = (Word) (ois.readObject());
            }

            fis.close();
        } catch (Exception e) {}


        return loaded;
    }

    //store user setting for quiz type
    public static int quizType = 0;

    public void onRadioButtonClicked (View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            case R.id.qT3:
                if (checked)
                    quizType = 0;
                break;
            case R.id.qT2:
                if (checked)
                    quizType = 1;
                break;
            case R.id.qT1:
                if (checked)
                    quizType = 2;
                break;
            case R.id.qT4:
                if (checked) {
                    //randomly set quizType to one of the other options
                    Random randy = new Random();
                    quizType = randy.nextInt(2);
                }
                break;
        }

    }

    //store user setting for quiz mode
    public static int quizMode = 0;

    public void onRadioButton2Clicked (View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            //quizMode 1 is Continuous
            case R.id.qM1:
                if (checked)
                    quizMode = 1;
                break;
            //quizMode 2 is Once Through
            case R.id.qM2:
                if (checked)
                    quizMode = 2;
                break;
        }

    }

    //start the quiz activity passing user choices
    //make sure this can only be selected when both radio buttons are checked?
    public void onStart(View view)
    {
        //clear radio button selections
        RadioGroup FirstGroup = (RadioGroup) findViewById(R.id.op1);
        FirstGroup.clearCheck();
        RadioGroup SecondGroup = (RadioGroup) findViewById(R.id.op2);
        SecondGroup.clearCheck();

        Intent intent = new Intent (this, PlayQuiz.class);

        intent.putExtra("quizType", quizType);
        intent.putExtra("quizMode", quizMode);

        startActivity(intent);
    }
}