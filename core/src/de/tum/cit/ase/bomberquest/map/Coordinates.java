package de.tum.cit.ase.bomberquest.map;

/**
 * Collects the x- and y-values of a coorinate into 1 object to avoid confusing length an hight
 */
public class Coordinates {
    // Extends horizontally
    private int x;
    // Extends vertically
    private int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
