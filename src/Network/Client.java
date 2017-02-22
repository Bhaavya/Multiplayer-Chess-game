package Network;

import GameLogic.Piece.Piece;
import View.BoardView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.HashMap;

/**
 * client class which handles UI for players
 */
public class Client {
    Socket requestSocket;
    boolean resetBoard = false;
    ObjectOutputStream out;
    ObjectInputStream in;
    private BoardView gui;
    private JButton sourceCellButton;
    private Icon sourceCellIcon;
    private Icon prevSourceIcon,prevDestIcon;
    private JButton prevSourceCellButton,prevDestinationCellButton;
    HashMap<String, Piece.pieceType> configMap=null;
    String playerTitle;                                             //title of gui
    enum state{WAITING,PLAYING}                                     //player is in PLAYING state, opponent is in WAITING state
    state clientState;

    Client(String playerTitle) {
        clientState = state.WAITING;
        this.playerTitle = playerTitle;
    }

    /**
     * connects to server and renders the gui board for players
     */
    public void run() {
        try {
            //1. creating a socket to connect to the server
            requestSocket = new Socket("localhost", 2004);
            //2. get Input and Output streams
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());
            String message=null;
            do {
                    message = readMessage(in);
            } while (message==null||!(message.equals("Ready")));
            //set up the gui and start game
            try {
                    configMap = new HashMap<>((HashMap<String, Piece.pieceType>) in.readObject());
                    gui = new BoardView(configMap,playerTitle);
                    gameStart();
            } catch (ClassNotFoundException classNot) {
                    System.err.println("data received in unknown format");
            }


        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {}

    }

