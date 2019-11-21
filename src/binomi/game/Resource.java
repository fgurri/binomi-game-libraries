package binomi.game;

public class Resource {

	public static final int RESOURCE_DRAWABLE_CARD = 1;
	
	private int id;
	private String name;
	private String description;
	private Effect effect;
	
	public Resource(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public Effect getEffect() {
		return effect;
	}
	
	public void setEffect(Effect effect) {
		this.effect = effect;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public int hashCode () {
		return this.id;
	}
	
}
