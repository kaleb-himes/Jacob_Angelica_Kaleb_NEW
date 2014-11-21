

public class JavaWinChecker
{
    static { System.loadLibrary("implementation"); }

    public static native boolean check_win(char cPlayerTurn, char[][] cPlayerBoardLoc);

}
