/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import java.util.Arrays;

/**
 * Used to find a heuristic value for movement
 * @author sweetness
 */
public class Simple_Heuristic {

    static int offset = 12;

    public Simple_Heuristic() {

    }

    /**
     * Find the heuristic value of the move
     *
     * @param in the move to get the heuristic of
     * @param board the current board state
     * @return double indicating heuristic value
     */
    public double getHeuristic(double[] in, double[] board) {
        int index = -1;
        int i;
        double heur = 0.0;
        double temp = 0.0;
        for (i = 0; i < board.length; i++) {
            if (in[i] != board[i]) {
                index = i;
                break;
            }
        }

        if (index < 0) {
            System.out.println("Error in getting heuristic");
            return 0;
        }

        //case to the right
        i = index;
        temp = 0.0;
        for (int j = 0; j < 3; j++) {
            i = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
            if (board[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }

        //case to the left -- add on to previous heur
        i = index;
        for (int j = 0; j < 3; j++) {
            i = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
            if (board[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }
        if (temp > heur) {
            heur = temp;
        }

        //case of to the left and up
        i = index;
        temp = 0.0;
        for (int j = 0; j < 3; j++) {
            i = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
            i += offset;
            if (i > board.length) {
                break;
            }
            if (board[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }

        //case of to the right and down -- add on to previous heur
        i = index;
        for (int j = 0; j < 3; j++) {
            i = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
            i -= offset;
            if (i < 0) {
                break;
            }
            if (board[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }
        if (temp > heur) {
            heur = temp;
        }

        //case of to the right and up
        i = index;
        temp = 0.0;
        for (int j = 0; j < 3; j++) {
            i = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
            i += offset;
            if (i > board.length) {
                break;
            }
            if (board[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }

        //case of to the left and down -- add on to last heur
        i = index;
        for (int j = 0; j < 3; j++) {
            i = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
            i -= offset;
            if (i < 0) {
                break;
            }
            if (board[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }
        if (temp > heur) {
            heur = temp;
        }

        return heur;

    }

    private double[] best(double[][] all, int player, double[] board) {
        double heur = 0.0;
        int index = 0;

        for (int i = 0; i < all.length; i++) {
            double temp = getHeuristic(all[i], board);
            if (temp > heur) {
                heur = temp;
                index = i;
            }
        }

        return all[index];
    }

    static int max; //used as a counter in possible_helper
    static boolean[] checked;

    //used for recursive search on possible moves
    private void possible_helper(double[][] all, double[] in,
            int i, int player) {
        if (checked[i] || i >= in.length || i < 0) {
            //base case of already being checked or array bounds
            return;
        }

        //base case of possible move
        if (in[i] == 0) {
            if (max > 48) { //@TODO check since more options than possible ...error
                System.out.println("strange error");
                return;
            }
            all[max++][i] = player;
            checked[i] = true;
            return;
        }
        checked[i] = true;

        //case of to the left
        int index = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
        if (index < in.length && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the left and up
        index = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
        index += offset;
        if (index < in.length && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the left and down
        index = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
        index -= offset;
        if (index >= 0 && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the right
        index = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
        if (!checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the right and up
        index = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
        index += offset;
        if (index < in.length) {
            if (!checked[index]) {
                possible_helper(all, in, index, player);
            }
        }

        //case of to the right and down
        index = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
        index -= offset;
        if (index >= 0) {
            if (!checked[index]) {
                possible_helper(all, in, index, player);
            }
        }

        //case of up and case of down
        index = i + offset;
        if (index < in.length && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        index = i - offset;
        if (index >= 0 && !checked[index]) {
            possible_helper(all, in, index, player);
        }
    }

    //find all possible moves. Specific to polar tic tac toe
    private double[][] possible(double[] in, int player) {
        max = offset * 4;
        int full = 0;
        checked = new boolean[in.length];
        double[][] all = new double[max + 1][in.length];
        double[][] ret;

        for (double[] x : all) {
            System.arraycopy(in, 0, x, 0, x.length);
        }

        Arrays.fill(checked, false);
        max = 0;

        for (int i = 0; i < in.length; i++) {
            if (in[i] != 0 && !checked[i]) {
                possible_helper(all, in, i, player);
            }
        }

        //case of empty or full board
        if (max == 0) {
            if (full >= (offset * 4)) {
                return null; //board is full
            }
            System.out.println("Empty board");
            for (int i = 0; i < in.length; i++) {
                all[max++][i] = player;
            }
        }

        ret = new double[max][in.length];
        int i = 0;
        for (double[] current : all) {
            System.arraycopy(current, 0, ret[i++], 0, in.length);
            if (i == max) {
                break;
            }
        }

        return ret;
    }

    /**
     * Exploit the network
     *
     * @param desired the output node desired to win ie player 1 would be 1, tie
     * 0, and player 2 would be 2
     * @param input input vector to network (board state) 0 for empty 1 for
     * player 1 and 2 for player 2
     * @return the best board position
     */
    public double[] exploit(int desired, double[] input) {
        if (input == null) {
            return null;
        }
        double[][] all;

        //find all possible moves
        all = possible(input, desired);

        //evaluate possible moves and choose best
        return best(all, desired, input);
    }
}
