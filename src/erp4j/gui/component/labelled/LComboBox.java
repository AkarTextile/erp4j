/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component.labelled;

import javax.swing.JComboBox;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class LComboBox<T> extends LComponent<JComboBox<T>> {
    /* generator */
    public LComboBox(String labelText, double labelSizeRate, Direction labelDirection) {
        super(labelText, labelSizeRate, labelDirection, new JComboBox<>());
    }
}
