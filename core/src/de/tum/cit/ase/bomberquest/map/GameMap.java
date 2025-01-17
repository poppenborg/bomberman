package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.gameobjects.*;
import de.tum.cit.ase.bomberquest.mobs.Enemy;
import de.tum.cit.ase.bomberquest.mobs.Player;
import de.tum.cit.ase.bomberquest.screen.MenuScreen;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the game map.
 * Holds all the objects and entities in the game.
 */
public class GameMap {
    
    // A static block is executed once when the class is referenced for the first time.
    static {
        // Initialize the Box2D physics engine.
        com.badlogic.gdx.physics.box2d.Box2D.init();
    }
    
    // Box2D physics simulation parameters (you can experiment with these if you want, but they work well as they are)
    /**
     * The time step for the physics simulation.
     * This is the amount of time that the physics simulation advances by in each frame.
     * It is set to 1/refreshRate, where refreshRate is the refresh rate of the monitor, e.g., 1/60 for 60 Hz.
     */
    private static final float TIME_STEP = 1f / Gdx.graphics.getDisplayMode().refreshRate;
    /** The number of velocity iterations for the physics simulation. */
    private static final int VELOCITY_ITERATIONS = 6;
    /** The number of position iterations for the physics simulation. */
    private static final int POSITION_ITERATIONS = 2;
    /**
     * The accumulated time since the last physics step.
     * We use this to keep the physics simulation at a constant rate even if the frame rate is variable.
     */
    private float physicsTime = 0;
    
    /** The game, in case the map needs to access it. */
    private final BomberQuestGame game;
    /** The Box2D world for physics simulation. */
    private final World world;
    /** Map used in the game */
    private final FileHandle mapFile;
    // Game objects
    private final Player player;
    
//    private final Chest chest;
    
    private final Flowers[][] flowers;

    private List<IndestructibleWall> indestructibleWalls;

    private List<DestructibleWall> destructibleWalls;

    private List<Enemy> enemies;

    private List<Bomb> bombs;

    //TODO: replce placeholder for Entrance + Enemy + Exit + power-up until respective class was created
    private List<Placeholder> placeholders;

    /** The time left in the Game. */
    private float timeLeft;


