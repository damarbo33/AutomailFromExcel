/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package correo2;

import javax.swing.JFrame;

/**
 *
 * @author dmarcobo
 */
public class Correo2 {
    
    public Correo2() {
        initComponents();
    }

    
    private void initComponents() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame settings like size, close operation etc.
        NewJPanel panel = new NewJPanel();
        // init textfields and buttons
        // add listeners or whatever
        // layout settings goes here
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Correo2();
            }
         
           
        });
    }
}
