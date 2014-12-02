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
public class AI {

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
}