    public GameMap(BomberQuestGame game, FileHandle mapFile) {
        this.game = game;
        this.mapFile = mapFile;
        timeLeft = 300;
        this.world = new World(Vector2.Zero, true);
        // initialise player position
        Coordinates entrancePlayerCoordinates = new Coordinates(0,0);
        // Create a chest in the middle of the map
//        this.chest = new Chest(world, 10, 6);
        MapLoader mapLoader = new MapLoader();
        // Create flowers/ ground for all tials
        float x = mapLoader.prasingMap(mapFile).keySet().stream().map(Coordinates::getX).max(Float::compare).orElse(0f) + 1f;
        // increases x by 1 because otherwise the coordinate 0 isn´t represented
        float y = mapLoader.prasingMap(mapFile).keySet().stream().map(Coordinates::getY).max(Float::compare).orElse(0f) + 1f;
        // increases y by 1 because otherwise the coordinate 0 isn´t represented
        this.flowers = new Flowers[(int) x][(int) y];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
        // initialize Lists of various objects
        indestructibleWalls = new ArrayList<>();
        destructibleWalls = new ArrayList<>();
        placeholders = new ArrayList<>();
        enemies = new ArrayList<>();
        bombs = new ArrayList<>();
        //TODO: replace placeholders
        Map<Coordinates, Integer> prasedMap = mapLoader.prasingMap(mapFile);
        // Iterates over the gameObjects map
        for (Map.Entry<Coordinates, Integer> entry : prasedMap.entrySet()) {
            switch (entry.getValue()) {
                case 0: indestructibleWalls.add(new IndestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY())); break;
                case 1: destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY())); break;
                case 2:
                    entrancePlayerCoordinates.setX(entry.getKey().getX());
                    entrancePlayerCoordinates.setY(entry.getKey().getY());
                    break;
                case 3: enemies.add(new Enemy(this.world,entry.getKey().getX(),entry.getKey().getY())); break;
                // needs to replce placeholder for Exit
                case 4:
                    placeholders.add(new Placeholder(entry.getKey().getX(), entry.getKey().getY()));
                    destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    break;
                // needs to replce placeholder for Concurrent bomb power-up
                case 5:
                    placeholders.add(new Placeholder(entry.getKey().getX(), entry.getKey().getY()));
                    destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    break;
                // needs to replce placeholder for Blast radius power-up
                case 6:
                    placeholders.add(new Placeholder(entry.getKey().getX(), entry.getKey().getY()));
                    destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    break;
            }
        }
        // add the exit underneath a random destructible wall if no exit was specified
        if (prasedMap.values().stream().filter(entry -> entry.equals(4)).count() == 0) {
            List<Coordinates> possibleEntries = prasedMap.entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(entry -> entry.getKey()).toList();
            Coordinates entryCoordinate = possibleEntries.get((int) (Math.random() * possibleEntries.size()));
            // TODO: replace placeholder with exit object
            placeholders.add(new Placeholder(entryCoordinate.getX(), entryCoordinate.getY()));
        }
        // Create a player with initial position at entrance (or 0, 0 if no entrance was specified)
        this.player = new Player(this.world, entrancePlayerCoordinates.getX(), entrancePlayerCoordinates.getY(), this);
    }
    
    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {

        //Player

        this.player.tick(frameTime);
        for (Enemy enemy : enemies) {
            enemy.tick(frameTime);
        }

        doPhysicsStep(frameTime);
    }
    
    /**
     * Performs as many physics steps as necessary to catch up to the given frame time.
     * This will update the Box2D world by the given time step.
     * @param frameTime Time since last frame in seconds
     */
    private void doPhysicsStep(float frameTime) {
        this.physicsTime += frameTime;
        while (this.physicsTime >= TIME_STEP) {
            this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
            this.physicsTime -= TIME_STEP;
        }
    }

    /**
     * Chescks wether one of the conditions to win the game is fulfilled
     * @return returns ture if one of the conditions to win the game is fulfilled
     */
    public boolean checkWinStatus() {
        boolean allEnemiesDefeated = getEnemies().isEmpty();
        return allEnemiesDefeated;
    }
    /**
     * Chescks whether one of the conditions to lose the game is fulfilled
     * @return returns true if one of the conditions to lose the game is fulfilled
     */
    public boolean checkLoseStatus() {
        boolean timeRunOut = timeLeft <= 0;
        return timeRunOut;
    }

    /**
     * Tests wether the player collides with an enemy
     * @return true if the player collides with the enemy
     */
    public boolean collisionPlayerEnemy() {
//        for (Enemy enemy : enemies) {
//            double xDistance = player.getX() - enemy.getX();
//            double yDistance = player.getY() - enemy.getY();
//            double squaredDistance = Math.pow(xDistance, 2.) + Math.pow(yDistance, 2);
//            double distance = Math.sqrt(squaredDistance);
//            if (distance < (player.getHitbox().)) {
//            }
//        }
        return false;
    }

    //Bombs

    /**
     * Update all the bombs on the map and remove them if they've exploded.
     */
    public void updateBombs(float deltaTime) {
        List<Bomb> bombsToRemove = new ArrayList<>();

        for (Bomb bomb : bombs) {
            bomb.update(deltaTime);

            if (bomb.isToBeRemoved()) {
                bombsToRemove.add(bomb);
            }
        }

        // Delete exploded Bombs
        bombs.removeAll(bombsToRemove);
    }

    /**
     * Checks if there is an indestructible wall at the specified coordinates in the parameter. Is used to properly animate the blast of the bomb.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if an indestructible wall exists at the given coordinates, false otherwise
     */


    public boolean isIndestructibleWallAt(float x, float y) {
        for (Wall wall : indestructibleWalls) {
            if (wall.getX() == x && wall.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is an destructible wall at the specified coordinates in the parameter. Is used to properly animate the blast of the bomb.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if an destructible wall exists at the given coordinates, false otherwise
     */

    public boolean isDestructibleWallAt(float x, float y) {
        for (Wall wall : destructibleWalls) {
            if (wall.getX() == x && wall.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a destructible wall located at the specified coordinates.
     * Searches through the list of destructible walls and returns the wall
     * matching the provided coordinates, if it exists.
     *
     * @param x the x-coordinate of the desired destructible wall
     * @param y the y-coordinate of the desired destructible wall
     * @return the DestructibleWall at the specified coordinates, or null if no such wall exists
     */

    public DestructibleWall getDestructibleWall(float x, float y) {
        for (DestructibleWall wall : destructibleWalls) {
            if (wall.getX() == x && wall.getY() == y) {
                return wall;
            }
        }
        return null;
    }


    /** Cleans up resources when the game is disposed. */
    public void dispose() {
        world.dispose();
    }

    /** Returns the player on the map. */
    public Player getPlayer() {
        return player;
    }
    
    /** Returns the chest on the map. */
//    public Chest getChest() {
//        return chest;
//    }
    
    /** Returns the flowers on the map. */
    public List<Flowers> getFlowers() {
        return Arrays.stream(flowers).flatMap(Arrays::stream).toList();
    }

    /** Returns the indestructibleWalls on the map. */
    public List<IndestructibleWall> getIndestructibleWalls() {
        return indestructibleWalls;
    }

    /** Returns the destructibleWalls on the map. */
    public List<DestructibleWall> getDestructibleWalls() {
        return destructibleWalls;
    }

    /** Returns the Mobs on the map. */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /** Returns the Bombs on the map. */
    public List<Bomb> getBombs() {
        return bombs;
    }

    /** Returns the placeholders on the map. */
    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

    /** Returns the world of the map. */
    public World getWorld() {
        return world;
    }

    // getter and setter for the timer
    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
    }
    public float getTimeLeft() {
        return timeLeft;
    }
}
