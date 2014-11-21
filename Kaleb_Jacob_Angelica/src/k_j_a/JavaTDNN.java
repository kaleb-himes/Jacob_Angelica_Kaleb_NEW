package k_j_a;

public class JavaTDNN
{
    static { System.loadLibrary("AI"); }

/**
  * Initialize the neural network
  * @return returns 0 on success and 1 on fail
  */
public static native int TDNN_init();

/**
  * Make a move with the neural network
  * @param xyarray the x and y integer for placement
  * @return 0 for success 
  */
public static native int TDNN_make_move(int[] xyarray);

/**
  * Set option for potential prunning
  * @param prune a boolean flag to set prunning
  * @return 0 for success
  */
public static native int TDNN_prune(int prune);

/**
  * Free up memory allocated for neural network
  * @return 0 for success
  */
public static native int TDNN_free();

/**
  * Set the depth of search if used
  * @param depth the depth to search
  * @return 0 for success
  */
public static native int TDNN_depth(int depth);
}

