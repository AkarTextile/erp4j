/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class Cluster<I,T> extends ArrayList<T> {
    /* generator */
    public Cluster() {
        super();
    }
    public Cluster(T... e) {
        super(Arrays.asList(e));
    }
    
    /* indexing of elements */
    public abstract I getIndexer(T e);
    public I getIndexer(int index) {
        return this.getIndexer(super.get(index));
    }
    public int getPrimaryKey(T e) {
        return getIndexer(e).hashCode();
    }
    public int getPrimaryKey(int i) {
        return getIndexer(i).hashCode();
    }
    
    /* binary searching */
    public T binarySearch(I indexer) {
        /* left and right side of array */
        int l = 0;
        int r = super.size() - 1;
                
        /* primary key of indexer */
        int indexerPK = indexer.hashCode();
        
        /* until each elements is checked */
        while (l <= r) {
            /* set mid point */
            int m = (l + r) / 2;
            
            /* primary key of mid point */
            int midpointPK = this.getPrimaryKey(m);
            /* if the both primary keys are same, return mth value */
            if(indexerPK == midpointPK)
                return super.get(m);
            /* if indexer is on left side, m <- r */
            else if(indexerPK < midpointPK)
                r = m - 1;
            /* if indexer is on right side l -> m */
            else if(midpointPK < indexerPK)
                l = m + 1;
        }
        
        /* the primary key was not found */
        this.notFoundIndexerException(indexer);
        return null;
    }
    
    /* add value */
    @Override
    public boolean add(T e) {
        /* if the array is empty add the value directly */
        if(this.isEmpty())
            return super.add(e);
        
        /* left and right side of array */
        int l = 0;
        int r = super.size() - 1;
                   
        /* primary key of element */
        int newElementPK = this.getPrimaryKey(e);
        
        while(l < r) {
            /* set mid point */
            int m = (l + r) / 2;
            
            /* hash code of mid point */
            int midpointPK = this.getPrimaryKey(m);
            
            /* if indexer is on left side, m <- r */
            if(newElementPK < midpointPK) {
                r = m - 1;
            }
            /* if indexer is on right side, l -> m */
            else if(midpointPK < newElementPK)
                l = m + 1;
            /* if the both primary keys are same, return mth value */
            else if(newElementPK == midpointPK) {
                this.add(m, e);
                return true;
            }
        }
        
        /* set the primary keys of left side and right side*/
        int leftPK = this.getPrimaryKey(l);
        
        if(newElementPK <= leftPK)
            this.add(l, e);
        else
            this.add(l + 1, e);
        
        return true;
    }
    
    /* remove value */
    public void removeElement(I indexer) {
        /* left and right side of array */
        int l = 0;
        int r = super.size() - 1;
                
        /* primary key of indexer */
        int indexerPK = indexer.hashCode();
        
        /* until each elements is checked */
        while (l <= r) {
            /* set mid point */
            int m = (l + r) / 2;
            
            /* primary key of mid point */
            int midpointPK = this.getPrimaryKey(m);
            /* if the both primary keys are same, remove mth value */
            if(indexerPK == midpointPK)
                super.remove(m);
            /* if indexer is on left side, m <- r */
            else if(indexerPK < midpointPK)
                r = m - 1;
            /* if indexer is on right side l -> m */
            else if(midpointPK < indexerPK)
                l = m + 1;
        }
        
        /* the primary key was not found */
        this.notFoundIndexerException(indexer);
    }
    
    /* throw run time exception */
    private void notFoundIndexerException(I indexer) {
        throw new RuntimeException("Indexer '" + indexer.toString() + "' was not found using binary searching algorithm.");
    }

    @Override
    public String toString() {
        String st;
        if(isEmpty())
            return "[]";
        else
            st = "[" + super.get(0);
        
        for(int i = 1; i < super.size(); i++) {
            st = st + ", " + super.get(i);
        }
        
        return st + "]";
    }
    
    public static Cluster<Integer, Integer> Test1(int min, int max, int lenght) {
        System.out.println("Test 1 is running.");
        Cluster<Integer, Integer> c = new Cluster<Integer, Integer>() {
            @Override
            public Integer getIndexer(Integer e) {
                return e;
            }
        };
        Random r = new Random();
        for(int i = 0; i < lenght; i++) {
            c.add(r.nextInt(max - min) + min);
        }
        
        System.out.println(c);
        
        System.out.println("Test 1 complated.");
        
        return c;
    }
}       
