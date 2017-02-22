package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/** returns valid moves as defined for knight and bishop
 *
 */
public interface KnightBishopMoves {
     static ArrayList<Cell> getKnightMoves(Cell currentCell,ArrayList<Cell> validMoves,Board board,Piece piece){
        validMoves=NoSkipMove.addValidMove(board, currentCell.getRank() - 2, currentCell.getFile() - 1, validMoves, piece);
        validMoves=NoSkipMove.addValidMove(board,currentCell.getRank() - 1, currentCell.getFile() - 2, validMoves, piece);
        validMoves=NoSkipMove.addValidMove(board, currentCell.getRank() + 1,currentCell.getFile() - 2, validMoves, piece);
        validMoves=NoSkipMove.addValidMove(board,  currentCell.getRank() + 2, currentCell.getFile() - 1, validMoves, piece);
        validMoves=NoSkipMove.addValidMove(board, currentCell.getRank() - 2,currentCell.getFile() + 1, validMoves, piece);
        validMoves=NoSkipMove.addValidMove(board, currentCell.getRank() - 1, currentCell.getFile() + 2, validMoves, piece);
        validMoves=NoSkipMove.addValidMove(board,  currentCell.getRank() + 1,  currentCell.getFile() + 2, validMoves,piece);
        validMoves=NoSkipMove.addValidMove(board, currentCell.getRank() + 2, currentCell.getFile() + 1, validMoves,piece);
        return validMoves;
    }

    static ArrayList<Cell> getBishopMoves(Cell currentCell,ArrayList<Cell> validMoves,Board board,Piece piece) {
        validMoves.addAll(MultiCellMove.getValidLeftDownDiagMoves(board, currentCell.getRank() - 1, currentCell.getFile() - 1, piece,false));
        validMoves.addAll(MultiCellMove.getValidUpRightDiagMoves(board,currentCell.getRank() + 1, currentCell.getFile() + 1,  piece,false));
        validMoves.addAll(MultiCellMove.getValidUpLeftDiagMoves(board,currentCell.getRank() + 1, currentCell.getFile() - 1, piece,false));
        validMoves.addAll(MultiCellMove.getValidDownRightDiagMoves(board, currentCell.getRank() - 1, currentCell.getFile() + 1, piece,false));
        return validMoves;
    }
}
