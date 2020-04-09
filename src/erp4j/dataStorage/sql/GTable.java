/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.dataStorage.sql;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import erp4j.gui.component.Panel;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class GTable extends Panel {
    /* generator */
    public GTable(double topFieldHeightRate, double headerHeightRate, double headerButtonHeightRate) {
        super(null);
        /* set the model of jtable */
        this.table = new JTable(this.model);
        this.filterList = null;
        
        /* add table on panel */
        super.add(this.table);
        
        /* set the height rates */
        this.topFieldHeightRate = topFieldHeightRate;
        this.headerHeightRate = headerHeightRate;
        this.headerButtonHeightRate = headerButtonHeightRate;
    }
    public GTable(FilterList filterList, double topFieldHeightRate, double headerHeightRate, double headerButtonHeightRate) {
        super(null);
        /* set the model of jtable */
        this.table = new JTable(this.model);
        this.filterList = filterList;
        
        this.topFieldHeightRate = topFieldHeightRate;
        this.headerHeightRate = headerHeightRate;
        this.headerButtonHeightRate = headerButtonHeightRate;
    }
    
    @Override
    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /* set bounds of each components */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        
        
        
        /* set bounds of table */
        int tableY_rate = (int)((this.topFieldHeightRate + this.headerHeightRate) * (double)width);
        this.table.setBounds(0, tableY_rate, width, height - tableY_rate);
    }
    private final double topFieldHeightRate;
    private final double headerHeightRate;
    private final double headerButtonHeightRate;
    
    /* components of gtable */
    private final JTable table;
    private DefaultTableModel model = new DefaultTableModel();
    protected final ArrayList<GColumn> gColumns = new ArrayList<>();
    private FilterList filterList;
    
    /* update the dataset */
    abstract public boolean next();
    public void update() {
        Object[] columnNames = new Object[this.gColumns.size()];
        for(int i = 0; i < columnNames.length; i++)
            columnNames[i] = this.gColumns.get(i).header;
        this.model = new DefaultTableModel(columnNames, 0);
        
        if(this.filterList == null)
            while(this.next()) {
                Object[] row = new Object[this.gColumns.size()];
                for(int j = 0; j < this.gColumns.size(); j++)
                    row[j] = this.gColumns.get(j).get();

                this.model.addRow(row);
            }
        else
            while(this.next()) if(this.filterList.state()) {
                Object[] row = new Object[this.gColumns.size()];
                for(int j = 0; j < this.gColumns.size(); j++)
                    row[j] = this.gColumns.get(j).get();

                this.model.addRow(row);
            }
        
        this.table.setModel(this.model);
    }
    public void update(FilterList filterList) {
        this.filterList = filterList;
        update();
    }
    
    /* table components */
    abstract public class GColumn<T> {
        /* generator */
        public GColumn(String header, int x, int y, int width, int height, double headerButtonHeightRate) {
            this.header = new Header(header);
            int headerButtonHeight = (int)((double)height * headerButtonHeightRate);
            this.header.button.setBounds(x, y, width, headerButtonHeight);
            this.header.textField.setBounds(x, y + headerButtonHeight, width, height - headerButtonHeight);
            GTable.this.gColumns.add(this);
        }
        
        /* header of gcolumn */
        public final Header header;
        
        /* get value */
        abstract public T get();
        
        /* header */
        public class Header {
            /* generator */
            public Header(String text) {
                this.button = new JButton(text);
                this.textField = new JTextField();
            }

            /* label and textField */
            public final JButton button;
            public final JTextField textField;
        }
    }
    
    /** static **/
    /* filter */
    abstract public static class Filter {
        abstract boolean state();
    }
    abstract public static class FilterList extends Filter {
        /* generator */
        public FilterList(Filter... filters) {
            this.filters = filters;
        }
        public final Filter[] filters;
    }
    public static class And extends FilterList {
        /* generator */
        public And(Filter... filters) {
            super(filters);
        }
        @Override
        public boolean state() {
            for(Filter f : this.filters)
                if(!f.state())
                    return false;
            
            return true;
        }
    }
    public static class Or extends FilterList {
        /* generator */
        public Or(Filter... filters) {
            super(filters);
        }
        @Override
        public boolean state() {
            if(this.filters.length == 0)
                return true;
            
            for(Filter f : this.filters)
                if(f.state())
                    return true;
            
            return false;
        }
    }
}