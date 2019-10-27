package binomi.game;

public class Player {

	private String name;
	private int id;

	public Player(int id, String name) {
		super();
		this.name = name;
		this.id = id;
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
