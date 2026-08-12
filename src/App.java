
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class App {

    final static int SCALE = 3;
    final static int WIDTH = 50;
    final static int HEIGHT = 25;

    public static void main(String[] args) throws Exception {
        SpriteLoader.getInstance(); // Force the spritesheets to be loaded

        JFrame frame = new JFrame("Mamono Sweeper");

        GameModel model = new GameModel(SCALE, WIDTH, HEIGHT);
        HeaderPanel headerPanel = new HeaderPanel(model);
        BoardPanel boardPanel = new BoardPanel(model);
        FooterPanel footerPanel = new FooterPanel(model);
        GameController controller = new GameController(model);

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int cellSize = SCALE * Config.CELL_SIZE;
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;

                if (e.getButton() == MouseEvent.BUTTON1)
                    controller.handleLeftClick(x, y);

                if (e.getButton() == MouseEvent.BUTTON2)
                    controller.handleMiddleClick(x, y);

                if (e.getButton() == MouseEvent.BUTTON3)
                    controller.handleRightClick(x, y);

                frame.repaint();
            }
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (c < '0' || c > '9')
                    return;

                Point point = boardPanel.getMousePosition();
                if (point == null)
                    return;

                int n = c - '0';
                int cellSize = SCALE * Config.CELL_SIZE;
                int x = point.x / cellSize;
                int y = point.y / cellSize;

                controller.handleMark(n, x, y);
                frame.repaint();
            }
        });

        Timer timer = new Timer(100, (_) -> headerPanel.repaint());
        timer.start();

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
