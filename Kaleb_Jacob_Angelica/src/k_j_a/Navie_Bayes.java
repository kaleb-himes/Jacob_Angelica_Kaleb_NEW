/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import java.util.Arrays;
import java.util.Stack;
import wincheck.Winchecker;

/**
 *
 * @author sweetness
 *
 * http://www.statsoft.com/Textbook/Naive-Bayes-Classifier
 *
 * http://www.cs.ucr.edu/~eamonn/CE/Bayesian%20Classification%20withInsect_examples.pdf
 *
 * http://stackoverflow.com/questions/10059594/a-simple-explanation-of-naive-bayes-classification
 *
 *
 */
public class Navie_Bayes extends AI {

    /*               Avg heuristic >= 2| Avg heuristic < 2  || Ring 1 >= 5 | Ring 1 < 5 ........ Ring 4 < 5  || total
     Player 1 win |                    |                    ||
     Player 2 win |                    |                    ||
     Tie          |                    |                    ||
     ____________________ ____________________ 
     Total        |                    |
     */
    private double[][] values;
    private int numTypes;
    private int numClass;
    private int offset = 12;
    private int evaluated;

    private double[] rememberedBoard;
    private Simple_Heuristic sh;

    /**
     * Preset dimensions for polar tic tac toe game
     */
    public Navie_Bayes() {
        sh = new Simple_Heuristic();
        numTypes = 10;
        numClass = 3;
        evaluated = 0;
        values = new double[numClass + 1][numTypes + 1];
        //add one to acount for totals

    }

    /**
     * Finds the probability of a player to win.
     *
     * @param player player who's probability of winning is to be found
     * @param board the state of the current board
     * @return probability of winning
     */
    public double probability(double[] board, int player) {
        double prob = 0.5;

        //if board in is not the same state of remembered calculate the difference
        //find prior
        double priorP1 = values[0][numTypes] / values[3][numTypes];
        double priorP2 = values[1][numTypes] / values[3][numTypes];
        double priorTi = values[2][numTypes] / values[3][numTypes];

        //find evidence
        double probAvg = values[3][0] / values[3][numTypes];
        double probRing1 = values[3][2] / values[3][numTypes];
        double probRing2 = values[3][4] / values[3][numTypes];
        double probRing3 = values[3][6] / values[3][numTypes];
        double probRing4 = values[3][8] / values[3][numTypes];

        //find likelihood
        double likely = 0.0;
        likely *= (values[player][0] / probAvg);
        likely *= (values[player][2] / probRing1);
        likely *= (values[player][4] / probRing2);
        likely *= (values[player][6] / probRing3);
        likely *= (values[player][8] / probRing4);
        likely *= priorP1;

        double totEv = probAvg * probRing1 * probRing2 * probRing3 * probRing4;

        prob = likely / totEv;

        return prob;
    }

    @Override
    public double[] exploit(double[] in, int player) {
        int index = 0;
        double highest = Double.MIN_VALUE;
        double[][] all = possible(in, player);
        evaluated = 0;

        //find board state with highest probability for player to win
        for (int i = 0; i < all.length; i++) {
            double temp = probability(all[i], player);
            evaluated++;
            if (temp > highest) {
                index = i;
                highest = temp;
            }
        }

        return all[index];
    }

    /**
     * Train for player 1 win / tie / player 2 win Specific to polar tic tac toe
     *
     * @param games the number of games to train with
     */
    public void train(int games) {
        for (int g = 0; g < games; g++) {
            double[] board = new double[4 * offset];
            Arrays.fill(board, 0);
            double winner = 0;
            do {
                board = random(board, 1);
                winner = Winchecker.check(board);
                if (winner > 0) {
                    break;
                }
                board = random(board, 2);
                winner = Winchecker.check(board);
                if (winner > 0) {
                    break;
                }
            } while (Winchecker.check(board) < 0);
            System.out.println("Finished trainging with game " + g + "\nWinner was player " + winner);

            //update probbilities in network
            //average heuristic value
            double avg = 0.0;
            double avg2 = 0.0;
            double total = 0.0;
            double total2 = 0.0;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 1.0) {
                    avg += sh.getHeuristic(board, i);
                    total++;
                }
                if (board[i] == 2.0) {
                    avg2 += sh.getHeuristic(board, i);
                    total2++;
                }
            }
            avg = avg / total;
            avg2 = avg2 / total2;

            values[3][0]++;
            values[3][1]++;
            if (winner == 1.0) {
                if (avg >= 2) {
                    values[0][0]++;
                } else {
                    values[0][1]++;
                }
            }

            if (winner == 2.0) {
                if (avg2 >= 2) {
                    values[1][0]++;
                } else {
                    values[1][1]++;
                }
            }

            //ring values
            int ring = 8; //index in values for outter ring + 
            int index = 0;
            for (int i = 0; i < 4; i++) {
                int p1 = 0;
                int p2 = 0;
                for (int j = 0; j < 12; j++) {
                    if (board[index] == 1.0) {
                        p1++;
                    }
                    if (board[index] == 2.0) {
                        p2++;
                    }
                    index++;
                }
                if (winner == 1.0) {
                    if (p1 >= 5) {
                        values[0][ring]++;
                    } else {
                        values[0][ring + 1]++;
                    }
                }
                if (winner == 2.0) {
                    if (p2 >= 5) {
                        values[1][ring]++;
                    } else {
                        values[1][ring + 1]++;
                    }
                }
                values[3][ring]++;
                values[3][ring + 1]++;
                ring = ring - 2;
            }

            //log winner
            if (winner == 1.0) {
                values[0][numTypes]++;
            }
            if (winner == 2.0) {
                values[1][numTypes]++;
            }

            //increase total
            values[3][numTypes]++;
        }
        System.out.println("Probability array ****************");
        for (int i = 0; i < numClass + 1; i++) {
            for (int j = 0; j < numTypes + 1; j++) {
                System.out.print(values[i][j] + "\t");
            }
            System.out.println("");
        }
    }

    @Override
    int numEvaluated() {
        return evaluated;
    }
}
