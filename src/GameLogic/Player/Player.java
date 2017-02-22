package GameLogic.Player;
import GameLogic.Board.Board;
import GameLogic.Board.Cell;
import GameLogic.Piece.King;
import GameLogic.Piece.Piece;

import java.util.ArrayList;
/**Represents a player of the game
 *
 */
public class Player {
    ArrayList<Piece> availablePieces= null;//list of  pieces
    String name;
    String playerColor;
    Player opponent=null;
    int score;

    public Player(String playerColor,String name) {
        this.playerColor=playerColor;
        this.name=name;
        score=0;
    }

    
    public Player(Player p){
        this.playerColor=p.playerColor;
        this.name=p.name;
        this.score=p.score;
    }

    public void setOpponent(Player op) {
        opponent=op;
    }

    public void setScore(int score){
        this.score=score;
    }

    public int getScore(){
        return score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public Player getOpponent() {
        return opponent;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setAvailablePieces(ArrayList<Piece> availablePieces) {
       this.availablePieces=new ArrayList<>(availablePieces);
    }

    public ArrayList<Piece> getAvailablePieces() {
        return availablePieces;
    }

    public void addAvailablePiece(Piece piece){availablePieces.add(piece);}

    //get current position of player's king
    public Cell getKingPos () {
        for (Piece piece:availablePieces) {
            if (King.class.isInstance(piece)) {
                return piece.getCurrentCell();
            }
        }
        return null;
    }
    //remove piece from list of available pieces
    public void pieceCaptured(Piece piece) {
        availablePieces.remove(piece);
    }
    //determine if player is in check
    public boolean isCheck(Board board) {
        for (Piece piece: opponent.availablePieces) {
            for(Cell opMove:piece.getValidMoves(board)) {
                if (opMove.getRank()==getKingPos().getRank()&& opMove.getFile()==getKingPos().getFile()) {
                    return true;
                }
            }
        }
        return false;
    }
    //determine if player is in checkmate
    public boolean playerLost(Board board) {
        ArrayList<Piece> allPieces = new ArrayList<Piece>(getAvailablePieces());
        for (Piece piece: allPieces){
            if (piece instanceof King){
                allPieces.remove(piece);
                break;
            }
        }
        for (Piece piece: allPieces){
            for (Cell validMove:piece.getValidMoves(board)) {
                Player p3=new Player(this.playerColor,this.name);
                p3.setAvailablePieces(this.getAvailablePieces());
                Player p4=new Player(this.getOpponent().playerColor,this.getOpponent().name);
                p4.setAvailablePieces(this.getOpponent().getAvailablePieces());
                p3.setOpponent(p4);
                p4.setOpponent(p3);
                Board scenarioBoard= board.tempBoard(board,this,this.getOpponent(),p3,p4);
                Cell scenarioNextCell=scenarioBoard.getCells()[validMove.getRank()][validMove.getFile()];
                Cell destinationCell;
                Cell sourceCell;
                Player player;
                destinationCell=scenarioNextCell;
                Cell currentCell = piece.getCurrentCell();
                sourceCell=scenarioBoard.getCells()[currentCell.getRank()][currentCell.getFile()];
                player=p3;
                piece.movePiece(destinationCell, player, sourceCell, sourceCell.getPieceAtCell());
                if (!isCheck(scenarioBoard)){
                    return false;
                }

            }
        }
        return true;
    }
}
