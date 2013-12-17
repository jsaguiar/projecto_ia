package com.mycompany.app;

/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 12/17/13
 * Time: 3:17
 * To change this template use File | Settings | File Templates.
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class FileOperations {

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    //opens a file on read mode
    public boolean openRead(String fileName){

        try{
            ois = new ObjectInputStream(new FileInputStream(fileName));
            return true;
        }

        catch(Exception e){
            System.out.println("Could not open the file for reading...");
            return false;
        }
    }

    //opens a file on write mode
    public boolean openWrite(String fileName){

        try{
            oos = new ObjectOutputStream(new FileOutputStream(fileName));
            return true;
        }

        catch(Exception e){
            System.out.println("Could not open the file for writing...");
            return false;
        }
    }

    //closes a file opened on read mode
    public void closeRead(){

        try{
            ois.close();
        }

        catch(Exception e){
            System.out.println("Could not close the file opened for reading...");
        }
    }

    //closes a file opened on write mode
    public void closeWrite(){

        try{
            oos.close();
        }

        catch (Exception e){
            System.out.println("Could not close the file opened for writing...");
        }
    }

    //reads an object from file
    public Object readObject(){

        try{
            return ois.readObject();
        }
        catch(ClassNotFoundException e) {
            System.out.println("The file has no objects...");
            return null;
        }
        catch(Exception e1){
            System.out.println("It was not possible to read the file.");
            return null;
        }
    }

    //writes an object in file
    public void writeObject(Object o){

        try{
            oos.writeObject(o);
        }

        catch(Exception e){
            System.out.println("It was not possible to write in the file...");
        }
    }
}
