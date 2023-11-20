package project04gomoku.app.logic;

import static org.junit.Assert.*;
import org.junit.Test;

import com.gomoku.project04gomoku.app.logic.Player;
import com.gomoku.project04gomoku.app.logic.HumanPlayer;

public class HumanPlayerTest {

    @Test
    public void testHumanPlayerColor() {
        HumanPlayer blackPlayer = new HumanPlayer(Player.PlayerColor.BLACK);
        assertEquals("HumanPlayer should have color BLACK.", Player.PlayerColor.BLACK, blackPlayer.getColor());

        HumanPlayer whitePlayer = new HumanPlayer(Player.PlayerColor.WHITE);
        assertEquals("HumanPlayer should have color WHITE.", Player.PlayerColor.WHITE, whitePlayer.getColor());
    }

    @Test
    public void testHumanPlayerType() {
        HumanPlayer player = new HumanPlayer(Player.PlayerColor.BLACK);
        assertEquals("HumanPlayer should have type HUMAN.", Player.PlayerType.HUMAN, player.getType());
    }
}
