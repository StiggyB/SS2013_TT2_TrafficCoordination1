package game;

import jgame.JGColor;
import jgame.JGPoint;
import jgame.platform.StdGame;

public class CarGame extends StdGame {

    private static final long serialVersionUID = -6899853102902331390L;

    public CarGame(JGPoint size) {
        initEngine(size.x, size.y);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new CarGame(parseSizeArgs(args, 0));

    }

    @Override
    public void initCanvas() {
        setCanvasSettings(100, 100, 8, 8, null, JGColor.gray, null);
    }

    @Override
    public void initGame() {
        // TODO Auto-generated method stub

    }

}
