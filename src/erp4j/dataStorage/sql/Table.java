/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.dataStorage.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import erp4j.basic.TextualOperation;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class Table {
    
    /* generator */
    public Table(String name) {
        this.name = name;
    }
    
    /* name, database and result set of database */
    public final String name;
    private Database database = null;
    private ResultSet resultSet = null;
    public ResultSet getResultSet() {
        return resultSet;
    }
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.resultSet.getMetaData();
    }
    
    /* result set type */
    private Integer resultSetType = null;
    private Integer resultSetConcurrency = null;
    private Integer resultSetHoldability = null;
    private ConnectionType connectionType = null;
    public enum ConnectionType {
        Simple,
        TypeConcurrency,
        TypeConcurrencyHoldability;
    }
    
    /* connection */
    public void connect(Database database) throws SQLException {
        if(database == null)
            this.database.tables.add(this);
        this.database = database;
        Statement statement = this.database.connection.createStatement();
        this.resultSet = statement.executeQuery("SELECT * FROM " + this.name);
        this.connectionType = ConnectionType.Simple;
    }
    public void connect(Database database, int resultSetType, int resultSetConcurrency) throws SQLException {
        if(database == null)
            this.database.tables.add(this);
        this.database = database;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        
        Statement statement = this.database.connection.createStatement(resultSetType, resultSetConcurrency);
        this.resultSet = statement.executeQuery("SELECT * FROM " + this.name);
        this.connectionType = ConnectionType.TypeConcurrency;
    }
    public void connect(Database database, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        if(database == null)
            this.database.tables.add(this);
        this.database = database;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
        
        Statement statement = this.database.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        this.resultSet = statement.executeQuery("SELECT * FROM " + this.name);
        this.connectionType = ConnectionType.TypeConcurrencyHoldability;
    }
    public void reconnect() throws SQLException {
        switch(this.connectionType) {
            case Simple:
                this.connect(this.database);
                break;
            case TypeConcurrency:
                this.connect(this.database, this.resultSetType, this.resultSetConcurrency);
                break;
            case TypeConcurrencyHoldability:
                this.connect(this.database, this.resultSetType, this.resultSetConcurrency, this.resultSetHoldability);
                break;
        }
    }
    public void close() throws SQLException {
        this.resultSet.close();
    }
    public boolean isClosed() throws SQLException {
        return this.resultSet.isClosed();
    }
    
    /** scrollable
     * @return  **/
    /* moves the cursor to the previous row */
    public boolean previous() {
        try {
            return this.resultSet.previous();
        } catch (SQLException ex) {
            return false;
        }
    }
    /* moves the cursor forward one row from its current position */
    public boolean next() {
        try {
            return this.resultSet.next();
        } catch (SQLException ex) {
            return false;
        }
    }
    /* moves the cursor to the first row in this */
    public boolean first() {
        try {
            return this.resultSet.first();
        } catch (SQLException ex) {
            return false;
        }
    }
    /* moves the cursor to the last row in this */
    public boolean last() {
        try {
            return this.resultSet.last();
        } catch (SQLException ex) {
            return false;
        }
    }
    /* moves the cursor to the given row number in this */
    public boolean absolute(int row) {
        try {
            return this.resultSet.absolute(row);
        } catch (SQLException ex) {
            return false;
        }
    }
    /* moves the cursor a relative number of rows, either positive or negative */
    public boolean relative(int row) {
        try {
            return this.resultSet.relative(row);
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /* insert new row */
    public void insert(Column.SQLValue... sqlValues) throws SQLException {
        
        /* set structure INSERT INTO table_name (columnName1,...) VALUES (?,...) */
        String columnNames = "(" + sqlValues[0].getColumn().name;
        String values = "(?";
        for(int i = 1; i < sqlValues.length; i++) {
            columnNames = columnNames + ", " + sqlValues[i].getColumn().name;
            values = values + ", ?";
        }
        columnNames = columnNames + ")";
        PreparedStatement preparedStatement = this.database.connection.prepareStatement("INSERT INTO " + this.name + " " + columnNames + " VALUES " + values + ")");
        
        /* set the values in prepared statement */
        for(int i = 1; i <= sqlValues.length; i++) {
            sqlValues[i - 1].setInPreparedStatement(i, preparedStatement);
        }
        
        preparedStatement.executeUpdate();
        this.reconnect();
    }
    
    /* delete row */
    private Column uniqueColumn = null;
    public void setUniqueColumn(Column uniqueColumn) {
        this.uniqueColumn = uniqueColumn;
    }
    public void delete() throws SQLException {
        if(this.uniqueColumn != null) {
            this.database.connection.prepareStatement("DELETE FROM " + this.name + " WHERE " + this.uniqueColumn.name + " = " + this.uniqueColumn.get()).executeUpdate();
            this.reconnect();
        }
        else
            throw new SQLException("The unique column of table '" + this.name + "' is null");
    }
    
    /* get number of row */
    public int getRowCount() {
        //Creating a Statement object
        Statement statement;
        try {
            /* create result set and import number of row */
            ResultSet rs = this.database.connection.createStatement().executeQuery("SELECT COUNT(*) FROM " + this.name);
            rs.next();
            return rs.getInt("count(*)");
        } catch (SQLException ex) {
            return -1;
        }
    }
    
    /* get current row */
    public int getRowIndex() {
        try {
            return this.resultSet.getRow();
        } catch (SQLException ex) {
            return -1;
        }
    }
    
    /** column of table
     * @param <T> **/
    abstract public class Column<T> {
        /* generator */
        public Column(String name) {
            this.name = name;
            Table.this.columns.add(this);
        }

        /* connection */
        private boolean connect() throws SQLException {
            for(int i = 1; i <= Table.this.getMetaData().getColumnCount(); i++)
                if(Table.this.getMetaData().getColumnName(i).equals(this.name)) {
                    this.index = i;
                    return true;
                }
                        
            throw new SQLException("'" + this.name + "' column " + " was not found on table '" + Table.this.name + "'");
        }
        
        /* label and index of column */
        public final String name;
        protected int index;
        public int getColIndex() {
            return index;
        }
        
        /* table, getter and setter mehods */
        public Table getTable() {
            return Table.this;
        }
        abstract public T get();
        public T get(Column column, Object value) {
            if(column.search(value))
                return this.get();
            return null;
        }
        abstract public void set(T x) throws SQLException;
        
        /* find matched elements */
        public <E>ArrayList<T> lookup(ArrayList<E> lookup_values, Column<E> lookup_column) {
            ArrayList<T> matchedList = new ArrayList<>();
            
            Table.this.absolute(0);
            while(Table.this.next())
                for(int i = 0; i < lookup_values.size(); i++) 
                    if(lookup_column.get().equals(lookup_values.get(i))) {
                        matchedList.add(this.get());
                        lookup_values.remove(i);
                    }
            
            return matchedList;
        }
        
        /* find the row */
        public boolean search(T findWhat) {
            /* turn the begining */
            Table.this.first();
            
            /* search all elements */
            while (Table.this.next())
                if(this.get().equals(findWhat))
                    return true;
            
            /* if it cannot be found, return false */
            return false;
        }
        
        /* create additive value */
        abstract public SQLValue createSQLValue(T value) throws SQLException;
        public abstract class SQLValue {
            /* generator */
            public SQLValue(T value) {
                this.value = value;
            }
            
            /* column name */
            public Column<T> getColumn() {
                return Column.this;
            }
            
            /* additive value */
            private final T value;
            
            /* set the value in statement */
            abstract public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException;
        }
    }
    
    private final ArrayList<Column> columns = new ArrayList<>();
    public void connectColumns() throws SQLException {
        for(Column column : columns)
            column.connect();
    }
    
    /** defined columns
     * @param name
     * @return  **/
    /* string column */
    public Column<String> stringColumn(String name) {
        return new Column<String>(name) {
            @Override
            public String get() {
                try {
                    return Table.this.resultSet.getString(this.index);
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(String x) throws SQLException {
                Table.this.resultSet.updateString(this.index, x);
                Table.this.resultSet.updateRow();
            }

            @Override
            public Column.SQLValue createSQLValue(String value) {
                if(value == null)
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setNull(parameterIndex, Types.VARCHAR);
                        }
                    };
                else
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement)throws SQLException{
                            preparedStatement.setString(parameterIndex, value);
                        }
                    };
            }
        };
    }
    /* integer column */
    public Column<Integer> integerColumn(String name) {
        return new Column<Integer>(name) {
            @Override
            public Integer get() {
                try {
                    return Table.this.resultSet.getInt(this.index);
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(Integer x) throws SQLException {
                Table.this.resultSet.updateInt(this.index, x);
            }
            
            @Override
            public Column.SQLValue createSQLValue(Integer value) {
                if(value == null)
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setNull(parameterIndex, Types.VARCHAR);
                        }
                    };
                else
                    return new Column.SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException{
                            preparedStatement.setInt(parameterIndex, value);
                        }
                    };
            }
        };
    }
    /* double column */
    public Column<Double> doubleColumn(String name) {
        return new Column<Double>(name) {
            @Override
            public Double get() {
                try {
                    return Table.this.resultSet.getDouble(this.index);
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(Double x) throws SQLException{
                Table.this.resultSet.updateDouble(this.index, x);
            }
            
            @Override
            public Column.SQLValue createSQLValue(Double value) {
                if(value == null)
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setNull(parameterIndex, Types.VARCHAR);
                        }
                    };
                else
                    return new Column.SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException{
                            preparedStatement.setDouble(parameterIndex, value);
                        }
                    };
            }
        };
    }
    /* boolean column */
    public Column<Boolean> booleanColumn(String name) {
        return new Column<Boolean>(name) {
            @Override
            public Boolean get() {
                try {
                    return Table.this.resultSet.getBoolean(this.index);
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(Boolean x) throws SQLException{
                    Table.this.resultSet.updateBoolean(this.index, x);
            }

            @Override
            public Column.SQLValue createSQLValue(Boolean value) {
                return new SQLValue(value) {
                    @Override
                    public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setBoolean(parameterIndex, value);
                    }
                };
            }
        };
    }
    /* date column */
    public Column<java.sql.Date> dateColumn(String name) {
        return new Column<java.sql.Date>(name) {
            @Override
            public java.sql.Date get() {
                try {
                    return Table.this.resultSet.getDate(this.index);
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(java.sql.Date x) throws SQLException{
                    Table.this.resultSet.updateDate(this.index, x);
            }

            @Override
            public Column.SQLValue createSQLValue(Date value) {
                if(value == null)
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setNull(parameterIndex, Types.DATE);
                        }
                    };
                else
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setDate(parameterIndex, value);
                        }
                    };
            }
        };
    }
    /* time column */
    public Column<java.sql.Time> timeColumn(String name) {
        return new Column<java.sql.Time>(name) {
            @Override
            public java.sql.Time get() {
                try {
                    return Table.this.resultSet.getTime(this.index);
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(java.sql.Time x) throws SQLException{
                    Table.this.resultSet.updateTime(this.index, x);
            }

            @Override
            public Column.SQLValue createSQLValue(Time value) {
                if(value == null)
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setNull(parameterIndex, Types.TIME);
                        }
                    };
                else
                    return new SQLValue(value) {
                        @Override
                        public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement) throws SQLException {
                            preparedStatement.setTime(parameterIndex, value);
                        }
                    };
            }
        };
    }
    /* array column */
    public <T> Column<ArrayList<T>> arrayColumn(String name, char seperator, TextualOperation.ObjectConvertor<T> objectConvertor) {
        return new Column<ArrayList<T>>(name) {
            @Override
            public ArrayList<T> get() {
                try {
                    return objectConvertor.readArray(seperator, Table.this.resultSet.getString(this.index));
                } catch (SQLException ex) {
                    return null;
                }
            }
            
            @Override
            public void set(ArrayList<T> x) throws SQLException {
                Table.this.resultSet.updateString(this.index, objectConvertor.writeArray(seperator, x));
            }
            
            @Override
            public Column.SQLValue createSQLValue(ArrayList<T> value) throws SQLException {
                return new SQLValue(value) {
                    @Override
                    public void setInPreparedStatement(int parameterIndex, PreparedStatement preparedStatement)throws SQLException{
                        preparedStatement.setString(parameterIndex, objectConvertor.writeArray(seperator, value));
                    }
                };
            }
        };
    }
}