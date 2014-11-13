package tp.pr1.logic;

public class Board {
	private Counter [][] board;
	private int width;
	private int height;
	
	public Board(int tx, int ty) {
		width = tx; // 
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

	public void testDiagonal1_top() {
		Counter counter = Counter.BLACK;
		board[0][0] = counter; 
		board[1][1] = counter;
		board[2][2] = counter;
		board[3][3] = counter;
	}
	public void testDiagonal1_Bottom() {
		Counter counter = Counter.BLACK;
		board[1][0] = counter; 
		board[2][1] = counter;
		board[3][2] = counter;
		board[4][3] = counter;
	}
	public void testDiagonal2_top() {
		Counter counter = Counter.BLACK;
		board[4][0] = counter; 
		board[3][1] = counter;
		board[2][2] = counter;
		board[1][3] = counter;
	}
	public void testDiagonal2_Bottom() {
		Counter counter = Counter.BLACK;
		board[4][1] = counter; 
		board[3][2] = counter;
		board[2][3] = counter;
		board[1][4] = counter;
	}
	// 	END FUNCTIONS TO TEST
	
	
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
		return board[x - 1][y - 1];
	}
	
	public void setPosition(int x, int y, Counter colour) { 
		board[x - 1][y - 1] = colour;
	}
	 
	public void emptyCells() {
		for (int row = 0; row < height; row++) {
			for (int colum = 0; colum < width; colum++) {
				board[row][colum] = Counter.EMPTY;
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
		
		for (int i = 1; i <= height; i++) 
		{
			line +=i;
			
			if (i < 10) {
				line += "  | "; // Add extra space for 1 digit numbers
			}		
			else {
				line += " | "; 
			}
			

			for (int j = 1; j <= width; j++) 
			{	
				if (getPosition(i, j) == Counter.EMPTY) 
				{
					line += " - ";
				}
				else if (getPosition(i, j) == Counter.BLACK)
				{
					line +=  " B "; // cambiar por O. Estoy probando
				}
				else if (getPosition(i, j) == Counter.WHITE)
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
