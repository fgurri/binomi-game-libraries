package binomi.test.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import binomi.game.*;

public class TestGame{

	@Test
	public void testGameConstructor(){
		String game_name = "Testing game";
		Player player1 = new Player(1, "Guiligait de planets");
		Player player2 = new Player(2, "Chupiter alains guit mars");
		Player player3 = new Player(3, "dieichofacuerio");
		Player player4 = new Player(4, "Jarmoni anderstandin");
		Player player5 = new Player(5, "anostapaundin");
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Player> players_sorted = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		
		Board board = new Board();
		
		Game game = new Game (game_name, board, players);
		players_sorted = game.getPlayers_sorted();
		
		assertTrue(game.getName() == game_name);
		assertTrue(game.getPlayers().size()==5);
		assertTrue(players_sorted.size()==5);
		
		// cycle throw players_sorted to garantee consistancy
		for (int j=0; j<3;j++)
			for (int i = 0; i<5; i++) {
				assertTrue(game.getPlayer_current().equals(players_sorted.get(i)));
				game.nextPlayer();
			}
		
		// we should be at start of turn 4
		assertTrue(game.getTurn_number() == 4);
		assertTrue(game.getPlayer_current().equals(players_sorted.get(0)));
	}
	
	
}
