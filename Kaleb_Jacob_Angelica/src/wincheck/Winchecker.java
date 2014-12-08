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
import static k_j_a.GUI.legal_moves;
import static k_j_a.GUI.m5;
import static k_j_a.GUI.paint_board;
import static k_j_a.GUI.play_again_but;

/**
 *
 * @author sweetness
 */
public class Winchecker {

    static int offset = 12;
    static int left_check = 0;
    static int right_check = 0;
    static int up_check = 0;
    static int down_check = 0;
    static int up_left_check = 0;
    static int down_right_check = 0;
    static int up_right_check = 0;
    static int down_left_check = 0;
    static int connected = 0;
    static int[][] board = GUI.legal_moves;
    static protected int check_node = 0;

    public static int aiWins = 0;
    public static int aiNotLoss = 0;

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
        int winner = 0;
        if (GUI.moves_made == 48) {
            GUI.moves_made = 0;
            for (int i = 0; i < legal_moves.length; i++) {
                legal_moves[i][2] = 0;
            }
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
//                            Thread.sleep(20000); //1000 is one second.
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
            aiNotLoss++;
            return winner; /* tie game, no winner */

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
            for (int i = 0; i < legal_moves.length; i++) {
                legal_moves[i][2] = 0;
                board[i][2] = 0;
            }
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
            for (int i = 0; i < legal_moves.length; i++) {
                legal_moves[i][2] = 0;
                board[i][2] = 0;
            }
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
//        System.out.print("[");
//        for (int i = 36; i <= 47; i++) {
//            System.out.print(" " + board[i][2] + ", ");
//        }
//        System.out.print("]\n");
//        System.out.print("[");
//        for (int i = 24; i <= 35; i++) {
//            System.out.print(" " + board[i][2] + ", ");
//        }
//        System.out.print("]\n");
//        System.out.print("[");
//        for (int i = 12; i <= 23; i++) {
//            System.out.print(" " + board[i][2] + ", ");
//        }
//        System.out.print("]\n");
//        System.out.print("[");
//        for (int i = 0; i <= 11; i++) {
//            System.out.print(" " + board[i][2] + ", ");
//        }
//        System.out.println("]\n");
            for (int i = 0; i < legal_moves.length; i++) {
                legal_moves[i][2] = 0;
                board[i][2] = 0;
            }
            /* uncomment if you want to visually verify wins 
             * tested thouroughly, they work.
             */
//            try {
//                Thread.sleep(20000); //1000 milliseconds is one second.
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
            if (winner == 1) {
                aiWins++;
                aiNotLoss++;
            }
            for (int k = 0; k < GUI.legal_moves.length; k++) {
                GUI.legal_moves[k][2] = 0;
            }
            Graphics g = game_board_panel.getGraphics();
            game_board_panel.paint(g);
            g.setColor(Color.LIGHT_GRAY);
            game_board_panel.repaint();
            paint_board(game_board_panel.getGraphics());
            GUI.game_state_display.append("moves made this game: " + GUI.moves_made + "\n");
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
        check_node = node;
        int upper = 0, lower = 0;
        /*
         * check left and right
         */
        if (check_node <= 11) {
            upper = 11;
            lower = 0;
        } else if (check_node > 11 && check_node <= 23) {
            upper = 23;
            lower = 12;
        } else if (check_node > 23 && check_node <= 35) {
            upper = 35;
            lower = 24;
        } else {
            upper = 47;
            lower = 36;
        }
        if (check_node - 1 >= 0 && check_node - 1 >= lower) {
            if (board[check_node - 1][2] == player) {
                left_check += check_left(check_node, player, upper, lower);
                right_check += check_right(check_node, player, upper, lower);
            }
        }
        if (check_node == a1 || check_node == a2 || check_node == a3 || check_node == a4) {
            left_check += check_left(check_node, player, upper, lower);
            right_check += check_right(check_node, player, upper, lower);
        }
        /*
         * if win_check reaches 6 (3 connected on either side)
         * we have win in a straight line
         * otherwise reset win_check to zero and proceed to check 
         * diagonals.
         */
        if (left_check == 3 || right_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            left_check = 0;
            right_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked left and right, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            left_check = 0;
            right_check = 0;
            connected = 0;
        }
        if (check_node + 1 < 48 && check_node + 1 <= upper) {
            if (board[check_node + 1][2] == player && board[check_node + 1][3] == 0) {
                right_check += check_right(check_node, player, upper, lower);
                left_check += check_left(check_node, player, upper, lower);
            }
        }
        if (check_node == l1 | check_node == l2 || check_node == l3 || check_node == l4) {
            right_check += check_right(check_node, player, upper, lower);
            left_check += check_left(check_node, player, upper, lower);
        }
        /*
         * if win_check reaches 6 we have win in a straight line
         * otherwise reset win_check to zero and proceed to check 
         * diagonals.
         */
        if (right_check == 3 || left_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            left_check = 0;
            right_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked left and right, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            left_check = 0;
            right_check = 0;
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
        if (check_node < 36) {
            if (board[check_node + 12][2] == player) {
                up_check += check_up(check_node, player);
                if (check_node > 11) {
                    down_check += check_down(check_node, player);
                }
            }
        }
        if (down_check == 3 || up_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_check = 0;
            up_check = 0;
            connected = 0;
            return player;
        } else {
            down_check = 0;
            up_check = 0;
            /* checked up and down, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_check = 0;
            up_check = 0;
            connected = 0;
        }
        if (check_node > 11) {
            if (board[check_node - 12][2] == player) {
                down_check += check_down(check_node, player);
                if (check_node < 36) {
                    up_check += check_up(check_node, player);
                }
            }
        }
        if (down_check == 3 || up_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_check = 0;
            up_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked up and down, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_check = 0;
            up_check = 0;
            connected = 0;
        }
        /*
         * check diagonal left up (check_node + 11)
         * if not row 4
         * and not off the board
         * 
         * check diagonal right down (check_node - 11)
         * if not row 1
         * and not off the board
         */
        if (check_node < 36) {
            if (board[check_node + 11][2] == player) {
                up_left_check += check_lup(check_node, player);
                if (check_node > 11) {
                    down_right_check += check_rdown(check_node, player);
                }
            }
        }
        if (check_node == a1 || check_node == a2 || check_node == a3) {
            if (board[check_node + 23][2] == player) {
                up_left_check += check_lup(check_node, player);
                down_right_check += check_rdown(check_node, player);
            }
        }
        if (up_left_check == 3 || down_left_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            up_left_check = 0;
            down_right_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked left-up and right-down, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            up_left_check = 0;
            down_right_check = 0;
            connected = 0;
        }

        if (check_node > 11) {
            if (board[check_node - 11][2] == player) {
                down_right_check += check_rdown(check_node, player);
                if (check_node < 36) {
                    up_left_check += check_lup(check_node, player);
                }
            }
        }
        if (check_node == l4 || check_node == l3 || check_node == l2) {
            if (board[check_node - 23][2] == player) {
                down_right_check += check_rdown(check_node, player);
                up_left_check += check_lup(check_node, player);
            }
        }
        if (down_right_check == 3 || up_left_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_right_check = 0;
            up_left_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked left-up and right-down, reset neighbor flags */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_right_check = 0;
            up_left_check = 0;
            connected = 0;
        }
        /*
         * check diagonal right up (check_node + 13)
         * if not row 4
         * and not off the board
         *
         * check diagonal left down (check_node - 13)
         * if not row 4 (a4-l4) = (36 - 47)
         */
        if (check_node < 35) {
            if (board[check_node + 13][2] == player) {
                up_right_check += check_rup(check_node, player);
                if (check_node > 12) {
                    down_left_check += check_ldown(check_node, player);
                }
            }
        }
        if (check_node == l3 || check_node == l2 || check_node == l1) {
            if (board[check_node + 1][2] == player) {
                up_right_check += check_rup(check_node, player);
                down_left_check += check_ldown(check_node, player);
            }
        }
//        
//        System.out.println("l3 = " + l3+" contains "+board[l3][2]+""
//                + " compared to: " + check_node + " contains " + board[check_node+1][2]);
        if (up_right_check == 3 || down_left_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            up_right_check = 0;
            down_left_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked right-up and left-down, 
             * reset neighbor flags before return 
             */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            up_right_check = 0;
            down_left_check = 0;
            connected = 0;
        }
        if (check_node > 12) {
            if (board[check_node - 13][2] == player) {
                down_left_check += check_ldown(check_node, player);
                if (check_node < 35) {
                    up_right_check += check_rup(check_node, player);
                }
            }
        }
        if (check_node == a2 || check_node == a3 || check_node == a4) {
            if (board[check_node - 1][2] == player) {
                down_left_check += check_ldown(check_node, player);
                up_right_check += check_rup(check_node, player);
            }
        }
        if (down_left_check == 3 || up_right_check == 3) {
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_left_check = 0;
            up_right_check = 0;
            connected = 0;
            return player;
        } else {
            /* checked right-up and left-down, 
             * reset neighbor flags before return 
             */
            for (int i = 0; i < board.length; i++) {
                board[i][3] = 0;
            }
            down_left_check = 0;
            up_right_check = 0;
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
        int rdown;
        if (node == l2) {
            rdown = l2_drn;
        } else if (node == l3) {
            rdown = l3_drn;
        } else if (node == l4) {
            rdown = l4_drn;
        } else {
            rdown = node - 11;
        }
        if (rdown >= 0) {
            if (board[rdown][2] == player && board[rdown][3] == 0) {
                board[rdown][3] = 1;
                connected += 1;
                if (rdown > 11) {
                    check_rdown(rdown, player);
                }
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
        int ldown;
        if (node == a2) {
            ldown = a2_dln;
        } else if (node == a3) {
            ldown = a3_dln;
        } else if (node == a4) {
            ldown = a4_dln;
        } else {
            ldown = node - 13;
        }
        if (ldown >= 0) {
            if (board[ldown][2] == player && board[ldown][3] == 0) {
                board[ldown][3] = 1;
                connected += 1;
                if (ldown > 11) {
                    check_ldown(ldown, player);
                }
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
        int rup;
        if (node == l1) {
            rup = l1_urn;
        }
        if (node == l2) {
            rup = l2_urn;
        }
        if (node == l3) {
            rup = l3_urn;
        } else {
            rup = node + 13;
        }
        if (rup < 48) {
            if (board[rup][2] == player && board[rup][3] == 0) {
                board[rup][3] = 1;
                connected += 1;
                if (rup < 36) {
                    check_rup(rup, player);
                }
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
        int lup;
        if (node == a1) {
            lup = a1_uln;
        } else if (node == a2) {
            lup = a2_uln;
        } else if (node == a3) {
            lup = a3_uln;
        } else {
            lup = node + 11;
        }
        if (lup < 48) {
            if (board[lup][2] == player && board[lup][3] == 0) {
                board[lup][3] = 1;
                connected += 1;
                if (lup < 36) {
                    check_lup(lup, player);
                }
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
