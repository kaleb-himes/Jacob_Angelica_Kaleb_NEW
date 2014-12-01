/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import static k_j_a.GUI.m5;
import static k_j_a.GUI.play_again_but;
import wincheck.Winchecker;


/**
 *
 * @author sweetness
 */
public class Minimax_Sim {
        /**
     * @param args the command line arguments
     */
    public static void minimax_sim() {
        Minimax tree = new Minimax();
        tree.turnPruningOn();
        //tree.turnPruningOff();
        tree.train(1);
        tree.prun(true, false);
        
        int d = 2; //depth
        
               //test trained network against random one
        int win = 0;
        for (int i = 0; i < 50; i++) {
            double[] board = new double[12 * 4];
            int player = (Math.random() > .5) ? 1 : 2;
            int player2 = (player == 1) ? 2 : 1;
            do {
                if (player == 1) {
                    board = tree.exploit(player, board, d);
                    board = tree.random(player2, board);
                } else {
                    board = tree.random(player2, board);
                    board = tree.exploit(player, board, d);
                }
            } while (Winchecker.check(board) < 0);
            GUI.game_state_display.append("Winner was player "
                    + "" + Winchecker.check(board) + " ai was " + player +"\n");
            GUI.game_state_display.append("Board State\n");
            int LMSV = 0;
            int index = 0;
            int get_x_y[] = new int[3];
            for (int z = 0; z < 4; z++) {
                for (int h = 0; h < 12; h++) {
                    if (board[index] == 0) {
                         /*LMSV = Legal Move Search Value */
                        if (z == 0) {
                            LMSV = z + h;
                        } else if (z == 1) {
                            LMSV = 1 + h + (12 - 1);
                        } else if (z == 2) {
                            LMSV = 1 + h + (24 - 1);
                        } else if (z == 3) {
                            LMSV = 1 + h + (36 - 1);
                        }
                        get_x_y = GUI.legal_moves[LMSV];
                        int x, y;
                        x = get_x_y[0];
                        y = get_x_y[1];

                        GUI.playerMove(x, y, 1);
                        if (GUI.won == player) {
                            win = Winchecker.aiWins;
                        }
//                        System.out.print(" 0.0");
                    } else {
//                        System.out.print(" " + board[index]);
                    }
                    index++;
                }
//                GUI.game_state_display.append("\n");
            }
            GUI.game_state_display.append("number of nodes searched: " + index + "\n");
        }
        GUI.game_state_display.append("percent won = " + (double) win / 50.0 + "\n");
        /* Activate play again button*/
        play_again_but.setEnabled(true);
        /* And turn on the mouse listeners so user can click it */
        for (int j = 0; j < m5.length; j++) {
            play_again_but.addMouseListener(m5[j]);
        }
        Winchecker.aiWins = 0;
    }
}
