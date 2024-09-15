import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private GameBoard gameboard;
    private List<String> previousShots;

    public Player(GameBoard gameboard) {
        this.gameboard = gameboard;
        this.previousShots = new ArrayList<>();
    }

    public boolean takeShot(int x, int y, boolean attackOnPlayer) {
        String shot = x + "," + y;
        if (previousShots.contains(shot)) {
            return false;
        }
        previousShots.add(shot);
        return gameboard.receiveAttack(x, y, attackOnPlayer);
    }

    public int[] getRandomShot() {
        Random random = new Random();
        int x, y;
        String shot;
        do {
            x = random.nextInt(gameboard.getGrid(false).length);
            y = random.nextInt(gameboard.getGrid(false)[0].length);
            shot = x + "," + y;
        } while (previousShots.contains(shot));
        return new int[]{x, y};
    }

    public GameBoard getGameboard() {
        return gameboard;
    }

    public List<String> getPreviousShots() {
        return previousShots;
    }
}