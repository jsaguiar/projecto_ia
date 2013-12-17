package com.mycompany.app;



public class Analyzer {

    public Analyzer() {

    }

    public String analyzeTerm(Polarity mailPolarity, String body) throws Exception {



        String polarity =  mailPolarity.check(body);

        return polarity;
    }
}