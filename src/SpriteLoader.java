
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteLoader {

    private static SpriteLoader instance;

    private final BufferedImage chip;
    private final BufferedImage smallnum;
    private final BufferedImage greensmallnum;
    private final BufferedImage redsmallnum;
    private final BufferedImage bignum;
    private final BufferedImage smallalpha;
    private final BufferedImage bigalpha;

    private SpriteLoader() {
        try {
            chip = loadPath("assets/chip.png");
            smallnum = loadPath("assets/smallnum.png");
            greensmallnum = loadPath("assets/greensmallnum.png");
            redsmallnum = loadPath("assets/redsmallnum.png");
            bignum = loadPath("assets/bignum.png");
            smallalpha = loadPath("assets/smallalpha.png");
            bigalpha = loadPath("assets/bigalpha.png");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load spritesheets");
        }
    }

    private BufferedImage loadPath(String path) throws IOException {
        File file = new File(path);
        return ImageIO.read(file);
    }

    static public SpriteLoader getInstance() {
        if (instance == null) {
            instance = new SpriteLoader();
        }
        return instance;
    }

    public BufferedImage digit(int num, NumberColor color) {
        if (num < 0 || num > Config.MAX_LEVEL) {
            throw new IllegalArgumentException("Not a digit");
        }

        BufferedImage sheet = switch (color) {
            case WHITE ->
                smallnum;
            case GREEN ->
                greensmallnum;
            case RED ->
                redsmallnum;
            default ->
                smallnum;
        };

        return sheet.getSubimage(
                num * Config.SMALL_TEXT_WIDTH,
                0,
                Config.SMALL_TEXT_WIDTH,
                Config.SMALL_TEXT_HEIGHT);
    }

    public BufferedImage digit(int num) {
        return digit(num, NumberColor.WHITE);
    }

    public BufferedImage enemy(int level) {
        if (level < 0 || level > 9) {
            throw new IllegalArgumentException("Invalid enemy level");
        }

        int x = level == 0 ? 1 : 2 + (level - 1) % 5;
        int y = (level - 1) / 5;
        return chip.getSubimage(
                x * Config.CELL_SIZE,
                y * Config.CELL_SIZE,
                Config.CELL_SIZE,
                Config.CELL_SIZE);
    }

    public BufferedImage cell(boolean open) {
        int x = open ? 9 : 0;
        int y = 0;

        return chip.getSubimage(
                x * Config.CELL_SIZE,
                y * Config.CELL_SIZE,
                Config.CELL_SIZE,
                Config.CELL_SIZE);
    }

    public BufferedImage character(char c, boolean big) {
        int x;
        BufferedImage sheet;

        if (c >= '0' && c <= '9') {
            x = c - '0';
            sheet = big ? bignum : smallnum;
        } else if (c >= 'A' && c <= 'Z') {
            x = c - 'A';
            sheet = big ? bigalpha : smallalpha;
        } else {
            x = switch (c) {
                case ':' ->
                    26;
                case '-' ->
                    27;
                case '*' ->
                    28;
                case ' ' ->
                    29;
                default ->
                    throw new IllegalArgumentException("Invalid character");
            };
            sheet = big ? bigalpha : smallalpha;
        }

        int charWidth = big ? Config.BIG_TEXT_WIDTH : Config.SMALL_TEXT_WIDTH;
        int charHeight = big ? Config.BIG_TEXT_HEIGHT : Config.SMALL_TEXT_HEIGHT;
        return sheet.getSubimage(x * charWidth, 0, charWidth, charHeight);
    }

}
