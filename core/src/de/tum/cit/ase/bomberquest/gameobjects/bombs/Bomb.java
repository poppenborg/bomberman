package de.tum.cit.ase.bomberquest.gameobjects.bombs;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import de.tum.cit.ase.bomberquest.audio.Soundeffect;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Enemy;
import de.tum.cit.ase.bomberquest.gameobjects.walls.DestructibleWall;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.screen.LooseScreen;
import de.tum.cit.ase.bomberquest.texture.Animations;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * This class represents the bomb in the game
 * When a bomb was created it remains 3 seconds on the map
 * Afterward an explosion is created which destroys all destructible walls and kills all enemies and players in its range
 */
public class Bomb extends GameObject {

    /** Time until bomb explodes */
    private static final float IGNITING_DURATION = 3.0f;
    /** Duration of the explosion  */
    private static final float EXPLOSION_DURATION = 0.5f;
    /** Time variable for the bomb */
    private float stateTime;
    /** Current state of the bomb (igniting or exploding) */
    private boolean isExploding;
    /** After the explosion, the bomb should be removed from the game */
    private boolean toBeRemoved;
    /** default blast radius */
    private int blastRadius = 1;
    /** Coordinates of the explosion animation */
    private List<Coordinates> blastCoordinates = new ArrayList<>();

    /** Maximum coordinate of the explosion in the horizontal direction */
    private float maxX = 0;
    /** Minimum coordinate of the explosion in the horizontal direction */
    private float minX = 0;
    /** Maximum coordinate of the explosion in the vertical direction */
    private float maxY = 0;
    /** Minimum coordinate of the explosion in the vertical direction */
    private float minY = 0;

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
     * Retrieve the current appearance of the bomb based on its state.
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
     * Retrieve the animation for an explosion specific to given coordinates.
     *
     * @param coord The coordinate for which to retrieve the appearance.
     * @return The appropriate texture region for a specific part of the explosion.
     */
    public TextureRegion getAppearanceForCoordinate(Coordinates coord) {
        // Center explosion
        if (coord.getX() == getX() && coord.getY() == getY()) {
            return Animations.BOMB_CENTER_EXPLOSION.getKeyFrame(stateTime, false);
        } else if (coord.getY() == getY()) {
            if (coord.getX() == maxX) {
                return Animations.BOMB_BLAST_END_RIGHT.getKeyFrame(stateTime, false);
            } else if (coord.getX() == minX) {
                return Animations.BOMB_BLAST_END_LEFT.getKeyFrame(stateTime, false);
            } else {
                return Animations.BOMB_BLAST_HORIZONTAL.getKeyFrame(stateTime, false);
            }
        } else {
            if (coord.getY() == maxY) {
                return Animations.BOMB_BLAST_END_UP.getKeyFrame(stateTime, false);
            } else if (coord.getY() == minY) {
                return Animations.BOMB_BLAST_END_DOWN.getKeyFrame(stateTime, false);
            } else {
                return Animations.BOMB_BLAST_VERTICAL.getKeyFrame(stateTime, false);
            }
        }
    }


