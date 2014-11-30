/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import static k_j_a.GUI.game_board_panel;
import static k_j_a.GUI.game_state_display;
import static k_j_a.GUI.paint_board;
import static k_j_a.GUI.set_all_non_focusable;

/**
 *
 * @author Kaleb
 */
public class Human_AI_Options extends JFrame {

    /**
     * Creates new form Human_AI_Options
     */
    private JButton normalAI_but;
    private JButton tdnn_but;
    private JButton minimax_but;
    private JLabel jLabel1;
    public static JLabel jLabel2; 
    
    public Human_AI_Options() {
        initComponents();
    }

    @SuppressWarnings("unchecked")                       
    private void initComponents() {

        jLabel1 = new JLabel();
        normalAI_but = new JButton();
        tdnn_but = new JButton();
        minimax_but = new JButton();
        jLabel2 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Which AI would you like to play against?");

        normalAI_but.setText("Normal AI");
        normalAI_but.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                normalAI_butMouseClicked(evt);
            }
        });

        tdnn_but.setText("TDNN");
        tdnn_but.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tdnn_butMouseClicked(evt);
            }
        });

        minimax_but.setText("Minimax");
        minimax_but.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                minimax_butMouseClicked(evt);
            }
        });

        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(normalAI_but)
                        .addGap(18, 18, 18)
                        .addComponent(tdnn_but)
                        .addGap(18, 18, 18)
                        .addComponent(minimax_but))
                .addComponent(jLabel2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(normalAI_but)
                                .addComponent(tdnn_but)
                                .addComponent(minimax_but))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }                       

    private void normalAI_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        System.out.println("Human can not play AI yet.");
        paint_board(game_board_panel.getGraphics());
        game_state_display.setText("");
        set_all_non_focusable();
    }

    private void tdnn_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        System.out.println("Human can not play AI yet.");
        paint_board(game_board_panel.getGraphics());
        game_state_display.setText("");
        set_all_non_focusable();
    }

    private void minimax_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        System.out.println("Human can not play AI yet.");
        paint_board(game_board_panel.getGraphics());
        game_state_display.setText("");
        set_all_non_focusable();
    }

    public static void human_v_ai_options() {
        new Human_AI_Options().setVisible(true);
    }                
}
