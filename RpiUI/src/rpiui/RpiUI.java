/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpiui;
import javax.swing.*;
import java.awt.*;
import java.awt.GraphicsDevice.*;
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
    
<<<<<<< HEAD
    public static void framer(){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
=======
    private static void framer(){
        JFrame frame = new JFrame("Raspberry Pi App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
>>>>>>> 65fa736e0cbe17570f67a6c58337e746b903b768
        frame.setUndecorated(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(screenSize);
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        gd.setFullScreenWindow(frame);
       
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.black);
        p.setOpaque(true);
        p.setPreferredSize(screenSize);
        
        
       JTextArea  twitArea = new JTextArea(
                sample+sample, 6, 20);
        twitArea.setFont(new Font("Roman", Font.BOLD, 20));
        twitArea.setLineWrap(true);
        twitArea.setWrapStyleWord(true);
        twitArea.setOpaque(false);
        twitArea.setEditable(false);
        twitArea.setForeground(Color.white);
        GridBagConstraints twitc = new GridBagConstraints();
        twitc.gridy= 0;
        twitc.gridx = 2;
        twitc.gridheight = 3;
        
        twitc.fill = GridBagConstraints.VERTICAL;
        twitc.anchor = GridBagConstraints.FIRST_LINE_END;
        
<<<<<<< HEAD
        JLabel blank = new JLabel();
   //     blank.setForeground(Color.red);
       JLabel blank2 = new JLabel();
     //   blank2.setForeground(Color.red);
        JLabel blank3 = new JLabel();
    //    blank3.setForeground(Color.red);
        JLabel blank4 = new JLabel();
      //  blank4.setForeground(Color.red);
        JLabel blank5 = new JLabel();
      //  blank5.setForeground(Color.red);
        JLabel blank6 = new JLabel();
  //      blank6.setForeground(Color.red);
=======
        JLabel blank = new JLabel("testing 1 2 3");
        blank.setForeground(Color.blue);
        JLabel blank2 = new JLabel("test");
        blank2.setForeground(Color.red);
>>>>>>> 65fa736e0cbe17570f67a6c58337e746b903b768
        GridBagConstraints topmiddlec = new GridBagConstraints();
        topmiddlec.gridy = 0;
        topmiddlec.gridx = 1;
        topmiddlec.weightx = 60;
        topmiddlec.ipadx = 45;
        
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
        calc.weightx = 30;
        calc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        GridBagConstraints row2left = new GridBagConstraints();
        row2left.gridy =1;
        row2left.gridx = 0;
        row2left.weightx = 60;
        row2left.weighty = 30;
        
        GridBagConstraints row2mid = new GridBagConstraints();
         row2mid.gridy =1;
        row2mid.gridx = 1;
        row2mid.weightx = 60;
        row2mid.weighty = 30;
        
        JTextArea  weatArea = new JTextArea(
                sample, 6, 20);
        weatArea.setFont(new Font("Roman", Font.BOLD, 20));
        weatArea.setLineWrap(true);
        weatArea.setWrapStyleWord(true);
        weatArea.setOpaque(false);
        weatArea.setEditable(false);
        weatArea.setForeground(Color.white);
        GridBagConstraints weatc = new GridBagConstraints();
        weatc.gridy= 2;
        weatc.gridx = 0;
        weatc.gridheight = 2;
        weatc.fill = GridBagConstraints.VERTICAL;
        weatc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        GridBagConstraints row3mid = new GridBagConstraints();
        row3mid.gridx =1;
        row3mid.gridy = 2;
        
        GridBagConstraints row3r = new GridBagConstraints();
        row3r.gridx =2;
        row3r.gridy = 2;
        
        
        JTextArea  quoteArea = new JTextArea(
                quote, 6, 20);
        quoteArea.setFont(new Font("Roman", Font.BOLD, 20));
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setOpaque(false);
        quoteArea.setEditable(false);
        quoteArea.setForeground(Color.white);
        GridBagConstraints quotec = new GridBagConstraints();
        quotec.gridx = 1;
        quotec.gridy = 3;
        quotec.anchor = GridBagConstraints.PAGE_END;
        
        GridBagConstraints lastblank = new GridBagConstraints();
        lastblank.gridx = 2;
        lastblank.gridy = 3;
        
        
        
        p.add(twitArea, twitc);
        p.add(blank, topmiddlec);
        p.add(calArea, calc);
        p.add(blank2, row2left);
        p.add(blank3,row2mid);
        p.add(weatArea, weatc);
        p.add(blank4, row3mid);
        p.add(blank5, row3r);
        p.add(quoteArea, quotec);
        frame.add(p);
        
        frame.pack();
        
        frame.setVisible(true);
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                framer();
            }
        });
    }
    
}
