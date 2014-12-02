/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Reffrences
 * http://www.dtic.mil/dtic/tr/fulltext/u2/a261434.pdf
 *
 * https://web.stanford.edu/group/pdplab/pdphandbook/handbookch10.html
 * 
 * Class book
 *
 * PDF link from google search
 * https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=2&cad=rja&uact=8&ved=0CCkQFjAB&url=http%3A%2F%2Fjmlr.org%2Fproceedings%2Fpapers%2Fv24%2Fsilver12a%2Fsilver12a.pdf&ei=osFnVJnZCJHkoASwl4GACg&usg=AFQjCNEI4RUaPoLUhfHh_RrE1dxLQxDaAg&sig2=7kiKWjzxVXpcDXciFrRzow&bvm=bv.79142246,bs.1,d.cGE
 */
package k_j_a;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

//local packages
import wincheck.Winchecker;

/**
 *
 * @author sweetness
 */
public class TDNN {

    Stack<double[][]> Y;  //memory for outcomes of moves
    Stack<double[][]> Y2; //memory for outcomes of moves

    double[][] w;      //weights [layer][node]
    double[][] state;  //output of nodes [layer][node]

    static int offset = 12; //number of points int a row of game board

    double lr;
    double bias;
    double lambda;

    int numIn;
    int numOut;

    /**
     * Tempral Difference Neural Network
     *
     * @param in the number of input nodes
     * @param hid the number of hidden nodes
     * @param out the number of output nodes
     * @param lay the number of hidden layers
     */
    public TDNN(int in, int hid, int out) {
        numIn = in;
        numOut = out;
        init(hid, 1);
    }

    //Set weight and topography
    private void init(int hid, int lay) {
        int numStateLayers = lay + 2;
        int numWeightLayers = lay + 1;
        int i;
        Random r = new Random();
        lr = 0.001;
        bias = -1.0;
        lambda = 0.3;

        //in the future the number of hidden nodes can be made to vary per layer
        state = new double[numStateLayers][];
        w = new double[numWeightLayers][];

        state[0] = new double[numIn];
        for (i = 1; i < numStateLayers - 1; i++) {
            state[i] = new double[hid];
        }
        state[i] = new double[numOut];

        r.setSeed(0);
        for (i = 0; i < numWeightLayers; i++) {
            w[i] = new double[hid + 1]; //plus one for bias
            for (int j = 0; j < w[i].length; j++) {
                w[i][j] = (r.nextDouble() > .5) ? r.nextDouble() : -r.nextDouble();
            }
        }
        //w[i] = new double[numOut + 1]; //plus one for bias
    }

    //sigmoid function 1 / (1 + e^-x)
    private double sigmoid(double x) {
        double d = 1.0 + Math.exp(-x);
        if (d != 0) {
            return 1.0 / d;
        } else {
            return (1.0 / .0001);
        }
    }

    //derivitave of sigmoid
    private double derivitave(double x) {
        return x * (1 - x);
    }

