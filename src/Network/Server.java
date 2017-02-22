package Network;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import Controller.GameLoop;
import GameLogic.Board.SquareBoard;
import GameLogic.Piece.Piece;
import Controller.GameLoop.moveOutcomes;

/**
 * server class which handles the controller
 */
public class Server {

        ServerSocket controller;
        Socket player1 = null;
        Socket player2 = null;
        ObjectOutputStream player1Out;
        ObjectInputStream player1In;
        ObjectOutputStream player2Out;
        ObjectInputStream player2In;
        String message;
        int playerInTurn;
        SquareBoard board;
        public Server(){}

        /**
        * connects to players and starts the game
        */
        public void run()
        {
            try{
                //1. creating a server socket
                controller= new ServerSocket(2004, 10);
                //2. Wait for 1st player
                player1 = controller.accept();
                player1Out = new ObjectOutputStream(player1.getOutputStream());
                player1Out.flush();
                player1In = new ObjectInputStream(player1.getInputStream());
                //Wait for 2nd player
                player2 = controller.accept();
                player2Out = new ObjectOutputStream(player2.getOutputStream());
                player2Out.flush();
                player2In = new ObjectInputStream(player2.getInputStream());
                gameInit();

            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
            finally{
                //4: Closing connection
                try{
                    player1In.close();
                    player1Out.close();
                    controller.close();
                }
                catch(IOException ioException){
                    ioException.printStackTrace();
                }
            }
        }
        public void sendMessage(Object msg,ObjectOutputStream out)
        {
            try{
                out.writeObject(msg);
                out.flush();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }

        /**
        * sends internal configuration for the UI to players
        */
        public void gameInit(){
            playerInTurn = 0;
            sendMessage("Ready", player1Out);
            sendMessage("Ready",player2Out);
            GameLoop gameController = new GameLoop();
            HashMap<String,Piece.pieceType> configMap = new HashMap<>(gameController.gameInit());
            sendMessage(configMap,player1Out);
            sendMessage(configMap,player2Out);
            game(gameController);
        }

        /**
        * runs the game forever
        * @param gameController controller object
        */
        public void game(GameLoop gameController) {
            while (true) {
                String msg = null;
                ObjectInputStream playerIn= null;
                ObjectOutputStream playerOut= null;
                ObjectOutputStream opponentOut= null;
                int sourceRank =-1, sourceFile = -1;
                //get player and opponent's input/output streams
                if (playerInTurn == 0) {
                        playerIn = player1In;
                        playerOut = player1Out;
                        opponentOut = player2Out;
                }
                else {
                        playerIn = player2In;
                        playerOut = player2Out;
                        opponentOut = player1Out;
                }

                sendMessage("Please make move", playerOut);
                sendMessage("Please wait", opponentOut);

                do {
                        msg = readMessage(playerIn);
                }while (msg==null||!(msg.equals("Move started")));
                //get source and destination of move from players
                if (msg.equals("Move started")){
                    sourceRank = readNum(playerIn);
                    sourceFile = readNum(playerIn);
                    gameController.startMove(sourceRank, sourceFile);
                    String msg1=null;
                    do {
                        msg1 = readMessage(playerIn);
                    } while (msg1==null ||!(msg1.equals("Move Ended")));
                }
                int destRank = readNum(playerIn);
                int destFile = readNum(playerIn);
                //determine if the move is valid and send information to both players accordingly to update UI
                moveOutcomes outcome = gameController.shouldMove(destRank,destFile,playerInTurn);
                if (outcome == moveOutcomes.VALID_MOVE || outcome == moveOutcomes.CHECK_MOVE){
                    if (outcome == moveOutcomes.VALID_MOVE){
                        sendMessage("Valid move made",playerOut);
                        sendMessage("Valid move made",opponentOut);
                        sendMoveInfo(destFile, destRank, sourceFile, sourceRank, playerOut);
                        sendMoveInfo(destFile, destRank, sourceFile, sourceRank, opponentOut);
                    }
                    else{
                        sendCheckInfo(destFile, destRank, sourceFile, sourceRank,playerOut,gameController);
                        sendCheckInfo(destFile, destRank, sourceFile, sourceRank,opponentOut,gameController);
                    }
                    //switch players
                    playerInTurn = (playerInTurn+1)%2;
                }
                else if (outcome == moveOutcomes.INVALID_MOVE) {
                    sendMessage("Invalid move made", playerOut);
                }
                else if (outcome == moveOutcomes.WINNING_MOVE) {
                    sendWinInfo(destFile, destRank, sourceFile, sourceRank,gameController, playerOut);
                    sendWinInfo(destFile, destRank, sourceFile, sourceRank, gameController, opponentOut);
                    playerInTurn=0;
                }
                else if (outcome == moveOutcomes.WRONG_PIECE) {
                    sendMessage("Wrong piece moved",playerOut);
                }
                else{
                    sendMessage("No move made",playerOut);
                }
            }


        }

    /**
     * sends information required for UI display on winning
     * @param destFile
     * @param destRank
     * @param sourceFile
     * @param sourceRank
     * @param gameController
     * @param out  player/opponent output stream
     */
    private void sendWinInfo(int destFile,int destRank,int sourceFile,int sourceRank,GameLoop gameController, ObjectOutputStream out) {
        String playerName=gameController.getLastPlayerName();
        String oppName =gameController.getOppName();
        int oppScore =gameController.getOppScore();
        int playerScore =gameController.getLastPlayerScore();
        sendMessage("Winning move", out);
        sendMoveInfo(destFile,destRank,sourceFile,sourceRank,out);
        sendMessage(playerName,out);
        sendMessage(oppName, out);
        sendMessage(playerScore, out);
        sendMessage(oppScore, out);
    }

    /**
     * sends information required for UI display on check
     * @param destFile
     * @param destRank
     * @param sourceFile
     * @param sourceRank
     * @param out player/opponent output stream
     * @param gameController controller object
     */
    private void sendCheckInfo(int destFile,int destRank,int sourceFile,int sourceRank,ObjectOutputStream out, GameLoop gameController) {
        String oppName=gameController.getOppName();
        sendMessage("Check move",out);
        sendMoveInfo(destFile,destRank,sourceFile,sourceRank,out);
        sendMessage(oppName,out);

    }

    /**
     * reads string messages from input stream
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
            } catch (IOException ioException) {}
            return msg;
    }

    /**
     * reads int messages from input stream
     * @param in
     * @return
     */
    public int readNum(ObjectInputStream in ){
        Integer msg = null;
        try{
            msg = (Integer) in.readObject();
        }
        catch(ClassNotFoundException classNot) {
            System.err.println("data received in unknown format");
        } catch (IOException ioException) {}
        return msg;
    }

    /**
     *  sends information required to move the piece on the UI
     * @param destFile
     * @param destRank
     * @param sourceFile
     * @param sourceRank
     * @param out
     */

    public void sendMoveInfo(int destFile,int destRank,int sourceFile,int sourceRank,ObjectOutputStream out){
            sendMessage(destFile, out);
            sendMessage(destRank,out);
            sendMessage(sourceFile,out);
            sendMessage(sourceRank,out);
        }
        public static void main(String args[])
        {
            Server server = new Server();
            while(true){
                server.run();
            }
        }
}
