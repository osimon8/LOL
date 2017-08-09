/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lol.cooldown.viewer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Owen
 */
public class Cacher {
    
    
    
    public static void store(Object o, String filepath) {

        FileOutputStream fout = null;
        ObjectOutputStream oos = null;

        try {

                fout = new FileOutputStream(filepath);
                oos = new ObjectOutputStream(fout);
                oos.writeObject(o);

                //System.out.println("Done");

        } catch (Exception ex) {

                ex.printStackTrace();

        } finally {

                if (fout != null) {
                        try {
                                fout.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                if (oos != null) {
                        try {
                                oos.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

        }
    }
    
    public static Object read(String filepath) {

        Object o = null;

        FileInputStream fin = null;
        ObjectInputStream ois = null;

        try {

                fin = new FileInputStream(filepath);
                ois = new ObjectInputStream(fin);
                o = (Object) ois.readObject();

        } catch (Exception ex) {
                ex.printStackTrace();
        } finally {

                if (fin != null) {
                        try {
                                fin.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                if (ois != null) {
                        try {
                                ois.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

        }

        return o;

    }
}



