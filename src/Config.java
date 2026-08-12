
public class Config {

    public static final int CELL_SIZE = 32;
    public static final int SMALL_TEXT_HEIGHT = 16;
    public static final int SMALL_TEXT_WIDTH = 12;
    public static final int BIG_TEXT_HEIGHT = 32;
    public static final int BIG_TEXT_WIDTH = 24;
    public static final int HEADER_HEIGHT = 32;
    public static final int FOOTER_HEIGHT = 48;
    public static final int MAX_LEVEL = 9;

    private static final int[] ENEMY_COUNTS = new int[] { 52, 46, 40, 36, 30, 24, 18, 13, 1 };
    private static final int[] ENEMY_EXP = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 0 };
    private static final int[] EXP_THRESHOLDS = new int[] { 10, 90, 202, 400, 1072, 1840, 2992, 4656, -1 };

    static {
        assert ENEMY_COUNTS.length == MAX_LEVEL;
        assert ENEMY_EXP.length == MAX_LEVEL;
        assert EXP_THRESHOLDS.length == MAX_LEVEL;
    }

    public static int getEnemyAmount(int level) {
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid enemy level");
        }
        return ENEMY_COUNTS[level - 1];
    }

    public static int getEnemyExp(int level) {
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid enemy level");
        }
        return ENEMY_EXP[level - 1];
    }

    public static int getExpThreshold(int level) {
        if (level < 1 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid level");
        }
        return EXP_THRESHOLDS[level - 1];
    }

}
