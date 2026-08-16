
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class GameModel {

    public final int scale;
    public final int width;
    public final int height;
    private boolean countdownMode;

    private final int[] enemies;
    private final int[] marks;
    private final boolean[] open;

    private final boolean[] enemyRevealed;
    private final int[] enemyCounts;

    private int level;
    private int hp;
    private int exp;

    private Instant start;
    private Instant end;

    public GameModel(int scale, int width, int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;

        enemies = new int[width * height];
        marks = new int[width * height];
        open = new boolean[width * height];

        enemyCounts = new int[Config.MAX_LEVEL];
        enemyRevealed = new boolean[Config.MAX_LEVEL];

        resetBoard();
    }

    public final void resetBoard() {
        int index = 0;
        for (int i = 1; i <= Config.MAX_LEVEL; i++) {
            int count = Config.getEnemyAmount(i);
            enemyCounts[i - 1] = count;
            enemyRevealed[i - 1] = false;
            for (int j = 0; j < count; j++) {
                enemies[index++] = i;
            }
        }

        while (index < enemies.length) {
            enemies[index++] = 0;
        }

        Random rand = new Random();
        for (int i = enemies.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = enemies[i];
            enemies[i] = enemies[j];
            enemies[j] = temp;
        }

        int size = width * height;
        for (int i = 0; i < size; i++) {
            marks[i] = 0;
            open[i] = false;
        }

        level = 1;
        hp = 30;
        exp = 0;
        start = null;
        end = null;
    }

    public boolean coordsValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void assertCoordsValid(int x, int y) {
        if (!coordsValid(x, y)) {
            throw new IllegalArgumentException("Invalid coords");
        }
    }

    public int getEnemy(int x, int y) {
        assertCoordsValid(x, y);
        return enemies[x + y * width];
    }

    public int getMark(int x, int y) {
        assertCoordsValid(x, y);
        return marks[x + y * width];
    }

    public void setMark(int n, int x, int y) {
        if (end != null)
            return;

        assertCoordsValid(x, y);
        marks[x + y * width] = n;
    }

    public boolean getOpen(int x, int y) {
        assertCoordsValid(x, y);
        return open[x + y * width];
    }

    public void open(int x, int y) {
        if (end != null)
            return;

        assertCoordsValid(x, y);

        int index = x + y * width;
        if (open[index] || marks[index] > level)
            return;

        if (start == null)
            start = Instant.now();

        open[index] = true;

        int enemy = enemies[index];
        if (enemy == 0)
            return;

        int damage = enemy * Math.ceilDiv(enemy - level, level);
        hp = Math.max(hp - damage, 0);

        if (hp == 0) {
            handleLose();
            return;
        }

        enemyCounts[enemy - 1]--;
        enemyRevealed[enemy - 1] = true;

        exp += Config.getEnemyExp(enemy);
        int threshold = nextExpThreshold();
        while (threshold >= 0 && exp >= threshold) {
            level++;
            threshold = nextExpThreshold();
        }

        boolean allOpen = true;
        for (int i = 0; i < open.length; i++)
            allOpen &= open[i];

        if (allOpen)
            end = Instant.now();
    }

    private void handleLose() {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] > 0) {
                open[i] = true;
            }
        }

        for (int i = 0; i < Config.MAX_LEVEL; i++) {
            enemyRevealed[i] = true;
        }

        end = Instant.now();
    }

    public int getEnemyCount(int level) {
        if (level < 1 || level > Config.MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid enemy level");
        }
        return enemyCounts[level - 1];
    }

    public boolean getEnemyRevealed(int level) {
        if (level < 1 || level > Config.MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid enemy level");
        }
        return enemyRevealed[level - 1];
    }

    public int getAdjacentCount(int x, int y, boolean remaining) {
        assertCoordsValid(x, y);
        int count = 0;
        for (int i = 0; i < 9; i++) {
            int dx = i % 3 - 1;
            int dy = i / 3 - 1;

            int offsetX = x + dx;
            int offsetY = y + dy;
            if (coordsValid(offsetX, offsetY)) {
                int index = offsetX + width * offsetY;
                count += enemies[index];

                if (remaining) {
                    count -= open[index] ? enemies[index] : marks[index];
                }
            }
        }
        return count;
    }

    public int getAdjacentCount(int x, int y) {
        return getAdjacentCount(x, y, false);
    }

    public int getLevel() {
        return level;
    }

    public int getHp() {
        return hp;
    }

    public int getExp() {
        return exp;
    }

    public boolean isCountdownMode() {
        return countdownMode;
    }

    public void setCountdownMode(boolean countdownMode) {
        this.countdownMode = countdownMode;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public long gameTime() {
        if (start == null)
            return 0;
        Instant endOrNow = end == null ? Instant.now() : end;
        Duration duration = Duration.between(start, endOrNow);
        return duration.toSeconds();
    }

    public int nextExpThreshold() {
        return Config.getExpThreshold(level);
    }

}
