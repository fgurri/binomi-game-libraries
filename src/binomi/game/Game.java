package binomi.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Game {

	private String name = null;
	private Board board = null;
	private ArrayList<Player> players = null;
	private ArrayList<Player> players_sorted = null;
	private Player player_current = null;
	private int turn_number = 1;
	private ArrayList<Dice> dices = new ArrayList<Dice> ();
	private HashMap<Integer, ArrayList<Resource>> resources = new HashMap<Integer, ArrayList<Resource>>();
	private int MAX_TURNS = Integer.MAX_VALUE;
	
	public Game(String name, Board board, ArrayList<Player> players) {
		this.name = name;
		this.board = board;
		this.players = players;
		this.players_sorted = (ArrayList<Player>)this.players.clone();
		sortPlayers(); //random order by default
		this.setPlayer_current(this.players_sorted.get(0));
		// by default we play with two 6 sided dices
		this.dices.add(new Dice());
		this.dices.add(new Dice());
	}
	
	/**
	 * Game engine main loop
	 */
	
	public void playGame() {
		boolean finished = false;
		main_loop: while (!finished && this.turn_number < this.MAX_TURNS) {
			for (Player player : this.players_sorted) {
				player.playTurn();
			}
		}
	}
	
	/**
	 * Randomly throws dices
	 * @return
	 */
	public DiceResult throwDices() {
		return DiceResult.generateResult(this.dices);
	}
	
	/**
	 * Randomly sorts players order
	 */
	public void sortPlayers() {
		Collections.shuffle(this.players_sorted);
	}
	
	/**
	 * Chooses the next player to play. If it reaches the end of the 
	 * players array it cycles to first one and launches a nextTurn call.
	 */
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

	/**
	 * Fills resources from a JSON object
	 * @param json
	 */
	public void loadResourceFromJSON (JSONObject json) {
		JSONArray resources;
		try {
			resources = json.getJSONArray("resources");
			for (int i = 0; i < resources.length(); i++) {
				JSONObject resource = resources.getJSONObject(i);
				int resource_id = resource.getInt("resource_id");
				JSONArray items = resource.getJSONArray("items");
				ArrayList<Resource> resource_list = new ArrayList<Resource>();
				for (int j = 0; j < items.length(); j++) {
					JSONObject item = items.getJSONObject(j);
					int item_id = item.getInt("id");
					String item_name = item.getString("name");
					String item_description = item.getString("description");
					resource_list.add(new Resource(item_id, item_name, item_description));
				}
				Collections.shuffle(resource_list);
				this.resources.put(resource_id, resource_list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Resource> getResources(int resource_type) {
		return this.resources.get(resource_type);
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
	
	public String getName() {
		return name;
	}
}
