package de.tum.cit.ase.bomberquest.gameobjects;

/**
 * Collects the x- and y-values of a coordinate into 1 object to avoid confusing length and height.
 * This class is implemented in all GameObjects
 */
public class Coordinates {
    /** Horizontal extension */
    private float x;
    /** Vertical extension */
    private float y;

    /**
     * Create a new 2-dimensional coordinate
     *
     * @param x Horizontal value
     * @param y Vertical value
     */
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
    /** Set both values - x and y - simultaneously */
    public void setXaY(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
