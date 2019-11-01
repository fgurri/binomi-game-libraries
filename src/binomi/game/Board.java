package binomi.game;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Ferran Gurri Mancera (ferran.gurri@gmail.com)
 * 
 *         Implements a turn-based game board.
 *
 */
public class Board {
	private String name;
	private Cell start;
	private int expected_max_path_length = Integer.MAX_VALUE;
	private Hashtable<Integer, Cell> cells = new Hashtable<Integer, Cell>();

	/**
	 * 
	 * @param path
	 * @return A readable String path in {} -> {} -> {} format
	 */
	public static String printablePath(ArrayList<Cell> path) {
		String res = "";
		for (Cell cell : path) {
			res += cell.getId() + "->";
		}
		return res;
	}

	/**
	 * Builds de current board from json object.
	 * json object must have attributes:
	 * - name : String
	 * - expected_max_path_lenght : int
	 * - cells : JSONArray
	 * 	each item in array must have:
	 * 		+ id : int
	 * 		+ x : int
	 * 		+ y : int
	 * - starting_cell : int
	 * - nexts : JSONArray
	 * 	each item in array must have:
	 * 		+ id : int
	 * 		+ next : JSONArray
	 * 			each item in array must have:
	 * 				- id : int
	 * 
	 * @param json
	 */
	public void buildFromJSON(JSONObject json) {
		try {
			this.setExpected_max_path_length(Integer.parseInt(json.getString("expected_max_path_length")));
			this.setName(json.getString("name"));
			JSONArray jsonarray = json.getJSONArray("cells");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				int id = Integer.parseInt(jsonobject.getString("id"));
				int x = Integer.parseInt(jsonobject.getString("x"));
				int y = Integer.parseInt(jsonobject.getString("y"));
				Cell cell = new Cell();
				cell.setId(id);
				cell.setX(x);
				cell.setY(y);

				cells.put(id, cell);
			}

			// set first cell
			this.start = this.cells.get(json.getInt("starting_cell"));

			jsonarray = json.getJSONArray("nexts");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				int idorigin = Integer.parseInt(jsonobject.getString("id"));
				Cell cell = this.cells.get(idorigin);
				JSONArray jsonnexts = jsonobject.getJSONArray("next");
				for (int j = 0; j < jsonnexts.length(); j++) {
					JSONObject jsonnext = jsonnexts.getJSONObject(j);
					int idnext = Integer.parseInt(jsonnext.getString("id"));
					cell.addNextCell(this.cells.get(idnext));
				}
			}
		} catch (JSONException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Generates distances between all cells in board
	 */
	public void constructCellDistances() {

		/*
		 * initialize distances to max. We need to separate init and
		 * findShortestPath in two fetches because initialize y,x overrides
		 * shortest path distance calculated for x,y
		 */
		Enumeration<Cell> en = this.cells.elements();
		while (en.hasMoreElements()) {
			Cell cell = en.nextElement();
			Enumeration<Cell> en2 = this.cells.elements();
			while (en2.hasMoreElements()) {
				Cell destiny_cell = en2.nextElement();
				cell.setDistance(destiny_cell, this.expected_max_path_length);
			}
		}

		en = this.cells.elements();
		while (en.hasMoreElements()) {
			Cell cell = en.nextElement();
			Enumeration<Cell> en2 = this.cells.elements();
			while (en2.hasMoreElements()) {
				Cell destiny_cell = en2.nextElement();
				findShortestPath(cell, destiny_cell, cell, new ArrayList<Cell>());
			}
		}

	}

	/**
	 * Recursive method to calculate the shortest path between two cells
	 * 
	 * @param origin
	 * @param destiny
	 * @param step
	 * @param previous_path
	 */
	public void findShortestPath(Cell origin, Cell destiny, Cell step, ArrayList<Cell> previous_path) {
		int current_distance = origin.getDistance(destiny);
		previous_path.add(step);
		if (destiny.equals(origin)) {
			origin.setPathIfShorterOrEmpty(destiny, previous_path);
			return;
		}
		if (step.isNeighbour(destiny)) {
			previous_path.add(destiny);
			origin.setPathIfShorterOrEmpty(destiny, previous_path);
		} else if (current_distance < previous_path.size()) {
			return;
		} else {
			Iterator<Cell> neightbours = step.getNextCells();
			while (neightbours.hasNext()) {
				Cell neightbour = neightbours.next();
				if (!previous_path.contains(neightbour)) {
					ArrayList<Cell> new_path = (ArrayList<Cell>) previous_path.clone();
					findShortestPath(origin, destiny, neightbour, new_path);
				}
			}
		}
	}

	/**
	 * Define the starting cell of the board
	 * 
	 * @param cell
	 */
	public void setStartCell(Cell cell) {
		this.start = cell;
	}

	/**
	 * Return the starting cell of the board
	 * 
	 * @return
	 */
	public Cell getStart() {
		return start;
	}

	/**
	 * Returns the distances between two reachable cells
	 * @param origin
	 * @param destiny
	 * @return
	 */
	public int getDistance(int origin, int destiny) {
		Cell origincell = this.cells.get(origin);
		Cell destinycell = this.cells.get(destiny);

		return origincell.getDistance(destinycell);
	}

	public int getExpected_max_path_length() {
		return expected_max_path_length;
	}

	public void setExpected_max_path_length(int expected_max_path_length) {
		this.expected_max_path_length = expected_max_path_length;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumCells() {
		return this.cells.size();
	}

	public Cell getCell(int cell_number) {
		return this.cells.get(cell_number);
	}

}
