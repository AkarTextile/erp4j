/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.basic;

import javax.swing.JOptionPane;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class Notice {
    /* generator */
    public Notice(DisplayScreen displayScreen, String title, String explanation, Message... messages) {
        this.displayScreen = displayScreen;
        this.title = title;
        this.explanation = explanation;
        this.messages = messages;
    }
    
    /* title and messages */
    public final String title;
    public final String explanation;
    public final Message[] messages;
    
    /* show notice */
    private final DisplayScreen displayScreen;
    public int showOption(int messageType, Object... options) {
        return this.displayScreen.showOption(this, messageType, options);
    }
    public void showMessage(int messageType) {
        this.displayScreen.showMessage(this, messageType);
    }
    
    /** static **/
    /* message class */
    public static class Message {
        /* generator */
        public Message(String title, String explanation) {
            this.title = title;
            this.explanation = explanation;
        }
        public Message(Exception exception) {
            this.title = TextualOperation.ClassNameOf(exception);
            this.explanation = exception.getLocalizedMessage();
        }
        
        /* title and explanation */
        public final String title;
        public final String explanation;

        @Override
        public String toString() {
            return this.title + ": " + this.explanation;
        }
        public static String toString(String seperator, Message... messages) {
            if(messages.length == 0)
                return "";
            String output = messages[0].toString();
            for(int i = 1; i < messages.length; i++)
                output = output + seperator + messages[i];
            
            return output;
        }
    }
    
    /* display system */
    abstract public static class DisplayScreen {
        abstract public int showOption(Notice notice, int messageType, Object... options);
        abstract public void showMessage(Notice notice, int messageType);
    }
    public static final DisplayScreen JOPTIONPANE_DISPLAY_SCREEN = new DisplayScreen() {
        @Override
        public int showOption(Notice notice, int messageType, Object... options) {
            
            return JOptionPane.showOptionDialog(
                    null, ((notice.explanation != null)?notice.explanation + "\n\n":"") + Message.toString("\n", notice.messages),
                    notice.title, JOptionPane.DEFAULT_OPTION, messageType, null, options, 0);
        }

        @Override
        public void showMessage(Notice notice, int messageType) {
            JOptionPane.showMessageDialog(null, ((notice.explanation != null)?notice.explanation + "\n\n":"")
                    + Message.toString("\n", notice.messages), notice.title, messageType);
        }
    };

    /* message types */
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
}

