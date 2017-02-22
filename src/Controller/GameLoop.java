package Controller;
import GameLogic.Board.SquareBoard;
import GameLogic.Player.Player;
import View.BoardView;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import GameLogic.Board.Cell;
import GameLogic.Piece.*;

/**
 * Controller class for game
 */
public class GameLoop {
    private SquareBoard board = new SquareBoard(8);
    private BoardView gui;
    private Player p1,p2;
    //private JPanel boardContainer;
    public enum PlayerInTurn {WHITE_TEAM, BLACK_TEAM}
    PlayerInTurn playerInTurn;
    private Piece pieceAtSource;
    private JButton sourceCellButton;
    private Cell sourceCell=null;
    private Piece.pieceType prevConfigArray[][];        /*Previous configuration array to keep track of special pieces*/
    private Icon sourceCellIcon;
    private Icon prevSourceIcon,prevDestIcon;
    private JButton prevSourceCellButton,prevDestinationCellButton;
    private Cell prevSourceCell,prevDestinationCell;
    private Piece prevPieceAtSource,prevPieceAtDestination;
    boolean isCaptureMove;
    public enum moveOutcomes{VALID_MOVE, WINNING_MOVE, CHECK_MOVE, INVALID_MOVE, WRONG_PIECE,NO_MOVE};          //all possible types of move

    /**
     * Sets up boards and players for the first time with default values
     */
    public HashMap <String,Piece.pieceType> gameInit() {
        p1 = new Player("white", "White Team");
        p2 = new Player("black", "Black Team");
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        Piece.pieceType configArray[][] = setStandardConfiguration();
        board.modifyConfig(configArray);
        prevConfigArray=configArray;
        HashMap<String,Piece.pieceType> configMap = new HashMap<>(board.getConfig());
        gameStart();
        return configMap;
    }

