/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import static k_j_a.GUI.legal_moves;
import static k_j_a.GUI.m5;
import static k_j_a.GUI.play_again_but;
import wincheck.Winchecker;

/**
 *
 * @author khimes
 */
public class TDNN_Sim {

    static int games_to_play = 100;
    private static TDNN aiPlayer;

    public static void tdnn_sim() {

        TDNN test = new TDNN(48, 40, 3);
        TDNN ran = new TDNN(48, 40, 3);
        test.train(1000);

        //test trained network against random one
        int win = 0;
        int notLoss = 0;
        for (int i = 0; i < games_to_play; i++) {

            double[] board = new double[12 * 4];
            int player = (Math.random() > .5) ? 1 : 2;
            int player2 = (player == 1) ? 2 : 1;
            do {
                if (player == 1) {
                    board = test.exploit(board, player);
                    if (Winchecker.check(board) > -1) {
                        break;
                    }
                    board = ran.random(board, player2);
                } else {
                    board = ran.random(board, player2);
                    if (Winchecker.check(board) > -1) {
                        break;
                    }
                    board = test.exploit(board, player);
                }
            } while (Winchecker.check(board) < 0);
            GUI.game_state_display.append("Winner was player " + Winchecker.check(board) + " ai was " + player + "\n");
            GUI.game_state_display.append("Board State\n");
            int index = 0;
            int get_x_y[];
            for (int z = 0; z < 4; z++) {
                for (int h = 0; h < 12; h++) {
                    if (board[index] == 0) {
                        get_x_y = GUI.legal_moves[index];
                        int x, y;
                        x = get_x_y[0];
                        y = get_x_y[1];
                        if (get_x_y[2] == 0) {
                            GUI.playerMove(x, y, 1);
                            if (GUI.won == player) {
                                win++;
                                notLoss++;
                                GUI.moves_made = 0;
                            }
                        }
                        /* need to add something for the winchecker here */
                    } else {
                    }
                    index++;
                }
            }
            GUI.game_state_display.append("number of nodes searched: " + index + "\n");
        }
        notLoss += Winchecker.aiNotLoss;
        GUI.game_state_display.append("percent won = " + (double) win / games_to_play + "\n");
        GUI.game_state_display.append("percent not losing = " + (double) notLoss / games_to_play + "\n");
        GUI.game_state_display.append("illegal moves attempted:"
                + "" + GUI.illegal_moves_made + "\n");
        GUI.illegal_moves_made = 0;

        /* Activate play again button*/
        play_again_but.setEnabled(true);
        /* And turn on the mouse listeners so user can click it */
        for (int j = 0; j < m5.length; j++) {
            play_again_but.addMouseListener(m5[j]);
        }
        win = 0;
        notLoss = 0;
    }

    public static void TDNN_move(double[] ai_board, int player) {
        int x;
        int y;
        int legal;
        int get_x_y[];

        double[] temp_board = new double[12 * 4];
        for (int i = 0; i < 12 * 4; i++) {
            if (ai_board[i] == 1.0 || ai_board[i] == 2.0) {
                temp_board[i] = 3.0;
            } else {
                temp_board[i] = 0.0;
            }
        }
        ai_board = aiPlayer.exploit(ai_board, player);

        for (int i = 0; i < 12 * 4; i++) {
            if ((ai_board[i] == 1.0 || ai_board[i] == 2.0) && temp_board[i] == 0.0) {
                get_x_y = GUI.legal_moves[i];
                x = get_x_y[0];
                y = get_x_y[1];
                legal = get_x_y[2];
                if (legal == 0) {
                    GUI.playerMove(x, y, 0);
                    break;
                } else {
                    GUI.game_state_display.append("AI attempted an illegal move.\n");
                }
            }
        }
    }

    public static void newPlayer() {
        aiPlayer = new TDNN(48, 40, 3);
        aiPlayer.train(1000);
    }

}
