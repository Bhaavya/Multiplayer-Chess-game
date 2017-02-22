package GameLogic.Board;
import GameLogic.Piece.Piece;

/**Represents squares on the board. Board has cells.
 * */
public class Cell {
    boolean isOccupied;
    Piece pieceAtCell;
    int rank,file;

    public Cell(int rank,int file) {
       isOccupied=false;
       pieceAtCell=null;
       this.rank=rank;
       this.file=file;
    }

    public int getRank() {
        return  rank;
    }

    public int getFile() {
        return file;
    }

    public void setPiece(Piece piece) {
       isOccupied=true;
       pieceAtCell=piece;
    }

    public boolean getIsOccupied(){
        return isOccupied;
    }
    /** removes piece
     *
     * @return piece removed
     */
    public Piece removePiece() {
        isOccupied=false;
        Piece removedPiece=pieceAtCell;
        pieceAtCell=null;
        return removedPiece;
    }

    public boolean getOccupancyStatus() {
        return isOccupied;
    }

    public Piece getPieceAtCell() {
       return pieceAtCell;
    }
}
