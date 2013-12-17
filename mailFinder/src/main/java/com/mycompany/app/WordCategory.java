package com.mycompany.app;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 12/17/13
 * Time: 3:41
 * To change this template use File | Settings | File Templates.
 */
public class WordCategory implements Serializable {

    private String word;
    private int polarity;


    public WordCategory(String word, int polarity){

        this.word=word;
        this.polarity=polarity;


    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }
}
