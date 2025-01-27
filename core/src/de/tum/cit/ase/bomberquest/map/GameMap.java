package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.gameobjects.*;
import de.tum.cit.ase.bomberquest.gameobjects.Exits.Exit;
import de.tum.cit.ase.bomberquest.gameobjects.bombs.Bomb;
import de.tum.cit.ase.bomberquest.gameobjects.flowers.Flowers;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BlastRadiusPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BombNbrPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.MovementSpeedPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.walls.DestructibleWall;
import de.tum.cit.ase.bomberquest.gameobjects.walls.IndestructibleWall;
import de.tum.cit.ase.bomberquest.gameobjects.walls.Wall;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Enemy;
import de.tum.cit.ase.bomberquest.gameobjects.mobs.Player;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

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
    /** Individual contact listener with a specific behaviour when some of the game objects collide */
    private GameContactListener contactListener;
    /** Player used on the map */
    private final Player player;
    /** Flowers on the map */
    private final Flowers[][] flowers;
    /** Indestructible walls on the map */
    private List<IndestructibleWall> indestructibleWalls = new ArrayList<>();
    /** Destructible Walls on the map */
    private List<DestructibleWall> destructibleWalls = new ArrayList<>();
    /** Enemies on the map */
    private List<Enemy> enemies = new ArrayList<>();
    /** Bombs on the map */
    private List<Bomb> bombs = new ArrayList<>();
    /** Bomb number power-ups on the map */
    private List<BombNbrPowerUp> bombNbrPowerUps = new ArrayList<>();
    /** Blast radius power-ups on the map */
    private List<BlastRadiusPowerUp> blastRadiusPowerUps = new ArrayList<>();
    /** Movement speed power-ups on the map */
    private List<MovementSpeedPowerUp> movementSpeedPowerUps = new ArrayList<>();
    /** Exit used on the map */
    private Exit exit;
    /** The time left in the Game. */
    private float timeLeft;
    /** Value to keep tract of the death animation */
    private float playerDeathAnimationTime = 0f;

    /**
     * Constructor of the GameMap, here are all GameObjects initialised
     * All GameObjects of the chosen map file are assigned to their respective List or attribute,
     * considering the individual coordinates.
     * Special cases: for powerUps and the exit also a destructible wall is created on the same coordinate.
     * if no exit was specified the coordinates of a random destructible wall are chosen.
     *
     * @param game Bomber Quest game
     * @param mapFile File from which the map data is retrieved
     */
    public GameMap(BomberQuestGame game, FileHandle mapFile) {
        this.game = game;
        this.mapFile = mapFile;
        this.timeLeft = 500;
        this.world = new World(Vector2.Zero, true);
        this.contactListener = new GameContactListener();
        world.setContactListener(contactListener);
        // initialise player position
        Coordinates entrancePlayerCoordinates = new Coordinates(0,0);
        MapLoader mapLoader = new MapLoader();
        // Create flowers/ ground for all tiles
        Map<Coordinates, Integer> prasedMap = mapLoader.prasingMap(mapFile);
        this.flowers = mapLoader.loadFlowerFloor(prasedMap);
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
        //Spawn movement speed power ups under a random DestructibleWall
        addRandomMovementSpeedPowerUps(5);

        // Create a player with initial position at entrance (or 0, 0 if no entrance was specified)
        this.player = new Player(this.world, entrancePlayerCoordinates.getX(), entrancePlayerCoordinates.getY(), this);
    }

    /**
     * Update the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     *
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
        // Player
        this.player.tick(frameTime);

        if ((contactListener.isPlayerEnemyCollision() || timeLeft <= 0) && player.isKilled() != true) {
            player.setKilled(true);
        }
        // Enemies
        for (Enemy enemy : enemies) {
            enemy.tick(frameTime);
        }
        doPhysicsStep(frameTime);
    }

    /**
     * Perform as many physics steps as necessary to catch up to the given frame time.
     * This will update the Box2D world by the given time step.
     *
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
     *
     * @return returns ture if one of the conditions to win the game is fulfilled
     */
    public boolean checkWinStatus() {
        return getEnemies().isEmpty() && contactListener.isExitReached();
    }

    /**
     * Check whether at least one of the conditions to lose the game is fulfilled.
     * In case a loose condition is fulfilled the player performs a death animation before the method returns true.
     *
     * @return true if one of the conditions to lose the game is fulfilled
     */
    public boolean checkLoseStatus() {
        if (player.isKilled()) {
            Animation<TextureRegion> dyingAnimation = Animations.CHARACTER_DYING;
            playerDeathAnimationTime += Gdx.graphics.getDeltaTime();

            if (!dyingAnimation.isAnimationFinished(playerDeathAnimationTime)) {
                return false;
            }
            playerDeathAnimationTime = 0f;

            return true;
        }

        return false;
    }

    // PowerUps

    /**
     * Update the power-ups on the map
     * If the player touches a power-up its is removed from the map and added into the power-up list of the player
     */
    public void updatepowerUps() {
        for (BombNbrPowerUp bombNbrPowerUp : bombNbrPowerUps) {
            if (bombNbrPowerUp.isMarkedForRemoval() && !bombNbrPowerUp.isTaken()) {
                player.addBombNbrPowerUp(bombNbrPowerUp);
                bombNbrPowerUp.destroy(world);
            }
        }
        for (BlastRadiusPowerUp blastRadiusPowerUp : blastRadiusPowerUps) {
            if (blastRadiusPowerUp.isMarkedForRemoval() && !blastRadiusPowerUp.isTaken()) {
                player.addBlastRadiusPowerUp(blastRadiusPowerUp);
                blastRadiusPowerUp.destroy(world);
            }
        }
        for (MovementSpeedPowerUp movementSpeedPowerUp : movementSpeedPowerUps) {
            if (movementSpeedPowerUp.isMarkedForRemoval() && !movementSpeedPowerUp.isTaken()) {
                player.addMovementSpeedPowerUp(movementSpeedPowerUp);
                movementSpeedPowerUp.destroy(world);
            }
        }

    }

    /**
     * Adds a specific number of movement speed power-ups under random destructible walls.
     * Checks that no other power-ups are already placed under the same wall.
     *
     * @param count The number of power-ups to add to the map.
     */
    private void addRandomMovementSpeedPowerUps(int count) {
        Random random = new Random();
        List<DestructibleWall> availableWalls = new ArrayList<>(destructibleWalls);
        for (int i = 0; i < count; i++) {
            boolean wallFound = false;
            while (!wallFound && !availableWalls.isEmpty()) {
                // random wall
                int randomIndex = random.nextInt(availableWalls.size());
                DestructibleWall selectedWall = availableWalls.get(randomIndex);
                availableWalls.remove(randomIndex);
                // Check if there is already a power-up under the wall
                boolean blastRadiusPowerUpAt = blastRadiusPowerUps.stream()
                        .anyMatch(p -> p.getX() == selectedWall.getX() && p.getY() == selectedWall.getY());
                boolean bombNbrPowerUpAt = bombNbrPowerUps.stream()
                        .anyMatch(p -> p.getX() == selectedWall.getX() && p.getY() == selectedWall.getY());
                boolean exitAt = exit.getX() == selectedWall.getX() && exit.getY() == selectedWall.getY();
                boolean objectAt = blastRadiusPowerUpAt || bombNbrPowerUpAt || exitAt;
                if (!objectAt) {
                    wallFound = true;
                    // Set movement speed power-up
                    MovementSpeedPowerUp powerUp = new MovementSpeedPowerUp(
                            world,
                            selectedWall.getX(),
                            selectedWall.getY()
                    );
                    movementSpeedPowerUps.add(powerUp);
                }
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
     * Retrieve a destructible wall located at the specified coordinates.
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
     * Remove the specified destructible wall from the list of destructible walls in the game map.
     *
     * @param wall the DestructibleWall to be removed from the game map
     */
    public void removeDestructibleWall(DestructibleWall wall) {
        destructibleWalls.remove(wall);
    }


    /**
     * Collect all drawable elements in one list
     * Determine which drawables will be drawn over the other drawables
     *
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
        for (MovementSpeedPowerUp movementSpeedPowerUp : movementSpeedPowerUps) {
            allDrawables.add(movementSpeedPowerUp);
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
        allDrawables.add(player);
        return allDrawables;
    }

    /** Clean up resources when the game is disposed. */
    public void dispose() {
        this.timeLeft = 0;
        setMapFile(null);
    }

    // Getter and Setter for the attributes
    public Player getPlayer() {
        return player;
    }
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
}
