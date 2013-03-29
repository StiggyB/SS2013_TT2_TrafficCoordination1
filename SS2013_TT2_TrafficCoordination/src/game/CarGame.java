package game;

import jgame.JGColor;
import jgame.JGPoint;
import jgame.platform.StdGame;

public class CarGame extends StdGame {

	private static final long serialVersionUID = -6899853102902331390L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CarGame(new JGPoint(800,800));

	}
	
	public CarGame() {
		initEngineApplet(); 
	}
	
	public CarGame(JGPoint size) {
		initEngine(size.x, size.y);
	}

	@Override
	public void initCanvas() {
		setCanvasSettings(100, 100, 8, 8, null, JGColor.gray, null);
	}

	@Override
	public void initGame() {
		setFrameRate(35, 2);
	}

	public void doFrame() {

	}
	
	public void paintFrame() {
		
	}
}
