
public class GameController {

    final private GameModel game;

    public GameController(GameModel game) {
        this.game = game;
    }

    public void handleLeftClick(int x, int y) {
        game.assertCoordsValid(x, y);

        if (game.getStart() == null) {
            while (game.getAdjacentCount(x, y) > 0) {
                game.resetBoard();
            }
        }

        recursiveOpen(x, y);
    }

    public void handleMiddleClick(int x, int y) {
        game.assertCoordsValid(x, y);
        if (!game.getOpen(x, y))
            return;

        if (game.getAdjacentCount(x, y, true) > game.getLevel()) 
            return;

        for (int i = 0; i < 9; i++) {
            int dx = i % 3 - 1;
            int dy = i / 3 - 1;

            int offsetX = x + dx;
            int offsetY = y + dy;
            if (game.coordsValid(offsetX, offsetY)) {
                recursiveOpen(offsetX, offsetY);
            }
        }
    }

    public void handleRightClick(int x, int y) {
        game.assertCoordsValid(x, y);
        int currentMark = game.getMark(x, y);
        int newMark = (currentMark + 1) % Config.MAX_LEVEL;
        game.setMark(newMark, x, y);
    }

    public void handleMark(int n, int x, int y) {
        game.assertCoordsValid(x, y);
        game.setMark(n, x, y);
    }

    private void recursiveOpen(int x, int y) {
        if (game.getEnd() != null)
            return;

        if (game.getOpen(x, y))
            return;

        game.open(x, y);

        int count = game.getAdjacentCount(x, y);
        if (count > 0)
            return;

        for (int i = 0; i < 9; i++) {
            int dx = i % 3 - 1;
            int dy = i / 3 - 1;

            int offsetX = x + dx;
            int offsetY = y + dy;
            if (game.coordsValid(offsetX, offsetY)) {
                recursiveOpen(offsetX, offsetY);
            }
        }
    }

}
