package tp.pr1.logic;

import tp.pr1.resources.Resources;


public class Game {
	private Board board;
	private Counter turn;
	private boolean finished = false;
	private Counter winner;
	private int[] undoStack;
	private int numUndo;
	
	public Game() {
		this.numUndo = 0;
		this.turn = Counter.WHITE;
		this.winner = Counter.EMPTY;
		this.board = new Board(Resources.BOARD_DIMX, Resources.BOARD_DIMY);
		this.undoStack = new int[board.getWidth() * board.getHeight()]; //maximo de deshacer igual al numero max de casillas		
	}
	
	public void test() {
		board.diago1();
	}
	
	public boolean executeMove(Counter colour, int column)
	{
		boolean validMove = false;
		int firstFreeRow = 1; // creo que aunque pensemos con la logica de una persona esto puede ser 0 porque se inicializa siempre aun asi probamos con 1
		 
		// board.todasColor();
		
		if (!finished) {
			if ((column >= 1) && (column <= Resources.BOARD_DIMX)) {
				if (colour == turn) {
					
					firstFreeRow = Resources.freeRowPosition(column, board); 

					if (firstFreeRow > - 1) {		
						validMove = true;

						board.setPosition(column, firstFreeRow, colour); 
						increaseStack(column);
						
						finished = isFinished(); // Is the game finish? 
						
						if (!finished) {
							changeTurn();  // No ha terminado. Change the turn!
						} 
					}
				}
			}
		}			
		return validMove;
	}
	
	public void increaseStack(int colum) {
		undoStack[numUndo] = colum;
		numUndo++;
	}
	
	public boolean undo() {// esta funcion no se si hay que cambiar algo para que siga la logica de empezar en 1 y no en cero esta igual que cuando seguia la de 0
		boolean success = false;
		int columnToUndo, rowToUndo; 
		
		if (numUndo > 0) {
			success = true;
			changeTurn();
			
			columnToUndo = undoStack[numUndo - 1];
			rowToUndo = Resources.occupiedRowPosition(board, columnToUndo); 
			
			board.setPosition(columnToUndo, rowToUndo, Counter.EMPTY); 
			
			numUndo--;
		}	 
		return success;
	}

	public void reset() {
		numUndo = 0;
		turn = Counter.WHITE;
		board.emptyCells();		
	}
	
	public boolean isFinished()//comprueba las dos anteriores a la vez
	{
		finished = isWon();
		
		if (!finished) {
			finished = fullBoard();
		}  
		
		return finished;
	}
	
	public boolean fullBoard()	//comprueba si esta el tablero lleno
	{
		int y = 1, x = 1;
		boolean full = true;
		while((y <= board.getHeight()) && (full)) {
			while(x <= board.getWidth() && (full)) {
				if (board.getPosition(x, y) == Counter.EMPTY) {
					full = false;
				}
				x++;
			}
			y++;
		}
		return full;
	}

	 // Comprueba si ha ganado alguien
	
	public boolean isWon()
	{
		boolean won = false; 
		 
		won = checkDiagonal1();
		 
		if (!won) {
			won = checkDiagonal2();
			if (!won) {
				 won = checkHorizontal();
				if (!won) {
					won = checkVertical();
				}
			}
		}
		
		return won;
	}

	/***
	 * Check horizontal 
	 * Comprueba si hay alguna fila horizontal donde se hayan formado 
	 * Y si es igual a la constante para ganar, finaliza el bucle
	 */
	
	// He vuelto a escribir esta funcion desde 0. Parece que funciona
	// La logica que sigue es comparar la celda actual con la siguiente
	
	public boolean checkHorizontal() {
		boolean isFormed = false;
		int tilesCounter, y, x;
		Counter counter, nextCounter;
		
		y = board.getHeight(); // Starts at bottom
		
		while((y >= 1) && (!isFormed)) 
		{	
			tilesCounter = 1; // Reset counter
			x = 1; // Starts at first position
			counter = board.getPosition(x, y); // Color of first cell on each iteration
			
			while ((x < Resources.BOARD_DIMX) && (!isFormed)) 
			{
				nextCounter = board.getPosition(x + 1, y);
				
				if ((counter == nextCounter) && (counter != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						this.winner = counter;
					}
				}
				else {
					tilesCounter = 1;
					counter = board.getPosition(x + 1, y); // next Cell color
				}		
				
				x++; // Go to right
			}			
			y--; // Decrease the row (from bottom to top)
		}
		return isFormed;
	}

