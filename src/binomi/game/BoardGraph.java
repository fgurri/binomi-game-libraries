/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binomi.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Ferran Gurri Mancera
 */
public class BoardGraph extends Board {
	private BoardCell start;
	private int expected_max_path_length = Integer.MAX_VALUE;
	private Hashtable<Integer, BoardCell> cells = new Hashtable<Integer, BoardCell>();

	public BoardCell getBoardCell(int cell_number) {
		return this.cells.get(cell_number);
	}

	public void constructBoard(String json) {
		try {
			JSONObject tomJsonObject = new JSONObject(json);
			this.setExpected_max_path_length(
					Integer.parseInt(tomJsonObject.getString("expected_max_path_length")));
			JSONArray jsonarray = tomJsonObject.getJSONArray("cells");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				int id = Integer.parseInt(jsonobject.getString("id"));
				int x = Integer.parseInt(jsonobject.getString("x"));
				int y = Integer.parseInt(jsonobject.getString("y"));
				BoardCell cell = new BoardCell();
				cell.setId(id);
				cell.setX(x);
				cell.setY(y);

				cells.put(id, cell);
			}

			// set first cell
			this.start = this.cells.get(1);

			jsonarray = tomJsonObject.getJSONArray("nexts");
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobject = jsonarray.getJSONObject(i);
				int idorigin = Integer.parseInt(jsonobject.getString("id"));
				BoardCell cell = this.cells.get(idorigin);
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

	public void constructCellDistances() {

		/*
		 *  initialize distances to max. We need to separate init and findShortestPath in two fetches 
		 *  because initialize y,x overrides shortest path distance calculated for x,y
		 */
		Enumeration<BoardCell> en = this.cells.elements();
		while (en.hasMoreElements()) {
			BoardCell cell = en.nextElement();
			Enumeration<BoardCell> en2 = this.cells.elements();
			while (en2.hasMoreElements()) {
				BoardCell destiny_cell = en2.nextElement();
				cell.setDistance(destiny_cell, this.expected_max_path_length);
			}
		}
		
		en = this.cells.elements();
		while (en.hasMoreElements()) {
			BoardCell cell = en.nextElement();
			Enumeration<BoardCell> en2 = this.cells.elements();
			while (en2.hasMoreElements()) {
				BoardCell destiny_cell = en2.nextElement();
				findShortestPath(cell, destiny_cell, cell, new ArrayList<BoardCell>());
			}
		}

	}

	/**
	 * One way (no looping back) recursive method to calculate distance between
	 * nodes in a full tree We use a iterator returning all related cells and a
	 * setDistance function on each BoardCell
	 * 
	 * @param origin
	 * @param cell
	 * @param distance
	 */
	public void findShortestPath(BoardCell origin, BoardCell destiny, BoardCell step,
			ArrayList<BoardCell> previous_path) {
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
			Iterator<BoardCell> neightbours = step.getNextCells();
			while (neightbours.hasNext()) {
				BoardCell neightbour = neightbours.next();
				if (!previous_path.contains(neightbour)) {
					ArrayList<BoardCell> new_path = (ArrayList<BoardCell>) previous_path.clone();
					findShortestPath(origin, destiny, neightbour, new_path);
				}
			}
		}
	}

	public String printablePath(ArrayList<BoardCell> path) {
		String res = "";
		for (BoardCell cell : path) {
			res += cell.getId() + "->";
		}
		return res;
	}

	/**
	 * Define the starting cell of the board
	 * 
	 * @param cell
	 */
	public void setStartCell(BoardCell cell) {
		this.start = cell;
	}

	/**
	 * Return the starting cell of the board
	 * 
	 * @return
	 */
	public BoardCell getStart() {
		return start;
	}

	public int getDistance(int origin, int destiny) {
		BoardCell origincell = this.cells.get(origin);
		BoardCell destinycell = this.cells.get(destiny);

		return origincell.getDistance(destinycell);
	}
	
	public int getExpected_max_path_length() {
		return expected_max_path_length;
	}

	public void setExpected_max_path_length(int expected_max_path_length) {
		this.expected_max_path_length = expected_max_path_length;
	}
}
