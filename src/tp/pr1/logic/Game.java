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
		numUndo = 0;
		turn = Counter.WHITE;
		this.board = new Board(Resources.BOARD_DIMX, Resources.BOARD_DIMY);
		undoStack = new int[board.getWidth() * board.getHeight()]; //maximo de deshacer igual al numero max de casillas		
	}
	
	public boolean executeMove(Counter colour, int column)
	{
		boolean validMove = true;
		int firstFreeRow = 1; // creo que aunque pensemos con la logica de una persona esto puede ser 0 porque se inicializa siempre aun asi probamos con 1
		
		if (column >= 1 && column <= Resources.BOARD_DIMX) {
			if (colour == turn) {
				firstFreeRow = Resources.freeRowPosition(column, board); 
				if (firstFreeRow == -1) {
					validMove = false;
				}
				else 
				{					
					board.setPosition(firstFreeRow, column, colour); 
					increaseStack(column);
					
					/** Is the game finished? **/
					finished = isFinished();
					
					if (!finished) {
						System.out.println("change turn");
						changeTurn(); 
					}
					else
					{
						Counter turn = getTurn();
						System.out.println("The winner is " + turn);
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
			System.out.println("UNDO");
			System.out.println("numUndo"+numUndo);
			
			columnToUndo = undoStack[numUndo - 1];
			System.out.println("columnToUndo"+columnToUndo);
			
			rowToUndo = Resources.occupiedRowPosition(board, columnToUndo); 
			System.out.println("rowToUndo"+rowToUndo);
			
			board.setPosition(rowToUndo, columnToUndo, Counter.EMPTY); 
			System.out.println("Get position"+board.getPosition(rowToUndo, columnToUndo));
			
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
	
	public boolean isWon()//comprueba si ha ganado alguien
	{
		boolean finished = false; 
		
		finished = checkHorizontal();

		if (!finished) {
			finished = checkVertical();
			if (!finished) {
				finished = checkDiagonal1();
				if (!finished) {
					finished = checkDiagonal2();
				}
			}
		}
		
		return finished;
	}
	
	public boolean fullBoard()//comprueba si esta el tablero lleno
	{
		int i = 1, j = 1;
		boolean full = true;
		while(i <= board.getHeight()) {
			while(j <= board.getWidth()) {
				if (board.getPosition(i, j) == Counter.EMPTY) {
					full = false;
				}
				j++;
			}
			i++;
		}
		return full;
	}
	
	public boolean isFinished()//comprueba las dos anteriores a la vez
	{
		boolean finished = false;
		
		if (fullBoard() || isWon())
		{
			finished = true;
		}
		return finished;
	}
	
	/***
	 * Check horizontal 
	 * Comprueba si hay alguna fila horizontal donde se hayan formado 
	 * Y si es igual a la constante para ganar, finaliza el bucle
	 */
	
	public boolean checkHorizontal() {
		boolean formedTiles = false;
		int row = 1, column = 1, tilesCounter;
		Counter counter; 
		
		while((!formedTiles) && (row <= board.getHeight())) 
		{
			column = 1; // Reset the Colum to 1	
			counter = board.getPosition(row, column);
			tilesCounter = 0;//estaba en uno y siempre contaba uno menos
			
			while((!formedTiles) && (column <= board.getWidth())) 
			{
				if ((board.getPosition(row, column) == counter) && board.getPosition(row, column) != Counter.EMPTY) //estaba solo la primera condicion
				{
					tilesCounter++;					
					if (tilesCounter == Resources.TILES_TO_WIN) {
						formedTiles = true;
					}
				}
				else
				{
					tilesCounter = 0;
					counter = board.getPosition(row, column);
				}
				
				column++;
			}		
			row++;			
		}
		return formedTiles;
	}

	/***
	 * Check Vertical tiles 
	 * Comprueba si hay en alguna columna se ha formado una tile con  
	 * la dimensi�n de la constante para ganar
	 */
	
	public boolean checkVertical() {
		boolean formedTiles = false;
		int row = 1, column = 1, tilesCounter;
		Counter counter; 
		
		while((!formedTiles) && (column <= board.getWidth())) 
		{
			row = 1; // Reset the Row to 0	
			counter = board.getPosition(row, column);
			tilesCounter = 0;//estaba en uno y siempre contaba uno menos
			
			while((!formedTiles) && (row <= board.getHeight())) 
			{
				if ((board.getPosition(row, column) == counter) && board.getPosition(row, column) != Counter.EMPTY) 
				{
					tilesCounter++;					
					if (tilesCounter == Resources.TILES_TO_WIN) {
						formedTiles = true;
					}
				}
				else
				{
					tilesCounter = 0;
					counter = board.getPosition(row, column);
				}
				
				row++;
			}		
			column++;			
		}
		return formedTiles;
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
		boolean formedTiles = false;
		int row = 0, column = 0, tilesCounter, auxColum, auxRow;
		Counter counter; 
		
		// Checks the Middle Left
		
		column = board.getWidth();
		
		while((!formedTiles) && (column > 0)) 
		{
			row = 1; // Reset the Row to 1	
			counter = board.getPosition(row, column);
			tilesCounter = 0;//estaba en uno y siempre contaba uno menos
			auxColum = column; // copy the value of colum
			
			while((!formedTiles) && (row <= column)) 
			{
				if((board.getPosition(row, auxColum) == counter) && board.getPosition(row, column) != Counter.EMPTY) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						formedTiles = true;
					}
				}
				else {
					tilesCounter = 0;
				}
				
				row++;
				auxColum--;
			}
			column--;
		}
		
		if(!formedTiles) {
			// Checks the Middle Right
			row = 2;
			
			while((!formedTiles) && (row <= board.getHeight())) 
			{
				column = board.getWidth(); // Reset the Row to 0	
				counter = board.getPosition(column, row);
				tilesCounter = 0;//estaba en uno y siempre contaba uno menos
				auxRow = row; // copy the value of row
				
				while((!formedTiles) && (column >= row)) 
				{
					if((board.getPosition(column, auxRow) == counter) && board.getPosition(row, column) != Counter.EMPTY) {
						tilesCounter++;
						if (tilesCounter == Resources.TILES_TO_WIN) {
							formedTiles = true;
						}
					}
					else {
						tilesCounter = 0;
					}

					column--;
					auxRow++;
				}
				row++;
			}
		}
		return formedTiles;
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
		boolean formedTiles = false;
		int row = 0, column = 0, tilesCounter, auxColum, auxRow;
		Counter counter; 
		
		// Checks the Middle Left
		
		column = 1;
		
		while((!formedTiles) && (column <= board.getWidth())) 
		{
			row = 1; // Reset the Row to 1	
			counter = board.getPosition(row, column);
			tilesCounter = 0;//estaba en uno y siempre contaba uno menos
			auxColum = column; // copy the value of colum
			
			while((!formedTiles) && (row <= (board.getHeight() + 1) - column))			//(row < (board.getHeight() - 1) - column)) 
			{
				if((board.getPosition(row, auxColum) == counter) && board.getPosition(row, column) != Counter.EMPTY) {
					tilesCounter++;
					if (tilesCounter == Resources.TILES_TO_WIN) {
						formedTiles = true;
					}
				}
				else {
					tilesCounter = 0;
				}	
				row++;
				auxColum++;
			}
			column++;
		}
		
		if(!formedTiles) {
			// Checks the Middle Right
			row = 2;
			
			while((!formedTiles) && (row <= board.getHeight())) 
			{
				column = board.getWidth(); // Reset the Row to 0	
				counter = board.getPosition(column, row);
				tilesCounter = 0;//estaba en uno y siempre contaba uno menos
				auxRow = row; // copy the value of row
				
				while((!formedTiles) && (column <= (board.getWidth() + 1) - row)) //(column <= (board.getWidth() - 1) - row)) 
				{
					if((board.getPosition(column, auxRow) == counter) && board.getPosition(row, column) != Counter.EMPTY) {
						tilesCounter++;
						if (tilesCounter == Resources.TILES_TO_WIN) {
							formedTiles = true;
						}
					}
					else {
						tilesCounter = 0;
					}
					column++;
					auxRow++;
				}
				row++;
			}
		}
		return formedTiles;
	}  
	
	// Show the winner.
	public Counter getWinner()//nos falta llamar a getwinner en algun lugar
	{
		return turn;
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
