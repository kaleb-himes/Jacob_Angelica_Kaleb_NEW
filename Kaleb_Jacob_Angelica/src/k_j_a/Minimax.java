/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 *
 * http://web.cs.wpi.edu/~rich/courses/imgd4000-d10/lectures/E-MiniMax.pdf
 *
 * http://www.flyingmachinestudios.com/programming/minimax/
 *
 * alpha-beta
 * http://repository.cmu.edu/cgi/viewcontent.cgi?article=2700&context=compsci
 *
 */
package k_j_a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import wincheck.Winchecker;

/**
 *
 * @author sweetness
 */
public class Minimax {

    boolean prunning;
    double alpha;
    double beta;

    double[][] values;
    int offset;

    int maxDepth;
    static boolean[] checked;
    static int max;

    node currentHead;
    node head;

    public Minimax() {
        offset = 12;
        int player = 2; //start with player one

        double[] board = new double[offset * 4];
        head = new node(board);
        head.player = 2;
        currentHead = head; //keep track of location in tree during games
        construct(head, player, 1);
    }

    //bubble up value from an end node (win/lose/tie)
    private void bubble(node in, int player, int value) {

        /* player one was last to play meaning the parent of the node is lookin
         to minamize */
        boolean min = (player == 1);

        node current = in;
        node parent = current.getParent();
        ArrayList<node> chldrn;
        current.setValue(value);

        while (parent != null) {
            chldrn = parent.getChildren();
            double temp = (min) ? Double.MAX_VALUE : Double.MIN_VALUE;

            for (node x : chldrn) {
                if (min) {
                    if (x.getValue() != null && x.getValue() < temp) {
                        temp = x.getValue();
                    }
                } else {
                    if (x.getValue() != null && x.getValue() > temp) {
                        temp = x.getValue();
                    }
                }
            }

            Double v = parent.getValue();
            if (v == null) {
                parent.setValue(temp);
            } else {
                if (min) {
                    if (temp < v) {
                        parent.setValue(temp);
                    }
                } else {
                    if (temp > v) {
                        parent.setValue(temp);
                    }
                }
            }

            min = !min;
            current = current.getParent();
            parent = current.getParent();
        }
    }

    //create a minimax tree
    private void construct(node localHead, int player, int depth) {

        Stack<node> stk = new Stack();
        Stack<node> stk2 = new Stack();
        int localDepth = 0;

        stk2.push(localHead);
        while (!stk2.isEmpty() && localDepth < depth) {
            while (!stk2.isEmpty()) {
                stk.push(stk2.pop());
            }
            while (!stk.isEmpty()) {
                localHead = stk.pop();
                //change for next player
                player = localHead.player;
                player = (player == 1) ? 2 : 1;
                if (localHead == null) {
                    break;
                }

                double[][] all = possible(localHead.getState(), player);
                if (all == null) {
                    bubble(localHead, player, 0);
                    return; //no more moves possible
                }

                ArrayList<node> chd = localHead.getChildren();

                for (int i = 0; i < all.length; i++) {
                    node current = null;
                    boolean exists = false;
                    if (chd != null) {
                        //see if node has already been created
                        for (node x : chd) {
                            if (Arrays.equals(x.getState(), all[i])) {
                                current = x;
                                exists = true;
                                break;
                            }
                        }
                    }
                    if (!exists) {
                        current = new node(all[i]);
                        current.player = player;
                        double win = Winchecker.check(all[i]);
                        if (win != 0) {
                            bubble(current, player, (win == 1) ? 1 : -1);
                        }

                        current.setParent(localHead);
                        localHead.addChild(current);

                    }

                    //check if its been prunned and if so do not explore
                    if (!current.prunned) {
                        stk2.push(current);
                    }
                }
            }
            localDepth++;
        }
    }

