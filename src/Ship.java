import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Ship {
    private int length;
    private int hitPoints;
    private List<Integer> hitPositions;
    private boolean sunk;
    private List<Coordinate> positionPlaced;
    private int boardSize = 10;
    private boolean sunkNotified = false;

    public Ship(int length) {
        this.length = length;
        this.hitPositions = new ArrayList<>();
        this.sunk = false;
        this.hitPoints = 0;
        this.positionPlaced = new ArrayList<>();
    }

    public void hit(int x, int y, int size) {
        int position = (x * boardSize) + y;
        for (Coordinate coord : positionPlaced) {
            if (coord.getX() == x && coord.getY() == y && !hitPositions.contains(position)) {
                hitPositions.add(position);
                hitPoints++;
                checkIfSunk();
                return;
            }
        }
    }

    private void checkIfSunk() {
        if (hitPoints == length) {
            sunk = true;
        }
    }

    public boolean isSunk() {
        return sunk;
    }

    public int getLength() {
        return length;
    }

    public List<Integer> getHitPositions() {
        return new ArrayList<>(hitPositions);
    }

    public int getHitPoints() { return hitPoints; }

    public void addPositionPlaced(int x, int y) { positionPlaced.add(new Coordinate(x, y)); }

    public List<Coordinate> getPositionPlaced() { return positionPlaced; }

    public boolean containsCoord(int x,int y){
        for(Coordinate coord : getPositionPlaced()) {
            if(coord.getX() == x && coord.getY() == y){
                return true;
            }
        }
        return false;
    }

    public boolean isSunkNotified() {
        return sunkNotified;
    }

    public void setSunkNotified(boolean sunkNotified) {
        this.sunkNotified = sunkNotified;
    }
}