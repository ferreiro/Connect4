package tp.pr1.logic;

public class Board {
	private Counter [][] board;
	private int width;
	private int height;
	
	public Board(int tx, int ty) {
		width= tx;
		height = ty;  
		if ((tx < 1) || (ty < 1))
		{
			width = 1;
			height = 1;
		} 
		
		board = new Counter[tx][ty];
		emptyCells(); // A�adir m�todo para inicializar
	}
	
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
		System.out.println(colour);
		board[x - 1][y - 1] = colour;
	}
	 
	public void emptyCells() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = Counter.EMPTY;
			}
		}
	}
	 
	public String toString() {
		String line = "";
		
		for (int i = 1; i <= height; i++) 
		{
			line += "| ";
			
			for (int j = 1; j <= width; j++) 
			{	
				if (getPosition(i, j) == Counter.EMPTY) 
				{
					line += "-";
				}
				else if (getPosition(i, j) == Counter.BLACK)
				{
					line +=  "O";
				}
				else if (getPosition(i, j) == Counter.WHITE)
				{
					line += "X";
				}			 
			}
			line += " |";
			line += " \n";			
		}	
		
		for (int i = 0; i < width; i++) {
			if (i == 0) {
				line += "--";
			}
			line += "-";
			if (i == width - 1) {
				line += "--";
			}
		}
		
		return line;
	}
		
}
