package com.example.projekt.scheduled;

import java.io.IOException;

public class Scraper {
    private static Scraper scraper;

    public static Scraper getInstance() {
        if (scraper == null)
            scraper =new Scraper();
        return scraper;
    }

    public void run(){
        try {
            Process p = Runtime.getRuntime().exec("python main.py");
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public void lokaty(){
        try {
            Process p = Runtime.getRuntime().exec("python lokaty.py");
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public void waluty(){
        try {
            Process p = Runtime.getRuntime().exec("python waluty.py");
        }
        catch(IOException e){
            System.out.println(e);
        }
    }

    public void krypto(){
        try {
            Process p = Runtime.getRuntime().exec("python krypto.py");
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}
