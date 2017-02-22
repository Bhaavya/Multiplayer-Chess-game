package GameLogic.Piece;

import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/**Defines behaviour of Queen
 *
 */
public class Queen extends Piece {
    public Queen(String color, Cell cell) {
        super(color, cell);
    }
    /**get valid moves in the left/downward direction
     *
     * @param board
     * @param startPos file/rank of cell below/left of current cell
     * @param leftOrDown LEFT/DOWN
     * @return valid moves
     */
    public ArrayList<Cell> getValidLeftDownMoves(Board board, int startPos,int leftOrDown) {
        return (MultiCellMove.getValidLeftDownMoves(board, startPos, leftOrDown, this,false));
    }
    /**get valid moves in the up/right direction
     *
     * @param board
     * @param startPos file/rank of cell above/right of current cell
     * @param upOrRight UP/RIGHT
     * @return valid moves
     */
    public ArrayList<Cell> getValidUpRightMoves(Board board, int startPos,int upOrRight) {
        return (MultiCellMove.getValidUpRightMoves(board, startPos, upOrRight,this,false));
    }
    /**get valid moves along lower left diagonal
     *
     * @param board
     * @param startRank rank of first cell in the lower left diagonal
     * @param startFile file of first cell in the lower left diagonal
     * @return valid moves
     */
    public ArrayList<Cell> getValidLeftDownDiagMoves(Board board, int startRank, int startFile) {
        return (MultiCellMove.getValidLeftDownDiagMoves(board, startRank, startFile, this,false));
    }
    /**get valid moves along upper right diagonal
     *
     * @param board
     * @param startRank rank of first cell in the upper right diagonal
     * @param startFile file of first cell in the upper right diagonal
     * @return valid moves
     */
    public ArrayList<Cell> getValidUpRightDiagMoves(Board board, int startRank, int startFile) {
        return (MultiCellMove.getValidUpRightDiagMoves(board, startRank, startFile, this,false));
    }
    /**get valid moves along upper left diagonal
     *
     * @param board
     * @param startRank rank of first cell in the upper left diagonal
     * @param startFile file of first cell in the upper left diagonal
     * @param startFile
     * @return valid moves
     */
    public ArrayList<Cell> getValidUpLeftDiagMoves(Board board, int startRank, int startFile) {
        return (MultiCellMove.getValidUpLeftDiagMoves(board, startRank, startFile, this,false));
    }
    /**get valid moves along lower right diagonal
     *
     * @param board
     * @param startRank rank of first cell in the lower right diagonal
     * @param startFile file of first cell in the lower right diagonal
     * @return valid moves
     */
    public ArrayList<Cell> getValidDownRightDiagMoves(Board board, int startRank, int startFile) {
        return (MultiCellMove.getValidDownRightDiagMoves(board, startRank, startFile, this,false));
    }
    @Override
    public ArrayList<Cell> getValidMoves(Board board) {
        ArrayList<Cell> validMoves = new ArrayList<>();
        validMoves.addAll(getValidLeftDownMoves(board, currentCell.getFile() - 1, MultiCellMove.LEFT));
        validMoves.addAll(getValidLeftDownMoves(board, currentCell.getRank() - 1, MultiCellMove.DOWN));
        validMoves.addAll(getValidLeftDownDiagMoves(board,  currentCell.getRank()-1, currentCell.getFile() - 1));
        validMoves.addAll(getValidUpRightMoves(board, currentCell.getFile() + 1, MultiCellMove.RIGHT));
        validMoves.addAll(getValidUpRightMoves(board, currentCell.getRank() + 1, MultiCellMove.UP));
        validMoves.addAll(getValidUpRightDiagMoves(board,currentCell.getRank() + 1,currentCell.getFile() + 1));
        validMoves.addAll(getValidUpLeftDiagMoves(board, currentCell.getRank() + 1, currentCell.getFile() - 1));
        validMoves.addAll(getValidDownRightDiagMoves(board,currentCell.getRank() - 1, currentCell.getFile() + 1));
        return  validMoves;
    }
}