	/***
	 * Check Vertical tiles 
	 * Comprueba si hay en alguna columna se ha formado una tile con  
	 * la dimension de la constante para ganar
	 */

	// He vuelto a escribir esta funcion desde 0. Parece que funciona
	// La logica que sigue es comparar la celda actual con la siguiente
	
	public boolean checkVertical() {
		boolean isFormed = false;
		int tilesCounter, y, x;
		Counter counter, nextCounter;
		
		x = 1;
		
		while((x <= Resources.BOARD_DIMX) && (!isFormed)) 
		{
			tilesCounter = 1; // Reset counter
			y = board.getHeight(); // Start at bottom
			counter = board.getPosition(x, y); // Color of first cell on each iteration
			
			while((y > 1) && (!isFormed))  
			{
				nextCounter = board.getPosition(x, y - 1); // take the color of row before
				
				if ((counter == nextCounter) && (counter != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						this.winner = counter;
					}
				}
				else {
					tilesCounter = 1;
					counter = board.getPosition(x, y - 1); // next Cell color
				}		
				
				y--;
			}			
			x++;
		}
		return isFormed;
	}
	
	/***
	 * Check Diagonal 1
	 * Sentido:  \
	 * Primero empieza desde la esquina superior izquierda y 
	 * comprueba cada diagonal (aumentando la row)
	 * En el segundo bucle comprueba las diagonales
	 * pero para abajo (desde la esquina superior derecha hasta abajo)
	 * 
	 */
	
//	public boolean checkDiagonal1() {
//		boolean isFormed = false;
//		int y, x, tilesCounter, aux_Y, aux_X, numIterations, counter;
//		Counter color, nextColor;
//		
//		// Starting at top right (y = 1, x = 7)
//		
//		y = 1;
//		x = board.getWidth();
//		
//		while ((x >= 1) && (!isFormed)) {
//			y = 1;
//			aux_X = x;
//			counter = 1;
//			tilesCounter = 1;	
//			numIterations = board.getWidth() - x;
//			
//			while ((counter <= numIterations) && (!isFormed)) {
//				color = board.getPosition(aux_X, y);
//				nextColor = board.getPosition(aux_X + 1, y + 1);
//
//				if ((color == nextColor) && (color != Counter.EMPTY)) 
//				{
//					tilesCounter++;
//					
//					if (tilesCounter == Resources.TILES_TO_WIN) {
//						isFormed = true;
//						finished = true;
//						winner = color;
//					}
//				}
//				else
//				{
//					tilesCounter = 1;
//				}	
//				counter++;
//				aux_X++;
//				y++;
//			}	
//			x--;
//		}
//		
//		if (!isFormed) 
//		{
//			// Starting at top right (y = 1, x = 7)
//			
//			y = 2;
//			x = 1;
//			
//			while ((y <= board.getHeight()) && (!isFormed)) {
//				x = 1;
//				aux_Y = y;
//				counter = 1;
//				tilesCounter = 1;
//				numIterations = board.getHeight() - y;
//				
//				while ((counter <= numIterations) && (!isFormed)) {
//					color = board.getPosition(x, aux_Y);
//					nextColor = board.getPosition(x + 1, aux_Y + 1);
//					
//					if ((color == nextColor) && (color != Counter.EMPTY)) 
//					{
//						tilesCounter++;
//						
//						if (tilesCounter == Resources.TILES_TO_WIN) {
//							isFormed = true;
//							finished = true;
//							winner = color;
//						}
//					}
//					else
//					{
//						tilesCounter = 1;
//					}	
//					counter++;
//					aux_Y++;
//					x++;					
//				}
//				y++;
//			}
//		}
//		
//		return isFormed;
//	}
//	
	
