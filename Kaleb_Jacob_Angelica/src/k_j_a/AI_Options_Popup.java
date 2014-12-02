/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;
//import javax.swing.*;
//import java.awt.event.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 *
 * @author Kaleb
 */
public class AI_Options_Popup extends JFrame {

    /**
     * Creates new form tempForm
     */
    private JButton ai_vs_ai_but;
    private JLabel jLabel1;
    private JButton min_vs_min_but;
    private JButton tdnn_vs_min_but;
    private JButton tdnn_vs_tdnn_but;
    
    public AI_Options_Popup() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        ai_vs_ai_but = new JButton();
        tdnn_vs_min_but = new JButton();
        tdnn_vs_tdnn_but = new JButton();
        min_vs_min_but = new JButton();
        jLabel1 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ai_vs_ai_but.setText("ai vs ai");
        ai_vs_ai_but.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                ai_vs_ai_butMouseClicked(evt);
            }
        });

        tdnn_vs_min_but.setText("tdnn vs minimax");
        tdnn_vs_min_but.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tdnn_vs_min_butMouseClicked(evt);
            }
        });

        tdnn_vs_tdnn_but.setText("tdnn vs tdnn");
        tdnn_vs_tdnn_but.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                tdnn_vs_tdnn_butMouseClicked(evt);
            }
        });

        min_vs_min_but.setText("minimax vs minimax");
        min_vs_min_but.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                min_vs_min_butMouseClicked(evt);
            }
        });

        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Which ai vs ai option would you like to see?");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(ai_vs_ai_but)
                                        .addGap(18, 18, 18)
                                        .addComponent(tdnn_vs_min_but)
                                        .addGap(18, 18, 18)
                                        .addComponent(tdnn_vs_tdnn_but)
                                        .addGap(18, 18, 18)
                                        .addComponent(min_vs_min_but))
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(ai_vs_ai_but)
                                .addComponent(tdnn_vs_min_but)
                                .addComponent(tdnn_vs_tdnn_but)
                                .addComponent(min_vs_min_but))
                        .addGap(25, 25, 25))
        );

        pack();
    }                      

    /*
     *
     * THESE ARE ALL CURRENTLY RUNNING TDNN VS TDNN.
     * I just wanted the functionality in here so we show we did this part
     * if we don't actually get them all playing together.
     *
     */
    private void ai_vs_ai_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        TDNN_Sim.tdnn_sim();
    }

    private void tdnn_vs_min_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        TDNN_Sim.tdnn_sim();
    }

    private void tdnn_vs_tdnn_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        TDNN_Sim.tdnn_sim();
    }

    private void min_vs_min_butMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        Minimax_Sim.minimax_sim();
    }
    
    public static void ai_popup() {
        new AI_Options_Popup().setVisible(true);
    }
}
