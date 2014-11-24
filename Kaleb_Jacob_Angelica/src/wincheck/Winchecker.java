/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wincheck;

/**
 *
 * @author sweetness
 */
public class Winchecker {

    static int offset = 12;

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
}
