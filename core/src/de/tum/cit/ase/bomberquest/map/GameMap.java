package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.gameobjects.*;
import de.tum.cit.ase.bomberquest.gameobjects.Exits.Exit;
import de.tum.cit.ase.bomberquest.gameobjects.bombs.Bomb;
import de.tum.cit.ase.bomberquest.gameobjects.flowers.Flowers;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BlastRadiusPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BombNbrPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.walls.DestructibleWall;
import de.tum.cit.ase.bomberquest.gameobjects.walls.IndestructibleWall;
import de.tum.cit.ase.bomberquest.gameobjects.walls.Wall;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Enemy;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Player;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.GameContactListener;

import java.util.*;

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
    private FileHandle mapFile;
    // Game objects
    private final Player player;
    /** Individual contact listener with a specific behaviour when some of the game objects collide */
    private GameContactListener contactListener;

//    private final Chest chest;

    // all game objects
    private final Flowers[][] flowers;
    private List<IndestructibleWall> indestructibleWalls;
    private List<DestructibleWall> destructibleWalls;
    private List<Enemy> enemies;
    private List<Bomb> bombs;
    private List<BombNbrPowerUp> bombNbrPowerUps;
    private List<BlastRadiusPowerUp> blastRadiusPowerUps;
    private Exit exit;

    /** The time left in the Game. */
    private float timeLeft;


    public GameMap(BomberQuestGame game, FileHandle mapFile) {
        this.game = game;
        this.mapFile = mapFile;
        this.timeLeft = 500;
        this.world = new World(Vector2.Zero, true);
        this.contactListener = new GameContactListener();
        world.setContactListener(contactListener);
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
        this.indestructibleWalls = new ArrayList<>();
        this.destructibleWalls = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.bombs = new ArrayList<>();
        this.bombNbrPowerUps = new ArrayList<>();
        this.blastRadiusPowerUps = new ArrayList<>();
        Map<Coordinates, Integer> prasedMap = mapLoader.prasingMap(mapFile);
        // Iterates over the gameObjects map
        for (Map.Entry<Coordinates, Integer> entry : prasedMap.entrySet()) {
            switch (entry.getValue()) {
                case 0: indestructibleWalls.add(new IndestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY())); break;
                case 1: destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY())); break;
                case 2: entrancePlayerCoordinates.setXaY(entry.getKey().getX(), entry.getKey().getY()); break;
                case 3: enemies.add(new Enemy(this.world,entry.getKey().getX(),entry.getKey().getY())); break;
                case 4:
                    this.exit = new Exit(this.world, entry.getKey().getX(), entry.getKey().getY());
                    destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    break;
                case 5:
                    bombNbrPowerUps.add(new BombNbrPowerUp(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    break;
                case 6:
                    blastRadiusPowerUps.add(new BlastRadiusPowerUp(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY()));
                    break;
            }
        }
        // add the exit underneath a random destructible wall if no exit was specified
        if (prasedMap.values().stream().filter(entry -> entry.equals(4)).count() == 0) {
            List<Coordinates> possibleEntries = prasedMap.entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(entry -> entry.getKey()).toList();
            Coordinates entryCoordinate = possibleEntries.get((int) (Math.random() * possibleEntries.size()));
            this.exit = new Exit(world, entryCoordinate.getX(), entryCoordinate.getY());
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
        // Player
        this.player.tick(frameTime);
        // Enemies
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
     * Checks whether one of the conditions to win the game is fulfilled
     * @return returns ture if one of the conditions to win the game is fulfilled
     */
    public boolean checkWinStatus() {
        boolean allEnemiesDead = getEnemies().isEmpty();
        boolean exitReached = contactListener.isExitReached();
        return allEnemiesDead && exitReached;
    }

    /**
     * Checks whether one of the conditions to lose the game is fulfilled
     * @return returns true if one of the conditions to lose the game is fulfilled
     */
    public boolean checkLoseStatus() {
        boolean timeRunOut = timeLeft <= 0;
        boolean playerEnemyCollision = contactListener.isPlayerEnemyCollision();
        boolean playerIsKilled = player.isKilled();
        return timeRunOut || playerEnemyCollision || playerIsKilled;
    }

    // PowerUps

    /**
     * Update the power-ups on the map
     * If the player touches a power-up its is removed from the map and added into the power-up list of the player
     */
    public void updatepowerUps() {
        for (BombNbrPowerUp bombNbrPowerUp : bombNbrPowerUps) {
            if (bombNbrPowerUp.isMarkedForRemoval() && !bombNbrPowerUp.isTaken() && bombNbrPowerUp.isBombNbrPowerUpAt(contactListener.getPowerUpCoordinates()) && contactListener.getPowerUpCoordinates() != null) {
                player.addBombNbrPowerUp(bombNbrPowerUp);
                bombNbrPowerUp.destroy(world);
            }
        }
        for (BlastRadiusPowerUp blastRadiusPowerUp : blastRadiusPowerUps) {
            if (blastRadiusPowerUp.isMarkedForRemoval() && !blastRadiusPowerUp.isTaken() && blastRadiusPowerUp.isBlastRadiusPowerUpAt(contactListener.getPowerUpCoordinates()) && contactListener.getPowerUpCoordinates() != null) {
                player.addBlastRadiusPowerUp(blastRadiusPowerUp);
                blastRadiusPowerUp.destroy(world);
            }
        }
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

    /**
     * Removes the specified destructible wall from the list of destructible walls in the game map.
     *
     * @param wall the DestructibleWall to be removed from the game map
     */
    public void removeDestructibleWall(DestructibleWall wall) {
        destructibleWalls.remove(wall);
    }


    /**
     * Collect all drawable elements in one list
     * Determins which drawables will be drawn over the other drawables
     * @return List of all drawables
     */
    public List<Drawable> allDrawablesOrdered() {
        List<Drawable> allDrawables = new ArrayList<>();
        for (Flowers flowers : getFlowers()) {
            allDrawables.add(flowers);
        }
        for (BombNbrPowerUp bombNbrPowerUp : bombNbrPowerUps) {
            allDrawables.add(bombNbrPowerUp);
        }
        for (BlastRadiusPowerUp blastRadiusPowerUp : blastRadiusPowerUps) {
            allDrawables.add(blastRadiusPowerUp);
        }
        allDrawables.add(this.exit);
        for (IndestructibleWall indestructibleWall : getIndestructibleWalls()) {
            allDrawables.add(indestructibleWall);
        }
        for (DestructibleWall destructibleWall : getDestructibleWalls()) {
            allDrawables.add(destructibleWall);
        }
        for (Bomb bomb : getBombs()) {
            allDrawables.add(bomb);
        }
        for (Enemy enemy : getEnemies()) {
            allDrawables.add(enemy);
        }
//        allDrawables.add(getChest());
        allDrawables.add(player);
        return allDrawables;
    }

    /**
     * Tests whether the player collides with an enemy
     * Note: method is based on calculating distances, different shapes need different calculations
     * @return true if the player collides with the enemy
     */
    public boolean collisionPlayerEnemy() {
        for (Enemy enemy : enemies) {
            // calculate the euclidean distance
            double xDistance = player.getX() - enemy.getX();
            double yDistance = player.getY() - enemy.getY();
            double squaredDistance = Math.pow(xDistance, 2.0f) + Math.pow(yDistance, 2f);
            double distance = Math.sqrt(squaredDistance);
            // objects overlap if the distance is smaller than the 2 radii of the circles
            if (distance < (player.getRadius() + enemy.getRadius())) {
                return true;
            }
        }
        return false;
    }
    // alternative method if one of the colliding objects is a rectangle
    public boolean circleRectangle() {
        for (Enemy enemy : enemies) {
            // calculate rectangle edges
            float xLeft = enemy.getX() - enemy.getRectangleWidth() / 2f;
            float xRight = enemy.getX() + enemy.getRectangleWidth() / 2f;
            float yLeft = enemy.getY() - enemy.getRectangleHeight() / 2f;
            float yRight = enemy.getY() + enemy.getRectangleHeight() / 2f;
            // calculate the closest point of the rectangle to circle
            float xClosest = Math.max(xLeft, Math.min(xRight, player.getX()));
            float yClosest = Math.max(yLeft, Math.min(yRight, player.getY()));
            // calculate the euclidean distance between the closest point of the rectangle and the circle center
            double xDistance = player.getX() - xClosest;
            double yDistance = player.getY() - yClosest;
            double distance = Math.sqrt(Math.pow(xDistance, 2f) + Math.pow(yDistance, 2f));
            // objects overlap if the distance is smaller than the radius of the circle
            if (distance < player.getRadius()) {
                return true;
            }
        }
        return false;
    }


    /** Cleans up resources when the game is disposed. */
    public void dispose() {
        this.timeLeft = 0;
        setMapFile(null);
    }
    // getter and setter for the attributes
    public Player getPlayer() {
        return player;
    }
    /** Returns the chest on the map. */
//    public Chest getChest() {
//        return chest;
//    }
    public List<Flowers> getFlowers() {
        return Arrays.stream(flowers).flatMap(Arrays::stream).toList();
    }
    public List<IndestructibleWall> getIndestructibleWalls() {
        return indestructibleWalls;
    }
    public List<DestructibleWall> getDestructibleWalls() {
        return destructibleWalls;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<Bomb> getBombs() {
        return bombs;
    }
    public List<BombNbrPowerUp> getBombNbrPowerUps() {
        return bombNbrPowerUps;
    }
    public List<BlastRadiusPowerUp> getBlastRadiusPowerUps() {
        return blastRadiusPowerUps;
    }
    public Exit getExit() {
        return exit;
    }
    public World getWorld() {
        return world;
    }
    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
    }
    public float getTimeLeft() {
        return timeLeft;
    }
    public void setMapFile(FileHandle mapFile) {
        this.mapFile = mapFile;
    }
    public FileHandle getMapFile() {
        return mapFile;
    }
    public GameContactListener getContactListener() {
        return contactListener;
    }
}
