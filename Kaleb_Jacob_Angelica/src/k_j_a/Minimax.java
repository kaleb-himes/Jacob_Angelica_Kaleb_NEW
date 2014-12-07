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
    
    private boolean prunning;
    private double alpha;
    private double beta;
    
    private double[][] values;
    private int offset;
    
    private int maxDepth;
    
    private Simple_Heuristic sh = new Simple_Heuristic();
    
    private node currentHead;
    private node head;
    
    public Minimax() {
        offset = 12;
        int player = 2; //start with player one
        maxDepth = 2;
        double[] board = new double[offset * 4];
        head = new node(board);
        head.player = 1;
        currentHead = head; //keep track of location in tree during games
        construct(head, player, 1);
    }

    //bubble up value from an end node (win/lose/tie)
    private void bubble(node in, double value) {
        node current = in;
        node parent = current.getParent();
        ArrayList<node> chldrn;
        current.setValue(value);
        
        while (parent != null) {
            chldrn = parent.getChildren();
            double temp = (parent.player == 1) ? 0 : Double.MAX_VALUE;
            
            for (node x : chldrn) {
                //if childern are player one than the parent is 2 and wants min
                if (current.player == 1) {
                    if (x.getValue() < temp) {
                        temp = x.getValue();
                    }
                } else {
                    if (x.getValue() > temp) {
                        temp = x.getValue();
                    }
                }
            }
            //set value and advance to next ply
            parent.setValue(temp);
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
                player = localHead.player;
                if (localHead == null) {
                    break;
                }
                
                double[][] all = possible(localHead.getState(), player);
                if (all == null) {
                    bubble(localHead, 0);
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
                        current.setParent(localHead);
                        localHead.addChild(current);
                        current.player = (player == 1) ? 2 : 1;
                        
                        if (current.player == 1) {
                            current.setValue(sh.getHeuristic(all[i], localHead.getState()));
                            bubble(current, current.getValue());
                        }
                        double win = Winchecker.check(all[i]);
                        if (win > -1) {
                            bubble(current, (win == 1.0) ? 5 : (win == 2.0) ? -5 : 0);
                        }
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
        if (currentHead == null) {
            System.out.println("could not find node in tree");
            System.exit(1);
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
        double temp;
        if (localHead.player == 2) {
            temp = Double.MAX_VALUE;
            for (int i = 0; i < chd.size(); i++) {
                double x = chd.get(i).getValue();
                if (x < temp) {
                    temp = x;
                    b = chd.get(i);
                }
            }
        } else {
            temp = -6.0;
            for (int i = 0; i < chd.size(); i++) {
                double x = chd.get(i).getValue();
                if (x > temp) {
                    temp = x;
                    b = chd.get(i);
                }
            }
        }
        //@TODO possibility to handle none found -- so maybe random?
        if (temp == 0.0) {
            //System.out.println("random selected");
            //b = chd.get((int) (Math.random() * chd.size()));
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
