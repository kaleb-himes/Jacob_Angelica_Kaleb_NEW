

public class JavaClassifier
{
    static { System.loadLibrary("AI"); }

/**
  * Initialize the classifier 
  * @return returns 0 on success and 1 on fail
  */
public static native int classifier_init();                    /*  */

/**
  * Make a move with the classifier
  * @param xyarray the x and y integer for placement
  * @return 0 for success 
  */
public static native int classifier_make_move(int[] xyarray);   /*  */

/**
  * Set option for potential prunning
  * @param prune a boolean flag to set prunning
  * @return 0 for success
  */
public static native int classifier_prune(int prune);

/**
  * Free up memory allocated for the classifier
  * @return 0 for success
  */
public static native int classifier_free();                    /*  */

/**
  * Set the depth of search if used
  * @param depth the depth to search
  * @return 0 for success
  */
public static native int classifier_depth(int depth);          /*  */

}

