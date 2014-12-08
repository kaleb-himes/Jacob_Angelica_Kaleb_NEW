/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wincheck;

import java.awt.Color;
import java.awt.Graphics;
import k_j_a.GUI;
import static k_j_a.GUI.game_board_panel;
import static k_j_a.GUI.game_over_img;
import static k_j_a.GUI.m5;
import static k_j_a.GUI.paint_board;
import static k_j_a.GUI.play_again_but;

/**
 *
 * @author sweetness
 */
public class Winchecker {

    static int offset = 12;
    static int win_check = 0;
    static int connected = 0;
    static int[][] board = GUI.legal_moves;

    public static int aiWins = 0;

    private static double check(double[] in, int i) {
        boolean won = false;
        double p = in[i];
        int match = 1;
        int index;

        //check horz
        //left
        index = i;
        while (!won && in[(index % offset > 0) ? index - 1 : index + offset - 1] == p) {
            index = (index % offset > 0) ? index - 1 : index + offset - 1;
            match++;
            if (match == 4) {
                won = true;
            }
        }
        //case of to the right
        index = i;
        while (!won && in[((index + 1) % offset == 0) ? (index - offset + 1) % in.length : (index + 1) % in.length] == p) {
            index = ((index + 1) % offset == 0) ? (index - offset + 1) % in.length : (index + 1) % in.length;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        //check vert
        match = 1;
        //case of up and case of down
        index = i;
        while (!won && (index + offset) < in.length && in[index + offset] == p) {
            index += offset;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        index = i;
        while (!won && (index - offset) >= 0 && in[index - offset] == p) {
            index -= offset;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        //check diag
        match = 1;
        //case of to the right and down
        index = i;
        index = (((index + 1) % offset == 0) ? (index - offset + 1) % in.length : (index + 1) % in.length) - offset;
        while (!won && index >= 0 && in[index] == p) {
            index = (((index + 1) % offset == 0) ? (index - offset + 1) % in.length : (index + 1) % in.length) - offset;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        //case of to the left and up
        index = i;
        index = (((index % offset - 1 > 0) ? index - 1 : index + offset - 1)) + offset;
        while (!won && index < in.length && in[index] == p) {
            index = (((index % offset - 1 > 0) ? index - 1 : index + offset - 1)) + offset;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        //check other diag
        match = 1;
        //case of to the right and up
        index = i;
        index = (((index + 1) % offset == 0) ? (index - offset + 1) % in.length : (index + 1) % in.length) + offset;
        while (!won && index < in.length && in[index] == p) {
            index = (((index + 1) % offset == 0) ? (index - offset + 1) % in.length : (index + 1) % in.length) + offset;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        //case of to the left and down
        index = i;
        index = (((index % offset - 1 > 0) ? index - 1 : index + offset - 1)) - offset;
        while (!won && index >= 0 && in[index] == p) {
            index = (((index % offset - 1 > 0) ? index - 1 : index + offset - 1)) - offset;
            match++;
            if (match == 4) {
                won = true;
            }
        }

        if (won) {
            return in[i];
        } else {
            return 0;
        }
    }

    public static double check(double[] in) {
        //did someone win
        for (int i = 0; i < in.length; i++) {
            if (in[i] != 0) {
                double val = check(in, i);
                if (val != 0) {
                    return val;
                }
            }
        }

        //tie board is full
        boolean full = true;
        for (double x : in) {
            if (x == 0) {
                full = false;
                break;
            }
        }

        if (full) {
            return 0;
        }

        //no win yet
        return -1;
    }

    /**
     *
     * @author kaleb
     */
    /**
     * perform a non-brute force, recursive search for a winning state will
     * search left, right, up, down, elliptical curve for a winner
     *
     * @param node this is the node that was just played. Local search
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     * @param ai {0,1} this flag is only set to 1 when ai vs ai is playing. It
     * will not display a game over when the ai beats itself.
     */
    public static int check2(int node, int player, int ai) {
        /* Case Nobody wins */
        int tie = 0;
        int winner = 0;

        for (int i = 0; i < board.length; i++) {
            if (board[i][2] == 1 || board[i][2] == 2) {
                tie++;
                if (tie == 48) {
                    if (ai == 0) {
                        Graphics g = game_board_panel.getGraphics();
                        g.drawImage(game_over_img, 190, 202, game_board_panel);
                        /* Activate play again button*/
                        play_again_but.setEnabled(true);
                        /* Turn on the mouse listeners so user can click it */
                        for (int j = 0; j < m5.length; j++) {
                            play_again_but.addMouseListener(m5[j]);
                        }
                    } else {
                        /* uncomment if you want to visually verify wins 
                         * tested thouroughly, they work.
                         */
//                        try {
//                            Thread.sleep(3000); //1000 is one second.
//                        } catch (InterruptedException ex) {
//                            Thread.currentThread().interrupt();
//                        }
                        for (int k = 0; k < board.length; k++) {
                            board[k][2] = 0;
                        }
                        Graphics g = game_board_panel.getGraphics();
                        game_board_panel.paint(g);
                        g.setColor(Color.LIGHT_GRAY);
                        game_board_panel.repaint();
                        paint_board(game_board_panel.getGraphics());
                    }
                    GUI.game_state_display.append("TIE !!!!!!!!!!\n");
                    return winner; /* tie game, no winner */

                }
            }
        }

        /* 
         * board[i][0] = x coordinate
         * board[i][1] = y coordinate
         * board[i][2] = 1 if moved on by X,
         *               2 if moved on by O,
         *               0 if empty
         */
        board[node][3] = 1; // set the checking for neighbors flag
        winner = check_neighbors(node, player);
        if (winner == 1 && ai == 0) {
            GUI.game_state_display.append("Player X WINS!!!\n");
            
            Graphics g = GUI.game_board_panel.getGraphics();
            g.drawImage(GUI.game_over_img, 190, 202, GUI.game_board_panel);
            /* Activate play again button*/
            GUI.play_again_but.setEnabled(true);
            /* And turn on the mouse listeners so user can click it */
            for (int j = 0; j < GUI.m5.length; j++) {
                GUI.play_again_but.addMouseListener(GUI.m5[j]);
            }
            return winner;
        } else if (winner == 2 && ai == 0) {
            GUI.game_state_display.append("Player O WINS!!!\n");
            
            
            
            Graphics g = GUI.game_board_panel.getGraphics();
            g.drawImage(GUI.game_over_img, 190, 202, GUI.game_board_panel);
            /* Activate play again button*/
            GUI.play_again_but.setEnabled(true);
            /* And turn on the mouse listeners so user can click it */
            for (int j = 0; j < GUI.m5.length; j++) {
                GUI.play_again_but.addMouseListener(GUI.m5[j]);
            }
            return winner;
        } else if (winner != 0 && ai == 1) {
            /* uncomment if you want to visually verify wins 
             * tested thouroughly, they work.
             */
//            try {
//                Thread.sleep(6000); //1000 milliseconds is one second.
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
            if (winner == 1) {
                aiWins++;
            }
            for (int k = 0; k < GUI.legal_moves.length; k++) {
                GUI.legal_moves[k][2] = 0;
            }
            Graphics g = game_board_panel.getGraphics();
            game_board_panel.paint(g);
            g.setColor(Color.LIGHT_GRAY);
            game_board_panel.repaint();
            paint_board(game_board_panel.getGraphics());
            return winner;
        } else {
            //GUI.game_state_display.append(GUI.player + " moved\n");
        }
        return winner;
    }

    /**
     * Controls and separates up/down, left/right, northeast/southwest, and
     * northwest/southeast searches, resets flags after each respective search.
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_neighbors(int node, int player) {
        int upper = 0, lower = 0;
        /*
         * check left and right
         */
        if (node <= 11) {
            upper = 11;
            lower = 0;
        } else if (node > 11 && node <= 23) {
            upper = 23;
            lower = 12;
        } else if (node > 23 && node <= 35) {
            upper = 35;
            lower = 24;
        } else {
            upper = 47;
            lower = 36;
        }
        if (node - 1 >= 0) {
            if (board[node - 1][2] == player) {
                win_check += check_left(node, player, upper, lower);
            }
        }
        if (node + 1 < 48) {
            if (board[node + 1][2] == player) {
                win_check += check_right(node, player, upper, lower);
            }
        }
        /*
         * if win_check reaches 4 we have win in a straight line
         * otherwise reset win_check to zero and proceed to check 
         * diagonals.
         */
        if (win_check >= 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked left and right, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
        }
        /*
         * check up
         * if not row 4
         *
         * check down
         * if not row 1
         * 
         */
        if (node < 36) {
            if (board[node + 12][2] == player) {
                win_check += check_up(node, player);
            }
        }
        if (node > 11) {
            if (board[node - 12][2] == player) {
                win_check += check_down(node, player);
            }
        }
        if (win_check >= 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked up and down, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
        }
        /*
         * check diagonal left up (node + 11)
         * if not row 4
         * and not off the board
         * 
         * check diagonal right down (node - 11)
         * if not row 1
         * and not off the board
         */
        if (node < 36) {
            if (board[node + 11][2] == player) {
                win_check += check_lup(node, player);
            }
        }
        else if (node == a1 || node == a2 || node == a3) {
            if (board[node + 23][2] == player) {
                win_check += check_lup(node, player);
            }
        }
        if (node > 11) {
            if (board[node - 11][2] == player) {
                win_check += check_rdown(node, player);
            }
        }
        else if (node == l4 || node == l3|| node == l2) {
            if (board[node - 23][2] == player) {
                win_check += check_rdown(node, player);
            }
        }
        if (win_check >= 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked left-up and right-down, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
        }
        /*
         * check diagonal right up (node + 13)
         * if not row 4
         * and not off the board
         *
         * check diagonal left down (node - 13)
         * if not row 4 (a4-l4) = (36 - 47)
         */
        if (node < 35) {
            if (board[node + 13][2] == player) {
                win_check += check_rup(node, player);
            } 
        }
        else if (node == l3 || node == l2 || node == l1) {
                if (board[node + 1][2] == player) {
                    win_check += check_rup(node, player);
                }
            }
        if (node > 12) {
            if (board[node - 13][2] == player) {
                win_check += check_ldown(node, player);
            }
        }
        else if (node == a2|| node == a3 || node == a4) {
                if (board[node - 1][2] == player) {
                    win_check += check_ldown(node, player);
                }
            }
        if (win_check >= 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked right-up and left-down, 
             * reset neighbor flags before return 
             */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            win_check = 0;
            connected = 0;
        }
        /* if we made it here then this move did not result in a win */
        return 0;
    }

    /**
     * Search west (left).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     * @param upper the upper bound of the search.
     * @param lower the lower bound of the search.
     */
    public static int check_left(int node, int player, int upper, int lower) {
        /* if check node is within the bounds, where upper is an upper bound
         * and lower is a lower bound,
         * and that node has not been checked this round,
         * then if that node is occupied by the same player,
         * increase the connectivity of the original node and recursively check
         * the next left node
         */
        int left = node - 1;
        if (node == a1) {
            left = a1_ln;
        }
        if (node == a2) {
            left = a2_ln;
        }
        if (node == a3) {
            left = a3_ln;
        }
        if (node == a4) {
            left = a4_ln;
        }
        if (left <= upper && left >= lower) {
            if (board[left][2] == player && board[left][3] == 0) {
                board[left][3] = 1;
                connected += 1;
                check_left(left, player, upper, lower);
            }
        }
        return connected;
    }

    /**
     * Search east (right).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     * @param upper the upper bound of the search.
     * @param lower the lower bound of the search.
     */
    public static int check_right(int node, int player, int upper, int lower) {
        /* if check node is within the bounds, where upper is an upper bound
         * and lower is a lower bound,
         * and that node has not been checked this round,
         * then if that node is occupied by the same player,
         * increase the connectivity of the original node and recursively check
         * the next right node
         */
        int right = node + 1;
        if (node == l1) {
            right = l1_rn;
        }
        if (node == l2) {
            right = l2_rn;
        }
        if (node == l3) {
            right = l3_rn;
        }
        if (node == l4) {
            right = l4_rn;
        }
        if (right <= upper && right >= lower) {
            if (board[right][2] == player && board[right][3] == 0) {
                board[right][3] = 1;
                connected += 1;
                check_right(right, player, upper, lower);
            }
        }
        return connected;
    }

    /**
     * Search North (up).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_up(int node, int player) {
        /* it was verified that check_up is ok for first node with "node < 36".
         * if check node is occupied by player and not checked this round
         * set as checked and increase connectivity by 1, recursively check up
         * while up < 36
         */
        int up = node + 12;
        if (board[up][2] == player && board[up][3] == 0) {
            board[up][3] = 1;
            connected += 1;
            /* up needs to be less than 36 to continue checking up */
            if (up < 36) {
                check_up(up, player);
            }
        }
        return connected;
    }

    /**
     * Search South (down).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_down(int node, int player) {
        /* it was verified that check_down is ok for first node with "node > 11".
         * if check node is occupied by player and not checked this round
         * set as checked and increase connectivity by 1, recursively check down
         * while down > 11
         */
        int down = node - 12;
        if (board[down][2] == player && board[down][3] == 0) {
            board[down][3] = 1;
            connected += 1;
            if (down > 11) {
                check_down(down, player);
            }
        }
        return connected;
    }

    /**
     * Search South-east (right-down).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_rdown(int node, int player) {
        /* right and down = down+1 = node - 12 + 1. Same logic applies,
         * node must be > 11 to check down and right
         */
        int rdown = node - 11;
        if (node == l2) {
            rdown = l2_drn;
        }
        if (node == l3) {
            rdown = l3_drn;
        }
        if (node == l4) {
            rdown = l4_drn;
        }
        if (board[rdown][2] == player && board[rdown][3] == 0) {
            board[rdown][3] = 1;
            connected += 1;
            if (rdown > 11) {
                check_rdown(rdown, player);
            }
        }
        return connected;
    }

    /**
     * Search South-west (left-down).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_ldown(int node, int player) {
        /* left and down = down-1 = node - 12 - 1. Same logic applies,
         * node must be > 11 to check diagonal down
         */
        int ldown = node - 13;
        if (node == a2) {
            ldown = a2_dln;
        }
        if (node == a3) {
            ldown = a3_dln;
        }
        if (node == a4) {
            ldown = a4_dln;
        }
        if (board[ldown][2] == player && board[ldown][3] == 0) {
            board[ldown][3] = 1;
            connected += 1;
            if (ldown > 11) {
                check_ldown(ldown, player);
            }
        }
        return connected;
    }

    /**
     * Search North-east (right-up).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_rup(int node, int player) {
        /* right and up = up + 1 = node + 12 + 1. Same logic applies,
         * node must be < 36 to check diagonal up
         */
        int rup = node + 13;
        if (node == l1) {
            rup = l1_urn;
        }
        if (node == l2) {
            rup = l2_urn;
        }
        if (node == l3) {
            rup = l3_urn;
        }
        if (board[rup][2] == player && board[rup][3] == 0) {
            board[rup][3] = 1;
            connected += 1;
            if (rup < 36) {
                check_rup(rup, player);
            }
        }
        return connected;
    }