    //create a minimax tree
//    private void construct(node localHead, int player) {
//        double[][] all = possible(localHead.getState(), player);
//        
//        //change for next player
//        player = (player == 1)? 2 : 1;
//        
//        if (all == null) {
//            return;
//        }
//        
//        for (int i = 0; i < all.length; i++) {
//            node current = new node(all[i]);
//            
//            current.setParent(localHead);
//            localHead.addChild(current);
//            
//            construct(current, player);
//        }
//    }
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
                System.out.println("strange error index of in at " + i + " been checked ? " + checked[i]);
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
                full++;
            }
        }

        //case of empty or full board
        if (max == 0) {
            if (full >= (4 * offset)) {
                return null; //board is full
            }
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
    public double[] random(int desired, double[] input) {
        double[][] all;

        if (input == null) {
            System.out.println("random of null board input errer");
            return null;
        }

        //find all possible moves
        all = possible(input, desired);

        int index = (int) (Math.random() * (double) all.length);
        return all[index];
    }

    /**
     * Alpha, Beta prunning, prune depending on which player
     *
     * @param p1 if true than prune for player one
     * @param p2 if true than prune for player two
     */
    public void prun(boolean p1, boolean p2) {
        ArrayList<node> chd;
        Stack<node> stk = new Stack();
        Stack<node> stk2 = new Stack();

        System.out.println("prunning for p1 " + p1);
        Double alpha;
        Double beta;
        if (p1) {
            node localHead = head;
            stk.push(localHead);

            while (!stk.isEmpty()) {
                localHead = stk.pop();
                alpha = localHead.getValue();

                // check if no childern have values yet...can not prune
                if (alpha != null) {
                    chd = localHead.getChildren();
                    boolean complete = true;
                    int index = 0;
                    for (int j = 0; j < chd.size(); j++) {
                        if (chd.get(j).getValue() == null && !chd.get(j).prunned) {
                            complete = false;
                            break;
                        } else {
                            if (chd.get(j).getValue().doubleValue() == alpha.doubleValue()) {
                                index = j;
                            }
                        }
                    }

                    // if complete than atomaticlly prun
                    if (complete) {
                        for (int j = 0; j < chd.size(); j++) {
                            if (j != index) {
                                System.out.println("Prunned a node");
                                chd.get(j).prunned = true;
                            }
                        }
                    }

                    for (node x : chd) {
                        if (!x.prunned) {
                            ArrayList<node> temp = x.getChildren();
                            for (node next : temp) {
                                if (!next.prunned) {
                                    stk.push(next);
                                }
                            }
                        }
                    }

                }
            }

        }

        if (p2) {

        }
    }

    /**
     * When pruning is turned on the minimax tree will not explore states that
     * have been pruned from the tree
     */
    public void turnPruningOn() {
        prunning = true;
    }

    /**
     * Allows the mimimax tree to explore nodes that were previously pruned
     */
    public void turnPruningOff() {
        prunning = false;
    }

    public void train(int g) {
        for (int i = 0; i < g; i++) {
            double[] board = new double[offset * 4];
            while (true) {
                board = exploit(1, board, 2);
                if (Winchecker.check(board) > -1) {
                    break;
                }
                board = random(2, board);
                if (Winchecker.check(board) > -1) {
                    break;
                }
            }
            GUI.game_state_display.append("Finshed training game " + i + "\n");
        }
    }

    private node find(double[] in) {
        double[] cmp;
        node current;
        Stack<node> stk = new Stack();
        Stack<node> stk2 = new Stack();
        ArrayList<node> c;
        stk.push(currentHead);
        while (!stk.isEmpty()) {
            while (!stk.isEmpty()) {
                stk2.push(stk.pop());
            }
            while (!stk2.isEmpty()) {
                current = stk2.pop();

                cmp = current.getState();
                boolean matched = true;
                for (int i = 0; i < cmp.length; i++) {
                    if (cmp[i] != in[i]) {
                        matched = false;
                        break;
                    }
                }
                if (matched) {
                    return current;
                }
                c = current.getChildren();
                for (node x : c) {
                    stk.push(x);
                }
            }
        }
        return null;
    }

    public double[] exploit(int player, double[] in, int depth) {
        if (in == null) {
            System.out.println("error null board used for exploit");
            return null;
        }

        currentHead = find(in);
        if (currentHead == null) { //restart search from top of list
            currentHead = head;
            currentHead = find(in);
        }

        //find max or min
        if (currentHead == null) {
            System.out.println("could not find node");
            for (int i = 0; i < in.length; i++) {
                if (in[i] > 0) {
                    System.out.print(" " + i + " value of " + in[i]);
                }
            }
            System.out.println(" indexs searching for");
            ArrayList<node> temp = head.getChildren();

            ArrayList<node> temp2;
            for (node x1 : temp) {
                temp2 = x1.getChildren();
                for (node x : temp2) {
                    for (int i = 0; i < in.length; i++) {
                        if (x.getState()[i] > 0) {
                            System.out.print(" " + i + " value of " + x.getState()[i]);
                        }
                    }
                    System.out.println("");
                }
            }
            return null;
        }
        currentHead = best(player, currentHead, depth);

        return currentHead.getState();
    }

    private node best(int player, node localHead, int depth) {
        if (localHead == null) {
            System.out.println("Error localHead null when calling best function");
            return null;
        }

        construct(localHead, player, depth);

        boolean min = (player != 1);

        ArrayList<node> chd = localHead.getChildren();
        node b = null;
        Double temp;

        if (min) {
            temp = Double.MAX_VALUE;
            for (int i = 0; i < chd.size(); i++) {
                Double x = chd.get(i).getValue();
                if (x != null && x < temp) {
                    temp = x;
                    b = chd.get(i);
                }
            }
            //none found so random
            if (temp == Double.MAX_VALUE) {
                b = chd.get((int) (Math.random() * chd.size()));
            }
        } else {
            temp = Double.MIN_VALUE;
            for (int i = 0; i < chd.size(); i++) {
                Double x = chd.get(i).getValue();
                if (x != null && x > temp) {
                    temp = x;
                    b = chd.get(i);
                }
            }
            //none found so random
            if (temp == Double.MIN_VALUE) {
                b = chd.get((int) (Math.random() * chd.size()));
            }
        }

        return b;
    }

    private class node {

        private double[] state;
        private Double value;
        private ArrayList<node> childern;
        private node parent;
        public boolean prunned; //stop at this node and don't explore farther
        public int player;

        /**
         * Constructer for new node state
         *
         * @param in the state of the board represented by node
         */
        public node(double[] in) {
            prunned = false;
            childern = new ArrayList();
            state = new double[in.length];
            for (int i = 0; i < in.length; i++) {
                state[i] = in[i];
            }
            value = null;
        }

        /**
         * Adding a child to the node
         *
         * @param child node to be added
         */
        public void addChild(node child) {
            childern.add(child);
        }

        /**
         * Remove a child from node
         *
         * @param child node to be removed
         */
        public void removeChild(node child) {
            childern.remove(child);
        }

        /**
         * Set the parent node
         *
         * @param p node that is the parent
         */
        public void setParent(node p) {
            parent = p;
        }

        /**
         * Get parent node
         *
         * @return node that is the parent
         */
        public node getParent() {
            return parent;
        }

        /**
         * Get array list of children
         *
         * @return array list of children
         */
        public ArrayList<node> getChildren() {
            return childern;
        }

        /**
         * Get board state of node
         *
         * @return board state of node
         */
        public double[] getState() {
            return state;
        }

        /**
         * Set the node value
         *
         * @param in the node value
         */
        public void setValue(double in) {
            value = in;
        }

        public Double getValue() {
            return value;
        }
    }
}