    /**
     * sends msg object to server
     * @param msg
     */
    void sendMessage(Object msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ioException) {}
    }

    /**
     * reads string message from server
     * @param in
     * @return
     */
    public String readMessage(ObjectInputStream in ){
        String msg = null;
        try{
            msg = (String) in.readObject();
        }
        catch(ClassNotFoundException classNot) {
            System.err.println("data received in unknown format");
        } catch (IOException ioException) {
        }
        return msg;
    }

    /**
     * reads int message from server
     * @param in
     * @return
     */
    public Integer readNum(ObjectInputStream in ){
        int msg = 0;
        try{
            msg = (Integer) in.readObject();
        }
        catch(ClassNotFoundException classNot) {
            System.err.println("data received in unknown format");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return msg;
    }

    /**
     * reset the board on the start of new game
     */
    public void gameStart() {
        resetBoard = false;
        JSplitPane gameContainer= gui.getGameContainer();
        JPanel boardContainer=gui.setUpBoard(configMap);
        gameContainer.setRightComponent(boardContainer);
        gui.getUndo().setEnabled(false);
        setCellListener();
        assignNextTurn();
}

    /**
     * determines player and opponent depending on message from server
     */
    private void assignNextTurn() {
        String stateMsg = (String) readMessage(in);
        if (stateMsg.equals("Please make move")){
            clientState = state.PLAYING;
        }

        else{
            clientState = state.WAITING;
            waitForMove();
        }
    }

    /**
     * after a valid move is made, player must wait for the opponent to make a move
     * thread is used for wait so that mouse events can be caught, and UI can be changed accordingly
     * if reset board is true, thread returns immediately
     */
    private void waitForMove() {
        WaitingThread wt = new WaitingThread(in,resetBoard);
        wt.addListener(this);
        wt.start();
    }

    /**
     * allow drag and drop of chess pieces
     */
    public void setCellListener() {
        JButton [][] cells=gui.getCells();
        MouseAdapter listener = new MouseAdapter() {
            JPanel boardContainer= (JPanel)gui.getGameContainer().getRightComponent();
            @Override
            public void mousePressed(MouseEvent me) {
                defineMousePressed(me);
            }
            @Override
            public void mouseReleased(MouseEvent me) {
                defineMouseReleased(me, boardContainer);
            }
        };
        for(int rank=0;rank<8;rank++) {
            for (int file = 0; file < 8; file++) {
                cells[rank][file].addMouseMotionListener(listener);
                cells[rank][file].addMouseListener(listener);
            }
        }
    }

    /**
     * action listener for mouse pressed event
     */
    public void defineMousePressed(MouseEvent me){
        //send move source info to server
        if (clientState == state.PLAYING) {
            JButton cell = (JButton) me.getSource();
            startMove(cell);
        }
        //opponent cannot make a move
        else {
            JOptionPane.showMessageDialog(gui.getWindow(), "Not your turn!!!", "Move not allowed", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * action listener for mouse released event
     */
    public void defineMouseReleased(MouseEvent me,JPanel boardContainer){
        if (clientState == state.PLAYING) {
            JButton cellButton = null;
            //find cell where piece was dropped and send this info to server
            for (Component component : boardContainer.getComponents()) {
                if (component.getLocationOnScreen().getX() <= me.getXOnScreen() && component.getLocationOnScreen().getX() + component.getWidth() >= me.getXOnScreen() &&
                        component.getLocationOnScreen().getY() <= me.getYOnScreen() && component.getLocationOnScreen().getY() + component.getHeight() >= me.getYOnScreen()) {
                    cellButton = (JButton) component;
                    sendMessage("Move Ended");
                    break;
                }
            }
            int buttonPos[] = getCellPos(cellButton);
            int destRank = buttonPos[0];
            int destFile = buttonPos[1];
            if (destFile != -1 && destRank != -1) {
                sendMessage(destRank);
                sendMessage(destFile);
            }
            String msg=null;
            //wait for server to check if move was valid
            do {
                msg = readMessage(in);
            }
            while (msg==null||!(msg.equals("Valid move made")) && !(msg.equals("Wrong piece moved")) && !(msg.equals("Check move") )&& !(msg.equals("Winning move") )&& !(msg.equals("Invalid move made"))&&!(msg.equals("No move made")));
            //make changes to UI based on move
            changeUI(msg);
        }

    }

    /**
     * sends info about source of move to server
     * @param cellButton
     */
    public void startMove(JButton cellButton){
        //Other Player has started his turn. Undo not possible now
        gui.getUndo().setEnabled(false);
        sendMessage("Move started");
        int buttonPos[] = getCellPos(cellButton);
        int sourceRank=buttonPos[0];
        int sourceFile=buttonPos[1];
        if (sourceFile!=-1 && sourceRank!=-1) {
            sendMessage(sourceRank);
            sendMessage(sourceFile);
        }

    }

    /**
     * determine cell position on internal board corresponding to button position on UI
     * @param cellButton
     * @return
     */
    public int[] getCellPos(JButton cellButton){
        int buttonPos[] = {-1,-1};
        for(int rank=0;rank<8;rank++){
            for(int file=0;file<8;file++){
                if ((gui.getCells()[rank][file]).equals(cellButton)) {
                    buttonPos[0]= 7 - rank;
                    buttonPos[1] = file;
                    break;
                }
            }
        }
        return buttonPos;
    }

    /**
     * moves piece on the UI
     */
    public void makeMove(){
        //determine source and destination buttons based on internal board cell positions
        int destFile = readNum(in);
        int destRank = 7-readNum(in);
        int sourceFile = readNum(in);
        int sourceRank = 7- readNum(in);
        sourceCellButton = gui.getCells()[sourceRank][sourceFile];
        JButton destinationCellButton = gui.getCells()[destRank][destFile];
        sourceCellIcon = sourceCellButton.getIcon();
        storeStateforUndo(sourceCellIcon, sourceCellButton, destinationCellButton);
        sourceCellButton.setIcon(null);
        destinationCellButton.setIcon(sourceCellIcon);
        gui.getUndo().setEnabled(true);
        sourceCellButton.repaint();
        destinationCellButton.repaint();

    }

    /**
     * stores source and destination buttons ans icons before move for undo
     * @param sourceCellIcon
     * @param sourceCellButton
     * @param destCellButton
     */
    public void storeStateforUndo(Icon sourceCellIcon,JButton sourceCellButton,JButton destCellButton){
        prevSourceIcon = sourceCellIcon;
        prevSourceCellButton = sourceCellButton;
        prevDestIcon = destCellButton.getIcon();
        prevDestinationCellButton = destCellButton;
    }

    /**
     * Change board view after move is made
     * @param msg message received from server
     */
    public void changeUI(String msg){
        if (msg.equals("Valid move made") || msg.equals("Check move") || msg.equals("Winning move"))
            makeMove();
        if (msg.equals("Check move")) {
                String oppName = readMessage(in);
                JOptionPane.showMessageDialog(gui.getWindow(), oppName + " is in check!!!", "Warning", JOptionPane.PLAIN_MESSAGE);
        }
        else if (msg.equals("Winning move")) {
                String playerName = readMessage(in);
                String oppName = readMessage(in);
                int playerScore = readNum(in);
                int oppScore = readNum(in);
                JOptionPane.showMessageDialog(gui.getWindow(), playerName + "won!!! \n" + playerName + " Score: " + Integer.toString(playerScore)
                        + "\n" + oppName + " Score: " + Integer.toString(oppScore), "Congratulations", JOptionPane.PLAIN_MESSAGE);
                //board should be reset
                resetBoard = true;
                waitForMove();
        }

        else if (msg.equals("Invalid move made"))
            JOptionPane.showMessageDialog(gui.getWindow(), "Invalid move!!!", "Move not allowed", JOptionPane.ERROR_MESSAGE);
        else if (msg.equals("Wrong piece moved"))
            JOptionPane.showMessageDialog(gui.getWindow(), "Not your piece!!!", "Move not allowed", JOptionPane.ERROR_MESSAGE);
        if (!msg.equals("Winning move"))
            assignNextTurn();
    }

}