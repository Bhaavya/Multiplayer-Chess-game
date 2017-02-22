package Test;
import GameLogic.Board.Board;
import GameLogic.Piece.Piece;
import GameLogic.Player.Player;
import GameLogic.Board.SquareBoard;
import GameLogic.Board.Cell;
import org.junit.Test;
import static org.junit.Assert.*;
//tests if white and black pieces are in correct ranks and other ranks are empty
public class BoardTest {
    @Test
    public void testSetUp() throws Exception {
        SquareBoard board = new SquareBoard(8);
        Player p1 = new Player("white","teamA");
        Player p2 = new Player("black","teamB");
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        Piece.pieceType configArray[][]=new Piece.pieceType[4][8];
        for (int j=0;j<8;j++){
            configArray[1][j]= Piece.pieceType.WHITE_PAWN;
            configArray[2][j]= Piece.pieceType.BLACK_PAWN;
        }
        configArray[0][0]= Piece.pieceType.WHITE_ROOK;
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
        board.modifyConfig(configArray);
        board.setBoard(p1, p2);
        Cell cells[][]=board.getCells();
        for(int rank = 2; rank < 6; rank++){
            for(int file = 0; file < board.getFiles(); file++){

                assertEquals(null, cells[rank][file].getPieceAtCell());
            }
        }
        /* Testing WHITE pieces Positions on a starting board */
        for (int rank=0;rank<2;rank++) {

            for (int file =0; file < 8; file++) {
                assertNotNull(cells[rank][file].getPieceAtCell());
                assertEquals("white", cells[rank][file].getPieceAtCell().getPieceColor());
            }
        }
        /* Testing BLACK pieces Positions on a starting board */
        for (int rank=6;rank<7;rank++) {

            for (int file = 0; file < 7; file++) {
                assertNotNull(cells[rank][file].getPieceAtCell());
                assertEquals("black", cells[rank][file].getPieceAtCell().getPieceColor());
            }
        }
    }
}


