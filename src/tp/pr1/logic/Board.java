package tp.pr1.logic;

public class Board {
	private Counter [][] board;
	private int width;
	private int height;
	
	public Board(int tx, int ty) {
		width = tx; // Row
		height = ty;  // column
		if ((tx < 1) || (ty < 1))
		{
			width = 1;
			height = 1;
		} 
		
		board = new Counter[height][width];
		emptyCells(); // Metodo para inicializar el tablero		
	}

	/// FUNCTIONS TO TEST

	public void todasColor() {
		for (int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if (i == 0 && j == 0) {
					
				}
				else
				{
					board[i][j] = Counter.WHITE;
				}				
			}
		}
	}

	public void testDiagonal1_top() {
		Counter counter = Counter.BLACK;
		board[4][0] = counter; 
		board[3][1] = counter;
		board[2][2] = counter;
		board[1][3] = counter;
	}
	public void testDiagonal1_Bottom() {
		Counter counter = Counter.BLACK;
		board[4][1] = counter; 
		board[3][2] = counter;
		board[2][3] = counter;
		board[1][4] = counter;
	}
 	
//	public void testDiagonal2_top() {
//		Counter counter = Counter.BLACK;
//		board[0][0] = counter; 
//		board[1][1] = counter;
//		board[2][2] = counter;
//		board[3][3] = counter;
//	}
//	public void testDiagonal2_Bottom() {
//		Counter counter = Counter.BLACK;
//		board[1][0] = counter; 
//		board[2][1] = counter;
//		board[3][2] = counter;
//		board[4][3] = counter;
//	}
	
	public Counter [][] getBoard() {
		return board;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height; 
	}
	
	public Counter getPosition(int x, int y) {
		Counter color = Counter.EMPTY;
		if ((x >= 1 && x <= width) || (y >= 1 && y <= height)) {
			color = board[y - 1][x - 1];
		}
		return color;		
	}
	
	public void setPosition(int x, int y, Counter colour) { 
		// if ((x >= 1 && x <= width) || (y >= 1 && y <= width)) {
			board[y - 1][x - 1] = colour;
		//}		
	}
	 
	public void emptyCells() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				board[y][x] = Counter.EMPTY;
			}
		}
	}
	 
	public String toString() {
		String line = "";
		
		for (int i = 0; i <= width; i++) {
			if (i == 0) {
				line += "    ";
			}
			if ((i > 0) && (i <= width)) {
				line += " " + i + " ";
			}
			if (i == width) {
				line += "  ";
			}
		}
		line += " \n";

		/*
		 * Top lines
		 */
		for (int i = 0; i <= width; i++) {
			if (i == 0) {
				line += "   ";
			}
			line += "---"; 
			if (i == width) {
				line += "\n";
			}
		}
		
		/*
		 * Design of
		 *   1  |
		 *   2  |
		 */
		
		for (int y = 1; y <= height; y++) 
		{
			line +=y;
			
			if (y < 10) {
				line += "  | "; // Add extra space for 1 digit numbers
			}		
			else {
				line += " | "; 
			}
			

			for (int x = 1; x <= width; x++) 
			{	
				if (getPosition(x, y) == Counter.EMPTY) 
				{
					line += " - ";
				}
				else if (getPosition(x, y) == Counter.BLACK)
				{
					line +=  " B "; // cambiar por O. Estoy probando
				}
				else if (getPosition(x, y) == Counter.WHITE)
				{
					line += " W "; // Cambiar por X cuando se termine de debug
				}			 
			}
			line += " |";
			line += " \n";			
		}	

		/*
		 * Bottom lines
		 */
		for (int i = 0; i <= width; i++) {
			if (i == 0) {
				line += "   ";
			}
			line += "---"; 
			if (i == width) {
				line += "\n";
			}
		}
		
		return line;
	}
		
}
