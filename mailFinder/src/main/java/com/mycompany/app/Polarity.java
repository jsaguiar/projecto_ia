package com.mycompany.app;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.AbstractExternalizable;

public class Polarity {

    private File mPolarityDir;
    private List<String> mCategories;
    private DynamicLMClassifier<NGramProcessLM> mClassifier;

    public Polarity(String dir) throws IOException, ClassNotFoundException {
        mPolarityDir = new File(dir);

       // System.out.println("Diretorio = " + mPolarityDir);
        //System.out.println("\nArquivos de treino: ");

        mCategories = new ArrayList<String>();

            for(String file : mPolarityDir.list()) {
            String aux = file.substring(0, file.lastIndexOf('.'));

            if(aux != null && !aux.equals("")) {
                mCategories.add(aux);
                //System.out.println("  " + aux);
            }
        }



        int nGram = 8;
        mClassifier
                = DynamicLMClassifier.createNGramProcess(mCategories.toArray(new String[mCategories.size()]),nGram);

        train();
        evaluate();
    }

    void train() throws IOException, ClassNotFoundException {
        int numTrainingCases = 0;
        int numTrainingChars = 0;

        //System.out.println("\nTreinando\n");

        for (int i = 0; i < mCategories.size(); ++i) {
            String category = mCategories.get(i);
            Classification classification = new Classification(category);

            File file = new File(mPolarityDir, category + ".txt");

            String tweet;
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while((tweet = br.readLine()) != null) {
                ++numTrainingCases;
                numTrainingChars += tweet.length();
                Classified<CharSequence> classified = new Classified<CharSequence>(tweet,classification);
                mClassifier.handle(classified);


            }
        }

       // System.out.println("#Casos = " + numTrainingCases);
        //System.out.println("#Caracteres = " + numTrainingChars);

    }

    void evaluate() throws IOException {
        //System.out.println("Avaliando\n");

        int numTests = 0;
        int numCorrect = 0;

        for (int i = 0; i < mCategories.size(); ++i) {
            String category = mCategories.get(i);

            File file = new File(mPolarityDir, category + ".txt");

            String body;
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while((body = br.readLine()) != null) {
                ++numTests;
                Classification classification = mClassifier.classify(body);
                if (classification.bestCategory().equals(category)) {
                    ++numCorrect;
                }
            }
        }

        System.out.println("#Test Cases = " + numTests);
        System.out.println("#Correct = " + numCorrect);
        System.out.println("%Correct = " + ((double)numCorrect)/(double)numTests);

    }

    public String check(String body) {
        return mClassifier.classify(body).bestCategory();
    }

}
