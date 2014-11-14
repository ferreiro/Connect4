package tp.pr1.resources;

import java.util.Scanner;
import tp.pr1.logic.Board;
import tp.pr1.logic.Counter;

public class Resources {
	
	public static final int TILES_TO_WIN = 4;
	public static final int BOARD_DIMX = 7, BOARD_DIMY = 6;

	public static int freeRowPosition(int col, Board board) {
		int row = -1;
		int y =  board.getHeight();
		boolean empty = false;
	
		while ((!empty) && (y >= 1)) {
			if (board.getPosition(col,y) == Counter.EMPTY) {
				empty = true;
				row = y;
			}
			else
			{
				y--;
			}			
		} 
		return row;
	}

	// This functions is used for the Undo Function
	// Which returns the first row occupied (means that is the last movement of the user in that colum)
	
	public static int occupiedRowPosition(Board board, int col) {
		int height = board.getHeight();
		int row = height, y = 1;
		boolean occupied = false;
		
		while (!occupied && y <= height){
			if (board.getPosition(col, y) != Counter.EMPTY) 
			{
				occupied = true;
				row = y;
			}
			y++;
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
			System.out.println(" ");
			
			option = in.nextInt();
			
			if (option >= 0 && option <= 3) {
				valid = true;
			}
		}
		 
		return option;
	}
	
}
