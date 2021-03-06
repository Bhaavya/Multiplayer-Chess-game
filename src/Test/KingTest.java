package Test;
import GameLogic.Board.Cell;
import GameLogic.Board.SquareBoard;
import GameLogic.Piece.Piece;
import GameLogic.Player.Player;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class KingTest {
    public Piece.pieceType[][] formConfigArray(){
        Piece.pieceType configArray[][]=new Piece.pieceType[4][8];
        for (int j=0;j<8;j++){
            configArray[1][j]= Piece.pieceType.WHITE_PAWN;
            configArray[2][j]= Piece.pieceType.BLACK_PAWN;
        }
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
     * test moving to a valid location like knight and bishop
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
        cells[6][4].removePiece();
        //move forward
        Cell sourceCell = cells[7][4];
        Cell destinationCell = cells[6][4];
        testMove(board, p2, sourceCell, destinationCell);
        //move right
        cells[6][5].removePiece();
        sourceCell = cells[6][4];
        Piece piece=sourceCell.getPieceAtCell();
        destinationCell = cells[6][5];
        testMove(board, p2, sourceCell, destinationCell);

    }
    @Test
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
        //try to leap over
        sourceCell = cells[7][4];
        destinationCell = cells[6][4];
        testMoveFailure(board, p, sourceCell, destinationCell);

    }
    @Test
    /**
     * test attacking move
     */
    public void testCapture() {
        SquareBoard board = new SquareBoard(8);
        Player p1 = new Player("white", "teamA");
        Player p2 = new Player("black", "teamB");
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        board.modifyConfig(formConfigArray());
        board.setBoard(p1, p2);
        Cell cells[][] = board.getCells();
        cells[1][4].removePiece();
        Cell sourceCell= cells[0][4];
        Piece black_pawn = cells[6][4].getPieceAtCell();
        black_pawn.moveTo(board,cells[4][4],p2,black_pawn);
        for(int i=1;i<4;i++) {
            Piece piece = sourceCell.getPieceAtCell();
            piece.moveTo(board, cells[i][4], p1, piece);
            sourceCell= cells[i][4];
        }
        //capture black pawn
        Cell cellUnderAttack = cells[4][4];
        testMoveCapture(board, p2, cells[3][4], cellUnderAttack);
    }

    /**
     * helper function for testing valid move to empty space,piece should be removed from source and set at destination
     * @param board
     * @param player
     * @param sourceCell
     * @param destinationCell
     */
    public void testMove(SquareBoard board, Player player,  Cell sourceCell, Cell destinationCell) {
        Piece piece = sourceCell.getPieceAtCell();
        piece.moveTo(board, destinationCell, player, piece);
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
    /**
     * helper function for testing attacking moves, captured piece is removed from list of available pieces
     * @param board
     * @param player
     * @param sourceCell
     * @param destinationCell
     */
    public void testMoveCapture(SquareBoard board, Player player,  Cell sourceCell, Cell destinationCell) {
        ArrayList <Piece> piecesBeforeCapture = new ArrayList<Piece>(player.getOpponent().getAvailablePieces());
        Piece attackingPiece=sourceCell.getPieceAtCell();
        Piece attackedPiece=destinationCell.getPieceAtCell();
        piecesBeforeCapture.remove(attackedPiece);
        attackingPiece.moveTo(board, destinationCell, player, attackingPiece);
        assertEquals(piecesBeforeCapture, player.getOpponent().getAvailablePieces());
    }

}




