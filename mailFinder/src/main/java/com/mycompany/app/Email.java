package com.mycompany.app;

/**
 * Created with IntelliJ IDEA.
 * User: joao
 * Date: 12/17/13
 * Time: 2:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class Email {
    private int id;
    private String subject;
    private String body;
    private String date;
    private String from;
    private String to;
    private int polarity;

    public Email(int id, String body, String date, int polarity, String from, String to, String subject) {
        this.id = id;
        this.body = body;
        this.date = date;
        this.polarity = polarity;
        this.from = from;
        this.to = to;
        this.subject = subject;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getPolarity() {
        return polarity;
    }

    public void setPolarity(int polarity) {
        this.polarity = polarity;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
