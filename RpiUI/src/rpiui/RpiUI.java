/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;
/**
 *
 * @author KatieHorn
 */
public class RpiUI extends JFrame {

    /**
     * @param args the command line arguments
     */
    static String sample = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, "
            + "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
            + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi"
            + " ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit"
            + " in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur "
            + "sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit "
            + "anim id est laborum.";
    static String quote = "I'm ready - Spongebob Squarepants";
    
    public static void framer(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setUndecorated(true);
        JPanel p = new JPanel(new GridBagLayout()); 
        p.setBackground(Color.black);
        p.setOpaque(true);
        
        
       JTextArea  twitArea = new JTextArea(
                sample, 6, 20);
        twitArea.setFont(new Font("Roman", Font.BOLD, 20));
        twitArea.setLineWrap(true);
        twitArea.setWrapStyleWord(true);
        twitArea.setOpaque(false);
        twitArea.setEditable(false);
        twitArea.setForeground(Color.white);
        GridBagConstraints twitc = new GridBagConstraints();
        twitc.gridy= 0;
        twitc.gridx = 2;
        twitc.gridheight = 2;
        
        JLabel blank = new JLabel("test");
        blank.setForeground(Color.red);
        JLabel blank2 = new JLabel("test");
        blank2.setForeground(Color.red);
        GridBagConstraints topmiddlec = new GridBagConstraints();
        topmiddlec.gridy = 0;
        topmiddlec.gridx = 1;
        topmiddlec.weightx = 60;
        
         JTextArea  calArea = new JTextArea(
                sample, 6, 20);
        calArea.setFont(new Font("Roman", Font.BOLD, 20));
        calArea.setLineWrap(true);
        calArea.setWrapStyleWord(true);
        calArea.setOpaque(false);
        calArea.setEditable(false);
        calArea.setForeground(Color.white);
        GridBagConstraints calc = new GridBagConstraints();
        calc.gridy = 0;
        calc.gridx = 0;
        
        GridBagConstraints row2left = new GridBagConstraints();
        row2left.gridy =1;
        row2left.gridx = 0;
        row2left.weightx = 60;
        row2left.weighty = 30;
        
        GridBagConstraints row2mid = new GridBagConstraints();
         row2mid.gridy =1;
        row2mid.gridx = 1;
        row2mid.weightx = 30;
        
        
        p.add(twitArea, twitc);
        p.add(blank, topmiddlec);
        p.add(calArea, calc);
        p.add(blank2, row2left);
        //p.add(blank,row2mid);
        frame.add(p);
        frame.pack();
 
        
        frame.setVisible(true);
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        framer();
    }
    
}
