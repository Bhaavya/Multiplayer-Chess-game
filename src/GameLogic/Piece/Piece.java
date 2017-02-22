package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import GameLogic.Player.Player;
import java.util.ArrayList;
/**An abstract class for defining common behaviour of every piece
 *
 */
public abstract class Piece {
    String pieceColor;
    //current position of piece
    Cell currentCell;
    public enum pieceType{
        BLACK_PAWN,
        BLACK_KING,
        BLACK_QUEEN,
        BLACK_BISHOP,
        BLACK_KNIGHT,
        BLACK_ROOK,
        BLACK_PRINCESS,
        BLACK_EMPRESS,
        BLACK_GRASSHOPPER,
        WHITE_PAWN,
        WHITE_KING,
        WHITE_QUEEN,
        WHITE_BISHOP,
        WHITE_KNIGHT,
        WHITE_ROOK,
        WHITE_PRINCESS,
        WHITE_EMPRESS,
        WHITE_GRASSHOPPER
    }
    public Piece(String color,Cell cell) {
        pieceColor=color;
        currentCell=cell;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell=cell;
    }
    /** determines if the piece can be moved to the destination cell
     *
     * @param b the board
     * @param nextCell the destination cell
     * @param p the player who is trying to perform the move
     * @return true if piece can be moved
     */
    public boolean canMoveTo(Board b,Cell nextCell, Player p) {

        ArrayList<Cell> validMoves = getValidMoves(b);
        // Destination cell can be reached using any allowed moves for the piece

        if (validMoves.contains(nextCell))
        {
            /* perform the move on a copy of the real board
               check if this puts the current player's king in check
               if it does, the move is not allowed */
            Player p3=new Player(p);
            Player p4=new Player(p.getOpponent());
            p3.setOpponent(p4);
            p4.setOpponent(p3);
            Board scenarioBoard= b.tempBoard(b,p,p.getOpponent(),p3,p4);
            Cell scenarioNextCell=scenarioBoard.getCells()[nextCell.getRank()][nextCell.getFile()];
            Cell destinationCell;
            Cell sourceCell;
            Player player;
            Board board;
            board=scenarioBoard;
            destinationCell=scenarioNextCell;
            sourceCell=board.getCells()[currentCell.getRank()][currentCell.getFile()];
            player=p3;
            movePiece(destinationCell, player, sourceCell, sourceCell.getPieceAtCell());
            if (!(p3.isCheck(scenarioBoard))) {
                return true;
            }
            else {
               //Move disallowed as it puts king in check
                return false;
            }
        }
        else {
            return false;
        }

    }
    /**move piece if it is a valid move
     *
     * @param b the board
     * @param nextCell the destination cell
     * @param p the player
     */
    public boolean moveTo(Board b,Cell nextCell, Player p,Piece piece) {
        //perform move if allowed
        if(canMoveTo(b,nextCell,p)) {
            movePiece(nextCell, p, b.getCells()[currentCell.getRank()][currentCell.getFile()], piece);
            return true;
        }
        return false;
    }
    /**change the position of piece on the model board
     *
     * @param destinationCell
     * @param player
     * @param startCell
     */
    public void movePiece(Cell destinationCell,Player player, Cell startCell,Piece piece) {
        //capture opponent's piece
        if (destinationCell.getIsOccupied()) {
            player.getOpponent().pieceCaptured(destinationCell.getPieceAtCell());
            destinationCell.getPieceAtCell().currentCell=null;
            destinationCell.setPiece(null);
        }
        startCell.removePiece();
        destinationCell.setPiece(piece);
        piece.currentCell=destinationCell;
    }
    //get list of allowed moves for the piece
    public ArrayList<Cell> getValidMoves(Board board) {
       return null;
    }
}
