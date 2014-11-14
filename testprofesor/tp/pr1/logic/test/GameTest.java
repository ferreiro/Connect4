package tp.pr1.logic.test;

import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import tp.pr1.logic.Game;
import tp.pr1.logic.Board;
import tp.pr1.logic.Counter;

public class GameTest {
	
	Game game;
	
	@Before
	public void init() {
		game = new Game();
	}
	
	@Test
	public void testCtor() {
		assertFalse("A game which has just started shouldn't have already finished", game.isFinished());
		assertEquals("White always starts.", Counter.WHITE, game.getTurn());
		assertEquals("The board dimensions should be fixed as 7x6", 7, game.getBoard().getWidth());
		assertEquals("The board dimensions should be fixed as 7x6", 6, game.getBoard().getHeight());
		assertFalse("At the start of the game, there is nothing to undo.", game.undo());
	}
	
	@Test
	public void testEjecutaMovimientoSimple() {
		assertTrue(game.executeMove(Counter.WHITE, 1));
		assertEquals("If the first move of a game is in column 1 then after making it, board position (1, 6) should contain a white counter",
				Counter.WHITE,
				game.getBoard().getPosition(1,  6));
		// added condition to handle a 1x1 board!!!
		assertFalse("After only one move the game should not have finished (on a non-trivial board).", ( game.isFinished() && (game.getBoard().getWidth() > 1 || game.getBoard().getHeight() > 1)) );
		assertEquals("After white moves it should be black's turn.", Counter.BLACK, game.getTurn());
	}
	
	@Test
	public void testEjecutaMovimientoInvalido1() {
		assertFalse("executeMove should not allow a move to be made out of turn.",
				game.executeMove(Counter.BLACK, 1));
	}
	
	@Test
	public void testEjecutaMovimientoInvalido2() {
		assertTrue(game.executeMove(Counter.WHITE, 3));
		assertTrue(game.executeMove(Counter.BLACK, 3));
		assertTrue(game.executeMove(Counter.WHITE, 3));
		assertTrue(game.executeMove(Counter.BLACK, 3));
		assertTrue(game.executeMove(Counter.WHITE, 3));
		assertTrue(game.executeMove(Counter.BLACK, 3));
		assertFalse("executMove should fail if the column whose number is passed as an argument is already full.", game.executeMove(Counter.WHITE, 3));
	}
	
	@Test
	public void testEjecutaMovimientoInvalido3() {
		for (int x = -10; x <= 10; ++x) {
			if ((1 <= x) && (x <= 7)) continue;
			assertFalse("executeMove should fail if an invalid column number is passed as an argument.", game.executeMove(Counter.WHITE, x));
		}
	}
	
	@Test
	public void persistenciaTablero() {
		// Comprobación que no está en la documentación pero de implementación
		// de sentido común (y, dicho sea de paso, que nos permite tomar atajos
		// en los test del cuatro en raya).
		Board board = game.getBoard();
		assertTrue(game.executeMove(Counter.WHITE, 3));
		assertTrue("The board object should not be changed in the middle of a game (except possibly on reset).",
				board == game.getBoard());
		assertEquals("After making a move in column 3, position (3, 6) should contain a white counter",
				Counter.WHITE,
				board.getPosition(3,  6));
	}
	
	
	@Test
	public void partidaEnTablas() {
		for (int x = 1; x <= 7; ++x) {
			if (x == 4) continue;
			for (int i = 0; i < 6; ++i) {
				if ((x == 7) && (i == 5)) continue;
				assertTrue(game.executeMove(game.getTurn(), x));
			}
		}
		
		for (int i = 0; i < 6; ++i) {
			assertTrue(game.executeMove(game.getTurn(), 4));
		}
		
		assertTrue(game.executeMove(game.getTurn(), 7));

		assertTrue("If the board is full, the game should be finished.", game.isFinished());
		assertEquals("If the board is full, the game should have finished in a draw, i.e. there should be no winner.", Counter.EMPTY, game.getWinner());
		
		for (int i = 1; i <= 7; ++i) {
			assertFalse("If the board is full, it should not be possible to make a move.", game.executeMove(game.getTurn(), i));
		}
	}
	
	@Test
	public void testReset1() {
		
		assertTrue(game.executeMove(Counter.WHITE, 3));
		game.reset();
		assertTrue("After a reset, the board should be empty", UtilsPartidaYTablero.tableroVacio(game.getBoard()));
		assertEquals("After a reset, it should be white's turn.", Counter.WHITE, game.getTurn());

	}
}
