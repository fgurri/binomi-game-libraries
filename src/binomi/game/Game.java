package binomi.game;

import java.util.ArrayList;
import java.util.Collections;

public class Game {

	private String name = null;
	private Board board = null;
	private ArrayList<Player> players = null;
	private ArrayList<Player> players_sorted = null;
	private Player player_current = null;
	private int turn_number = 1;
	
	public Game(String name, Board board, ArrayList<Player> players) {
		this.name = name;
		this.board = board;
		this.players = players;
		this.players_sorted = (ArrayList<Player>)this.players.clone();
		sortPlayers(); //random order by default
		this.setPlayer_current(this.players_sorted.get(0));
	}
	
	public String getName() {
		return name;
	}

	public void sortPlayers() {
		Collections.shuffle(this.players_sorted);
	}
	
	public void nextPlayer () {
		int i = this.players_sorted.indexOf(this.player_current)+1;
		if (i == this.players_sorted.size()) {
			this.setPlayer_current(this.players_sorted.get(0));
			// new turn
			this.nextTurn();
		}
		else
			this.setPlayer_current(this.players_sorted.get(i));
	}

	public Board getBoard() {
		return board;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public ArrayList<Player> getPlayers_sorted() {
		return players_sorted;
	}

	public Player getPlayer_current() {
		return player_current;
	}

	public int getTurn_number() {
		return turn_number;
	}

	public void setTurn_number(int turn_number) {
		this.turn_number = turn_number;
	}

	public void setPlayer_current(Player player_current) {
		this.player_current = player_current;
	}

	public void nextTurn() {
		this.turn_number++;
	}

}
