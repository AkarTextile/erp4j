/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.account;

import erp4j.dataStorage.sql.ClassifiableTable;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class UserAccount extends ClassifiableTable.Row{
    /* generator */
    public UserAccount(int id, String firstName, String middleName, String surname, 
            String username, String password, String nickname, String company,
            String facility, String department, String subdepartment, String position, 
            String phoneNumber, String e_mail) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.company = company;
        this.facility = facility;
        this.department = department;
        this.subdepartment = subdepartment;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.e_mail = e_mail;
    }
    
    /* columns */
    public final int id;
    public final String firstName;
    public final String middleName;
    public final String surname;
    public final String username;
    public final String password;
    public final String nickname;
    public final String company;
    public final String facility;
    public final String department;
    public final String subdepartment;
    public final String position;
    public final String phoneNumber;
    public final String e_mail;
}
