/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.basic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class ObjectConvertor<T> {
    /* generator */
    public ObjectConvertor(String filePath) {
        this.filePath = filePath;
    }
    
    /* file path */
    public final String filePath;
    
    /* read configurator */
    public T read()
            throws FileNotFoundException, IOException, ClassNotFoundException {
        /* read object */
        FileInputStream fis = new FileInputStream(this.filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);
        //fis.close();
        //ois.close();
        return (T)ois.readObject();
    }
    /* write configurator */
    public void write(T object)
            throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(this.filePath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        fos.close();
        oos.close();
    }
}
