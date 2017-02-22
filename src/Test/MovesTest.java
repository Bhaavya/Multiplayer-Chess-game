package Test;
import GameLogic.Board.Cell;
import GameLogic.Board.SquareBoard;
import GameLogic.Piece.Piece;
import GameLogic.Player.Player;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;
public class MovesTest {
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
        //move white pawn 1 cell forward
        Cell sourceCell = cells[1][4];
        Cell destinationCell = cells[2][4];
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
        Player p=p1;
        //try to leap over pawn
        sourceCell=cells[0][5];
        destinationCell=cells[1][5];
        testMoveFailure(board, p, sourceCell, destinationCell);
        //trying an invalid move for king
        sourceCell=cells[0][4];
        destinationCell=cells[2][0];
        testMoveFailure(board, p, sourceCell, destinationCell);
        //moving out of the board
        sourceCell=cells[1][7];
        destinationCell=cells[1][8];
        testMoveFailure(board, p, sourceCell, destinationCell);
        //this move will put the king in check, hence should not be allowed
        Piece opQueen = cells[7][3].removePiece();
        cells[6][4].setPiece(opQueen);
        sourceCell = cells[6][4];
        destinationCell = cells[6][5];
        testMoveFailure(board, p2, sourceCell, destinationCell);
        cells[6][4].removePiece();
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
        Cell cellUnderAttack = cells[6][0];
        Cell sourceCell =cells[0][0];
        //pawn of player 1 is removed, exposing the pawn of opponent to the rook
        Piece rem_piece = cells[1][0].removePiece();
        p1.pieceCaptured(rem_piece);
        Piece attackingPiece = cells[0][0].getPieceAtCell();
        testMoveCapture(board, p1, sourceCell, cellUnderAttack);
    }
    @Test
    /**
     * test checkmate conditions
     */
    public void testCheck() {
        SquareBoard board = new SquareBoard(8);
        Player p1 = new Player("white", "teamA");
        Player p2 = new Player("black", "teamB");
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        board.modifyConfig(formConfigArray());
        board.setBoard(p1, p2);
        /*Testing fool's mate
        Moves are-
        1. f2 -> f3 ,e7->e5
        2.g2->g4,d8->h4
        Player 1's king is in checkmate
         */
        Cell cells[][] = board.getCells();
        Cell whiteMove1=cells[1][5];
        whiteMove1.getPieceAtCell().movePiece(cells[2][5], p1, whiteMove1, whiteMove1.getPieceAtCell());
        Cell blackMove1=cells[6][4];
        blackMove1.getPieceAtCell().movePiece(cells[4][4], p2, blackMove1, blackMove1.getPieceAtCell());
        Cell whiteMove2=cells[1][6];
        whiteMove2.getPieceAtCell().movePiece(cells[3][6], p1, whiteMove2, whiteMove2.getPieceAtCell());
        Cell blackMove2=cells[7][3];
        testMoveCheckmate(board, p2,blackMove2, cells[3][7]);
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
        assertEquals(null,sourceCell.getPieceAtCell());
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
        boolean result=piece.moveTo(board,destinationCell, player,piece);
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
    /**
     * P1 is in checkmate and playerLost function should evaluate to true
     * @param board
     * @param player
     * @param sourceCell
     * @param destinationCell
     */
    public void testMoveCheckmate(SquareBoard board, Player player,  Cell sourceCell, Cell destinationCell) {
        sourceCell.getPieceAtCell().movePiece(destinationCell, player, sourceCell, sourceCell.getPieceAtCell());
        assertEquals(true, player.getOpponent().playerLost(board));
    }
}
