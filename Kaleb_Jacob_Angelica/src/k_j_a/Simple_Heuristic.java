/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

/**
 * Used to find a heuristic value for movement
 *
 * @author sweetness
 */
public class Simple_Heuristic extends AI{

    static int offset = 12;

    public Simple_Heuristic() {

    }

    /**
     * Find the heuristic value of the move if the index of move is known
     *
     * @param in the move to get the heuristic of
     * @param play the move index
     * @return double indicating heuristic value
     */
    public double getHeuristic(double[] in, int play) {
        int index = play;
        int i;
        double heur = 0.0;
        double temp = 0.0;

        if (index < 0) {
            System.out.println("Error in getting heuristic");
            return 0;
        }

        //case to the right
        i = index;
        temp = 0.0;
        for (int j = 0; j < 3; j++) {
            i = ((i + 1) % offset == 0) ? (i - offset + 1) % in.length : (i + 1) % in.length;
            if (in[i] == in[index]) {
                temp++;
            } else {
                break;
            }
        }

        //case to the left -- add on to previous heur
        i = index;
        for (int j = 0; j < 3; j++) {
            i = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
            if (i >= in.length) {
                break;
            }
            if (in[i] == in[index]) {
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
            if (i >= in.length) {
                break;
            }
            if (in[i] == in[index]) {
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
            if (in[i] == in[index]) {
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
            if (i >= in.length) {
                break;
            }
            if (in[i] == in[index]) {
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
            if (in[i] == in[index]) {
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
            if (i >= board.length) {
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

        //case of to the left and up
        i = index;
        temp = 0.0;
        for (int j = 0; j < 3; j++) {
            i = ((i % offset - 1 > 0) ? i - 1 : i + offset - 1);
            i += offset;
            if (i >= board.length) {
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
            if (i >= board.length) {
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


    @Override
    double[] exploit(double[] input, int desired) {
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
