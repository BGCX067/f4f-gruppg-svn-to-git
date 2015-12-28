package Försäljning;

import food4fun.Kassasystem;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.*;
import java.io.*;

public class Historik {
    private final String HISTORIK = "test.dat";
    
    public Historik() {
    }
    
    public void saveFile(Kassasystem kassaregister) {
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(new FileOutputStream(HISTORIK));
            out.writeObject(kassaregister);
            out.close();
        }
        catch (IOException ie){
          ie.printStackTrace();
        }
    }
    public Kassasystem loadFile() {
        Kassasystem kassaregister = new Kassasystem();
        try{
          ObjectInputStream in= new ObjectInputStream
                                (new FileInputStream(HISTORIK));
          kassaregister = (Kassasystem)in.readObject();
          in.close();  
        }
        catch (IOException ie){
          ie.printStackTrace();
        }
        catch (ClassNotFoundException ce){
          ce.printStackTrace();
        }
        return kassaregister;
    }
    public boolean tomFil() {
        return new File(HISTORIK).exists();
    }
}
 
