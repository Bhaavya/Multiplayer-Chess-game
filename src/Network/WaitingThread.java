package Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Waits till server sends message about the move made and notifies its listeners
 * if reset board is true, it immediately returns
 */
public class WaitingThread extends Thread{
    ObjectInputStream in;                   // input stream used for checking the required message from server
    boolean resetBoard;
    ArrayList<Client> listeners= new ArrayList<Client>();
    WaitingThread(ObjectInputStream in, boolean resetBoard){
    this.in = in;
    this.resetBoard = resetBoard;}
    public void run() {
        String msg = null;
        if (resetBoard){
            for(Client l:listeners){
                l.gameStart();
            }
        }
        else {
            do {
                msg = readMessage(in);
            }
            while (msg == null || !(msg.equals("Valid move made")) && !(msg.equals("Check move")) && !(msg.equals("Winning move")));

            for (Client l : listeners) {
                l.changeUI(msg);
            }
        }
    }

    public void addListener(Client client){
        this.listeners.add(client);
    }

    public String readMessage(ObjectInputStream in){
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

}
