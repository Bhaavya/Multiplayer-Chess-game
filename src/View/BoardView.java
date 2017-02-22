package View;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;

import GameLogic.Piece.Piece;
import GameLogic.Board.SquareBoard;

/**
 * User Interface for a standard 8 by 8 Chess board
 */
public class BoardView  {
    private JButton cells[][]=new JButton[8][8];
    private JFrame window;
    //Specifies white and black ranks
    private enum pieceRanks{FIRST_WHITE_RANK(0),SECOND_WHITE_RANK(1),FIRST_BLACK_RANK(6),SECOND_BLACK_RANK(7);
        int rank;
    pieceRanks(int rank) {
        this.rank= rank;
    }
    };
    private Color darkColor=new Color(0x5D5354);
    private Color lightColor=Color.white;
    private final JButton forfeit = new JButton("Forfeit");
    private final JButton restartGame= new JButton("Restart Round");
    private final JButton settings=new JButton("Settings");
    private final JButton scoreBoard=new JButton("Score Board");
    private final JButton newMatch=new JButton("New Match");
    private final JPopupMenu settingsMenu= new JPopupMenu("Settings");
    private final JMenuItem changePlayerName= new JMenuItem("Change Player names");
    private final JMenuItem addSpecialPieces = new JMenuItem("Add special pieces");
    private final JButton undo= new JButton("Undo last move");                                    /*Undo allowed before opponent moves*/
    private JPanel menuContainer,boardContainer;
    private JSplitPane gameContainer;
    //enum for piece image urls
    public enum pieceImgLoc {
        BLACK_PAWN("/Images/BlackPawn.png"),
        BLACK_KING("/Images/BlackKing.png"),
        BLACK_QUEEN("/Images/BlackQueen.png"),
        BLACK_BISHOP("/Images/BlackBishop.png"),
        BLACK_KNIGHT("/Images/BlackKnight.png"),
        BLACK_ROOK("/Images/BlackRook.png"),
        BLACK_PRINCESS("/Images/BlackPrincess.png"),
        BLACK_EMPRESS("/Images/BlackEmpress.png"),
        BLACK_GRASSHOPPER("/Images/BlackGrasshopper.png"),
        WHITE_PAWN("/Images/WhitePawn.png"),
        WHITE_KING("/Images/WhiteKing.png"),
        WHITE_QUEEN("/Images/WhiteQueen.png"),
        WHITE_BISHOP("/Images/WhiteBishop.png"),
        WHITE_KNIGHT("/Images/WhiteKnight.png"),
        WHITE_ROOK("/Images/WhiteRook.png"),
        WHITE_PRINCESS("/Images/WhitePrincess.png"),
        WHITE_EMPRESS("/Images/WhiteEmpress.png"),
        WHITE_GRASSHOPPER("/Images/WhiteGrasshopper.png");

        private String loc;
        pieceImgLoc(String loc) {
            this.loc=loc;

        }
    }

