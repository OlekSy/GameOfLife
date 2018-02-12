package sample;

import javafx.geometry.Point2D;

public class Cell {

    private Point2D coordinates;
    private boolean isAlive;

    Cell(int xCoordinate, int yCoordiinate, boolean isAlive){
        coordinates = new Point2D(xCoordinate, yCoordiinate);
        this.isAlive = isAlive;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setCoordinates(int xCoordinate, int yCoordinate) {
        this.coordinates = new Point2D(xCoordinate, yCoordinate);
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
