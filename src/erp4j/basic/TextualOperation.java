/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.basic;

import java.util.ArrayList;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class TextualOperation {
    /* get clas name of any object */
    public static String ClassNameOf(Object object) {
        return object.getClass().toString().substring(object.getClass().toString().lastIndexOf(".") + 1);
    }
    
    /** object converter **/
    abstract public static class ObjectConvertor<T> {
        /* read and write methods for single obejcts */
        abstract public T read(String stringValue);
        abstract public String write(T object);
    
        /* read and write methods for arrays */
        public ArrayList<T> readArray(char seperator, String stringArray) {
            ArrayList<T> arrayList = new ArrayList<>();
            int i = 0;
            int j = 0;
            for(; j <stringArray.length(); j++)
                if(stringArray.charAt(j) == seperator) {
                    arrayList.add(this.read(stringArray.substring(i, j)));
                    i = j + 1;
                }
            
            if(i < j)
                arrayList.add(this.read(stringArray.substring(i, j)));
            
            return arrayList;
        }
        public String writeArray(char seperator, ArrayList<T> arrayList) {
            if(arrayList.size() > 0) {
                String stringArray = this.write(arrayList.get(0));
                
                for(int i = 1; i < arrayList.size(); i++)
                    stringArray = stringArray + seperator + this.write(arrayList.get(i));
                
                return stringArray;
            }
            else
                return "";
        }
        /** static **/
        /* string converter */
        public static ObjectConvertor<String> STRING = new ObjectConvertor<String>() {
            @Override
            public String read(String stringValue) {
                return stringValue;
            }

            @Override
            public String write(String object) {
                return object;
            }
        };
        /* integer converter */
        public static final ObjectConvertor<Integer> INTEGER = new ObjectConvertor<Integer>() {
            @Override
            public Integer read(String stringValue) {
                return Integer.parseInt(stringValue);
            }

            @Override
            public String write(Integer object) {
                return object.toString();
            }
        };
        /* double converter */
        public static final ObjectConvertor<Double> DOUBLE = new ObjectConvertor<Double>() {
            @Override
            public Double read(String stringValue) {
                return Double.parseDouble(stringValue);
            }

            @Override
            public String write(Double object) {
                return object.toString();
            }
        };
    }
    
    /** replace **/
    /* spesific index */
    public static String replace(String s, int index, char c) {
        return s.substring(0, index) + c + s.substring(index + 1);
    }
    
    /* upper */
    public static String replaceWithUpper(String s, int index) {
        return replace(s, index, Character.toUpperCase(s.charAt(index)));
    }
    
    /* lower */
    public static String replaceWithLower(String s, int index) {
        return replace(s, index, Character.toLowerCase(s.charAt(index)));
    }
    
    /* proper noun */
    public static String ProperNoun(String s) {
        String newS = Character.toUpperCase(s.charAt(0)) + "";
        
        for(int i = 1; i < s.length(); i++)
            if(s.charAt(i - 1) == ' ')
                newS = newS + Character.toUpperCase(s.charAt(i));
            else
                newS = newS + Character.toLowerCase(s.charAt(i));
        return newS;
    }
    
    /** string type **/
    public static boolean is(char c, char... conditions) {
        for(char condition : conditions)
            if(c == condition)
                return true;
        
        return false;
    }
    public static boolean is(JTextComponent textComponent, int index, char... conditions) {
        return is(textComponent.getText().charAt(index), conditions);
    }
    public static boolean is(String s, char... conditions) {
        for(int i = 0; i < s.length(); i++)
            if(!is(s.charAt(i), conditions))
                return false;
        return true;
    }
    public static boolean is(JTextComponent textComponent, char... conditions) {
        return is(textComponent.getText(), conditions);
    }
    
    /* defined conditions */
    public static final char[] NUMERICAL = "0123456789".toCharArray();
    
    /** text component methods **/
    /* char at */
    public static void delete(JTextComponent textComponent, int lenght) {
        textComponent.setText(textComponent.getText().substring(0, textComponent.getText().length() - lenght));
    }
}