    /**
     * Start/restart round
     */
    public void gameStart() {
        board.setBoard(p1, p2);
        playerInTurn = PlayerInTurn.WHITE_TEAM;
    }
    /** Add listeners for menu buttons
    */
    public void setMenuListeners() {
        JButton forfeit = gui.getForfeit();
        JButton restartGame = gui.getRestartGame();
        JButton undo=gui.getUndo();
        JMenuItem changePlayerName= gui.getChangePlayerName();
        JMenuItem addSpecialPieces= gui.getAddSpecialPieces();
        JButton newMatch=gui.getNewMatch();
        JButton scoreBoard=gui.getScoreBoard();
        setForfeitListener(forfeit);
        setRestartListener(restartGame);
        setChangePlayerNameListener(changePlayerName);
        setSpecialPiecesListener(addSpecialPieces);
        setNewMatchListener(newMatch);
        setScoreBoardListener(scoreBoard);
        setUndo(undo);
    }
    /**Starts a new match. Previous scores are deleted
     *
     * @param newMatch
     */
    public void setNewMatchListener(JButton newMatch){
        newMatch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1.setScore(0);
                p2.setScore(0);
                p1.setName("White team");
                p2.setName("Black team");
                board.modifyConfig(setStandardConfiguration());
                gameStart();
            }
        });
    }

    /**
     * display current scores and pieces alive
     * @param scoreBoard score board button
     */
    public void setScoreBoardListener(JButton scoreBoard){
        scoreBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int whitePiecesCount[] = getPiecesAlive(p1);
                int blackPiecesCount[] = getPiecesAlive(p2);
                String score = "      " + p1.getName() + "  -   " + p2.getName() + "\n\n" + " Overall Score:  " + Integer.toString(p1.getScore()) + "      -        " + Integer.toString(p2.getScore())
                        + "\n\n Number of pieces alive:";
                score += "\n King:       " + whitePiecesCount[0] + "       -        " + blackPiecesCount[0];
                score += "\n Queen:    " + whitePiecesCount[1] + "       -        " + blackPiecesCount[1];
                score += "\n Bishop:    " + whitePiecesCount[2] + "       -        " + blackPiecesCount[2];
                score += "\n Knight:    " + whitePiecesCount[3] + "       -        " + blackPiecesCount[3];
                score += "\n Rook:      " + whitePiecesCount[6] + "       -        " + blackPiecesCount[6];
                score += "\n Pawn:      " + whitePiecesCount[4] + "       -        " + blackPiecesCount[4];
                score += "\n Empress: " + whitePiecesCount[5] + "       -        " + blackPiecesCount[5];
                score += "\n Princess: " + whitePiecesCount[7] + "       -        " + blackPiecesCount[7];
                score += "\n Grasshopper: " + whitePiecesCount[8] + "       -        " + blackPiecesCount[8];
                JOptionPane.showMessageDialog(gui.getWindow(), score, "Current score", JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    /**
     * returns number of alive pieces of of each piece type
     * @param p the player
     * @return
     */
    public int[] getPiecesAlive(Player p) {
        int[] stats = new int[9];
        for (Piece piece : p.getAvailablePieces()) {
            if (piece instanceof King) {
                stats[0]++;
            } else if (piece instanceof Queen) {
                stats[1]++;
            } else if (piece instanceof Bishop) {
                stats[2]++;
            } else if (piece instanceof Knight) {
                stats[3]++;
            } else if (piece instanceof Pawn) {
                stats[4]++;
            } else if (piece instanceof Empress) {
                stats[5]++;
            } else if (piece instanceof Rook) {
                stats[6]++;
            } else if (piece instanceof Princess)  {
                stats[7]++;
            }
            else{
                stats[8]++;
            }
        }
        return stats;
    }

    /**
     * Start a new round if player forfeits. The player in turn loses the round
     *
     * @param forfeit
     */
    public void setForfeitListener(JButton forfeit) {
        forfeit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int prevScore;
                String playerName;
                //increase score of player's opponent
                switch (playerInTurn) {
                    case BLACK_TEAM:
                        prevScore = p1.getScore();
                        p1.setScore(prevScore + 1);
                        playerName = p1.getName();
                        break;
                    default:
                        prevScore = p2.getScore();
                        p2.setScore(prevScore + 1);
                        playerName = p2.getName();
                        break;
                }

                JFrame window = gui.getWindow();
                String scoreMessage = playerName + " won last round!!! \n" + p1.getName() + " Score: " + Integer.toString(p1.getScore())
                        + "\n" + p2.getName() + " Score: " + Integer.toString(p2.getScore());
                JOptionPane.showMessageDialog(window, scoreMessage, "Current score", JOptionPane.PLAIN_MESSAGE);
                gameStart();

            }
        });
    }

    /**
     * Restart round if both players agree.Round results in draw
     *
     * @param restartGame
     */
    public void setRestartListener(JButton restartGame){
        restartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame window = gui.getWindow();
                String playerName;
                switch (playerInTurn) {
                    case BLACK_TEAM:
                        playerName = p1.getName();
                        break;
                    default:
                        playerName = p2.getName();
                        break;
                }
                //Both players must agree to restart round
                String message = "Does " + playerName + " agree to restart round? \nRound will result in draw.";
                int permission = JOptionPane.showConfirmDialog(window, message,
                        "Opponent confirmation required",
                        JOptionPane.YES_NO_OPTION
                );
                if (permission == JOptionPane.YES_OPTION) {
                    gameStart();
                }
            }
        });
    }

    /**
     * Change player names if they are unique
     *
     * @param changePlayerName
     */
    public void setChangePlayerNameListener(JMenuItem changePlayerName){
        changePlayerName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get Player Names
                String player1Name = JOptionPane.showInputDialog("Enter Player 1 Name");
                String player2Name = JOptionPane.showInputDialog("Enter Player 2 Name");
                //Player names must be unique
                if (player1Name != null && player2Name != null) {
                    if (player1Name.equals(player2Name)) {
                    JFrame window = gui.getWindow();
                    JOptionPane.showMessageDialog(window, "Player names must be different!", "Error changing names", JOptionPane.ERROR_MESSAGE);
                     } else {
                    p1.setName(player1Name);
                    p2.setName(player2Name);
                    }
                }
            }
        });
    }

    /**
     * define behaviour when addSpecialPieces option is selected
     *
     */
    public void setSpecialPiecesListener(JMenuItem addSpecialPieces){
        addSpecialPieces.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame window = gui.getWindow();
                JDialog addPieceDialog = new JDialog();
                JLabel message = new JLabel("Choose piece to be replaced with special piece", SwingConstants.CENTER);
                JButton submit = new JButton("Submit");
                String[] pieces = {"White Piece at A1", "White Piece at B1", "White Piece at C1", "White Piece at D1", "White Piece at F1", "White Piece at G1", "White Piece at H1",
                        "White Piece at A2", "White Piece at B2", "White Piece at C2", "White Piece at D2", "White Piece at E2", "White Piece at F2", "White Piece at G2", "White Piece at H2",
                        "Black Piece at A7", "Black Piece at B7", "Black Piece at C7", "Black Piece at D7", "Black Piece at E7", "Black Piece at F7", "Black Piece at G7", "Black Piece at H7",
                        "Black Piece at A8", "Black Piece at B8", "Black Piece at C8", "Black Piece at D8", "Black Piece at F8", "Black Piece at G8", "Black Piece at H8"};
                JComboBox replacePiece = new JComboBox(pieces);
                addPieceDialog.getContentPane().add(message, BorderLayout.NORTH);
                addPieceDialog.getContentPane().add(replacePiece, BorderLayout.CENTER);
                addPieceDialog.getContentPane().add(submit, BorderLayout.SOUTH);
                addPieceDialog.setLocationRelativeTo(window);
                addPieceDialog.setLocation(window.getWidth() / 2, window.getHeight() / 2);
                addPieceDialog.setSize(400, 250);
                addPieceDialog.setVisible(true);
                createReplaceWithDialog(submit, replacePiece, window, addPieceDialog);
            }
        });
    }

    /**
     * Dialog to choose special piece to be added
     * @param submit button which triggers the event to create dialog
     * @param replacePiece combo box specifying piece which is to be replaced
     * @param window window where dialog is to be placed
     * @param addPieceDialog dialog box which contains replacePiece
     */
    public void createReplaceWithDialog(JButton submit,JComboBox replacePiece,JFrame window,JDialog addPieceDialog) {
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel message = new JLabel("Choose special piece", SwingConstants.CENTER);
                JButton submit = new JButton("Replace");
                String pieceToReplace = (String) replacePiece.getSelectedItem();
                JDialog replaceWithDialog = new JDialog();
                JComboBox specialPieceList;
                if (pieceToReplace.contains("White")) {
                    String[] specialPieces = {"White Empress", "White Princess", "White Grasshopper"};
                    specialPieceList = new JComboBox(specialPieces);
                    replaceWithDialog.getContentPane().add(specialPieceList, BorderLayout.CENTER);
                } else {
                    String[] specialPieces = {"Black Empress", "Black Princess", "Black Grasshopper"};
                    specialPieceList = new JComboBox(specialPieces);
                    replaceWithDialog.getContentPane().add(specialPieceList, BorderLayout.CENTER);
                }
                replaceWithDialog.getContentPane().add(message, BorderLayout.NORTH);
                replaceWithDialog.getContentPane().add(submit, BorderLayout.SOUTH);
                replaceWithDialog.setLocationRelativeTo(window);
                replaceWithDialog.setLocation(window.getWidth() / 2, window.getHeight() / 2);
                replaceWithDialog.setSize(400, 250);
                addPieceDialog.setVisible(false);
                replaceWithDialog.setVisible(true);
                replace(submit, pieceToReplace, specialPieceList, replaceWithDialog);
            }
        });
    }


