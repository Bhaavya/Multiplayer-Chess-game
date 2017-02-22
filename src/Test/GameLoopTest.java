package Test;

import Controller.GameLoop;
import View.BoardView;
import org.junit.Test;
import GameLogic.Player.Player;
import static org.junit.Assert.*;
/**
 *Tests proper setup of view and model
 */
public class GameLoopTest {

    @Test
    public void initializationTest() throws Exception{
        GameLoop game=new GameLoop();
        game.gameInit();
        //test player names are assigned correctly
        Player player1=(game.getPlayer())[0];
        Player player2=(game.getPlayer())[1];
        assertEquals(player1.getName(),"White Team");
        assertEquals(player2.getName(), "Black Team");
        //test if player scores are 0
        assertEquals(player1.getScore(),0);
        assertEquals(player2.getScore(), 0);
        //test board is set up correctly
        BoardTest boardTest= new BoardTest();
        boardTest.testSetUp();
        //test if white team player has first turn
        assertEquals(game.getPlayerInTurn(), GameLoop.PlayerInTurn.WHITE_TEAM);
        //test if left and right components of game container splitpane are assigned correctly
        BoardView view=game.getView();
        //assertEquals(view.getGameContainer().getRightComponent(), game.getBoardContainer());
        //assertEquals(view.getGameContainer().getLeftComponent(),view.getMenuContainer());
    }

}