package GameLogic.Piece;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import java.util.ArrayList;
/** defines behaviour for pieces which can move multiple cells at a time but no leaping
 *
 */
public interface MultiCellMove {
    int EMPTY = 0;
    int FRIEND_PIECE = 1;
    int OPPONENT_PIECE = 2;
    int LEFT = 2;
    int DOWN = 1;
    int UP = 3;
    int RIGHT = 4;
    /**checks if given cell is occupied
     *
     * @param nextCell the cell whose occupancy status is being tested
     * @param piece piece at the current cell
     * @return
     */
     static int getNextCellOccupancy(Cell nextCell, Piece piece) {
        if (nextCell.getIsOccupied() && !(nextCell.getPieceAtCell().pieceColor.equals(piece.getPieceColor())))
            return OPPONENT_PIECE;
        else if (nextCell.getIsOccupied())
            return FRIEND_PIECE;
        else
            return EMPTY;
    }
    /**
     * get valid moves down the right diagonal
     *
     * @param board
     * @param startRank rank of first cell on the lower right diagonal
     * @param startFile file of first cell on the lower right diagonal
     * @param piece     piece to be moved
     * @return
     */
     static ArrayList<Cell> getValidDownRightDiagMoves(Board board, int startRank, int startFile, Piece piece,boolean sp) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        Cell nextCell;
        int nextRank = startRank;
        int nextFile = startFile;
         //for special piece, find an occupied cell and then continue normal moves
         if (sp){
             while (nextRank >= 0 && nextFile < board.getFiles()) {
                 nextCell = board.getCells()[nextRank][nextFile];
                 nextFile++;
                 nextRank--;
                 if (getNextCellOccupancy(nextCell, piece) !=EMPTY)
                     break;
             }
         }
        /* add  cells below on the right diagonal until an occupied cell is reached
        if that cell occupied by an opponent's piece, capture it
         */
        while (nextRank >= 0 && nextFile < board.getFiles()) {
            nextCell = board.getCells()[nextRank][nextFile];
            if(!canContinueMoving(nextCell,piece,validMoves))
                break;
            nextFile++;
            nextRank--;
        }
        return validMoves;
    }
    /**
     * get valid moves down along the left diagonal
     *
     * @param board
     * @param startRank rank of cell below the current cell on the left diagonal
     * @param startFile file of cell below the current cell on the left diagonal
     * @param piece
     * @return
     */
     static ArrayList<Cell> getValidLeftDownDiagMoves(Board board, int startRank, int startFile, Piece piece, boolean sp) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        Cell nextCell;
        int nextRank = startRank;
        int nextFile = startFile;
        //for special piece, find an occupied cell and then continue normal moves
        if (sp){
            while (nextRank >= 0 && nextFile >= 0) {
                nextCell = board.getCells()[nextRank][nextFile];
                nextRank--;
                nextFile--;
                if (getNextCellOccupancy(nextCell, piece) !=EMPTY)
                    break;
            }
        }
        /*add  cells below on the left diagonal until an occupied cell is reached
        if that cell occupied by an opponent's piece, capture it
         */
        while (nextRank >= 0 && nextFile >= 0) {
            nextCell = board.getCells()[nextRank][nextFile];
            if(!canContinueMoving(nextCell,piece,validMoves))
                break;
            nextRank--;
            nextFile--;
        }
        return validMoves;
    }
    /**
     * get valid moves upward along the upper right diagonal
     *
     * @param board
     * @param startRank rank of cell above the current cell on the right diagonal
     * @param startFile file of cell above the current cell on the right diagonal
     * @return
     */
     static ArrayList<Cell> getValidUpRightDiagMoves(Board board, int startRank, int startFile, Piece piece,boolean sp) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        Cell nextCell;
        int nextRank = startRank;
        int nextFile = startFile;
        //for special piece, find an occupied cell and then continue normal moves
        if (sp){
             while (nextRank < board.getRanks() && nextFile < board.getFiles()) {
                 nextCell = board.getCells()[nextRank][nextFile];
                 nextFile++;
                 nextRank++;
                 if (getNextCellOccupancy(nextCell, piece) !=EMPTY)
                     break;
             }
        }
        /* add  cells above on the left diagonal until an occupied cell is reached
        if that cell occupied by an opponent's piece, capture it
         */
        while (nextRank < board.getRanks() && nextFile < board.getFiles()) {
            nextCell = board.getCells()[nextRank][nextFile];
            if(!canContinueMoving(nextCell,piece,validMoves))
                break;
            nextFile++;
            nextRank++;
        }
        return validMoves;
    }
    /**
     * get valid moves upward along the left diagonal
     *
     * @param board
     * @param startRank rank of cell above the current cell on the left diagonal
     * @param startFile file of cell above the current cell on the left diagonal
     * @param
     * @return
     */
    static ArrayList<Cell> getValidUpLeftDiagMoves(Board board, int startRank, int startFile, Piece piece, boolean sp) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        Cell nextCell;
        int nextRank = startRank;
        int nextFile = startFile;
        //for special piece, find an occupied cell and then continue normal moves
        if (sp){
            while (nextRank < board.getRanks() && nextFile >= 0) {
                nextCell = board.getCells()[nextRank][nextFile];
                nextFile--;
                nextRank++;
                if (getNextCellOccupancy(nextCell, piece) !=EMPTY)
                    break;
            }
        }
        /* add  cells above on the left diagonal until an occupied cell is reached
        if that cell occupied by an opponent's piece, capture it
         */
        while (nextRank < board.getRanks() && nextFile >= 0) {
            nextCell = board.getCells()[nextRank][nextFile];
            if(!canContinueMoving(nextCell,piece,validMoves))
                break;
            nextFile--;
            nextRank++;
        }
        return validMoves;
    }
    /**
     * generates list of allowed moves towards left or down
     *
     * @param board
     * @param startPos   starting rank/file
     * @param leftOrDown LEFT/DOWN
     * @return list of valid destination cells
     */
     static ArrayList<Cell> getValidLeftDownMoves(Board board, int startPos, int leftOrDown, Piece piece, boolean sp) {
        ArrayList<Cell> validMoves = new ArrayList<Cell>();
        Cell nextCell;
        int nextPos = startPos;
        int rank,file;
        //for special piece, find an occupied cell and then continue normal moves
        if (sp){
                while (nextPos >= 0) {
                    rank = getNextCellPos(leftOrDown, piece, nextPos)[0];
                    file = getNextCellPos(leftOrDown, piece, nextPos)[1];
                    nextCell = board.getCells()[rank][file];
                    nextPos--;
                    if (getNextCellOccupancy(nextCell, piece) !=EMPTY)
                        break;
                }
        }
        /* add  cells until an occupied cell is reached
        if that cell occupied by an opponent's piece, capture it
         */
        while (nextPos >= 0) {
                rank = getNextCellPos(leftOrDown, piece, nextPos)[0];
                file = getNextCellPos(leftOrDown, piece, nextPos)[1];
                nextCell = board.getCells()[rank][file];
                if(!canContinueMoving(nextCell,piece,validMoves))
                    break;
                nextPos--;
        }
         return validMoves;

    }
    /**
     * generates list of allowed moves towards right or up
     *
     * @param board
     * @param startPos  starting rank/file
     * @param upOrRight UP/RIGHT
     * @return list of allowed destination cells
     */
     static ArrayList<Cell> getValidUpRightMoves(Board board, int startPos, int upOrRight, Piece piece, boolean sp) {
         ArrayList<Cell> validMoves = new ArrayList<Cell>();
         Cell nextCell;
         int nextPos = startPos;
         int rank, file;
         int maxMove;
         if (upOrRight == UP)
             maxMove = board.getRanks();
         else
            maxMove = board.getFiles();
         //for special piece, find an occupied cell and then continue normal moves
         if (sp) {
                 while (nextPos < maxMove) {
                     rank = getNextCellPos(upOrRight, piece, nextPos)[0];
                     file = getNextCellPos(upOrRight, piece, nextPos)[1];
                     nextCell = board.getCells()[rank][file];
                     nextPos++;
                     if (getNextCellOccupancy(nextCell, piece) != EMPTY)
                         break;
                 }
         }
/*       /* add  cells  until an occupied cell is reached
         if that cell occupied by an opponent's piece, capture it
         */
         while (nextPos < maxMove) {
                 rank = getNextCellPos(upOrRight, piece, nextPos)[0];
                 file = getNextCellPos(upOrRight, piece, nextPos)[1];
                 nextCell = board.getCells()[rank][file];
                 if(!canContinueMoving(nextCell,piece,validMoves))
                     break;
                 nextPos++;
         }
         return validMoves;
     }

    static boolean canContinueMoving(Cell nextCell, Piece piece, ArrayList<Cell> validMoves) {
        if (getNextCellOccupancy(nextCell, piece) == OPPONENT_PIECE) {
            validMoves.add(nextCell);
            return false;

        }
        else if (getNextCellOccupancy(nextCell, piece) == FRIEND_PIECE)
            return false;
        else
            validMoves.add(nextCell);
        return true;
    }

    static int [] getNextCellPos(int direction, Piece piece, int nextPos) {
        int nextCellPos[]=new int[2];
        if (direction == DOWN || direction == UP) {
            nextCellPos[0] = nextPos;
            nextCellPos[1] = piece.getCurrentCell().getFile();
        }
        else {
            nextCellPos[0] = piece.getCurrentCell().getRank();
            nextCellPos[1] = nextPos;
        }
        return nextCellPos;
    }



}