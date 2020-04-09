/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public interface ComponentI extends java.io.Serializable {
    /* generator */
    public void addScrollPane();
    
    public void setBounds(int x, int y, int width, int height);
    
    public void refresh();
}
