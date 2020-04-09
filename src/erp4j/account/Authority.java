/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.account;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class Authority extends ArrayList<Authority.Feature> {
    /* generator */
    public Authority() {
        super();
    }
    public Authority(Feature... features) {
        super(Arrays.asList(features));
    }
    
    /* is user authorized */
    public boolean authorized(UserAccount userAccount) {
        for(Feature feature : this)
            if(!feature.have(userAccount))
                return false;
        
        return true;
    }
    
    /* feature for authority */
    abstract public static class Feature extends ArrayList<Feature>{
        abstract public boolean have(UserAccount userAccount);
    }
}