package tp.pr1.logic.test;

import tp.pr1.logic.Counter;
import tp.pr1.logic.Game;
import tp.pr1.logic.Board;

public class UtilsPartidaYTablero {

	public static boolean tableroVacio(Board t) {
		for (int x = 1; x <= t.getWidth(); ++x)
			if (!columnaVacia(t, x))
				return false;
		return true;
	}
	
	private static boolean columnaVacia(Board b, int x) {
		for (int y = 1; y <= b.getHeight(); ++y)
			if (b.getPosition(x,  y) != Counter.EMPTY)
				return false;
		return true;
	}
	
	private static int columnaAdecuada(Board b, Counter col, int noUses) {
		
		if (noUses == 1) return 6;
		if (noUses == 6) return 1;
		
		if (columnaVacia(b, 1) && columnaVacia(b, 2))
			return 1;
		
		return 6;
	}
	
	// Prepara la partida para que se pueda colocar, en el siguiente movimiento
	// la ficha del color dado en la posición indicada. Para eso utiliza
	// las reglas de la partida de C4. Para que pueda hacerlo, debe haber una columna
	// vacía en el tablero y que la columna donde se quiere colocar
	// cumpla las restricciones del conecta 4...
	// Y también asume que no habrá riesgo de hacer C4 si se pone en
	// alguna de las columnas vacías.
	public static boolean preparaColocacionFicha(Game g, Counter colour, int x, int y) {

		if (g.isFinished()) return false;
		
		Board b = g.getBoard();

		// Sanity-check: encima de y no hay nada
		for (int i = y; i >= 1; --i)
			if (b.getPosition(x, i) != Counter.EMPTY)
				return false;
		
		// Sacamos la fila sobre la que nos apoyaríamos
		int ultimaConFicha = y + 1;
		while ((ultimaConFicha <= b.getHeight()) && (b.getPosition(x, ultimaConFicha) == Counter.EMPTY))
			ultimaConFicha++;
		
		int aPoner = ultimaConFicha - y; // Con la ficha final que no pondremos

		if ((aPoner % 2 == 1) != (g.getTurn() == colour)) {
			// Hay que poner una en una columna dummy para ajustar
			// turno
			int aux = columnaAdecuada(b, g.getTurn(), x);
			if (aux == 0) return false;
			g.executeMove(g.getTurn(), aux);
		}
		
		// Antes de poner, garantizamos que no hay huecos por
		// debajo...
		for (int i = ultimaConFicha + 1; i <= b.getHeight(); ++i) {
			if (b.getPosition(x,i) == Counter.EMPTY)
				b.setPosition(x, i, (colour == Counter.WHITE) ? Counter.BLACK : Counter.WHITE);
		}
		
		while (aPoner > 1) {
			g.executeMove(g.getTurn(), x);
			aPoner--;
		}
		return true;
	}
	
}
