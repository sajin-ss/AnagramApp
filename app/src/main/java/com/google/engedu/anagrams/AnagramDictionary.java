package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int chooser=-1;

    int wordLength=DEFAULT_WORD_LENGTH;

    HashSet wordSet = new HashSet();
    ArrayList wordList = new ArrayList();
    HashMap<String, ArrayList> lettersToWord = new HashMap();
    HashMap<Integer, ArrayList> sizeToWords = new HashMap<>();
    ArrayList<Integer> seed;



    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordSet.add(word);
            wordList.add(word);


            if (lettersToWord.containsKey(sortLetters(word)))
                lettersToWord.get(sortLetters(word)).add(word);
            else
            {
                ArrayList a = new ArrayList();
                a.add(word);
                lettersToWord.put(sortLetters(word), a);
            }

            int s = word.length();
            if (sizeToWords.containsKey(s))
                sizeToWords.get(s).add(word);
            else
            {
                ArrayList a = new ArrayList();
                a.add(word);
                sizeToWords.put(s, a);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {

        if (!wordSet.contains(word))
            return false;

        if (word.contains(base))
            return false;

//        Log.d("mine", "good word");

        return true;
    }

    public String sortLetters(String word)
    {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();

        String temp=word;


//        Log.i("words size : ",wordList.size()+"");
//        Log.i("map size : ",lettersToWord.size()+"");
//        for (String key : lettersToWord.keySet()) {
//            System.out.println("KEY" + key + " ");
//            ArrayList<String> anagrams = lettersToWord.get(key);
//            for(String tempo : anagrams)
//                System.out.println(tempo);
//            System.out.println("\n\n");
//        }


        for (char i='a'; i<='z'; ++i) {
//            Log.d("sdf", sortLetters(temp+i));
            if (lettersToWord.containsKey(sortLetters(temp+i)))
            {
//                Log.d("asdfg", "yathaaaaa");
                ArrayList<String> anagrams = lettersToWord.get(sortLetters(temp+i));
                for (String ana : anagrams)
                    if (!ana.contains(word))
                        result.add(ana);
            }

        }

//        for (int i=0; i<result.size(); ++i)
//            Log.d("ans", result.get(i));

        return result;

    }


    public ArrayList<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char ch = 'a';
        char c;
        ArrayList<String> anagrams;
        for(int i =0;i<26 ;i++) {
            c = 'a';
            for(int j =0;j<26 ;j++) {
                if (lettersToWord.containsKey(sortLetters(word + ch + c))) {
                    anagrams = lettersToWord.get(sortLetters(word + ch + c));
                    for (String temp : anagrams)
                        if (!temp.contains(word))
                            result.add(temp);
                }
                c++;
            }
            ch++;
        }
        return result;

    }
//    public void permutation(String str) {
//        permutation("", str, str);
//    }
//
//    private void permutation(String prefix, String str, String real) {
//        int n = str.length();
//        if (n == 0) {
//            if (wordSet.contains(prefix)) {
//                if (!prefix.contains(real))
//                    result.add(prefix);
//            }
//        }
//        else {
//            for (int i = 0; i < n; i++)
//                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n), real);
//        }
//    }

    public String pickGoodStarterWord() {
        String send;
        ArrayList<String> ana = sizeToWords.get(wordLength);

        if (chooser == -1){
            chooser=0;

            seed = new ArrayList<>();
            for(int i=0; i<ana.size(); ++i)
                seed.add(i);
            Collections.shuffle(seed);
        }else if (chooser==ana.size()){

            chooser = 0;
            wordLength += 1;

            if (wordLength==MAX_WORD_LENGTH)
                wordLength=DEFAULT_WORD_LENGTH;

            ana = sizeToWords.get(wordLength);

            seed = new ArrayList<>();
            for(int i=0; i<ana.size(); ++i)
                seed.add(i);
            Collections.shuffle(seed);
        }

        send = ana.get(seed.get(chooser));
        while(getAnagramsWithOneMoreLetter(send).size() < MIN_NUM_ANAGRAMS){
            chooser++;
            send = ana.get(seed.get(chooser));
        }
        chooser++;


        // Random randomizer = new Random();
        //int i = randomizer.nextInt(wordList.size());


//        send = ana.get(i);
//        send = (String) wordList.get(i);
//        while (lettersToWord.get(sortLetters(send)).size() < MIN_NUM_ANAGRAMS){
//            i++;
//        if (i == wordList.size())
//            i = 0;
//        send = (String) wordList.get(i);
//        }

        return send;
    }
}