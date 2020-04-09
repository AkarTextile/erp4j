/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component.labelled;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import javax.swing.JTextField;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class LTextField extends LComponent<JTextField>{
    /* generator */
    public LTextField(String labelText, double labelSizeRate, Direction labelDirection) {       
        super(labelText, labelSizeRate, labelDirection, new JTextField());
    }
    
    /* textfield methods */
    public void del(int lenght) {
        this.mainComponent.setText(this.mainComponent.getText().substring(0, this.mainComponent.getText().length() - lenght));
    }
    public void del() {
        this.mainComponent.setText(this.mainComponent.getText().substring(0, this.mainComponent.getText().length() - 1));
    }
    
    /** get value **/
    public java.sql.Date getSQLDate(DateFormat dateFormat) throws ParseException {
        return new java.sql.Date(dateFormat.parse(this.mainComponent.getText()).getTime());
    }
    public Time getSQLTime(DateFormat dateFormat) throws ParseException {
        return new java.sql.Time(dateFormat.parse(this.mainComponent.getText()).getTime());
    }
    public String getText() {
        return this.mainComponent.getText();
    }
    public int getInt() {
        return Integer.parseInt(this.mainComponent.getText());
    }
    public double getDouble() {
        return Double.parseDouble(this.mainComponent.getText());
    }
    
    /** set text methods
     * @param t **/
    public void setText(String t) {
        this.mainComponent.setText(t);
    }
    
    @Override
    public void refresh() {
        this.mainComponent.setText("");
    }
}
