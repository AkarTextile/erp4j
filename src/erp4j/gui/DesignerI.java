/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import javax.swing.JComponent;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public interface DesignerI {
    /* screen size */
    public final static Dimension ScreenSize =
            new Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth(),
                            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight());
    
    /* shift rates */
    public final double LeftOrTop = 0.0;
    public final double RightOrBottom = 1.0;
    public final double Center = 0.5;
    
    /** add and size components **/
    /* add component with size infos */
    public void size(JComponent component, double refRightShift, double refScrollDown, double x_rate,
            double y_rate, double width_rate, double height_rate);
    
    /* add component with location infos */
    public void size(JComponent component, double x1_rate, double y1_rate, double x2_rate, double y2_rate);
    
    /* set components */
    public void sizeComponents();
}
