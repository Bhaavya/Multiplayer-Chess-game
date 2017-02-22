package GameLogic.Board;
import GameLogic.Piece.Piece;
import GameLogic.Player.Player;
import java.util.ArrayList;
/**
 Extensible Chess board implemented as a 2D Array
 */
public abstract class Board {
    Cell cells[][];
    int numRanks,numFiles;
    ArrayList <Piece> whitePieces=new ArrayList<>();
    ArrayList <Piece> blackPieces=new ArrayList<>();


    public Board(int numRanks, int numFiles) {
        cells=new Cell[numRanks][numFiles];
        this.numRanks=numRanks;
        this.numFiles=numFiles;
    }

    public void setWhitePieces(ArrayList<Piece> whitePieces) {
        this.whitePieces=whitePieces;
    }

    public void setBlackPieces(ArrayList<Piece> blackPieces){
        this.blackPieces=blackPieces;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public int getRanks() {
        return numRanks;
    }

    public int getFiles() {
        return numFiles;
    }

    public Cell[][] getCells() {
        return cells;
    }
    /** sets up initial board
     *
     * @param white white player
     * @param black black player
     */
    public void setBoard(Player white, Player black) {}
    /**creates a deep copy of the board in current state
     *
     * @param board
     * @param p1 current player
     * @param p2 player 2
     * @param p3 copy of current player
     * @param p4 copy of player 2
     * @return
     */
    public Board tempBoard(Board board, Player p1,Player p2,Player p3,Player p4){
        return null;
    }
}


