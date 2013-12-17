package com.mycompany.app;

/**
 * Created with IntelliJ IDEA.
 * User: goncalodias
 * Date: 12/17/13
 * Time: 3:18
 * To change this template use File | Settings | File Templates.
 */
import java.io.*;

public class TextOperations {

    private BufferedReader br;
    private BufferedWriter bw;

    public boolean openRead(String fileName){

        try{
            File f = new File(fileName);
            FileReader fr = new FileReader(f);
            br = new BufferedReader(fr);
            return true;
        }

        catch(Exception e){
            System.out.println("Nao foi possivel abrir o ficheiro para leitura.");
            return false;
        }
    }

    public boolean openWrite(String fileName){

        try{
            File f = new File(fileName);
            FileWriter fw = new FileWriter(f);
            bw = new BufferedWriter(fw);
            return true;
        }

        catch(Exception e){
            System.out.println("Nao foi possivel abrir o ficheiro para escrita.");
            return false;
        }
    }

    public void closeRead(){

        try{
            br.close();
        }

        catch(Exception e){
            System.out.println("Nao foi possÕvel fechar o ficheiro aberto para leitura.");
        }
    }

    public void closeWrite(){

        try{
            bw.close();
        }

        catch(Exception e){
            System.out.println("Nao foi possÕvel fechar o ficheiro aberto para escrita.");
        }
    }

    public String readLine(){

        try{
            return br.readLine();
        }

        catch(Exception e){
            System.out.println("Nao foi possivel ler o ficheiro.");
            return null;
        }
    }

    public void writeLine(String linha){

        try{
            bw.write(linha, 0, linha.length());
            bw.newLine();
        }

        catch(Exception e){
            System.out.println("Nao foi possivel escrever no ficheiro.");
        }
    }
}
