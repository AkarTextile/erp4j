/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component.labelled;

import java.util.ArrayList;
import javax.swing.JComponent;
import erp4j.gui.component.Panel;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class LPanel extends Panel {
    /* generator */
    public LPanel(Direction direction, double gapRate) {
        super(null);
        this.direction = direction;
        this.gapRate = gapRate;
    }

    @Override
    public void refresh() {
    }
    
    @Override
    public void sizeComponents() {
        switch(this.direction) {
            case LeftToRight:
                int gapWidth = (int)(this.gapRate * this.getSize().getWidth());
                int componentWidth = (int)((this.getSize().getWidth() - (this.components.size() - 1) * gapWidth) / this.components.size());
                for(int j = 0; j < this.components.size(); j++) {
                    this.add(this.components.get(j));
                    this.components.get(j).setBounds(j * (componentWidth + gapWidth), 0, componentWidth, this.getHeight());
                }
                break;
            case TopToBottom:
                int gapHeight = (int)(this.gapRate * this.getSize().getHeight());
                int componentHeight = (int)((this.getSize().getHeight() - (this.components.size() - 1) * gapHeight) / this.components.size());
                
                for(int i = 0; i < this.components.size(); i++) {
                    this.add(this.components.get(i));
                    this.components.get(i).setBounds(0, i * (componentHeight + gapHeight), this.getWidth(), componentHeight);
                    
                }
                break;
        }
    }
    
    /* add l components lcomponents */
    private final ArrayList<JComponent> components = new ArrayList<>();
    private final ArrayList<Double> componentSizeRate = new ArrayList<>();
    public boolean add(JComponent componentI, double sizeRate) {
        return this.components.add(componentI) && this.componentSizeRate.add(sizeRate);
    }
    
    /* direction parameters */
    private final LPanel.Direction direction;
    private final double gapRate;
    
    public enum Direction {
        LeftToRight,
        TopToBottom;
    }
}
