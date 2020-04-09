/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import javax.swing.JComponent;
import erp4j.account.Authority;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class Page extends javax.swing.JFrame implements DesignerI {
    /* generator */
    public Page(LayoutManager layoutManager, int closeOperation, int extendedState, String name, Point p, Dimension d, Authority.Feature... features) {
        /** set basic frame properties **/
        /* set layout manager of the page */
        super.setLayout(layoutManager);
        /* set the default close operation */
        super.setDefaultCloseOperation(closeOperation);
        /* set extended state */
        super.setExtendedState(extendedState);
        
        /* set the name of page */
        this.name = name;
        
        /* set the bounds of page */
        super.setBounds(p.x, p.y, d.width, d.height);
        
        /* set the authority of page */
        this.authority = new Authority(features);
    }
    
    /* name of frame */
    public final String name;
    
    /* authorities of frame */
    public final Authority authority;
    
    @Override
    public void size(JComponent component, double refRightShift, double refScrollDown, double x_rate, double y_rate, double width_rate, double height_rate) {
        /* shift */
        x_rate -= refRightShift * width_rate;
        refScrollDown -= refScrollDown * width_rate;
        
        component.setBounds((int)(x_rate * this.getSize().getWidth()), (int)(y_rate * this.getSize().getHeight()),
                (int)(width_rate * this.getSize().getWidth()), (int)(height_rate * this.getSize().getHeight()));
    }

    @Override
    public void size(JComponent component, double x1_rate, double y1_rate, double x2_rate, double y2_rate) {
        this.size(component, DesignerI.LeftOrTop, DesignerI.LeftOrTop, x1_rate, y1_rate, x2_rate - x1_rate, y2_rate - y1_rate);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.sizeComponents();
    }
    
    abstract public void refresh();
}
