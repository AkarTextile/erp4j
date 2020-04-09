/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class GTable extends Panel {
    /* generator */
    public GTable(TableModel tableModel, double filterHeightRate, double scrollPaneWidthRate) {
        /* set absolute layout */
        super(null);
        
        /* create and add textfields */
        this.textFields = new JTextField[tableModel.getColumnCount()];
        for(int i = 0; i < this.textFields.length; i++) {
            this.textFields[i] = new JTextField();
            super.add(this.textFields[i]);
        }
        
        /* create table */
        this.table = new JTable();
        this.table.setColumnModel(new DefaultTableColumnModel());
        this.table.setModel(tableModel);
        super.add(this.table);
        
        /* create scrollpane */
        this.scrollPane = new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        super.add(this.scrollPane);
        
        /* set the filter height and scrollpane width rates */
        this.filterHeightRate = filterHeightRate;
        this.scrollPaneWidthRate = scrollPaneWidthRate;
    }
    
    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /* components of table */
    private final javax.swing.JTextField[] textFields;
    private final javax.swing.JTable table;
    private final  JScrollPane scrollPane;
    
    /* set bounds of the table */
    private final double filterHeightRate;
    
    /* scrollpane width rate */
    private final double scrollPaneWidthRate;
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        
        super.setBounds(x, y, width, height);
        
        
        int filterHeight = (int)(this.filterHeightRate * (double)height);
        int scrolPaneWidth = (int)(this.scrollPaneWidthRate * (double)width);
        
        /* set bounds of table, columns and scrollpane */
        this.table.setBounds(0, filterHeight, width, height - filterHeight);
        for(int j = 0; j < this.table.getColumnCount(); j++) {
            this.table.getColumnModel().getColumn(j).setPreferredWidth((int)((double)(width - scrolPaneWidth) / this.table.getColumnCount()));
        }
        this.scrollPane.setBounds(0, filterHeight, width, height - filterHeight);
        
        /* set filter row */
        int filter_x = 2;
        for(int j = 0; j < this.textFields.length; j++) {
            this.textFields[j].setBounds(filter_x, 0, this.table.getColumnModel().getColumn(j).getPreferredWidth() - 1, filterHeight);
            filter_x += this.table.getColumnModel().getColumn(j).getPreferredWidth();
        }
    }

    @Override
    public void sizeComponents() {};

    @Override
    public void size(JComponent component, double refRightShift, double refScrollDown, double x_rate, double y_rate, double width_rate, double height_rate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void size(JComponent component, double x1_rate, double y1_rate, double x2_rate, double y2_rate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}