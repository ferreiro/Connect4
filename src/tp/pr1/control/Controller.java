package tp.pr1.control;

import java.util.Scanner;

import tp.pr1.logic.Counter;
import tp.pr1.logic.Game;
import tp.pr1.resources.Resources;

public class Controller {
	private Game game;
	private Scanner in;
	
	public Controller(Game g, java.util.Scanner in) {
		game = g;
		this.in = in;
	}

	public void run() {
		int option, col;
		boolean exit = false;
		boolean valid = false;
		boolean undo;
		
		game.printBoard();
		
		do {
			/*
			if(!game.isFinished())
			{
			*/
				option = Resources.menu();
				
				switch(option) {
				case 0: 
					// Make a move 
					
					do 
					{
						System.out.println("Colum: ");
						col = in.nextInt();
						valid = game.executeMove(game.getTurn(), col);
						
						if (!valid) {
							System.out.println("Movement not valid!");
						}
						
					} while(!valid);
					
					break;
				case 1:
					// Undo 
					undo = false;
					undo = game.undo();
					
					if (!undo) {
						System.out.println("Pues va a ser que no...");
					}
					else
					{
						game.changeTurn();
					}
					
					break;
				case 2:
					// Restart 
					game.reset();
					
					break;
				case 3:
					// Exist
					exit = true;
					break;
					
				}
				
				game.printBoard();
				
				// If it's finished. Then exit the loop.
				
				if (game.getFinished()) {
					exit = true;
					System.out.println("You finish the game");
					
					if (game.getWinner() != Counter.EMPTY) {
						System.out.println(game.getWinner()+ "win the game");
					}
					
				}
				
			/* 
			else
			{
				exit = true;
				System.out.println("GAME IS OVER");
			}
			*/
			
		} while(!exit);
		
	}
}
