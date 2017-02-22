package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/**Defines behaviour of a Knight
 *
 */
public class Knight extends Piece  {
    public Knight(String color, Cell cell) {
        super(color, cell);
    }

    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<>();
        validMoves=KnightBishopMoves.getKnightMoves(currentCell,validMoves,board,this);
        return validMoves;
    }

}
