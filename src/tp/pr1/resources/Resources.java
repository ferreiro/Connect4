package tp.pr1.resources;

import java.util.Scanner;
import tp.pr1.logic.Board;
import tp.pr1.logic.Counter;

public class Resources {
	
	public static final int TILES_TO_WIN = 4;
	public static final int BOARD_DIMX = 4, BOARD_DIMY = 4;

	public static int freeRowPosition(int col, Board board) {
		int row = -1;
		int i =  board.getHeight();
		boolean empty = false;
	
		while ((!empty) && (i >= 1)) {
			if (board.getPosition(i, col) == Counter.EMPTY) {
				empty = true;
				row = i;
			}
			else
			{
				i--;
			}			
		} 
		return row;
	}

	// This functions is used for the Undo Function
	// Which returns the first row occupied (means that is the last movement of the user in that colum)
	
	public static int occupiedRowPosition(Board board, int col) {
		int height = board.getHeight();
		int row = height, i = 1;
		boolean occupied = false;
		
		while (!occupied && i <= height){
			if (board.getPosition(i, col) != Counter.EMPTY) 
			{
				occupied = true;
				row = i;
			}
			i++;
		}
		return row;
	}
	
	public static int menu() {
		int option = - 1;
		boolean valid = false;
		
		Scanner in = new Scanner(System.in);
		
		while(!valid) { 
			System.out.println("0. Make a move   1. Undo");
			System.out.println("2. Restart       3. Exit");
//			System.out.println("1. Undo");
//			System.out.println("2. Restart"); 
//			System.out.println("3. Exist"); 
			System.out.println(" ");
			
			option = in.nextInt();
			
			if (option >= 0 && option <= 3) {
				valid = true;
			}
		}
		 
		return option;
	}
	
}
