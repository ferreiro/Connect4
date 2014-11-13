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
	
	public boolean executeMove(Counter colour, int column)
	{
		boolean validMove = true;
		int firstFreeRow = 1; // creo que aunque pensemos con la logica de una persona esto puede ser 0 porque se inicializa siempre aun asi probamos con 1
		
		if ((column >= 1) && (column <= Resources.BOARD_DIMX)) {
			if (colour == turn) {
				
				firstFreeRow = Resources.freeRowPosition(column, board); 

				if (firstFreeRow == - 1) {
					validMove = false;
				}
				else 
				{					
					board.setPosition(firstFreeRow, column, colour); 
					increaseStack(column);
					
					finished = isFinished(); // Is the game finish?
					
					if (!finished) {
						changeTurn();  // No ha terminado. Change the turn!
					} 
				}
			}
			else
			{
				validMove = false;
			}
		}
		else
		{
			validMove = false;
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
			columnToUndo = undoStack[numUndo - 1];
			rowToUndo = Resources.occupiedRowPosition(board, columnToUndo); 
			board.setPosition(rowToUndo, columnToUndo, Counter.EMPTY); 
			
			success = true;
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
		boolean finished = false;
		
		if (fullBoard() || isWon()) {
			finished = true; 		
		} 
		 
		return finished;
	}
	
	public boolean fullBoard()	//comprueba si esta el tablero lleno
	{
		int i = 1, j = 1;
		boolean full = true;
		while((i <= board.getHeight()) && (full)) {
			while(j <= board.getWidth() && (full)) {
				if (board.getPosition(i, j) == Counter.EMPTY) {
					full = false;
				}
				j++;
			}
			i++;
		}
		return full;
	}

	 // Comprueba si ha ganado alguien
	
	public boolean isWon()
	{
		boolean won = false; 
		 
		won = checkHorizontal();

		if (!won) {
			won = checkVertical();
			if (!won) {
				won = checkDiagonal1();
				if (!won) {
					won = checkDiagonal2();
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
		int tilesCounter, row, column;
		Counter counter, nextCounter;
		
		row = board.getHeight(); // Starts at bottom
		
		while((row >= 1) && (!isFormed)) 
		{	
			tilesCounter = 1; // Reset counter
			column = 1; // Starts at first position
			counter = board.getPosition(row, column); // Color of first cell on each iteration
			
			while ((column < Resources.BOARD_DIMX) && (!isFormed)) 
			{
				nextCounter = board.getPosition(row, column + 1);
				
				if ((counter == nextCounter) && (counter != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						this.winner = counter;
					}
				}
				else {
					tilesCounter = 1;
					counter = board.getPosition(row, column + 1); // next Cell color
				}		
				
				column++; // Go to right
			}			
			row--; // Decrease the row (from bottom to top)
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
		int tilesCounter, row, column;
		Counter counter, nextCounter;
		
		column = 1;
		
		while((column <= Resources.BOARD_DIMX) && (!isFormed)) 
		{
			tilesCounter = 1; // Reset counter
			row = board.getHeight(); // Start at bottom
			counter = board.getPosition(row, column); // Color of first cell on each iteration
			
			while((row > 1) && (!isFormed))  
			{
				nextCounter = board.getPosition(row - 1, column); // take the color of row before
				
				if ((counter == nextCounter) && (counter != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						this.winner = counter;
					}
				}
				else {
					tilesCounter = 1;
					counter = board.getPosition(row - 1, column); // next Cell color
				}		
				
				row--;
			}			
			column++;
		}
		return isFormed;
	}
	
	/***
	 * Check Diagonal
	 * Primero empieza desde la esquina superior derecha y 
	 * comprueba cada diagonal hacia la izquierda
	 * En el segundo bucle comprueba las diagonales
	 * pero para abajo (desde la esquina superior derecha hasta abajo)
	 * 
	 */
	
	public boolean checkDiagonal1() {
		boolean isFormed = false;
		int row, column, tilesCounter, auxColumn, auxRow, numIterations;
		Counter color, nextColor;
		
		row = 1; // Always start in the firt row
		column = board.getWidth(); // Always start in the last column
		color = board.getPosition(row, column);
		
		// starting top right position
		// Checks until the first diagonal
		
		while ((column >= 1) && !(isFormed)) {
			row = 1;
			auxColumn = column;
			tilesCounter = 1;
			numIterations = board.getHeight() - column;
			
			while ((row <= numIterations) && !(isFormed)) {
				color = board.getPosition(row, auxColumn);
				nextColor = board.getPosition(row + 1, auxColumn + 1);
				
				if ((color == nextColor) && (color != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						winner = color;
					}
				}
				else
				{
					tilesCounter = 1;
				}	
				row++;
				auxColumn++;
			}			
			column--;
		}
		
		// starting at (height, 1)
		// Checks diagonals to bottom

		column = 1; // Always start in the first column
		row = board.getHeight(); // Always start in the firt row
		color = board.getPosition(row, column);
		
		while ((row >= 1) && !(isFormed)) {
			column = 1;
			auxRow = row;
			tilesCounter = 1;
			numIterations = board.getWidth() - row;
			
			while ((column <= numIterations) && !(isFormed)) {
				color = board.getPosition(auxRow, column);
				nextColor = board.getPosition(auxRow + 1, column + 1);
				
				if ((color == nextColor) && (color != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						winner = color;
					}
				}
				else
				{
					tilesCounter = 1;
				}	
				column++;
				auxRow++;
			}			
			row--;
		}			
		
		return isFormed;
	}	
	
	/***
	 * Check Diagonal 2
	 * Primero empieza desde la esquina superior izquierda y 
	 * comprueba cada diagonal (aumentando la row)
	 * En el segundo bucle comprueba las diagonales
	 * pero para abajo (desde la esquina superior derecha hasta abajo)
	 * 
	 */
	
	public boolean checkDiagonal2() {
		boolean isFormed = false;
		int row, column, tilesCounter, auxColumn, auxRow, numIterations;
		Counter color, nextColor;
		
		// starting bottom left position
		// Checks diagonals until the first cell (1,1)

		row = board.getHeight(); // Always start in the last row
		column = 1; // Always start in the first column
		color = board.getPosition(row, column);
		
		while ((row >= 1) && !(isFormed)) {
			column = 1;
			auxRow = row;
			tilesCounter = 1;
			numIterations = row - 1;
			
			while ((column <= numIterations) && !(isFormed)) {
				color = board.getPosition(auxRow, column);
				nextColor = board.getPosition(auxRow - 1, column + 1);
				
				if ((color == nextColor) && (color != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						winner = color;
					}
				}
				else
				{
					tilesCounter = 1;
				}	
				auxRow--;
				column++;
			}			
			row--;
		}
		
		// starting at (height, height) ex: (5,5)
		// Checks from bottom to top right
 
		row = board.getHeight(); // Always start in the last row
		column = board.getHeight(); // Always start in the first column
		color = board.getPosition(row, column);
		
		while ((column >= 1) && !(isFormed)) {
			row = board.getHeight();
			auxColumn = column;
			tilesCounter = 1;
			numIterations = board.getWidth() - column; 
			
			while ((numIterations > 0) && !(isFormed)) {
				color = board.getPosition(row, auxColumn);
				nextColor = board.getPosition(row - 1, auxColumn + 1);
				
				if ((color == nextColor) && (color != Counter.EMPTY)) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						isFormed = true;
						winner = color;
					}
				}
				else
				{
					tilesCounter = 1;
				}	
				row--;
				auxColumn++;
				numIterations--;
			}			
			column--;
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
