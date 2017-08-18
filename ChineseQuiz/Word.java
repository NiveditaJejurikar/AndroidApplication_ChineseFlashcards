package com.example.niveditajejurikar.chinesequiz;

/**
 * Created by niveditajejurikar on 4/20/17.
 */



    import java.io.Serializable;

    public class Word implements Serializable {
        public String[] definitions;
        public boolean[] charAcc;
        public boolean[] pinyinAcc;
        public boolean[] englishAcc;
        public int[] accTrackers;
        public long[] lastTest;
        public int interestLevel;
    }


