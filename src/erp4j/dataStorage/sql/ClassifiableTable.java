/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.dataStorage.sql;

import java.sql.SQLException;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class ClassifiableTable<T extends ClassifiableTable.Row> extends Table {
    /* generator */
    public ClassifiableTable(String name) {
        super(name);
    }
    
    /* get row */
    abstract public T getRow() throws SQLException;
    
    /* row class */
    public static class Row {}
}
