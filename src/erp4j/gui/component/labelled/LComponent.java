/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component.labelled;

import javax.swing.JComponent;
import javax.swing.JLabel;
import erp4j.basic.TextualOperation;
import erp4j.gui.component.Panel;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class LComponent <T extends JComponent> extends Panel {
    /* generator */
    public LComponent(String labelText, double labelSizeRate, Direction labelDirection, T mainComponent) {
        /* set absolute layout manager */
        super(null);
        
        /* create add components */
        this.label = new JLabel(labelText);
        this.labelSizeRate = labelSizeRate;
        this.mainComponent = mainComponent;
        super.add(this.label);
        super.add(this.mainComponent);
        /* set the label direction */
        this.labelDirection = labelDirection;
    }
    
    /* label and text field */
    public final JLabel label;
    public final T mainComponent;
    private final double labelSizeRate;
    private final Direction labelDirection;
    
    /* set bounds of components */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.sizeComponents();
    }

    @Override
    public void sizeComponents() {
        switch (this.labelDirection) {
            case Left:
                int labelWidth = (int)(this.labelSizeRate * this.getSize().getWidth());
                this.label.setBounds(0, 0, labelWidth, this.getSize().height);
                this.mainComponent.setBounds(labelWidth, 0, this.getSize().width - labelWidth, this.getSize().height);
                break;
            case Top:
                int labelHeight = (int)(this.labelSizeRate * this.getSize().getHeight());
                this.label.setBounds(0, 0, this.getSize().width, labelHeight);
                this.mainComponent.setBounds(0, labelHeight, this.getSize().width, this.getSize().height - labelHeight);
                break;
            default:
                throw new RuntimeException("Undefined label direction in " + TextualOperation.ClassNameOf(this) + ".");
        }
    }
    
    /** static **/
    /* direction */
    public enum Direction {
        Left,
        Top,
        Right,
        Bottom,
        Center;
    }
}
