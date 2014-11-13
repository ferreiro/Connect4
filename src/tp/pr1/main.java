package tp.pr1;

import java.util.Scanner;

import tp.pr1.control.Controller;
import tp.pr1.logic.Board;
import tp.pr1.logic.Counter;
import tp.pr1.logic.Game;
import tp.pr1.resources.Resources;


public class main {

	public static void main(String[] args) {
		Controller controller;
		Game game  = new Game(); 
		Scanner in = new Scanner(System.in); //	Read from the keyboard
		controller = new Controller(game, in);
	
		controller.run();
	}	
}

//		UNIT TESTS
//		SOME TESTS ON THE CODE

/*		Board board = new Board(10, 10);
		System.out.println(board.getHeight());
		System.out.println(board.getWidth());

		counter = board.getPosition(3, 3);
		System.out.println(counter);

		board.setPosition(3, 3, Counter.WHITE);
		board.setPosition(0,0, Counter.BLACK);
		board.setPosition(1,1, Counter.BLACK);
		counter = board.getPosition(3, 3);
		System.out.println(counter);

		board.printBoard();
*/
			
//		counter = game.getTurn();
//		System.out.println(counter);
//
//		counter = game.getWinner();
//		System.out.println(counter);

//		boolean valid;
//		valid = game.executeMove(Counter.WHITE, 3);
//		valid = game.executeMove(Counter.WHITE, 2);
//		valid = game.executeMove(Counter.BLACK, 3);
//		valid = game.executeMove(Counter.WHITE, 5);
//		valid = game.executeMove(Counter.BLACK, 6);
//		valid = game.executeMove(Counter.WHITE, 7);
//		valid = game.executeMove(Counter.BLACK, 9); 
//		valid = game.executeMove(Counter.BLACK, 15); 
//		valid = game.executeMove(Counter.WHITE, 10);  
			
//		valid = game.executeMove(Counter.BLACK, 15); 
		
//		Board board1 = game.getBoard();
//		Counter counter1 = board1.getPosition(2, 2);
		
//		for (int i = 0; i < Resources.BOARD_DIM;i++) {
//			for (int j = 0; j < Resources.BOARD_DIM;j++) {
//				if (j % 2 == 0) {
//					board1.setPosition(i, j, Counter.BLACK);
//				}
//				else
//				{
//					board1.setPosition(i, j, Counter.W);
//				}				
//			}
//		}
//		
//		game.printBoard();  
		
		 
 