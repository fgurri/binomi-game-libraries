package binomi.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author Ferran Gurri Mancera (ferran.gurri@gmail.com)
 *
 */
public class Cell {
    private int id;
    private Board board;
    private LinkedList<Cell> nextCells = new LinkedList<Cell>();
    // map containing all minimum distances to other cells. Used to check wether two cells are reacheble with a given dice roll
    // and to walk close to a desired cell
    private HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
    private HashMap<Integer, ArrayList<Cell>> paths = new HashMap<Integer, ArrayList<Cell>>();
    
    /*
        Phisical board atributes inside Board implementation
    */
    private int x, y; // draw position inside board image. (0,0) is upper left

    /**
     * Ensures consistancy between paths and distances based on current path values.
     */
    public void normalizePathAndDistance () {
    	for (Integer key : this.distances.keySet()) {
    		int distance = this.distances.get(key);
    		ArrayList<Cell> path = this.paths.getOrDefault(key, null);
    		if (path == null) 
    			this.distances.remove(key);
    		else 
    			if (distance != path.size())
    				this.distances.put(key, path.size()-1);
    	}
    	for (Integer key : this.paths.keySet()) {
    		int distance = this.distances.getOrDefault(key, -1);
    		ArrayList<Cell> path = this.paths.get(key);
    		if (distance != path.size())
    			this.distances.put(key, path.size()-1);
    	}
    }
    
    /**
     * Returns whether cell is next to current
     * @param cell
     * @return
     */
    public boolean isNeighbour(Cell cell) {
    	return this.nextCells.contains(cell);
    }
    
    /**
     * Overrides the path to cell if new path is shorter or current is empty.
     * @param cell
     * @param path
     */
    public void setPathIfShorterOrEmpty(Cell cell, ArrayList<Cell> path) {
    	if (!this.paths.containsKey(cell.getId())) {
    		this.setPathAndDistance(cell, path);
    	}
    	else {
    		int old_distance = this.getPath(cell).size();
    		if (old_distance > path.size()) {
    			this.setPathAndDistance(cell, path);
    		}
    	}
    }
    
    /**
     * Sets the path to the destiny cell. Distance is not recalculated.
     * If you want to automatically recalculate distance too, use setPathAndDistance.
     * It doesn't set the reverse path either.
     * @param cell
     * @param path
     */
    public void setPath (Cell cell, ArrayList<Cell> path) {
    	this.paths.put(cell.getId(), path);
        cell.paths.put(this.getId(), path);
    }
    
    /**
     * Sets path to the destiny cell and also the distance based on path size.
     * It doesn't set the reverse path and distance form destiny cell to current cell.
     * @param cell
     * @param path
     */
    public void setPathAndDistance (Cell cell, ArrayList<Cell> path) {
    	this.setPath(cell, path);
    	this.setDistance(cell, path.size()-1);
    }
    
    public void addNextCell(Cell cell) {
        this.nextCells.add(cell);
    }
    
    public Iterator<Cell> getNextCells () {
        return this.nextCells.iterator();
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    public void setDistance (Cell cell, int distance) {
    	this.distances.put(cell.getId(), distance);
        cell.distances.put(this.getId(), distance);
    }
    
    public int getDistance (Cell cell) {
    	// TODO How to handle distance between unreachable cells? Return -1?
        int res = 0;
        if (this.distances.containsKey(cell.getId())) {
            res = this.distances.get(cell.getId());
        }
        return res;
    }
    
    public ArrayList<Cell> getPath (Cell cell) {
        if (this.paths.containsKey(cell.getId())) {
            return this.paths.get(cell.getId());
        }
        return null;
    }
}
