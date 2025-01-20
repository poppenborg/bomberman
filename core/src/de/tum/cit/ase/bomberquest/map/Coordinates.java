package de.tum.cit.ase.bomberquest.map;

/**
 * Collects the x- and y-values of a coorinate into 1 object to avoid confusing length and hight
 */
public class Coordinates {
    // Extends horizontally
    private float x;
    // Extends vertically
    private float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // getters and setters
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }
    // Set both coordinates simultaneously
    public void setXaY(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
