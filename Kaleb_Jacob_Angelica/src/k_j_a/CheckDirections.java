/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k_j_a;

import static k_j_a.GUI.legal_moves;

/**
 *
 * @author khimes
 */
public class CheckDirections {
    
    public static boolean up(int x)
    {
        if (x < 48) {
            if (legal_moves[x][2] == 1) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean down(int x)
    {
        if (x > 0) {
            if (legal_moves[x][2] == 1) {
                return true;
            }
        }
        return false;
    }
    
}