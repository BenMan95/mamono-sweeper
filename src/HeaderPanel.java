
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JPanel;

public class HeaderPanel extends JPanel {

    private final GameModel game;

    public HeaderPanel(GameModel game) {
        this.game = game;
        int height = game.scale * Config.HEADER_HEIGHT;
        int width = game.scale * game.width * Config.CELL_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(game.scale, game.scale);

        int margin = (Config.HEADER_HEIGHT - Config.BIG_TEXT_HEIGHT) / 2;

        int threshold = game.nextExpThreshold();
        String thresholdStr = threshold < 0 ? "-" : String.valueOf(threshold);
        String leftHeader = String.format(
                "LV:%d HP:%d EX:%d NE:%s",
                game.getLevel(),
                game.getHp(),
                game.getExp(),
                thresholdStr);
        paintText(g, leftHeader, margin, margin, false);

        int width = game.width * Config.CELL_SIZE;
        String rightHeader = String.format("TIME:%d", game.gameTime());
        paintText(g, rightHeader, width - margin, margin, true);
    }

    private void paintText(Graphics g, String text, int x, int y, boolean reverse) {
        SpriteLoader sprites = SpriteLoader.getInstance();
        if (reverse) {
            x -= text.length() * Config.BIG_TEXT_WIDTH;
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Image charImage = sprites.character(c, true);
            g.drawImage(charImage, x, y, null);
            x += Config.BIG_TEXT_WIDTH;
        }
    }

}
