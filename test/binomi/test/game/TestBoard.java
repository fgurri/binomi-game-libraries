package binomi.test.game;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import binomi.game.BoardGraph;

public class TestBoard {
	
	@Test
	public void testBoardMappedConstructionFromTestJsonFile() {
		BoardGraph board = new BoardGraph();
		File file = new File(TestGame.class.getResource("/test/json/ClueBoard.json").getFile());
		
        try {
			board.constructBoard(FileUtils.readFileToString(file, "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		board.constructCellDistances();
		// check some distances. Values depend on content of json test file
		assertTrue(board.getDistance(16, 16) == 0);
		assertTrue(board.getDistance(1, 16) == 1);
		assertTrue(board.getDistance(1, 23) == 2);
		assertTrue(board.getDistance(1, 17) == 2);
		assertTrue(board.getDistance(1, 15) == 2);
		assertTrue(board.getDistance(1, 5) == 3);
		assertTrue(board.getDistance(1, 14) == 3);
		assertTrue(board.getDistance(1, 22) == 3);
		assertTrue(board.getDistance(1, 24) == 3);
		assertTrue(board.getDistance(22, 25) == 1);
		assertTrue(board.getDistance(1, 51) == 5);
	}
}
