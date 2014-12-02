/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sweetness
 */
public class Driver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /* Start up the GUI */
        run();
        /* The AI stuff has been moved to the GUI for private method access */
    }
    public static void run() {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    new GUI().setVisible(true);
                /* store mouselisteners in predefined arrays see variable section */
                GUI.m1 = GUI.ai_v_ai_but.getMouseListeners();
                GUI.m2 = GUI.human_v_human_but.getMouseListeners();
                GUI.m3 = GUI.ai_v_human_but.getMouseListeners();
                GUI.m4 = GUI.human_v_ai_but.getMouseListeners();
                GUI.m5 = GUI.play_again_but.getMouseListeners();
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
