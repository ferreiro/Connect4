package tp.pr1.logic.test;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import tp.pr1.logic.Game;
import tp.pr1.logic.Counter;

public class UndoTest {
	
	Game game;
	
	@Before
	public void init() {
		game = new Game();
	}
	
	@Test
	public void testUndoTrasMovimiento() {
		game.executeMove(Counter.WHITE, 1);
		assertTrue("After making a move, it should be possible to perform undo", game.undo());
		assertTrue("After undoing the first move, the board should be empty.", UtilsPartidaYTablero.tableroVacio(game.getBoard()));
		assertEquals("After undoing the first move, it should be white's move.", Counter.WHITE, game.getTurn());
		assertFalse("After undoing a move, the game cannot be finished.", game.isFinished());
	}
	
	@Test
	public void testUndo10Veces() {		
		for (int i = 1; i <= 3; ++i)
			for (int x = 1; x <= 6; ++x) {
				game.executeMove(game.getTurn(), x);
				assertFalse(game.isFinished());
			}
		
		for (int i = 0; i < 10; ++i) {
			Counter turn = game.getTurn();
			assertTrue("It should be possible to undo at least 10 moves.", game.undo());
			assertTrue("After undo, the turn should change.", turn != game.getTurn());
			assertFalse(game.isFinished());
		}
		
		for (int i = 0; i < 28; ++i) {
			int x = 1 + (i % 7);
			int y = 6 - (i / 7);
			Counter colour = (i % 2 == 0) ? Counter.WHITE : Counter.BLACK;
			if (i >= 11) colour = Counter.EMPTY;
			assertEquals("After undo, the board should not remain unchanged at position (" + x + ", " + y + ").", colour,
					game.getBoard().getPosition(x, y));
		}
	}
	
	@Test
	public void testNoUndoTrasReset() {
		assertTrue(game.executeMove(game.getTurn(), 3));
		game.reset();
		assertFalse("After a reset, it should not be possible to perform undo.", game.undo());
	}
}
