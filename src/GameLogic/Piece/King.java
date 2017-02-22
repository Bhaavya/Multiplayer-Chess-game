package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.*;
/**Defines behaviour of a King
 *
 */
public class King extends Piece  {
    public King(String color, Cell cell) {
        super(color, cell);
    }
    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        //King can move 1 cell , 8 possible ways
        for(int i=-1;i<2;i++){
            for(int j=-1;j<2;j++)
                if(!(i==0&&j==0))
                    validMoves=NoSkipMove.addValidMove(board,currentCell.getRank() + i,currentCell.getFile() + j, validMoves, this);
        }

        return validMoves;
    }
}