    /**
     * Update the bombs state depending on the passed time.
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
     * Trigger the explosion sequence for the bomb. This method performs the following tasks:
     * 1. Sets the bomb's state to "exploding" and resets the internal timer to track the explosion animation.
     * 2. Calculates the bomb's blast coordinates to determine the explosion radius using the surrounding area.
     * 3. Checks each blast coordinate for destructible walls. If a destructible wall is present at a given coordinate:
     *    a. Schedules the destruction of the wall after the explosion animation is complete.
     *    b. Schedules the removal of the wall from the game map once both the wall destruction and explosion animations are finished.
     * 4. Checks if the are enemies within the blast radius of the bomb
     *    a. Stores the killed enemies in list
     *    b. Removes the enemies from the list from the GameMap
     * 5. Checks if the player is the blast radius of the bomb
     *    If the player is in the blast radius the players status is set to killed which triggers the LooseScreen
     */
    public void explode() {
        isExploding = true;
        stateTime = 0f; // reset timer for explosion
        calculateBlastCoordinates(); //calculate the explosion radius

        // Sound effect
        Soundeffect.EXPLODS_SOUND.play(0.5f);

        //Destroy Destructible walls.

        for (Coordinates coordinates : blastCoordinates) {
            if (gameMap.isDestructibleWallAt(coordinates.getX(), coordinates.getY())) {
                DestructibleWall wallToDestroy = gameMap.getDestructibleWall(coordinates.getX(), coordinates.getY());
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

        //Kill enemies

        List<Enemy> killedEnemies = new ArrayList<>();
        for (Coordinates coordinates : blastCoordinates) {
            for (Enemy enemy : gameMap.getEnemies()) {
                // Test if the enemy is in the blast radius
                float enemyXCoordinate = (float) Math.round(enemy.getX());
                float enemyYCoordinate = (float) Math.round(enemy.getY());
                if (coordinates.getX() == enemyXCoordinate && coordinates.getY() == enemyYCoordinate) {
                    enemy.setKilled(true);
                    killedEnemies.add(enemy);
                    Soundeffect.ENEMYDEATH_SOUN.play(Soundeffect.getVOLUME());

                }
            }
        }

        for (Enemy enemy : killedEnemies) {

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    gameMap.getEnemies().remove(enemy);
                    gameMap.getWorld().destroyBody(enemy.getHitbox());
                }
            }, Animations.BOMB_CENTER_EXPLOSION.getAnimationDuration());
        }

        //Player in Blast radius

        for (Coordinates coordinates : blastCoordinates) {
            if (Math.round(gameMap.getPlayer().getX()) == coordinates.getX() && Math.round(gameMap.getPlayer().getY()) == coordinates.getY() && !gameMap.getPlayer().isKilled()) {
                gameMap.getPlayer().setKilled(true);
                return;
            }
        }
    }

    /**
     * Calculate the blast coordinates in all 4 directions
     */
    private void calculateBlastCoordinates() {
        this.blastCoordinates.clear();

        // Central explosion
        blastCoordinates.add(new Coordinates(getX(), getY()));

        addBlastCoordinatesInDirection(0, 1);   // Up
        addBlastCoordinatesInDirection(0, -1);  // Down
        addBlastCoordinatesInDirection(-1, 0);  // Left
        addBlastCoordinatesInDirection(1, 0);   // Right
    }

    /**
     * Calculate the coordinates of the explosion for 1 specific direction
     *
     * @param dx initial value of the horizontal direction
     * @param dy initial value of the vertical direction
     */
    private void addBlastCoordinatesInDirection(int dx, int dy) {
        for (int i = 1; i <= blastRadius; i++) {
            float newX = getX() + i * dx;
            float newY = getY() + i * dy;

            // stop the animation at a wall if there are walls within the blast radius

            if (gameMap.isIndestructibleWallAt(newX, newY)) {
                setMaxCoordinate(dx, dy, getX() + (i - 1) * dx, getY() + (i - 1) * dy);
                break;
            }

            blastCoordinates.add(new Coordinates(newX, newY));

            if (gameMap.isDestructibleWallAt(newX, newY)) {
                setMaxCoordinate(dx, dy, newX, newY);
                break;
            }

            if (i == blastRadius) {
                setMaxCoordinate(dx, dy, newX, newY);
            }
        }
    }

    /**
     * Set the maximum value of the explosion coordinates in the respective direction
     *
     * @param dx Horizontal direction
     * @param dy Vertical direction
     * @param newX Current x coordinate to be set as maximum
     * @param newY Current y coordinate to be set as maximum
     */
    public void setMaxCoordinate(int dx, int dy, float newX, float newY) {
        if (dx == 0) {
            if (dy > 0) {
                this.maxY = newY;
            }
            if (dy < 0) {
                this.minY = newY;
            }
        }
        if (dy == 0) {
            if (dx > 0) {
                this.maxX = newX;
            }
            if (dx < 0) {
                this.minX = newX;
            }
        }
    }

    //Getters and Setter
    public boolean isToBeRemoved() {
        return toBeRemoved;
    }
    public void setBlastRadius(int blastRadius) {
        this.blastRadius = blastRadius;
    }
    public List<Coordinates> getBlastCoordinates() {
        return blastCoordinates;
    }
}