	public boolean checkDiagonal1() {
		boolean isFormed = false;
		int y, x, tilesCounter, aux_Y, aux_X, numIterations;
		Counter color, nextColor;
		
		// starting bottom left position
		// Checks diagonals until the first cell (1,1)

		x = 1; // Always start in the first column
		y = 1; // Always start in the last row 
		numIterations = y;
		
		while ((y <= board.getHeight()) && !(isFormed)) {
			x = 1;
			aux_Y = y;
			tilesCounter = 1;
			
			if (numIterations > board.getWidth())
			{
				numIterations = board.getWidth();
			}
			
			while ((x < numIterations) && !(isFormed)) {
				color = board.getPosition(x, aux_Y);
				nextColor = board.getPosition(x + 1, aux_Y - 1);
				
				if ((color == nextColor) && (color != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						finished = true;
						winner = color;
					}
				}
				else
				{
					tilesCounter = 1;
				}	
				aux_Y--;
				x++;
			}			
			y++;
			numIterations++;
		}
		
		if (!isFormed) {
			// starting at (height, height) ex: (5,5)
			// Checks from bottom to top right
	 
			y = board.getHeight(); // Always start in the last row
			x = board.getWidth(); // Always start in the first column; pero aqui ponia getHeight no Width
			color = board.getPosition(x, y);
			int counter = 1;
			
			while ((x > 1) && !(isFormed)) {
				y = board.getHeight();
				aux_X = x;
				tilesCounter = 1;
				numIterations = board.getWidth() - x + 1;
				if (numIterations > board.getHeight())
				{
					numIterations = board.getHeight();
				}
				counter = 1;
				while ((counter < numIterations) && !(isFormed)) {
					color = board.getPosition(aux_X, y);
					nextColor = board.getPosition(aux_X + 1, y - 1);
					
					if ((color == nextColor) && (color != Counter.EMPTY)) {
						tilesCounter++;
						if (tilesCounter == Resources.TILES_TO_WIN) {
							isFormed = true;
							finished = true;
							winner = color;
						}
					}
					else
					{
						tilesCounter = 1;
					}	
					y--;
					aux_X++;
					
					counter++;
				}			
				x--;
			}			
		}
		
		return isFormed;
	}	
	
	
	/***
	 * Check Diagonal 2
	 * Sentido /
	 * Primero empieza desde la esquina superior derecha y 
	 * comprueba cada diagonal hacia la izquierda
	 * En el segundo bucle comprueba las diagonales
	 * pero para abajo (desde la esquina superior derecha hasta abajo)
	 * 
	 */
	
	public boolean checkDiagonal2() {
		boolean isFormed = false;
		int y, x, tilesCounter, aux_X, aux_Y, numIterations;
		Counter color, nextColor;
		
		y = 1; // Always start in the firt row
		x = board.getWidth(); // Always start in the last column
		color = board.getPosition(x, y);
		numIterations = 1;
		// starting top right position
		// Checks until the first diagonal
		
		while ((x > 1) && !(isFormed)) {
			y = 1;
			aux_X = x;
			tilesCounter = 1;
			if (numIterations > board.getHeight())
			{
				numIterations = board.getHeight();
			}
			
			while ((y < numIterations) && !(isFormed)) { // o aqui
				color = board.getPosition(aux_X, y);
				nextColor = board.getPosition(aux_X + 1, y + 1);
				
				if ((color == nextColor) && (color != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						finished = true;
						winner = color;
					}
				}
				else
				{
					tilesCounter = 1;
				}	
				y++;
				aux_X++;
			}			
			x--;
			numIterations++;
		}
		
		if (!isFormed) {
			// starting at (height, 1)
			// Checks diagonals to bottom

			x = 1; // Always start in the first column
			y = board.getHeight(); // Always start in the firt row
			color = board.getPosition(x, y);
			
			while ((y >= 1) && !(isFormed)) {
				x = 1;
				aux_Y = y;
				tilesCounter = 1;
				numIterations = board.getHeight() - y + 1;//antes era width y sin el +1
				if (numIterations > board.getWidth())
				{
					numIterations = board.getWidth();
				}
				
				while ((x < numIterations) && !(isFormed)) {//<=
					color = board.getPosition(x, aux_Y);
					nextColor = board.getPosition(x + 1, aux_Y + 1);
					
					if ((color == nextColor) && (color != Counter.EMPTY)) {
						tilesCounter++;
						if (tilesCounter == Resources.TILES_TO_WIN) {
							isFormed = true;
							finished = true;
							winner = color;
						}
					}
					else
					{
						tilesCounter = 1;
					}	
					x++;
					aux_Y++;
				}			
				y--;
			}			
		}
		
		return isFormed;
	}	
	
	// Show the winner.
	public Counter getWinner()//nos falta llamar a getwinner en algun lugar
	{
		return winner;
	}

	public boolean getFinished() {
		return finished;
	}
 
	public void changeTurn ()
	{
		if (turn == Counter.WHITE) {
			turn = Counter.BLACK;
		}
		else if (turn == Counter.BLACK) {
			turn = Counter.WHITE;
		}
	}
	
	public void printBoard() {
		String boardStr;
		boardStr = board.toString();
		System.out.println(boardStr);
	}
		
	
	public Counter getTurn()
	{
		return turn;
	}
	
	// This functions is useless.
	// Due to the javadoc, we have to include it.
	
	public Board getBoard()
	{
		return board;
	}
	
}
