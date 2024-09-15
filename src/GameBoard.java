import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private int size;
    private char[][] gridPlayer;
    private char[][] gridComputer;
    private List<Ship> shipsPlayer;
    private List<Ship> shipsComputer;
    private List<int[]> missedShots;

    public GameBoard(int size) {
        this.size = size;
        this.gridPlayer = new char[size][size];
        this.gridComputer = new char[size][size];
        this.shipsPlayer = new ArrayList<>();
        this.shipsComputer = new ArrayList<>();
        this.missedShots = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridPlayer[i][j] = ' ';
                gridComputer[i][j] = ' ';
            }
        }
    }

    public boolean placeShip(Ship ship, int startX, int startY, boolean horizontal, boolean placeShipOnPlayer) {
        char[][] grid;
        List<Ship> ships;
        if (placeShipOnPlayer) {
            grid = gridPlayer;
            ships = shipsPlayer;
        } else {
            grid = gridComputer;
            ships = shipsComputer;
        }
        if (horizontal) {
            if (startX + ship.getLength() > size) return false;
            for (int i = 0; i < ship.getLength(); i++) {
                if (grid[startX + i][startY] != ' ') return false;
            }
            for (int i = 0; i < ship.getLength(); i++) {
                grid[startX + i][startY] = 'S';
            }
        } else {
            if (startY + ship.getLength() > size) return false;
            for (int i = 0; i < ship.getLength(); i++) {
                if (grid[startX][startY + i] != ' ') return false;
            }
            for (int i = 0; i < ship.getLength(); i++) {
                grid[startX][startY + i] = 'S';
            }
        }
        if (placeShipOnPlayer) {
            gridPlayer = grid;
        } else {
            gridComputer = grid;
        }
        ships.add(ship);
        return true;
    }


    public boolean receiveAttack(int x, int y, boolean attackOnPlayer) {
        boolean hit = false;
        char[][] grid;
        List<Ship> ships;
        if (attackOnPlayer) {
            grid = gridPlayer;
            ships = shipsPlayer;
        } else {
            grid = gridComputer;
            ships = shipsComputer;
        }
        if (grid[x][y] == 'S') {
            grid[x][y] = 'X';
            for (Ship ship : ships) {
                int pos = (x * size) + y;
                if (ship.containsCoord(x,y)) {
                    ship.hit(x, y, size);
                    hit = true;
                    break;
                }
            }
        } else {
            grid[x][y] = 'O';
            missedShots.add(new int[]{x, y});
        }
        if (attackOnPlayer) {
            gridPlayer = grid;
        } else {
            gridComputer = grid;
        }
        return hit;
    }

    public List<int[]> getMissedShots() {
        return missedShots;
    }

    public char[][] getGrid(boolean playerGrid) {
        return playerGrid ? gridPlayer : gridComputer;
    }

    public List<Ship> getShipsPlayer() {
        return shipsPlayer;
    }

    public List<Ship> getShipsComputer() { return shipsComputer; }
}