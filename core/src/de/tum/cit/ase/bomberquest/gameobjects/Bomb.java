package de.tum.cit.ase.bomberquest.gameobjects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import de.tum.cit.ase.bomberquest.map.Coordinates;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

import java.util.ArrayList;
import java.util.List;

public class Bomb extends Coordinates implements Drawable {

    private static final float IGNITING_DURATION = 3.0f;
    private static final float EXPLOSION_DURATION = 0.5f;
    private float stateTime; //time variable for the bomb
    private boolean isExploding; // current state of the bomb (igniting or exploding)
    private boolean toBeRemoved; // After the explosion, the bomb should be removed from the game

    private int blastRadius = 1; // default blast radius
    private List<Coordinates> blastCoordinates = new ArrayList<>(); // Coordinates of the explosion animation
    private GameMap gameMap;


    /**
     * Creates a new Bomb object at the specified coordinates.
     *
     * @param x The x-coordinate where the bomb is placed.
     * @param y The y-coordinate where the bomb is placed.
     */


    public Bomb(float x, float y, GameMap gameMap) {
        super(x, y);
        this.gameMap = gameMap;
        this.stateTime = 0f;
        this.isExploding = false;
        this.toBeRemoved = false;
    }

    //Methods

    /**
     * Retrieves the current appearance of the bomb based on its state.
     * If the bomb is in the "exploding" state, it returns the appropriate frame of the explosion
     * animation. Otherwise, it returns the appropriate frame of the ignition animation.
     *
     * @return The current texture region representing the bomb's appearance.
     */

    public TextureRegion getCurrentAppearance () {
        if (isExploding) {
            return Animations.BOMB_CENTER_EXPLOSION.getKeyFrame(stateTime, false); // Render center explosion
        }
        return Animations.BOMB_IGNITING.getKeyFrame(stateTime, true); // Looping the Animation of the ignition
    }

    /**
     * Retrieves the animation for an explosion specific to given coordinates.
     *
     * @param coord The coordinate for which to retrieve the appearance.
     * @return The appropriate texture region for a specific part of the explosion.
     */
    public TextureRegion getAppearanceForCoordinate(Coordinates coord) {
        // Center explosion
        if (coord.equals(new Coordinates(getX(), getY()))) {
            return Animations.BOMB_CENTER_EXPLOSION.getKeyFrame(stateTime, false);
        }

        // Vertical explosion
        if (coord.getX() == getX()) {
            if (coord.getY() > getY() && coord.getY() < getY() + blastRadius) { // Up
                return Animations.BOMB_BLAST_VERTICAL.getKeyFrame(stateTime, false);
            }
            if (coord.getY() < getY() && coord.getY() > getY() - blastRadius) { // Down
                return Animations.BOMB_BLAST_VERTICAL.getKeyFrame(stateTime, false);
            }
        }

        // Horizontal explosion
        if (coord.getY() == getY()) {
            if (coord.getX() > getX() && coord.getX() < getX() + blastRadius) { // Right
                return Animations.BOMB_BLAST_HORIZONTAL.getKeyFrame(stateTime, false);
            }
            if (coord.getX() < getX() && coord.getX() > getX() - blastRadius) { // Left
                return Animations.BOMB_BLAST_HORIZONTAL.getKeyFrame(stateTime, false);
            }
        }

        // Endpoints of vertical explosion
        if (coord.equals(new Coordinates(getX(), getY() + blastRadius))) { // End-Up
            return Animations.BOMB_BLAST_END_UP.getKeyFrame(stateTime, false);
        }
        if (coord.equals(new Coordinates(getX(), getY() - blastRadius))) { // End-Down
            return Animations.BOMB_BLAST_END_DOWN.getKeyFrame(stateTime, false);
        }

        // Endpoints of horizontal explosion
        if (coord.equals(new Coordinates(getX() + blastRadius, getY()))) { // End-Right
            return Animations.BOMB_BLAST_END_RIGHT.getKeyFrame(stateTime, false);
        }
        if (coord.equals(new Coordinates(getX() - blastRadius, getY()))) { // End-Left
            return Animations.BOMB_BLAST_END_LEFT.getKeyFrame(stateTime, false);
        }

        // Fallback (just in case)
        return Animations.BOMB_CENTER_EXPLOSION.getKeyFrame(stateTime, false);
    }

    /**
     * Updates the bombs state depending o the passed time.
     */

    public void update(float deltaTime) {
        stateTime += deltaTime;

        if (isExploding) {
            if (stateTime > EXPLOSION_DURATION) {
                toBeRemoved = true;
            }
        } else {
            if (stateTime > IGNITING_DURATION) {
                explode();
            }
        }
    }

    /**
     * Triggers the explosion sequence for the bomb. This method performs the following tasks:
     * 1. Sets the bomb's state to "exploding" and resets the internal timer to track the explosion animation.
     * 2. Calculates the bomb's blast coordinates to determine the explosion radius using the surrounding area.
     * 3. Checks each blast coordinate for destructible walls. If a destructible wall is present at a given coordinate:
     *    a. Schedules the destruction of the wall after the explosion animation is complete.
     *    b. Schedules the removal of the wall from the game map once both the wall destruction and explosion animations are finished.
     */

    public void explode() {
        isExploding = true;
        stateTime = 0f; // reset timer for explosion
        calculateBlastCoordinates(); //calculate the explosion radius

        for (Coordinates coord : blastCoordinates) {
            if (gameMap.isDestructibleWallAt(coord.getX(), coord.getY())) {
                DestructibleWall wallToDestroy = gameMap.getDestructibleWall(coord.getX(), coord.getY());
                if (wallToDestroy != null) {

                    // detroy the  wall, after the explosion animation is finished

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            wallToDestroy.destroy(gameMap.getWorld()); // call destroy () on wall
                        }
                    }, Animations.BOMB_CENTER_EXPLOSION.getAnimationDuration());

                    // remove wall, after the wall and explosion animation is finished
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            gameMap.removeDestructibleWall(wallToDestroy); //remove wall from the map
                        }
                    }, Animations.DESTRUCTIBLE_WALL_DESTROY.getAnimationDuration() + Animations.BOMB_CENTER_EXPLOSION.getAnimationDuration());
                }
            }

        }
    }

    private void calculateBlastCoordinates() {
        blastCoordinates.clear();

        // Central explsion
        blastCoordinates.add(new Coordinates(getX(), getY()));

        addBlastCoordinatesInDirection(0, 1);   // Up
        addBlastCoordinatesInDirection(0, -1);  // Down
        addBlastCoordinatesInDirection(-1, 0);  // Left
        addBlastCoordinatesInDirection(1, 0);   // Right
    }

    private void addBlastCoordinatesInDirection(int dx, int dy) {
        for (int i = 0; i <= blastRadius; i++) {
            float newX = getX() + i * dx;
            float newY = getY() + i * dy;

            // stop the animation at a wall if there are walls within the blast radius


            if (gameMap.isIndestructibleWallAt(newX, newY)) {
                break;
            }

            blastCoordinates.add(new Coordinates(newX, newY));

            if (gameMap.isDestructibleWallAt(newX, newY)) {
                break;
            }
        }
    }




    //Getters and Setter

    public boolean isToBeRemoved() {
        return toBeRemoved;
    }

    public float getStateTime() {
        return stateTime;
    }

    public boolean isExploding() {
        return isExploding;
    }


    public int getBlastRadius() {
        return blastRadius;
    }

    public void setBlastRadius(int blastRadius) {
        this.blastRadius = blastRadius;
    }

    public List<Coordinates> getBlastCoordinates() {
        return blastCoordinates;
    }
}
