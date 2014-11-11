package tp.pr1.control;

import java.util.Scanner;
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
		
		do {
			/*
			if(!game.isFinished())
			{
			*/
				game.printBoard();
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

				if (game.getFinished()) {
					exit = true;
				}
				 
				// if is_finished then Comprobamos el ganador
				// els if (!is_finished) then comprobar si ha ganado alguien
				//	--> Si ha ganado alguien, entonces finished = true
				
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
