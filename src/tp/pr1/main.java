package tp.pr1;

import java.util.Scanner;

import tp.pr1.control.Controller;
import tp.pr1.logic.Game; 


public class main {

	public static void main(String[] args) {
		Controller controller;
		Game game  = new Game(); 
		Scanner in = new Scanner(System.in); //	Read from the keyboard
		controller = new Controller(game, in);
	
		controller.run();
	}	
}
