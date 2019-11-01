package binomi.test.game;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ComparisonFailure;
import org.junit.Test;

import binomi.game.Board;

public class TestBoard {
	private static ArrayList<Path> board_names = new ArrayList<Path>();
	private ArrayList<Board> boards = new ArrayList<Board>();

	@BeforeClass
	public static void setUp() {
		try {
			Files.newDirectoryStream(Paths.get(TestBoard.class.getResource("/test/json/").toURI()),
					path -> path.toString().endsWith("Board.json")).forEach(board_names::add);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Before
	public void loadBoard() throws JSONException {
		for (Path path : TestBoard.board_names) {
			Board board = new Board();

			try {
				board.buildFromJSON(new JSONObject(FileUtils.readFileToString(path.toFile(), "utf-8")));
				this.boards.add(board);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testBoardConstructionFromJsonFiles() throws JSONException, IOException {
		File tests_board_building = new File(
				TestBoard.class.getResource("/test/json/TestingBoardBuilding.json").getFile());
		JSONObject tests_board_building_json = new JSONObject(
				FileUtils.readFileToString(tests_board_building, "utf-8"));
		for (Board board : this.boards) {
			JSONObject tests_building = tests_board_building_json.getJSONObject(board.getName());
			String name = tests_building.getString("name");
			int expected_max_path_length = tests_building.getInt("expected_max_path_length");
			int num_cells = tests_building.getInt("num_cells");
			int starting_cell = tests_building.getInt("starting_cell");
			assertTrue(board.getName().equals(name));
			assertTrue(board.getExpected_max_path_length() == expected_max_path_length);
			assertTrue(board.getNumCells() == num_cells);
			assertTrue(board.getStart().getId() == starting_cell);
			JSONArray nexts = tests_building.getJSONArray("nexts");
			for (int i = 0; i < nexts.length(); i++) {
				JSONObject next = nexts.getJSONObject(i);
				int cell1 = next.getInt("cell1");
				int cell2 = next.getInt("cell2");
				assertTrue(board.getCell(cell1).isNeighbour(board.getCell(cell2)));
			}
		}
	}

	@Test
	public void testDistanceCalculation() throws JSONException, IOException {
		File tests_board_distances = new File(
				TestBoard.class.getResource("/test/json/TestingBoardDistances.json").getFile());
		JSONObject tests_board_distances_json = new JSONObject(
				FileUtils.readFileToString(tests_board_distances, "utf-8"));
		for (Board board : this.boards) {

			board.constructCellDistances();

			JSONArray tests_distances = tests_board_distances_json.getJSONArray(board.getName());
			for (int i = 0; i < tests_distances.length(); i++) {
				JSONObject jsonobject = tests_distances.getJSONObject(i);
				int origin = jsonobject.getInt("origin");
				int destiny = jsonobject.getInt("destiny");
				int distance = jsonobject.getInt("distance");
				assertTrue(board.getDistance(origin, destiny) == distance);
				assertTrue(board.getDistance(destiny, origin) == distance);
			}
		}
	}
}
