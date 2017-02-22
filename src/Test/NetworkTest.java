package Test;

import Network.Client;
import Network.Server;
import org.junit.Test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class NetworkTest {
    @Test
    public void testNetwork() {
        ServerSocket servSock = mock(ServerSocket.class);
        Socket player = mock(Socket.class);
        ObjectInputStream pIn = mock(ObjectInputStream.class);
        ObjectOutputStream pOut = mock(ObjectOutputStream.class);
        try {
            when((servSock.accept())).thenReturn(player);
        }
        catch (Exception e){System.err.print(e);}
        try {
            when(player.getInputStream()).thenReturn(pIn);
        }
        catch (Exception e){System.err.print(e);}
        try {
            when(player.getOutputStream()).thenReturn(pOut);
        }
        catch (Exception e){System.err.print(e);}

    }

}
