package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/**Defines behaviour of Empress(Rook+Knight)
 *
 */
public class Empress extends Piece {
    public Empress(String color,Cell cell){
        super(color,cell);
    }

    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<>();
        //add knight moves
        validMoves.addAll(KnightBishopMoves.getKnightMoves(currentCell,validMoves,board,this));
        //add rook moves
        validMoves.addAll(MultiCellMove.getValidLeftDownMoves(board,  currentCell.getFile() - 1, MultiCellMove.LEFT,this,false));
        validMoves.addAll(MultiCellMove.getValidLeftDownMoves(board,currentCell.getRank() - 1, MultiCellMove.DOWN,this,false));
        validMoves.addAll(MultiCellMove.getValidUpRightMoves(board, currentCell.getFile() + 1, MultiCellMove.RIGHT,this,false));
        validMoves.addAll(MultiCellMove.getValidUpRightMoves(board,currentCell.getRank() + 1, MultiCellMove.UP,this,false));
        return validMoves;
    }
}