/**
 * Replaces pieceToReplace with selected piece in specialPieceList for all subsequent rounds
 * @param submit button clicked in replaceWithDialog
 * @param pieceToReplace
 * @param specialPieceList
 * @param  replaceWithDialog
 */
    public void replace(JButton submit,String pieceToReplace, JComboBox specialPieceList,JDialog replaceWithDialog){
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String replaceWith = (String) specialPieceList.getSelectedItem();
                replaceWithDialog.setVisible(false);
                Piece.pieceType configArray[][] = prevConfigArray;
                int[] positionOfReplacement = getPositionOfReplacement(pieceToReplace);
                int rank, file;
                rank = positionOfReplacement[0];
                file = positionOfReplacement[1];
                if (replaceWith.equals("White Empress")) {
                    configArray[rank][file] = Piece.pieceType.WHITE_EMPRESS;
                }
                else if (replaceWith.equals("White Princess")) {
                    configArray[rank][file] = Piece.pieceType.WHITE_PRINCESS;
                }
                else if (replaceWith.equals("White Grasshopper")) {
                    configArray[rank][file] = Piece.pieceType.WHITE_GRASSHOPPER;
                }
                else if (replaceWith.equals("Black Empress")) {
                    configArray[rank][file] = Piece.pieceType.BLACK_EMPRESS;
                }
                else if (replaceWith.equals("Black Grasshopper")) {
                    configArray[rank][file] = Piece.pieceType.WHITE_GRASSHOPPER;
                }
                else {
                    configArray[rank][file] = Piece.pieceType.BLACK_PRINCESS;
                }
                prevConfigArray = configArray;
                board.modifyConfig(configArray);
                gameStart();
            }
        });
    }

    /**
     * Gives position which needs to be modified in configuration array when special pieces are added
     * @param pieceToReplace string chosen in combo box
     * @return
     */
    public int[] getPositionOfReplacement(String pieceToReplace){
        int pos []= new int[2];
        int length=pieceToReplace.length();
        char secondLastChar= pieceToReplace.charAt(length-2);
        char lastChar= pieceToReplace.charAt(length-1);
        //second last character specifies column and last character specifies row
        pos[0]=getRowOfReplacement(lastChar);
        pos[1]=getColumnOfReplacement(secondLastChar);
        return pos;
    }

    /**
     * helper for getPositionOfReplacement
     * @param lastChar
     * @return
     */
    public int getRowOfReplacement(char lastChar){
        int row;
        switch(lastChar) {
            case '1':
                row = 0;
                break;
            case '2':
                row = 1;
                break;
            case '7':
                row = 2;
                break;
            default:
                row = 3;
                break;
        }
        return row;
    }

    /**
     * helper for getPositionOfReplacement
     * @param SecondLastChar
     * @return
     */
    public int getColumnOfReplacement(char SecondLastChar){
        int col;
        switch(SecondLastChar) {
            case 'A':
                col = 0;
                break;
            case 'B':
                col = 1;
                break;
            case 'C':
                col = 2;
                break;
            case 'D':
                col=3;
                break;
            case 'E':
                col=4;
                break;
            case 'F':
                col=5;
                break;
            case 'G':
                col=6;
                break;
            default:
                col= 7;
                break;
        }
        return col;
    }


    /**
     * define undo behaviour
     */
    public void setUndo(JButton undo) {
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prevSourceCellButton.setIcon(prevSourceIcon);
                Player currentPlayer;
                PlayerInTurn nextPlayerInTurn;
                //Return turn to player who performed undo
                switch (playerInTurn) {
                    case WHITE_TEAM:
                        currentPlayer = p2;
                        nextPlayerInTurn = PlayerInTurn.BLACK_TEAM;
                        break;
                    default:
                        currentPlayer = p1;
                        nextPlayerInTurn = PlayerInTurn.WHITE_TEAM;
                }
                playerInTurn = nextPlayerInTurn;
                restorePrevState(currentPlayer);
                undo.setEnabled(false);

            }
        });
    }
    /**
     * Restore model and view on undo move
     * @param currentPlayer
     */
    public void restorePrevState(Player currentPlayer){
        //if move was a capture move, return previous piece at destination cell
        if (isCaptureMove) {
            prevDestinationCellButton.setIcon(prevDestIcon);
            currentPlayer.addAvailablePiece(prevPieceAtDestination);
            prevDestinationCell.setPiece(prevPieceAtDestination);
            prevPieceAtDestination.setCurrentCell(prevDestinationCell);
        }
        //otherwise remove current piece from destination
        else{
            prevDestinationCellButton.setIcon(null);
            prevDestinationCell.removePiece();
        }
        //restore piece at current cell
        prevSourceCell.setPiece(prevPieceAtSource);
        prevPieceAtSource.setCurrentCell(prevSourceCell);
    }
    /**
     * set source cell buttons
     * @param sourceFile, sourceRank
     */
    public void startMove(int sourceRank, int sourceFile){
        //Other Player has started his turn. Undo not possible now
        //gui.getUndo().setEnabled(false);
        Cell [][] internalCells= board.getCells();
        sourceCell = internalCells[sourceRank][sourceFile];
        if(sourceCell!=null){
            pieceAtSource = sourceCell.getPieceAtCell();
            prevSourceCell=sourceCell;
            prevPieceAtSource=pieceAtSource;
        }

    }
    /**
     * determine outcome of move
     * @param destRank, destFile
     * @return
     */
    public moveOutcomes shouldMove(int destRank, int destFile, int player) {
        Cell[][] internalCells = board.getCells();
        Cell destinationCell = null;

        destinationCell = internalCells[destRank][destFile];
        Player currentPlayer;

        switch (player) {
            case 0:
                currentPlayer = p1;
                break;
            default:
                currentPlayer = p2;
        }

        //Store current state for allowing undo
        if (destinationCell != null) {
            prevDestinationCell=destinationCell;
            prevPieceAtDestination = destinationCell.getPieceAtCell();
            if(prevPieceAtDestination!=null) {
                if (!(prevPieceAtDestination.getPieceColor().equals(currentPlayer.getPlayerColor()))) {
                    isCaptureMove = true;
                }
            }
            else {
                isCaptureMove=false;
            }
        }

        if (pieceAtSource== null)
            return moveOutcomes.NO_MOVE;

        if ((currentPlayer.getPlayerColor()).equals(pieceAtSource.getPieceColor())) {
            if (!(pieceAtSource.moveTo(board, destinationCell, currentPlayer, pieceAtSource))) {
                return moveOutcomes.INVALID_MOVE;
            }
            return validMovePerformed(currentPlayer);

        }
        else
            return moveOutcomes.WRONG_PIECE;

    }
    /**
     * After a valid move is performed,switch turns, update score
     * @param currentPlayer
     * @return type of move
     */
    public moveOutcomes validMovePerformed(Player currentPlayer){
        //Switch turns
        switch(playerInTurn) {
            case BLACK_TEAM:
                playerInTurn = PlayerInTurn.WHITE_TEAM;
                break;
            default:
                playerInTurn = PlayerInTurn.BLACK_TEAM;
        }

        if(currentPlayer.getOpponent().playerLost(board)){
            gameStart();
            currentPlayer.setScore(currentPlayer.getScore()+1);
            return moveOutcomes.WINNING_MOVE;
        }
        if(currentPlayer.getOpponent().isCheck(board)){
            return moveOutcomes.CHECK_MOVE;
        }
        else{
            return moveOutcomes.VALID_MOVE;
        }

    }

    /**
     * Gives configuration array for standard board
     * @return
     */
    public Piece.pieceType [][] setStandardConfiguration(){
        Piece.pieceType configArray[][] = new Piece.pieceType[4][8];
        for (int j = 0; j < 8; j++) {
            configArray[1][j] = Piece.pieceType.WHITE_PAWN;
            configArray[2][j] = Piece.pieceType.BLACK_PAWN;
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

    public Player[] getPlayer(){
        Player players[]=new Player[2];
        players[0]=p1;
        players[1]=p2;
        return players;
    }

    public BoardView getView(){
        return gui;
    }

    public SquareBoard getModel(){
        return board;
    }

    public PlayerInTurn getPlayerInTurn(){
        return playerInTurn;
    }

    public String getLastPlayerName(){
        String name;
        switch (playerInTurn) {
            case WHITE_TEAM:
                name = p2.getName();
                break;
            default:
                name = p1.getName();
        }
        return name;

    }

    public String getOppName(){
        String name;
        switch (playerInTurn) {
            case WHITE_TEAM:
                name = p1.getName();
                break;
            default:
                name = p2.getName();
        }
        return name;

    }

    public int getLastPlayerScore(){
        int score;
        switch (playerInTurn) {
            case WHITE_TEAM:
                score = p2.getScore();
                break;
            default:
                score = p1.getScore();
        }
        return score;
    }

    public int getOppScore(){
        int score;
        switch (playerInTurn) {
            case WHITE_TEAM:
                score = p1.getScore();
                break;
            default:
                score = p2.getScore();
        }
        return score;
    }


}
