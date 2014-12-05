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
    double[][] values;
    int numTypes;
    int numClass;
    int offset = 12;

    double[] rememberedBoard;
    Simple_Heuristic sh;

    /**
     * Preset dimensions for polar tic tac toe game
     */
    public Navie_Bayes() {
        sh = new Simple_Heuristic();
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
        //find evidence
        //find likelihood
        return prob;
    }

    @Override
    public double[] exploit(double[] in, int player) {
        int index = 0;
        double highest = Double.MIN_VALUE;
        double[][] all = possible(in, player);

        //find board state with highest probability for player to win
        for (int i = 0; i < all.length; i++) {
            double temp = probability(all[i], player);
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
            double avg = 0.0;
            
            for (int i = 0; i < 4; i++) {

            }
        }
    }
}
