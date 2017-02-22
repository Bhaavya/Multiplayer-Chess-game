package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/** defines valid behaviour for a single cell move without leaps
 *
 */
public interface NoSkipMove {
    /** add move to list of valid moves if it is on board and does not move to cell occupied by friendly piece
     *
     * @param board
     * @param rank rank of destination cell
     * @param file file of destination cell
     * @param validMoves list of valid moves
     * @param piece piece to be moved
     * @return updated valid moves
     */
    static ArrayList<Cell> addValidMove(Board board, int rank, int file, ArrayList<Cell> validMoves,Piece piece) {

        if (rank < board.getRanks() && rank >= 0 && file < board.getFiles() && file >= 0)
        {   Cell nextCell = board.getCells()[rank][file];
            if ((nextCell.getIsOccupied() && !(nextCell.getPieceAtCell().pieceColor.equals(piece.getPieceColor()))) || !(nextCell.getIsOccupied()))
                validMoves.add(board.getCells()[rank][file]);
        }
        return validMoves;
    }
}