    //returns the output
    private double[] forwardPass(double[] input) {
        double[] output = new double[numOut];

        //clear state
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                state[i][j] = 0.0;
            }
        }

        //set input
        System.arraycopy(input, 0, state[0], 0, numIn);

        //input to hidden
        for (int i = 0; i < state[0].length; i++) {
            for (int j = 0; j < state[1].length; j++) {
                state[1][j] += state[0][i] * w[0][j];
            }
        }
        //factor in bias and activation function
        for (int i = 0; i < state[1].length; i++) {
            state[1][i] += bias * w[0][w[0].length - 1];
            state[1][i] = sigmoid(state[1][i]);
        }

        //hidden layer to output
        for (int i = 0; i < state[2].length; i++) {
            int j;
            for (j = 0; j < state[1].length; j++) {
                state[2][i] += state[1][j] * w[1][j];
            }
            state[2][i] += bias * w[1][j];
        }

        //output layer
        System.arraycopy(state[state.length - 1], 0, output, 0, numOut);

        return output;
    }

    /**
     * Train for player 1 win / tie / player 2 win Specific to polar tic tac toe
     *
     * @param games the number of games to train with
     */
    public void train(int games) {
        int trainRandom = 20; //number of games played against a random opponent
        for (int g = 0; g < games; g++) {
            double[] board = new double[4 * offset];
            double[][] derP1;
            double[][] derP2;

            Y = new Stack();
            Y2 = new Stack();

            double[] sumP1 = new double[w[1].length];
            double[] sumP2 = new double[w[1].length];

            Arrays.fill(board, 0);

            //play both sides player one and player two
            //uses TD(lambda) algorithm with backpropogation
            board = exploit(1, board);
            double[][] temp = new double[state.length][];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = new double[state[i].length];
                System.arraycopy(state[i], 0, temp[i], 0, state[i].length);
            }
            Y.push(temp);

            board = exploit(2, board);

            temp = new double[state.length][];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = new double[state[i].length];
                System.arraycopy(state[i], 0, temp[i], 0, state[i].length);
            }
            Y2.push(temp);

            int move = 1;
            do {
                //System.out.println("move " + move);
                board = exploit(1, board);

                temp = new double[state.length][];
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = new double[state[i].length];
                    System.arraycopy(state[i], 0, temp[i], 0, state[i].length);
                }
                Y.push(temp);

                //Player twos move
                if (g < trainRandom) {
                    board = exploit(2, board);

                    temp = new double[state.length][];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = new double[state[i].length];
                        System.arraycopy(state[i], 0, temp[i], 0, state[i].length);
                    }
                    Y2.push(temp);
                } else {
                    board = random(2, board);
                }
            } while (Winchecker.check(board) < 0);
            //System.out.println("Finished trainging with game " + g + "\nWinner was player " + Winchecker.check(board));

            //***********Train from player ones perspective
            int ex = Y.size();
            double[][] trace = new double[ex][numOut];
            state = Y.pop();
            int winner = (int) Winchecker.check(board);
            for (int i = 0; i < numOut; i++) {
                if (i == winner) {
                    state[2][i] = 1.0;
                } else {
                    state[2][i] = 0.0;
                }
            }
            int z = 0;
            while (!Y.empty()) {
                Arrays.fill(sumP1, 0);
                double[] TDerror = new double[numOut];
                derP1 = Y.pop();
                for (int i = 0; i < numOut; i++) {
                    trace[z][i] = (state[2][i] - derP1[2][i]);
                }

                z++;
                for (int i = 0; i < z; i++) {
                    for (int j = 0; j < numOut; j++) {
                        double p = Math.pow(lambda, z - i);
                        TDerror[j] += trace[i][j] * ((p != 0) ? p : 1);
                    }
                }

                for (int i = 0; i < TDerror.length; i++) {
                    TDerror[i] = lr * TDerror[i];
                    //System.out.println("td error = " + TDerror[i]);
                }

                //bound weights to -3 to 3
                for (int i = 0; i < derP1[1].length; i++) {
                    for (int j = 0; j < numOut; j++) {
                        w[1][i] += derP1[1][i] * TDerror[j];
                        sumP1[i] += derivitave(derP1[1][i]) * TDerror[j];
                    }
                    if (w[1][i] > 3) {
                        w[1][i] = 3;
                    }
                    if (w[1][i] < -3) {
                        w[1][i] = -3;
                    }
                }

                for (int j = 0; j < numOut; j++) {
                    w[1][w[1].length - 1] += TDerror[j];
                    if (w[1][w[1].length - 1] > 3) {
                        w[1][w[1].length - 1] = 3;
                    }
                    if (w[1][w[1].length - 1] < -3) {
                        w[1][w[1].length - 1] = -3;
                    }
                }

                //weights from input to hidden layer
                for (int i = 0; i < derP1[0].length; i++) {
                    for (int j = 0; j < derP1[1].length; j++) {
                        w[0][j] += derP1[0][i] * sumP1[j];
                        if (w[0][j] > 3) {
                            w[0][j] = 3;
                        }
                        if (w[0][j] < -3) {
                            w[0][j] = -3;
                        }
                    }
                }

                state = derP1;
            }

            //*******Train from Player twos perspective
            if (g < trainRandom) {
                state = Y2.pop();
                winner = (int) Winchecker.check(board);
                for (int i = 0; i < numOut; i++) {
                    if (i == winner) {
                        state[2][i] = 1.0;
                    } else {
                        state[2][i] = 0.0;
                    }
                }
                z = 0;
                while (!Y2.empty()) {
                    Arrays.fill(sumP2, 0);
                    double[] TDerror = new double[numOut];
                    derP2 = Y2.pop();
                    for (int i = 0; i < numOut; i++) {
                        trace[z][i] = (state[2][i] - derP2[2][i]);
                    }

                    z++;
                    for (int i = 0; i < z; i++) {
                        for (int j = 0; j < numOut; j++) {
                            double p = Math.pow(lambda, z - i);
                            TDerror[j] += trace[i][j] * ((p != 0) ? p : 1);
                        }
                    }

                    for (int i = 0; i < TDerror.length; i++) {
                        TDerror[i] = lr * TDerror[i];
                        //System.out.println("td error = " + TDerror[i]);
                    }

                    //bound weights to -3 to 3
                    for (int i = 0; i < derP2[1].length; i++) {
                        for (int j = 0; j < numOut; j++) {
                            w[1][i] += derP2[1][i] * TDerror[j];
                            sumP2[i] += derivitave(derP2[1][i]) * TDerror[j];
                        }
                        if (w[1][i] > 3) {
                            w[1][i] = 3;
                        }
                        if (w[1][i] < -3) {
                            w[1][i] = -3;
                        }
                    }

                    for (int j = 0; j < numOut; j++) {
                        w[1][w[1].length - 1] += TDerror[j];
                        if (w[1][w[1].length - 1] > 3) {
                            w[1][w[1].length - 1] = 3;
                        }
                        if (w[1][w[1].length - 1] < -3) {
                            w[1][w[1].length - 1] = -3;
                        }
                    }

                    //weights from input to hidden layer
                    for (int i = 0; i < derP2[0].length; i++) {
                        for (int j = 0; j < derP2[1].length; j++) {
                            w[0][j] += derP2[0][i] * sumP2[j];
                            if (w[0][j] > 3) {
                                w[0][j] = 3;
                            }
                            if (w[0][j] < -3) {
                                w[0][j] = -3;
                            }
                        }
                    }

                    state = derP2;
                }
            }

            //System.out.println("Weights");
            for (int i = 0; i < w.length; i++) {
                for (int j = 0; j < w[i].length; j++) {
                    //System.out.print(" " + w[i][j]);
                }
                // System.out.println("");
            }
        }
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
            checked[i] = true;
            all[max++][i] = player;
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

        //case of empty board then all possible
        if (max == 0) {
            //System.out.println("Empty board");
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

    //find best move for all possible moves
    private double[] best(double[][] all, int desired) {
        double max = 0.0; //diffrence in win vs loss for desired
        double[] out = new double[numOut];
        int index = 0;

        for (int i = 0; i < all.length; i++) {
            double local = 0.0;
            out = forwardPass(all[i]);
            for (int j = 0; j < numOut; j++) {
                local += out[desired] - out[j];
            }
            if (local > max) {
                max = local;
                index = i;
            }
        }

        return all[index];
    }

    //convert from double array to single vector. Assumes square grid.
    private double[] convert(double[][] in) {
        double[] out = new double[in.length * in[0].length];
        int index = 0;

        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[i].length; j++) {
                out[index++] = in[i][j];
            }
        }

        return out;
    }

    //convert from input vector back to 2D board
    private double[][] convert(double[] in, int length, int width) {
        double[][] out = new double[length][width];
        int index = 0;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                out[i][j] = in[index++];
            }
        }

        return out;
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
    public double[][] exploit(int desired, double[][] input) {
        if (input == null) {
            return null;
        }
        double[] in = convert(input);
        double[][] all;

        //find all possible moves
        all = possible(in, desired);

        //evaluate possible moves and choose best
        in = best(all, desired);

        return convert(in, input.length, input[0].length);
    }

    public double[] exploit(int desired, double[] input) {
        double[][] all;

        //find all possible moves
        all = possible(input, desired);

        //evaluate possible moves and choose best
        input = best(all, desired);

        return input;
    }

    /**
     * Make a random move
     *
     * @param desired player to move
     * @param input board state
     * @return
     */
    public double[] random(int desired, double[] input) {
        double[][] all;

        //find all possible moves
        all = possible(input, desired);

        int index = (int) (Math.random() * (double) all.length);
        return all[index];
    }

    /**
     * If user has predefined weights they want to use.
     *
     * @param in weights to set between nodes [layer][weight] where layer 0 is
     * between input and next layer (Don't forget bias weight)
     */
    public void setWeights(double[][] in) {
        if (in.length != w.length) {
            //System.out.println("Error with set weight input!");
            return;
        }

        for (int i = 0; i < w.length; i++) {
            if (in[i].length != w[i].length) {
                //System.out.println("Error setting weights");
                return;
            }
            for (int j = 0; j < w[i].length; j++) {
                w[i][j] = in[i][j];
            }
        }
    }
}
