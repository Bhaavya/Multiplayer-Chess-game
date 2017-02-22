package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/**Defines behaviour of Pawn
 *
 */
public class Pawn extends Piece {
    public Pawn(String color,Cell cell) {
        super(color,cell);
    }
    //check if cell coordinates are on board
    public boolean isOnBoard(int rank,int file,Board board) {
        if (1<=rank && rank<=board.getRanks() && 1<=rank && file <=board.getFiles())
            return true;
        return false;
    }
    /**check if the cell is occupied by an opponent's piece
     *
     * @param nextRank the rank of cell to be checked
     * @param nextFile the file of cell to be checked
     * @param board
     * @return
     */
    public boolean isValidCaptureMove(int nextRank,int nextFile,Board board) {
        Cell  nextCell= board.getCells()[nextRank][nextFile];
        if (isOnBoard(nextRank,nextFile,board)) {
            if (nextCell.getIsOccupied() && !(nextCell.getPieceAtCell().pieceColor.equals(pieceColor)))
                return true;
        }
        return false;
    }
    /** get valid moves for white pawn
     *
     * @param board
     * @param currRank current rank
     * @param currFile current  file
     * @return list of valid moves
     */
    public ArrayList<Cell> getValidWhitePawnMove(Board board,int currRank,int currFile) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        int nextRank,nextFile;
        Cell nextCell;
        int initialRank=1;
        nextRank=currRank+2;
        if(nextRank<board.getRanks()) {
            nextCell = board.getCells()[nextRank][currFile];
            // pawn can move 2 cells up on first move if both cells are empty
            if (isOnBoard(nextRank, currFile, board)) {
                if ((currentCell.getRank() == initialRank) && !(board.getCells()[currRank + 1][currFile].getIsOccupied()) && !(board.getCells()[currRank + 2][currFile].getIsOccupied())) {
                    validMoves.add(nextCell);
                }
            }
        }
        nextRank=currRank+1;
        if(nextRank<board.getRanks()) {
            nextCell = board.getCells()[nextRank][currFile];
            //pawn can move one cell up if cell is empty
            if (isOnBoard(nextRank, currFile, board)) {
                if (!(board.getCells()[currRank + 1][currFile].getIsOccupied())) {
                    validMoves.add(nextCell);
                }
            }
        }
        nextRank=currRank+1;
        nextFile=currFile+1;
        if(nextRank<board.getRanks()&&nextFile<board.getFiles()) {
            nextCell = board.getCells()[nextRank][nextFile];
            //if the cell diagonally above the current cell to the right is occupied by opponent's piece, move is allowed
            if (isValidCaptureMove(nextRank, nextFile, board))
                validMoves.add(nextCell);
        }
        nextRank=currRank+1;
        nextFile=currFile-1;
        if(nextRank<board.getRanks()&&nextFile>=0) {
            //if the cell diagonally above the current cell to the left is occupied by opponent's piece, move is allowed
            nextCell = board.getCells()[nextRank][nextFile];
            if (isValidCaptureMove(nextRank, nextFile, board))
                validMoves.add(nextCell);
        }
        return validMoves;
    }
    /** get valid moves for white pawn
     *
     * @param board
     * @param currRank current rank
     * @param currFile current  file
     * @return list of valid moves
     */
    public ArrayList<Cell> getValidBlackPawnMove(Board board,int currRank,int currFile) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        int nextRank,nextFile;
        Cell nextCell;
        int initialRank=6;
        nextRank=currRank-2;
        if (nextRank>=0) {
            nextCell = board.getCells()[nextRank][currFile];
            if (isOnBoard(nextRank, currFile, board)) {
                if ((currentCell.getRank() == initialRank) && !(board.getCells()[currRank - 1][currFile].getIsOccupied()) && !(board.getCells()[currRank - 2][currFile].getIsOccupied())) {
                    validMoves.add(nextCell);
                }
            }
        }
        nextRank = currRank - 1;
        if (nextRank>=0) {
            nextCell = board.getCells()[nextRank][currFile];
            if (isOnBoard(nextRank, currFile, board)) {
                if (!(board.getCells()[currRank - 1][currFile].getIsOccupied())) {
                    validMoves.add(nextCell);
                }
            }
        }
        nextRank=currRank-1;
        nextFile=currFile+1;
        if (nextRank>=0&& nextFile<board.getFiles()) {
            nextCell = board.getCells()[nextRank][nextFile];
            if (isValidCaptureMove(nextRank, nextFile, board))
                validMoves.add(nextCell);
        }
        nextRank=currRank-1;
        nextFile=currFile-1;
        if (nextRank>=0&& nextFile>=0) {
            nextCell = board.getCells()[nextRank][nextFile];
            if (isValidCaptureMove(nextRank, nextFile, board))
                validMoves.add(nextCell);
        }
        return validMoves;
    }
    @Override
    public ArrayList<Cell> getValidMoves(Board board)
    {   ArrayList<Cell> validMoves = new ArrayList<Cell>();
        int currRank=getCurrentCell().getRank();
        int currFile=getCurrentCell().getFile();
        if (pieceColor.equals("white"))
            validMoves.addAll(getValidWhitePawnMove(board,currRank,currFile));
        else
            validMoves.addAll(getValidBlackPawnMove(board,currRank,currFile));
        return validMoves;
    }
}
