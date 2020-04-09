/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component;

import java.awt.LayoutManager;
import erp4j.gui.DesignerI;
import javax.swing.JComponent;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class Panel extends javax.swing.JPanel implements DesignerI, ComponentI {
    /* generator */
    public Panel(LayoutManager layoutManager) {
        /* set the layout manager of panel */
        super.setLayout(layoutManager);
    }
    @Override
    public void size(JComponent component, double refRightShift, double refScrollDown, double x_rate, double y_rate, double width_rate, double height_rate) {
        /* shift */
        x_rate -= refRightShift * width_rate;
        refScrollDown -= refScrollDown * width_rate;
        
        /*  */
        this.add(component);
        component.setBounds((int)(x_rate * this.getSize().getWidth()), (int)(y_rate * this.getSize().getHeight()),
                (int)(width_rate * this.getSize().getWidth()), (int)(height_rate * this.getSize().getHeight()));
    }

    @Override
    public void size(JComponent component, double x1_rate, double y1_rate, double x2_rate, double y2_rate) {
        this.size(component, DesignerI.LeftOrTop, DesignerI.LeftOrTop, x1_rate, y1_rate, x2_rate - x1_rate, y2_rate - y1_rate);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height); //To change body of generated methods, choose Tools | Templates.
        this.sizeComponents();
    }
    
    @Override
    public void addScrollPane() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}