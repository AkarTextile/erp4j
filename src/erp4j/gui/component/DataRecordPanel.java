/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import erp4j.application.Executive;
import erp4j.dataStorage.sql.Table;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class DataRecordPanel extends Panel {
    /* generator */
    public DataRecordPanel(LayoutManager layoutManager, Executive executive, Table table) {
        super(layoutManager);
        this.executive = executive;
        this.table = table;
    }
    
    /* executive */
    private final Executive executive;
    
    /** database connection **/
    /* databases */
    public final Table table;
    @Override
    public void refresh() {
        /* set initial page */
        switch(this.table.getRowCount()) {
            case 0:
                this.isBlankPage = true;
                this.updateComponentData();
                break;
            case 1:
                this.table.absolute(1);
                this.isBlankPage = false;
                this.updateComponentData();
                break;
            default:
                this.table.absolute(-1);
                this.isBlankPage = false;
                this.updateComponentData();
                break;
        }
    }
    
    /* update data */
    abstract void updateComponentData();
    
    /** button actions **/
    /* record button */
    abstract ActionListener createRecordActionListener();
    public ActionListener createPreviousActionListener() {
        return (ActionEvent e) -> {
            if(DataRecordPanel.this.table.previous()) {
                DataRecordPanel.this.isBlankPage = false;
                DataRecordPanel.this.updateComponentData();
            }
            else {
                if(DataRecordPanel.this.table.next()) {
                    DataRecordPanel.this.isBlankPage = false;
                    DataRecordPanel.this.updateComponentData();
                }
                else {
                    DataRecordPanel.this.isBlankPage = true;
                    DataRecordPanel.this.updateComponentData();
                }
            }
        };
    }
    /* hold info about the page is blank or not blank */
    private boolean isBlankPage;
}
