/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import k_j_a.AI;
import k_j_a.Minimax;
import k_j_a.Navie_Bayes;
import k_j_a.Simple_Heuristic;
import k_j_a.TDNN;
import wincheck.Winchecker;

/**
 *
 * @author sweetness
 */
public class Driver {

    private final int numGames = 250;
    TDNN tdnn = new TDNN(48, 40, 3);

    public Driver() {
        try {

            //train tdnn to pit against 
            tdnn.train(20000);

            //TDNN 40 hidden nodes test
            File td = new File("TDNN40_Results.csv");
            PrintWriter out = new PrintWriter(td, "UTF-8");
            out.println("Algorithm,win,tie,loss");
            out.close();
            TDNN tdnn = new TDNN(48, 40, 3);
            tdnn.train(20000);
            AI ai = tdnn;
            againstAll(td, ai);

            //TDNN 160 hidden nodes test
            File td160 = new File("TDNN160_Results.csv");
            PrintWriter out160 = new PrintWriter(td160, "UTF-8");
            out160.println("Algorithm,win,tie,loss");
            out160.close();
            tdnn = new TDNN(48, 160, 3);
            tdnn.train(20000);
            ai = tdnn;
            againstAll(td160, ai);

            //Minimax test
            File mn = new File("Minimax_Results.csv");
            PrintWriter out2 = new PrintWriter(mn, "UTF-8");
            out2.println("Algorithm,win,tie,loss");
            out2.close();
            ai = new Minimax();
            againstAll(mn, ai);

            //Simple_Heuristic test
            File sh = new File("Heuristic_Results.csv");
            PrintWriter out3 = new PrintWriter(sh, "UTF-8");
            out3.println("Algorithm,win,tie,loss");
            out3.close();
            ai = new Simple_Heuristic();
            againstAll(sh, ai);

            //Navie_Bayes test
            File nb = new File("Navie_Bayes_Results.csv");
            PrintWriter out4 = new PrintWriter(nb, "UTF-8");
            out4.println("Algorithm,win,tie,loss");
            out4.close();
            ai = new Navie_Bayes();
            againstAll(nb, ai);

        } catch (Exception ex) {
            Logger.getLogger(TDNN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void againstAll(File f, AI ai) throws FileNotFoundException,
            UnsupportedEncodingException,
            IOException {
        String name = ai.getName();

        AI tdnn = this.tdnn;
        AI minm = new Minimax();
        AI simp = new Simple_Heuristic();
        AI baye = new Navie_Bayes();

        if (!name.equals(tdnn.getName())) {
            runTest(f, ai, tdnn);
        }

        if (!name.equals(minm.getName())) {
            runTest(f, ai, minm);
        }

        if (!name.equals(simp.getName())) {
            runTest(f, ai, simp);
        }

        if (!name.equals(baye.getName())) {
            runTest(f, ai, baye);
        }
    }

    /**
     * Run the test of ai one vs ai two
     *
     * @param f File to append info to
     * @param ai
     * @param ai2
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void runTest(File f, AI ai, AI ai2) throws FileNotFoundException,
            UnsupportedEncodingException,
            IOException {
        FileWriter out = new FileWriter(f, true);
        int wins = 0;
        int ties = 0;
        int loss = 0;

        for (int g = 0; g < numGames; g++) {
            double[] board = new double[12 * 4];
            int player = (Math.random() > .5) ? 1 : 2;
            int player2 = (player == 1) ? 2 : 1;
            do {
                if (player == 1) {
                    board = ai.exploit(board, player);
                    if (Winchecker.check(board) > 0) {
                        break;
                    }
                    board = ai2.exploit(board, player2);
                } else {
                    board = ai2.exploit(board, player2);
                    if (Winchecker.check(board) > 0) {
                        break;
                    }
                    board = ai.exploit(board, player);
                }
            } while (Winchecker.check(board) < 0);
            if (Winchecker.check(board) == player) {
                wins++;
            } else {
                if (Winchecker.check(board) == player2) {
                    loss++;
                } else {
                    ties++;
                }
            }
        }
        System.out.println("Finished " + ai.getName() + " against " + ai2.getName());
        out.write(ai2.getName() + "," + wins + "," + ties + "," + loss + "\n");
        out.close();
    }

    public static void main(String[] args) {
        System.out.println("Running test driver");
        new Driver();
    }
}
