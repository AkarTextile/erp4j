/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.dataStorage.sql;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class Database implements java.io.Serializable {
    /* generator */
    public Database(String name, ManagementSystem managementSystem) {
        this.name = name;
        this.managementSystem = managementSystem;
    }
    
    /* database name */
    public final String name;
    
    /* database management system */
    public final ManagementSystem managementSystem;
    
    /* load the class of database */
    public void loadClass() throws ClassNotFoundException {
        Class.forName(this.managementSystem.className);
    }
    
    /* connection */
    private ConnectionType connectionType = null;
    private Properties info;
    private String user, password;
    protected java.sql.Connection connection;
    public void connect() throws SQLException {
        this.connectionType = ConnectionType.Simple;
        this.info = null;
        this.user = null;
        this.password = null;
        this.connection = DriverManager.getConnection(this.managementSystem.url);
    }
    public void connect(Properties info) throws SQLException {
        this.connectionType = ConnectionType.Info;
        this.info = info;
        this.user = null;
        this.password = null;
        this.connection = DriverManager.getConnection(this.managementSystem.url, info);
    }
    public void connect(String user, String password) throws SQLException {
        this.connectionType = ConnectionType.UserPassword;
        this.info = null;
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(this.managementSystem.url, user, password);
    }
    public void reconnect() throws SQLException {
        switch(this.connectionType) {
            case Simple:
                this.connect();
                break;
            case Info:
                this.connect(this.info);
                break;
            case UserPassword:
                this.connect(this.user, this.password);
                break;
            default:
                throw new SQLException("");
        }
    }
    public void close() throws SQLException, NullPointerException {
        this.connection.close();
    }
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }
    
    /* tables of database */
    protected ArrayList<Table> tables = new ArrayList<>();
    
    /* connection type */
    public enum ConnectionType {
        Simple,
        Info,
        UserPassword
    }
    public SQLException ConnectionTypeException() {
        return new SQLException("Connection " + this.managementSystem.name + " database " + this.name + " was connected with");
    }
    
    /** static **/
    /* database management system */
    public static class ManagementSystem implements java.io.Serializable {
        /* generator */
        public ManagementSystem(String name, String className, String url) {
            this.name = name;
            this.className = className;
            this.url = url;
        }
        
        /* name of management system */
        public final String name;
        
        /* class name and url of database */
        public final String className;
        public final String url;
        
        /** defined management systems **/
        /* UCanAccess */
        public static ManagementSystem UCanAccess(String path) {
            return new ManagementSystem("UCanAccess", "net.ucanaccess.jdbc.UcanaccessDriver", "jdbc:ucanaccess://" + path.replace("\\", "/"));
        }
        public static ManagementSystem SQLite(String path) {
            return new ManagementSystem("SQLite", null, "jdbc:sqlite:" + path.replace("\\", "/"));
        }
        public static ManagementSystem MySQL(String hostname, String dbName) {
            return new ManagementSystem("MySQL", "com.mysql.jdbc.Driver", "jdbc:mysql://" + hostname + "/" + dbName);
        }
        public static ManagementSystem MySQL(String ip, String port, String dbName) {
            return new ManagementSystem("MySQL", "com.mysql.jdbc.Driver", "jdbc:mysql://" + ip + ":" + port + "/" + dbName);
        }
        public static ManagementSystem ORACLE(String hostname, int portNumber, String dbName) {
            return new ManagementSystem("ORACLES", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@" + hostname + ":" + portNumber + ":" + dbName);
        }   
    }
    
    /* defined ip adresses */
    public static String HostAddress_Local() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
    public static String HostName_Local() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
    public static String CanonicalHostName_Local() throws UnknownHostException {
        return InetAddress.getLocalHost().getCanonicalHostName();
    }
}