    public BoardView(HashMap configMap, String title) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //silently ignore
        }

        window = new JFrame(title);
        window.setSize(1000, 1000);
        boardContainer = setUpBoard(configMap);
        menuContainer= setUpMenu();
        gameContainer= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,menuContainer,boardContainer);
        window.setVisible(true);
        window.setContentPane(gameContainer);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    /** set up the chess board pane with appropriate pieces based on configuration array
     *@param configMap
     * @return
     */
    public JPanel setUpBoard(HashMap configMap) {
        JPanel boardContainer = new JPanel();
        boardContainer.setMinimumSize(new Dimension(0, 0));
        boardContainer.setLayout(new GridLayout(0, 8, 0, 0));
        //add colored buttons representing cells on board
        for (int rank=0;rank<8;rank++){
            for(int file=0;file<8;file++) {
                cells[rank][file] = new JButton();
                //compute color of cell
                if ((rank + file) % 2 == 0) {
                    cells[rank][file].setBackground(lightColor);
                } else
                    cells[rank][file].setBackground(darkColor);
                cells[rank][file].setOpaque(true);
                cells[rank][file].setBorderPainted(false);
                //set piece image at cells
                ImageIcon pieceImage = null;
                if (rank == pieceRanks.FIRST_WHITE_RANK.rank || rank == pieceRanks.SECOND_WHITE_RANK.rank)
                    pieceImage = setBlackPiece(rank, file, configMap);
                else if (rank ==pieceRanks.FIRST_BLACK_RANK.rank|| rank == pieceRanks.SECOND_BLACK_RANK.rank)
                    pieceImage = setWhitePiece(rank, file, configMap);
                if (pieceImage != null)
                    cells[rank][file].setIcon(pieceImage);
                boardContainer.add(cells[rank][file]);
            }
        }

        return boardContainer;
    }
    /** sets image of appropriate black piece at given position
     *
     * @param rank
     * @param file
     * @param configMap
     * @return
     */
    public ImageIcon setBlackPiece(int rank,int file,HashMap configMap){
        ImageIcon pieceImage=null;
        String imgLoc=null;
        String cell;
        cell=Integer.toString(7-rank) + Integer.toString(file);
        //piece to be positioned at cell
        Piece.pieceType pieceType= (Piece.pieceType) configMap.get(cell);
        try {
            switch (pieceType) {
                case BLACK_BISHOP:
                    imgLoc = pieceImgLoc.BLACK_BISHOP.loc;
                    break;
                case BLACK_EMPRESS:
                    imgLoc = pieceImgLoc.BLACK_EMPRESS.loc;
                    break;
                case BLACK_ROOK:
                    imgLoc = pieceImgLoc.BLACK_ROOK.loc;
                    break;
                case BLACK_KING:
                    imgLoc = pieceImgLoc.BLACK_KING.loc;
                    break;
                case BLACK_QUEEN:
                    imgLoc = pieceImgLoc.BLACK_QUEEN.loc;
                    break;
                case BLACK_PAWN:
                    imgLoc = pieceImgLoc.BLACK_PAWN.loc;
                    break;
                case BLACK_KNIGHT:
                    imgLoc = pieceImgLoc.BLACK_KNIGHT.loc;
                    break;
                case BLACK_PRINCESS:
                    imgLoc = pieceImgLoc.BLACK_PRINCESS.loc;
                    break;
                case BLACK_GRASSHOPPER:
                    imgLoc = pieceImgLoc.BLACK_GRASSHOPPER.loc;
                    break;
            }
            if (imgLoc != null) {
                pieceImage = new ImageIcon(getClass().getResource(imgLoc));
            }
        }
        catch (Exception e){
                System.out.println("Image source unavailable");
            }
        return pieceImage;
    }
    /** sets image of appropriate white piece at given position
     *
     * @param rank
     * @param file
     * @param configMap
     * @return
     */
    public ImageIcon setWhitePiece(int rank,int file,HashMap configMap){
        ImageIcon pieceImage=null;
        String imgLoc=null;
        String cell;
        cell=Integer.toString(7- rank) + Integer.toString(file);
        //piece to be positioned at cell
        Piece.pieceType pieceType= (Piece.pieceType) configMap.get(cell);
        try {
            switch (pieceType) {
                case WHITE_BISHOP:
                    imgLoc = pieceImgLoc.WHITE_BISHOP.loc;
                    break;
                case WHITE_EMPRESS:
                    imgLoc = pieceImgLoc.WHITE_EMPRESS.loc;
                    break;
                case WHITE_ROOK:
                    imgLoc = pieceImgLoc.WHITE_ROOK.loc;
                    break;
                case WHITE_KING:
                    imgLoc = pieceImgLoc.WHITE_KING.loc;
                    break;
                case WHITE_QUEEN:
                    imgLoc = pieceImgLoc.WHITE_QUEEN.loc;
                    break;
                case WHITE_PAWN:
                    imgLoc = pieceImgLoc.WHITE_PAWN.loc;
                    break;
                case WHITE_KNIGHT:
                    imgLoc = pieceImgLoc.WHITE_KNIGHT.loc;
                    break;
                case WHITE_PRINCESS:
                    imgLoc = pieceImgLoc.WHITE_PRINCESS.loc;
                    break;
                case WHITE_GRASSHOPPER:
                    break;
            }
            if (imgLoc != null) {
                pieceImage = new ImageIcon(getClass().getResource(imgLoc));
            }
        }
        catch (Exception e){
            System.out.println("Image source unavailable");
        }
        return pieceImage;
    }

    /** sets up menu pane
     *
     * @return
     */
    public JPanel setUpMenu(){
        JPanel menu= new JPanel();
        menu.setMinimumSize(new Dimension(0, 0));
        forfeit.setFont(new Font("Arial", Font.ITALIC, 20));
        restartGame.setFont(new Font("Arial", Font.ITALIC, 20));
        settings.setFont(new Font("Arial", Font.ITALIC, 20));
        undo.setFont(new Font("Arial", Font.ITALIC, 20));
        newMatch.setFont(new Font("Arial", Font.ITALIC, 20));
        scoreBoard.setFont(new Font("Arial", Font.ITALIC, 20));
        GridLayout layout=new GridLayout(6,1,0,0);
        menu.setLayout(layout);
        settingsMenu.add(changePlayerName);
        settingsMenu.add(addSpecialPieces);
        setSettingsMenu();
        menu.add(settings);
        menu.add(newMatch);
        menu.add(scoreBoard);
        menu.add(restartGame);
        menu.add(forfeit);
        menu.add(undo);
        undo.setEnabled(false);
        return menu;
    }
    /**Show popup settings menu when settings button is clicked
     *
     */
    public void setSettingsMenu(){
        settings.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsMenu.show(settings, settings.getWidth(), settings.getHeight() / 2);
            }
        });
    }

    public JButton[][] getCells(){
        return  cells;
    }

    public JButton getForfeit(){
        return forfeit;
    }

    public JButton getRestartGame(){
        return restartGame;
    }

    public JButton getUndo(){
        return undo;
    }

    public JMenuItem getChangePlayerName(){
        return changePlayerName;
    }

    public JMenuItem getAddSpecialPieces(){
        return addSpecialPieces;
    }

    public JFrame getWindow(){
        return window;
    }

    public JSplitPane getGameContainer(){
        return gameContainer;

    }

    public JButton getNewMatch(){
        return newMatch;
    }

    public JButton getScoreBoard(){
        return scoreBoard;
    }

    public JPanel getMenuContainer(){
        return menuContainer;
    }
}
