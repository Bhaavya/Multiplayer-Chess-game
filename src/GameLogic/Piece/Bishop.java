package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/**Defines behaviour of a Bishop piece
 *
 */
public class Bishop extends Piece {

    public Bishop(String color,Cell cell) {
        super(color,cell);
    }
    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        validMoves=KnightBishopMoves.getBishopMoves(currentCell,validMoves,board,this);
        return validMoves;
    }
}
