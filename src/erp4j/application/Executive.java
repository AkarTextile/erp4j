/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.application;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import erp4j.account.UserAccount;
import erp4j.basic.Notice;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
abstract public class Executive<T extends UserAccount> implements CommandI {    
    /* generator */
    public Executive(String version, Locale locale, Notice.DisplayScreen noticeDisplayScreen) {
        /* set version */
        this.version = version;
        
        /* set locales and formats */
        this.locale = locale;
        Locale.setDefault(locale);
        this.sortDateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
        this.longDateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm", locale);
        this.timeFormat24 = new SimpleDateFormat("HH:mm", locale);
        
        /* set notice display scren */
        this.noticeDisplayScreen = noticeDisplayScreen;
    }
    
    /* application version */
    public final String version;
    
    /* logged in user */
    protected T loggedInUserAccount = null;
    public T getLoggedInUserAccount() {
        return loggedInUserAccount;
    }
    public final boolean isLoggedIn() {
        return this.loggedInUserAccount != null;
    }
    abstract public void logIn(String username, String password);
    public void logOut() {
        this.loggedInUserAccount = null;
    }
    
    /* restart and exit */
    public void restart() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

        /* is it a jar file? */
        if(!currentJar.getName().endsWith(".jar"))
            return;

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
    public void exit() {
        System.exit(0);
    }
    
    /* locale and formats */
    public final Locale locale;
    public final SimpleDateFormat sortDateFormat;
    public final SimpleDateFormat longDateFormat;
    public final SimpleDateFormat timeFormat24;
    
    /* run app */
    @Override
    abstract public void run();
    
    /** notice **/
    private final Notice.DisplayScreen noticeDisplayScreen;
    public int showUserNotice(int messageType, Object[] options, String title, String explanation, Notice.Message... messages) {
        return new Notice(this.noticeDisplayScreen, title, explanation, messages)
                .showOption(messageType, options);
    }
    public void showUserNotice(int messageType, String title, String explanation, Notice.Message... messages) {
        //System.out.println(messages.length);
        new Notice(this.noticeDisplayScreen, title, explanation, messages).showMessage(messageType);
    }
    
    /** static **/
    /* defined paths */
    public static final String WORKINGDIRECTORY_PATH
            = System.getProperty("user.dir").replace("\\", "/");
    
    public static final String RESOURCES_PATH = WORKINGDIRECTORY_PATH + "/resources";
    
    /* defined locales */
    public static final Locale TURKEY_LOCALE = new Locale("tr", "TR");
    public static final Locale CURRENT_LOCALE = Locale.getDefault();
    
    /* date */
    public static final java.sql.Date getCurrentDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }
}