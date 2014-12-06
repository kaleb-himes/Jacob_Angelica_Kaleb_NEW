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
public class Minimax extends AI {

    boolean prunning;
    double alpha;
    double beta;

    double[][] values;
    int offset;

    int maxDepth;

    Simple_Heuristic sh = new Simple_Heuristic();
    ;
    node currentHead;
    node head;

    public Minimax() {
        offset = 12;
        int player = 2; //start with player one
        maxDepth = 2;

        double[] board = new double[offset * 4];
        head = new node(board);
        head.player = 2;
        currentHead = head; //keep track of location in tree during games
        construct(head, player, 1);
    }

    //bubble up value from an end node (win/lose/tie)
    private void bubble(node in, int player, double value) {

        /* player one was last to play meaning the parent of the node is lookin
         to minamize */
        boolean min = (player == 1);

        node current = in;
        node parent = current.getParent();
        //ArrayList<node> chldrn;
        current.setValue(value);

        while (parent != null) {
            //chldrn = parent.getChildren();
            //double temp = (parent.player == 1) ? Double.MAX_VALUE : Double.MIN_VALUE;

            //for (node x : chldrn) {
                //if childern are player one than the parent is 2 and wants min
                if (current.player == 1) {
                    if (parent.player != 2) {
                        System.out.println("well theres your problem");
                        System.exit(0);
                    }
                    if (current.getValue() < parent.getValue()) {
                        parent.setValue(current.getValue());
                    }
                } else {
                    if(current.player != 2 || parent.player !=1) {
                        System.out.println("no heres a big problem");
                        System.exit(0);
                    }
                    if (current.getValue() > parent.getValue()) {
                        parent.setValue(current.getValue());
                    }
                }
            //}

//            Double v = parent.getValue();
//            if (v == null) {
//                parent.setValue(temp);
//            } else {
//                if (parent.player == 2) {
//                    if (temp < v) {
//                        parent.setValue(temp);
//                    }
//                } else {
//                    if (temp > v) {
//                        parent.setValue(temp);
//                    }
//                }
//            }

            //min = !min;
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
                        if (all[i] == null) {
                            System.out.println("all is null");
                        }
                        if (localHead == null) {
                            System.out.println("local head is null");
                        }
                        if (localHead.getState() == null) {
                            System.out.println("local heads state is null");
                        }
                        if (current == null) {
                            System.out.println("current is null");
                        }
                        if (sh == null) {
                            System.out.println("sh is null");
                        }
                        
                        current.player = player;
                        //if(current.player == 1){
                        //current.setValue(sh.getHeuristic(all[i], localHead.getState()));
                        //}else{
                        //    current.setValue(-sh.getHeuristic(all[i], localHead.getState()));
                        //}
                        //bubble(current, player, current.getValue());
                        double win = Winchecker.check(all[i]);
                        if (win > -1) {
                            bubble(current, player, (win == 1.0) ? 5 : (win == 2.0) ? -5 : 0);
                            //System.out.println("flagged as a win");
                            //printBoard(all[i]);
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
                        if (!chd.get(j).prunned) {
                            complete = false;
                            break;
                        } else {
                            if (chd.get(j).getValue() == alpha.doubleValue()) {
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
                board = exploit(board, 1);
                if (Winchecker.check(board) > -1) {
                    break;
                }
                board = random(board, 2);
                if (Winchecker.check(board) > -1) {
                    break;
                }
            }
            //printBoard(board);
        }
        //print();
    }

    private void printBoard(double[] board) {
        int index = 0;
        for (int k = 0; k < 4; k++) {
            for (int y = 0; y < 12; y++) {
                System.out.print(board[index++] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private void print() {
        int ply = 0;
        Stack<node> stk = new Stack();
        Stack<node> next = new Stack();
        System.out.println("Printing");
        node lh;
        stk.push(head);
        ArrayList<node> ch;
        while (!stk.isEmpty()) {

            lh = stk.pop();
            ch = lh.getChildren();
            System.out.println(ply + " Player: " + lh.player + " Head = " + lh.getValue());
            System.out.print("Childern : ");
            for (node x : ch) {
                System.out.print("  " + x.getValue() + "  ");
                stk.push(x);
            }
            System.out.println("\n");

            ply++;
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

    @Override
    double[] exploit(double[] in, int player) {
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
//            for (int i = 0; i < in.length; i++) {
//                if (in[i] > 0) {
//                    System.out.print(" " + i + " value of " + in[i]);
//                }
//            }
//            System.out.println(" indexs searching for");
//            ArrayList<node> temp = head.getChildren();
//
//            ArrayList<node> temp2;
//            for (node x1 : temp) {
//                temp2 = x1.getChildren();
//                for (node x : temp2) {
//                    for (int i = 0; i < in.length; i++) {
//                        if (x.getState()[i] > 0) {
//                            System.out.print(" " + i + " value of " + x.getState()[i]);
//                        }
//                    }
//                    System.out.println("");
//                }
//            }
//            return null;
        }
        currentHead = best(player, currentHead, maxDepth);

        return currentHead.getState();
    }

    private node best(int player, node localHead, int depth) {
        if (localHead == null) {
            System.out.println("Error localHead null when calling best function");
            return null;
        }

        construct(localHead, player, depth);

        ArrayList<node> chd = localHead.getChildren();
        node b = null;
        Double temp;

        if (localHead.player == 2) {
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
    
    public void setDepth(int d) {
        maxDepth = d;
    }

    private class node {

        private double[] state;
        private double value;
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
            value = 0.0;
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

        public double getValue() {
            return value;
        }
    }
}
