package binomi.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Ferran Gurri Mancera
 */
public class BoardCell {
    private int id;
    private Board board;
    private LinkedList<BoardCell> nextCells = new LinkedList<BoardCell>();
    // map containing all minimum distances to other cells. Used to check wether two cells are reacheble with a given dice roll
    // and to walk close to a desired cell
    private HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
    private HashMap<Integer, ArrayList<BoardCell>> paths = new HashMap<Integer, ArrayList<BoardCell>>();
    
    /*
        Phisical board atributes inside Board implementation
    */
    private int x, y; // position inside Board. (0,0) is upper left
    
    public void addNextCell(BoardCell cell) {
        this.nextCells.add(cell);
    }
    
    public Iterator<BoardCell> getNextCells () {
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
    
    
    public void setDistance (BoardCell cell, int distance) {
    	System.out.println("Setting distance "+distance+" from "+this.getId()+" to "+cell.getId());
        this.distances.put(cell.getId(), distance-1);
        cell.distances.put(this.getId(), distance-1);
    }
    
    public int getDistance (BoardCell cell) {
        int res = 0;
        if (this.distances.containsKey(cell.getId())) {
            res = this.distances.get(cell.getId());
        }
        return res;
    }
    
    public void setPath (BoardCell cell, ArrayList<BoardCell> path) {
    	this.paths.put(cell.getId(), path);
        cell.paths.put(this.getId(), path);
    }
    
    public void setPathAndDistance (BoardCell cell, ArrayList<BoardCell> path) {
    	this.setPath(cell, path);
    	this.setDistance(cell, path.size());
    }
    
    public ArrayList<BoardCell> getPath (BoardCell cell) {
        if (this.paths.containsKey(cell.getId())) {
            return this.paths.get(cell.getId());
        }
        return null;
    }
    
    public void normalizePathAndDistance () {
    	for (Integer key : this.distances.keySet()) {
    		int distance = this.distances.get(key);
    		ArrayList<BoardCell> path = this.paths.getOrDefault(key, null);
    		if (path == null) 
    			this.distances.put(key, 0);
    		else 
    			if (distance != path.size())
    				this.distances.put(key, path.size()-1);
    	}
    	for (Integer key : this.paths.keySet()) {
    		int distance = this.distances.getOrDefault(key, -1);
    		ArrayList<BoardCell> path = this.paths.get(key);
    		if (distance != path.size())
    			this.distances.put(key, path.size()-1);
    	}
    }
    
    public boolean isNeighbour(BoardCell cell) {
    	return this.nextCells.contains(cell);
    }
    
    public void setPathIfShorterOrEmpty(BoardCell cell, ArrayList<BoardCell> path) {
    	//System.out.println("From: " + this.id  + " to: " + cell.id);
    	//System.out.println("Curr: " + printablePath(this.getPath(cell)));
    	//System.out.println("Candidate: " + printablePath(path));
    	if (!this.paths.containsKey(cell.getId())) {
    		//System.out.println("NEW: ");
    		this.setPathAndDistance(cell, path);
    	}
    	else {
    		int old_distance = this.getPath(cell).size();
    		if (old_distance > path.size()) {
    			//System.out.println("SHORTER: ");
    			this.setPathAndDistance(cell, path);
    		}
    	}
    }
    
    public String printablePath(ArrayList<BoardCell> path) {
		String res = "";
		if (path == null)
			return res;
		for (BoardCell cell : path) {
			res += cell.getId() + "->";
		}
		return res;
	}
}
