package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import GameLogic.Piece.Piece;

import java.util.ArrayList;
/**Defines behaviour of a Rook
 *
 */
public class Rook extends Piece{

    public Rook(String color,Cell cell) {
        super(color,cell);
    }
    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        validMoves.addAll(MultiCellMove.getValidLeftDownMoves(board, currentCell.getFile()-1, MultiCellMove.LEFT,this,false));
        validMoves.addAll(MultiCellMove.getValidLeftDownMoves(board, currentCell.getRank()-1, MultiCellMove.DOWN,this,false));
        validMoves.addAll(MultiCellMove.getValidUpRightMoves(board, currentCell.getFile()+1, MultiCellMove.RIGHT,this,false));
        validMoves.addAll(MultiCellMove.getValidUpRightMoves(board, currentCell.getRank()+1,MultiCellMove.UP , this,false));
        return validMoves;
    }
}