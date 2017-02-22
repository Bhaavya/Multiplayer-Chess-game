package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/**Defines behaviour of Princess(Knight + Bishop)
 *
 */
public class Princess extends Piece {
    public Princess(String color,Cell cell){
        super(color,cell);
    }

    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<>();
        //get knight moves
        validMoves.addAll(KnightBishopMoves.getKnightMoves(currentCell,validMoves,board,this));
        //get bishop moves
        validMoves.addAll(KnightBishopMoves.getBishopMoves(currentCell,validMoves,board,this));
        return validMoves;
    }

}
