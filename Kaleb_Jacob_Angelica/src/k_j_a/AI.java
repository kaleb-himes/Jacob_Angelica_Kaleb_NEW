/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import java.util.Arrays;

/**
 *
 * @author sweetness
 */
public abstract class AI {

    static int offset = 12;
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
        checked[i] = true;
        if (in[i] == 0) {
            all[max++][i] = player;
            return;
        }

        //case of to the left
        int index = ((i % offset - 1 >= 0) ? i - 1 : i + offset - 1);
        if (index < in.length && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the left and up
        index = (((i % offset) - 1 >= 0) ? i - 1 : i + offset - 1);
        index -= offset;
        if (index < in.length && index >= 0 && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the left and down
        index = ((i % offset - 1 >= 0) ? i - 1 : i + offset - 1);
        index += offset;
        if (index >= 0 && index < in.length && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the right
        index = ((i % offset) + 1 >= offset) ? (i - offset + 1) : (i + 1);
        if (index >= 0 && index < in.length && !checked[index]) {
            possible_helper(all, in, index, player);
        }

        //case of to the right and up
        index = ((i % offset) + 1 >= offset) ? (i - offset + 1) : (i + 1);
        index -= offset;
        if (index < in.length && index >= 0) {
            if (!checked[index]) {
                possible_helper(all, in, index, player);
            }
        }

        //case of to the right and down
        index = ((i % offset) + 1 >= offset) ? (i - offset + 1) : (i + 1);
        index += offset;
        if (index >= 0 && index < in.length) {
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
    public double[][] possible(double[] in, int player) {
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
     * Make a random move
     *
     * @param desired player to move
     * @param input board state
     * @return
     */
    public double[] random(double[] input, int desired) {
        double[][] all;

        //find all possible moves
        all = possible(input, desired);

        int index = (int) (Math.random() * (double) all.length);
        return all[index];
    }

    /**
     * This takes in the current board state and return a new board state with
     * the AI's move. The returned value is in the format of 0.0 for no move 1.0
     * for player 1 2.0 for player 2 and the outer ring is the first 12 elements
     * in the array
     *
     * @param board current state of the board
     * @param player player to make a move
     * @return new board state that includes the players move
     */
    abstract double[] exploit(double[] board, int player);

    /**
     * Number of nodes evaluated for the last move the AI made.
     *
     * @return number of nodes evaluated.
     */
    //abstract int numEvaluated();
}
