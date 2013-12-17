package com.mycompany.app;

import java.io.File;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 12/17/13
 * Time: 3:03
 * To change this template use File | Settings | File Templates.
 */
public class HashOps {




    public HashOps(){

    }



    public int getPolarity(String body){


        int polarity=0;
        int positive=0;
        int negative=0;
        int neutral=0;


        Hashtable<String,Integer> polTable;

        FileOperations f = new FileOperations();

        if(f.openRead("HashFile") == false){

            createHashTable();

        }

        f.openRead("HashFile");
        polTable = (Hashtable) f.readObject();

        String[] words = body.split(" ");

        for(int i = 0; i<words.length; i++){

            if(polTable.containsKey(words[i])){
                if(polTable.get(words[i])==1){
                    positive++;
                }
                else if (polTable.get(words[i])==0){
                    neutral++;
                }
                else{
                    negative++;
                }

            }
        }

        if(positive>neutral && positive>negative){
            polarity=1;

        }

        else if(negative>positive && negative>neutral){
            polarity=-1;
        }
        else{
            polarity=0;
        }

        System.out.println("polarity: " + polarity);

        return polarity;




    }



    public void createHashTable(){

        Hashtable<String,Integer> polTable = new Hashtable<String,Integer>();


        TextOperations t = new TextOperations();
        FileOperations f = new FileOperations();

        String word;


        t.openRead("Sentilex/Positivos.txt");

        while ((word=t.readLine())!=null){



            WordCategory newWord = new WordCategory(word,1);

            polTable.put(newWord.getWord(),newWord.getPolarity());

        }

        t.closeRead();


        t.openRead("Sentilex/Neutros.txt");


        while ((word=t.readLine())!=null){

            WordCategory newWord = new WordCategory(word,0);

            polTable.put(newWord.getWord(),newWord.getPolarity());

        }

        t.closeRead();

        t.openRead("Sentilex/Negativos.txt");


        while ((word=t.readLine())!=null){

            WordCategory newWord = new WordCategory(word,-1);

            polTable.put(newWord.getWord(),newWord.getPolarity());

        }

        t.closeRead();

        if(f.openWrite("HashFile")){
            f.writeObject(polTable);
        }

        f.closeWrite();


    }















}
