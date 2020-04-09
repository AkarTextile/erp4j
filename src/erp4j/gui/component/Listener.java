/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp4j.gui.component;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import erp4j.basic.TextualOperation;

/**
 *
 * @author Hakan Tek
 * @author Gülşah Aslı Arslan
 */
public class Listener {    
    /* because block over click */
    abstract public static class SingleClick_Button implements java.awt.event.ActionListener {    
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!this.running) {
                this.running = true;
                this.action(e);
                this.running = false;
            }
        }
        
        private boolean running = false;
        abstract public void action(ActionEvent e);
    }

    /** declerated document listeners **/
    /* Autocomplete */
    public static DocumentListener Autocomplete() {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {}

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
    
    /* Autocomplete  */
    public static class Autocomplete2 implements DocumentListener {

      private static enum Mode {
        INSERT,
        COMPLETION
      };

      private JTextField textField;
      private final List<String> keywords;
      private Mode mode = Mode.INSERT;

      public Autocomplete2(JTextField textField, List<String> keywords) {
        this.textField = textField;
        this.keywords = keywords;
        Collections.sort(keywords);
      }

      @Override
      public void changedUpdate(DocumentEvent ev) { }

      @Override
      public void removeUpdate(DocumentEvent ev) { }

      @Override
      public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() != 1)
          return;

        int pos = ev.getOffset();
        String content = null;
        try {
          content = textField.getText(0, pos + 1);
        } catch (BadLocationException e) {
          e.printStackTrace();
        }

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
          if (!Character.isLetter(content.charAt(w))) {
            break;
          }
        }

        // Too few chars
        if (pos - w < 2)
          return;

        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(keywords, prefix);
        if (n < 0 && -n <= keywords.size()) {
          String match = keywords.get(-n - 1);
          if (match.startsWith(prefix)) {
            // A completion is found
            String completion = match.substring(pos - w);
            // We cannot modify Document from within notification,
            // so we submit a task that does the change later
            SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
          }
        } else {
          // Nothing found
          mode = Mode.INSERT;
        }
      }
      
      public class CommitAction extends AbstractAction {
        /**
         * 
         */
        private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent ev) {
          if (mode == Mode.COMPLETION) {
            int pos = textField.getSelectionEnd();
            StringBuffer sb = new StringBuffer(textField.getText());
            sb.insert(pos, " ");
            textField.setText(sb.toString());
            textField.setCaretPosition(pos + 1);
            mode = Mode.INSERT;
          } else {
            textField.replaceSelection("\t");
          }
        }
      }

      private class CompletionTask implements Runnable {
        private String completion;
        private int position;

        CompletionTask(String completion, int position) {
          this.completion = completion;
          this.position = position;
        }

        public void run() {
          StringBuffer sb = new StringBuffer(textField.getText());
          sb.insert(position, completion);
          textField.setText(sb.toString());
          textField.setCaretPosition(position + completion.length());
          textField.moveCaretPosition(position);
          mode = Mode.COMPLETION;
        }
      }

      public static void Test() {
        String COMMIT_ACTION = "commit";
        JTextField mainTextField = new JTextField();

        // Without this, cursor always leaves text field
        mainTextField.setFocusTraversalKeysEnabled(false);

        // Our words to complete
        ArrayList<String> keywords = new ArrayList<String>(5);
                keywords.add("hakan");
                keywords.add("autocomplete");
                keywords.add("stackabuse");
                keywords.add("java");
        Autocomplete2 autoComplete = new Autocomplete2(mainTextField, keywords);
        mainTextField.getDocument().addDocumentListener(autoComplete);

        // Maps the tab key to the commit action, which finishes the autocomplete
        // when given a suggestion
        mainTextField.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        mainTextField.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setBounds(0,0,1000,1000);
        frame.add(mainTextField);
        mainTextField.setBounds(10, 100, 300, 300);
        frame.setVisible(true);
      }
      
    }
    
    /* regular name */
    public static DocumentListener ProperNoun(JTextField textField) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doHighlight = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String s = textField.getText();
                            String newS = Character.toUpperCase(s.charAt(0)) + "";

                            for(int i = 1; i < s.length(); i++)
                                if(s.charAt(i - 1) == ' ')
                                    newS = newS + Character.toUpperCase(s.charAt(i));
                                else
                                    newS = newS + Character.toLowerCase(s.charAt(i));
                            textField.setText(newS);
                        } catch (Exception e) {}
                    }
                };       
                SwingUtilities.invokeLater(doHighlight);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        };
    }
    
    /* date */
    public static DocumentListener Date(JTextField textField) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Runnable doHighlight = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            switch(textField.getText().length() - 1) {
                                case 0: case 1: case 3: case 4: case 6: case 7: case 8: case 9:
                                    if(!TextualOperation.is(textField, textField.getText().length() - 1, TextualOperation.NUMERICAL))
                                        TextualOperation.delete(textField, 1);
                                    break;
                                case 2: case 5:
                                    if(!TextualOperation.is(textField, textField.getText().length() - 1, '.'))
                                        TextualOperation.delete(textField, 1);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {}
                    }
                };       
                SwingUtilities.invokeLater(doHighlight);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        };
    }
    
    /* only int */
    
    /*
    public void TimeDo(JTextField textField) {
        onlyInt(textField);
        Runnable doHighlight = new Runnable() {
            @Override
            public void run() {
                try {
                    String stTime = textField.getText();
                    if(stTime.length() == 1) {
                        int firstNum = Integer.parseInt(stTime.substring(0, 1));
                        if(firstNum < 0 || firstNum > 2) {
                            textField.setText("");
                        }
                    }
                    else if(stTime.length() == 2) {
                        int hour = Integer.parseInt(stTime.substring(0, 2));
                        if(hour < 0 || hour > 23) {
                            textField.setText(stTime.substring(0, 1));
                        }
                    }
                    else if(stTime.length() == 3 && stTime.charAt(2) != ':') {
                        int firstMin = Integer.parseInt(stTime.substring(2, 3));
                        if(firstMin < 0 || firstMin > 5) {
                            textField.setText(stTime.substring(0, 2));
                        }
                        else
                            textField.setText(stTime.substring(0, 2) + ":" + stTime.charAt(2));
                    }
                    else if(stTime.length() == 4 && stTime.charAt(2) == ':') {
                        int firstMin = Integer.parseInt(stTime.substring(3, 4));
                        if(firstMin < 0 || firstMin > 5) {
                            textField.setText(stTime.substring(0, 3));
                        }
                    }
                    else if(stTime.length() >= 3 && stTime.charAt(2) != ':') {
                        textField.setText(stTime.substring(0,3));
                    }
                    else if(stTime.length() == 5) {
                        int minute = Integer.parseInt(textField.getText().substring(3,5));
                        if(minute < 0 || minute > 59)
                            textField.setText(stTime.substring(0, 4));
                    }
                    else {
                        try {
                            textField.setText(stTime.substring(0, 5));
                        } catch (Exception e) {}
                    }
                    for(int i = 0; i < stTime.length(); i++)
                        if(!(TextualOperation.is(stTime.charAt(i), TextualOperation.NUMERICAL) || (stTime.charAt(i) == ':' && i == 2)))
                            textField.setText(stTime.substring(0, i));
                } catch (Exception e) {}
            }
        };       
        SwingUtilities.invokeLater(doHighlight);
    }
*/
}