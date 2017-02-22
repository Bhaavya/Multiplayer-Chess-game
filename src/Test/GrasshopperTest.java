package Test;
import GameLogic.Board.Cell;
import GameLogic.Board.SquareBoard;
import GameLogic.Piece.Piece;
import GameLogic.Player.Player;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;
public class GrasshopperTest {
    /**
     * helper function to create configuration array for initial piece positions
     * @return
     */

    public Piece.pieceType[][] formConfigArray(){
        Piece.pieceType configArray[][]=new Piece.pieceType[4][8];
        for (int j=0;j<8;j++){
            configArray[1][j]= Piece.pieceType.WHITE_PAWN;
            configArray[2][j]= Piece.pieceType.BLACK_PAWN;
        }
        configArray[1][0] =Piece.pieceType.WHITE_GRASSHOPPER;
        configArray[0][0]=Piece.pieceType.WHITE_ROOK;
        configArray[0][7]=Piece.pieceType.WHITE_ROOK;
        configArray[0][1]=Piece.pieceType.WHITE_KNIGHT;
        configArray[0][6]=Piece.pieceType.WHITE_KNIGHT;
        configArray[0][2]=Piece.pieceType.WHITE_BISHOP;
        configArray[0][5]=Piece.pieceType.WHITE_BISHOP;
        configArray[0][3]=Piece.pieceType.WHITE_QUEEN;
        configArray[0][4]=Piece.pieceType.WHITE_KING;
        configArray[3][0]=Piece.pieceType.BLACK_ROOK;
        configArray[3][7]=Piece.pieceType.BLACK_ROOK;
        configArray[3][1]=Piece.pieceType.BLACK_KNIGHT;
        configArray[3][6]=Piece.pieceType.BLACK_KNIGHT;
        configArray[3][2]=Piece.pieceType.BLACK_BISHOP;
        configArray[3][5]= Piece.pieceType.BLACK_BISHOP;
        configArray[3][3]=Piece.pieceType.BLACK_QUEEN;
        configArray[3][4]= Piece.pieceType.BLACK_KING;
        return configArray;
    }
    @Test
    /**
     * test moving to a valid location
     */
    public void testMoveToEmptySpot() {
        SquareBoard board = new SquareBoard(8);
        Player p1 = new Player("white", "teamA");
        Player p2 = new Player("black", "teamB");
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        board.modifyConfig(formConfigArray());
        board.setBoard(p1, p2);
        Cell cells[][] = board.getCells();
        cells[6][0].removePiece();
        //test leaping over black rook
        Piece piece=cells[7][0].getPieceAtCell();
        piece.moveTo(board,cells[5][0], p2,piece);
        Cell sourceCell = cells[1][0];
        Cell destinationCell = cells[6][0];
        testMove(board, p1, sourceCell, destinationCell);

    }
    @Test(expected=ArrayIndexOutOfBoundsException.class )
    /**
     * test invalid move
     */
    public void invalidMovesTest() {
        SquareBoard board = new SquareBoard(8);
        Player p1 = new Player("white", "teamA");
        Player p2 = new Player("black", "teamB");
        board.modifyConfig(formConfigArray());
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        board.setBoard(p1, p2);
        Cell cells[][] = board.getCells();
        Cell sourceCell;
        Cell destinationCell;
        Player p = p1;
        //try to move diagonally without any piece in between
        sourceCell = cells[1][0];
        destinationCell = cells[3][2];
        testMoveFailure(board, p, sourceCell, destinationCell);
        //moving out of the board
        sourceCell = cells[1][0];
        destinationCell = cells[1][8];
        testMoveFailure(board, p, sourceCell, destinationCell);
    }

    /**
     * helper function for testing valid move to empty space,piece should be removed from source and set at destination
     * @param board
     * @param player
     * @param sourceCell
     * @param destinationCell
     */
    public void testMove(SquareBoard board, Player player,  Cell sourceCell, Cell destinationCell) {
        Piece piece=sourceCell.getPieceAtCell();
        piece.moveTo(board,destinationCell, player,piece);
        assertEquals(piece, destinationCell.getPieceAtCell());
        assertEquals(null, sourceCell.getPieceAtCell());
    }
    /**
     * helper function for testing invalid moves, move should be unsuccessful
     * @param board
     * @param player
     * @param sourceCell
     * @param destinationCell
     */
    public void testMoveFailure(SquareBoard board, Player player,  Cell sourceCell, Cell destinationCell) {
        Piece piece=sourceCell.getPieceAtCell();
        boolean result=piece.moveTo(board, destinationCell, player, piece);
        assertEquals(false,result );
    }

}