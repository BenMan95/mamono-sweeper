
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

    private final GameModel game;

    public BoardPanel(GameModel game) {
        this.game = game;
        int width = game.scale * game.width * Config.CELL_SIZE;
        int height = game.scale * game.height * Config.CELL_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(game.scale, game.scale);
        for (int x = 0; x < game.width; x++) {
            for (int y = 0; y < game.height; y++) {
                paintCell(g, x, y);
            }
        }
    }

    private void paintCell(Graphics g, int x, int y) {
        SpriteLoader sprites = SpriteLoader.getInstance();
        int cellX = x * Config.CELL_SIZE;
        int cellY = y * Config.CELL_SIZE;

        boolean open = game.getOpen(x, y);
        Image bg = sprites.cell(open);
        g.drawImage(bg, cellX, cellY, null);

        if (!open) {
            int mark = game.getMark(x, y);
            paintNumber(g, mark, x, y, NumberColor.GREEN);
            return;
        }

        int enemy = game.getEnemy(x, y);
        if (enemy != 0) {
            Image enemySprite = sprites.enemy(enemy);
            g.drawImage(enemySprite, cellX, cellY, null);
            return;
        }

        int count = game.getAdjacentCount(x, y, true);
        if (count != 0) {
            String countStr = String.valueOf(count);
            int pixelX = x * Config.CELL_SIZE + Config.CELL_SIZE / 2;
            int pixelY = y * Config.CELL_SIZE + (Config.CELL_SIZE - Config.SMALL_TEXT_HEIGHT) / 2;
            paintText(g, countStr, pixelX, pixelY);
        }
    }

    private void paintNumber(Graphics g, int num, int x, int y, NumberColor color) {
        SpriteLoader sprites = SpriteLoader.getInstance();

        int digits = 0;
        for (int temp = num; temp != 0; temp /= 10) {
            digits++;
        }

        int cellX = Config.CELL_SIZE * x;
        int cellY = Config.CELL_SIZE * y + (Config.CELL_SIZE - Config.SMALL_TEXT_HEIGHT) / 2;
        int offsetX = (Config.CELL_SIZE + digits * Config.SMALL_TEXT_WIDTH) / 2;
        for (int i = 0; i < digits; i++) {
            int digit = num % 10;
            num /= 10;

            offsetX -= Config.SMALL_TEXT_WIDTH;
            g.drawImage(sprites.digit(digit, color), cellX + offsetX, cellY, this);
        }
    }

    private void paintText(Graphics g, String text, int x, int y) {
        SpriteLoader sprites = SpriteLoader.getInstance();
        x -= text.length() * Config.SMALL_TEXT_WIDTH / 2;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Image charImage = sprites.character(c, false);
            g.drawImage(charImage, x, y, null);
            x += Config.SMALL_TEXT_WIDTH;
        }
    }

}
