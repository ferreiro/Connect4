package tp.pr1.logic.test;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import tp.pr1.logic.Game;
import tp.pr1.logic.Board;
import tp.pr1.logic.Counter;

public class FourInARowTest {

	
	private void testCuatroEnRaya(int posX[], int posY[], int last, Counter colour) {
		Game game = new Game();
		Board b = game.getBoard();
		for (int i = 0; i < posX.length; ++i)
			if (i != last)
				b.setPosition(posX[i], posY[i], colour);
		if (!UtilsPartidaYTablero.preparaColocacionFicha(game, colour, posX[last], posY[last]))
			fail("Internal error in the test :-?");

		assertFalse(
				"Game finished unexpectedly with only three consecutive " + colour + " counters on the board.",
				game.isFinished()
				);
		assertTrue(game.executeMove(colour, posX[last]));

		assertTrue("Game has not finished even though there are now four consecutive " + colour + " counters on the board.",
				game.isFinished());
		assertEquals("Winner incorrectly assigned after win by " + colour, colour, game.getWinner());
		
		for (int x = 1; x <= 6; ++x) {
			assertFalse("It should not be possible to make a move after the game has finished.", game.executeMove(Counter.WHITE, x));
			assertFalse("It should not be possible to make a move after the game has finished.", game.executeMove(Counter.BLACK, x));
		}
		
	}
	
	private void pruebaCuatroEnRaya(int posX[], int posY[]) {
		for (int i = 0; i < posX.length; ++i) {
			testCuatroEnRaya(posX, posY, i, Counter.WHITE);
			testCuatroEnRaya(posX, posY, i, Counter.BLACK);
		}
	}
	
	// Partidas que terminan con todas las posibles 4 en raya
	// horizontal
	@Test
	public void testCuatroEnRayaHorizontal() {
		
		int []posX = new int[4];
		int []posY = new int[4];
		for (int x = 1; x <= 6 - 3; ++x) {
			for (int y = 1; y <= 6; ++y) {
				for (int l = 0; l < 4; ++l) {
					posX[l] = x + l;
					posY[l] = y;
				}
				pruebaCuatroEnRaya(posX, posY);
			}
		}
	}
	
	// Partidas que terminan con todas las posibles 4 en raya
	// vertical
	@Test
	public void testCuatroEnRayaVertical() {
		
		int []posX = new int[4];
		int []posY = new int[4];
		for (int x = 1; x <= 6; ++x) {
			for (int y = 1; y <= 6 - 3; ++y) {
				for (int l = 0; l < 4; ++l) {
					posX[l] = x;
					posY[l] = y + l;
				}
				testCuatroEnRaya(posX, posY, 0, Counter.WHITE);
				testCuatroEnRaya(posX, posY, 0, Counter.BLACK);
			}
		}
	}
	
	// Partidas que terminan con todas las posibles 4 en raya
	// diagonal /
	@Test
	public void testCuatroEnRayaDiag1() {
		
		int []posX = new int[4];
		int []posY = new int[4];
		for (int i = 1; i <= 12; ++i) {
			int sx = Math.max(1, i-5);
			int sy = Math.min(i, 6);
			while ((sy - 4 >= 0) && (sx + 3 <= 7)) {
				for (int l = 0; l < 4; ++l) {
					posX[l] = sx + l;
					posY[l] = sy - l;
				}
				pruebaCuatroEnRaya(posX, posY);
				sy--; sx++;
			}
		}
	}
	
	// Partidas que terminan con todas las posibles 4 en raya
	// diagonal \
	@Test
	public void testCuatroEnRayaDiag2() {
		
		int []posX = new int[4];
		int []posY = new int[4];
		for (int i = 1; i <= 12; ++i) {
			int sx = Math.min(i,  7);
			int sy = Math.min(13 - i, 6);
			while ((sy - 4 >= 0) && (sx - 4 >= 0)) {
				for (int l = 0; l < 4; ++l) {
					posX[l] = sx - l;
					posY[l] = sy - l;
				}
				pruebaCuatroEnRaya(posX, posY);
				sy--; sx--;
			}
		}
	}
	
}