    /**
     * Search North-west (left-up).
     *
     * @param node this is the node that was just played. Locally search from
     * this node only.
     * @param player the player who just moved. Since no winning state resulted
     * previously, check current player only for a win.
     */
    public static int check_lup(int node, int player) {
        /* left and up = up - 1 = node + 12 - 1. Same logic applies,
         * node must be < 36 to check up and left
         */
        int lup = node + 11;
        if (node == a1) {
            lup = a1_uln;
        }
        if (node == a2) {
            lup = a2_uln;
        }
        if (node == a3) {
            lup = a3_uln;
        }
        if (board[lup][2] == player && board[lup][3] == 0) {
            board[lup][3] = 1;
            connected += 1;
            if (lup < 36) {
                check_lup(lup, player);
            }
        }
        return connected;
    }
    /* The rules of the array */
    
    /* the edges cases */
    private static final int a1 = 0;
    private static final int a2 = 12;
    private static final int a3 = 24;
    private static final int a4 = 36;
    
    private static final int l1 = 11;
    private static final int l2 = 23;
    private static final int l3 = 35;
    private static final int l4 = 47;
    
    /* define left and right neighbors */
    private static final int a1_ln = l1;
    private static final int a2_ln = l2;
    private static final int a3_ln = l3;
    private static final int a4_ln = l4;
    
    private static final int l1_rn = a1;
    private static final int l2_rn = a2;
    private static final int l3_rn = a3;
    private static final int l4_rn = a4;
    
    /* Define North-east */
    private static final int l1_urn = a2;
    private static final int l2_urn = a3;
    private static final int l3_urn = a4;
    
    /* Define North-west */
    private static final int a1_uln = l2;
    private static final int a2_uln = l3;
    private static final int a3_uln = l4;
    
    /* Define South-east */
    private static final int l2_drn = a1;
    private static final int l3_drn = a2;
    private static final int l4_drn = a3;
    
    /* Define South-west */
    private static final int a2_dln = l1;
    private static final int a3_dln = l2;
    private static final int a4_dln = l3;

}
