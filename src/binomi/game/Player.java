package binomi.game;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {

	private String name;
	private int id;
	private HashMap<Integer, ArrayList<Resource>> resources = new HashMap<Integer, ArrayList<Resource>>();
	private Game game;

	public Player(int id, String name) {
		super();
		this.name = name;
		this.id = id;
	}

	public void playTurn () {
	}
	
	public void addResource (int resource_type, Resource resource) {
		ArrayList<Resource> actual_list = this.getResources(resource_type);
		if (actual_list.isEmpty())
			actual_list = new ArrayList<Resource>();
		actual_list.add(resource);
	}
	
	public ArrayList<Resource> getResources(int resource_type) {
		return this.resources.get(resource_type);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	/***
	 * Override hashCode to consider equal objects the concatenation of
	 * id and name. So we can have two players with the same name but different
	 * id.
	 */
	@Override	
	public int hashCode() {
		return (id + name).hashCode();
	}
}
