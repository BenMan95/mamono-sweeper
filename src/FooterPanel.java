
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

public class FooterPanel extends JPanel {

    private final GameModel game;

    public FooterPanel(GameModel game) {
        this.game = game;
        int width = game.scale * Config.FOOTER_HEIGHT;
        int height = game.scale * game.width * Config.CELL_SIZE;
        setPreferredSize(new Dimension(height, width));
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(game.scale, game.scale);

        SpriteLoader sprites = SpriteLoader.getInstance();

        int width = game.width * Config.CELL_SIZE;
        int subwidth = Config.CELL_SIZE + 9 * Config.SMALL_TEXT_WIDTH;
        int x = (width - 9 * subwidth) / 2;
        int y1 = (Config.FOOTER_HEIGHT - Config.CELL_SIZE) / 2;
        int y2 = (Config.FOOTER_HEIGHT - Config.SMALL_TEXT_HEIGHT) / 2;

        for (int level = 1; level <= Config.MAX_LEVEL; level++) {
            int count = game.getEnemyCount(level);
            boolean revealed = game.getEnemyRevealed(level);
            Image enemy = sprites.enemy(revealed ? level : 0);
            g.drawImage(enemy, x, y1, null);

            String display = String.format("LV%d:*%d", level, count);
            paintText(g, display, x + Config.CELL_SIZE, y2);

            x += subwidth;
        }
    }

    private void paintText(Graphics g, String text, int x, int y) {
        SpriteLoader sprites = SpriteLoader.getInstance();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Image charImage = sprites.character(c, false);
            g.drawImage(charImage, x, y, null);
            x += Config.SMALL_TEXT_WIDTH;
        }
    }

}
