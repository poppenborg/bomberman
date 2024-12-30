package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.BomberQuestGame;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final File mapFile;
    // Game objects
    private final Player player;
    
    private final Chest chest;
    
    private final Flowers[][] flowers;

    private List<IndestructibleWall> indestructibleWalls;

    private List<DestructibleWall> destructibleWalls;
    //TODO: replce placeholder for Entrance + Enemy + Exit + power-up until respective class was created
    private List<Placeholder> placeholders;

    public GameMap(BomberQuestGame game) {
        this.game = game;
        this.world = new World(Vector2.Zero, true);
        // Create a player with initial position (1, 3)
        this.player = new Player(this.world, 6, 5);
        // Create a chest in the middle of the map
        this.chest = new Chest(world, 10, 6);
        //TODO: replace manual file insertion!!!!
        this.mapFile = fillMapFiles()[1];
        // Create flowers/ ground for all tials
        int x = prasingMap(mapFile).keySet().stream().map(Coordinates::getX).max(Integer::compare).orElse(0) + 1;
        // increases x by 1 because otherwise the coordinate 0 isn´t represented
        int y = prasingMap(mapFile).keySet().stream().map(Coordinates::getY).max(Integer::compare).orElse(0) + 1;
        // increases y by 1 because otherwise the coordinate 0 isn´t represented
        this.flowers = new Flowers[x][y];
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                this.flowers[i][j] = new Flowers(i, j);
            }
        }
        // initialize Lists of various objects
        indestructibleWalls = new ArrayList<>();
        destructibleWalls = new ArrayList<>();
        placeholders = new ArrayList<>();
        // Iterates over the gameObjects map
        //TODO: replace placeholders
        for (Map.Entry<Coordinates, Integer> entry : prasingMap(mapFile).entrySet()) {
            switch (entry.getValue()) {
                case 0: indestructibleWalls.add(new IndestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY())); break;
                case 1: destructibleWalls.add(new DestructibleWall(this.world, entry.getKey().getX(), entry.getKey().getY())); break;
                // needs to replce placeholder for Entrance
                case 2: placeholders.add(new Placeholder(entry.getKey().getX(), entry.getKey().getY())); break;
                // needs to replce placeholder for Enemy
                case 3: placeholders.add(new Placeholder(entry.getKey().getX(), entry.getKey().getY())); break;
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
    }
    
    /**
     * Updates the game state. This is called once per frame.
     * Every dynamic object in the game should update its state here.
     * @param frameTime the time that has passed since the last update
     */
    public void tick(float frameTime) {
        this.player.tick(frameTime);
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
    
    /** Returns the player on the map. */
    public Player getPlayer() {
        return player;
    }
    
    /** Returns the chest on the map. */
    public Chest getChest() {
        return chest;
    }
    
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

    /** Returns the placeholders on the map. */
    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

// added by Flo
    /**
     * Fills up an array with all the existing maps
     * @return Array of all file paths for maps
     */
    public File[] fillMapFiles() {
        File directory = new File("maps");
        File[] mapFiles = new File[]{};
        if (directory.exists() && directory.isDirectory()) {
            mapFiles = directory.listFiles();
        }
        return mapFiles;
    }

    /**
     * Prases a map
     * @param mapFile the file to be prase (change later to mapFile when it´s clear where to get the input from)
     * @return Map with keys consiting of x- and y-coordinates and values indicating the game object
     */
    public Map<Coordinates, Integer> prasingMap(File mapFile) {
        // Change file path format to use java.nio.file.Files.readString
        java.nio.file.Path filePath = java.nio.file.Paths.get(mapFile.toString());
        String fileContent = "";
        // save the file content in fileContent
        try {
            fileContent = java.nio.file.Files.readString(filePath);
        } catch (IOException e) {
            System.out.println("Exception " + e + " thrown!");
        }
        // split the fileContent for every new line
        String[] linesAsArray = fileContent.split("\n");
        Map<Coordinates, Integer> gameObjects = new HashMap<>();
        // specifies the necessary basic structure of every line (int,int=int) while leaving the possibility for whitespaces
        Pattern overallPattern = Pattern.compile("\\s*\\d+\\s*\\,\\s*\\d+\\s*\\=\\s*\\d\\s*$");
        // Patterns for x, y and value respectively
        Pattern xPattern = Pattern.compile("\\s*\\d+\\s*\\,");
        Pattern yPattern = Pattern.compile("\\,\\s*\\d+\\s*\\=");
        Pattern valuePattern = Pattern.compile("\\=\\s*\\d\\s*$");
        // Pattern to filter the digit
        Pattern digitPattern = Pattern.compile("\\d+");
        // Fill up the Map
        for (String line : linesAsArray) {
            // excluding empty lines and those starting with #
            if (!line.startsWith("#") && overallPattern.matcher(line).matches()) {
                // Convert respective Strings into Integers
                Integer x = Integer.parseInt(twoLvlMatching(line, xPattern, digitPattern));
                Integer y = Integer.parseInt(twoLvlMatching(line, yPattern, digitPattern));
                Integer value = Integer.parseInt(twoLvlMatching(line, valuePattern, digitPattern));
                // parse the values into a map
                gameObjects.put(new Coordinates(x, y), value);
            }
        }
        return gameObjects;
    }

    /**
     * Helper method to find and return a regex specified by 2 patterns
     * purpose: make the code of the method prasingMap more readable
     * @param input String to be filetred
     * @param lvl1Pattern first filerting condition
     * @param lvl2Pattern second filerting condition
     * @return String found when applying both filters
     */
    public String twoLvlMatching(String input, Pattern lvl1Pattern, Pattern lvl2Pattern) {
        String lvl2Filtered = "";
        Matcher lvl1Matcher = lvl1Pattern.matcher(input);
        if (lvl1Matcher.find()) {
            String lvl1Filtered = lvl1Matcher.group();
            Matcher lvl2Matcher = lvl2Pattern.matcher(lvl1Filtered);
            if (lvl2Matcher.find()) {
                lvl2Filtered = lvl2Matcher.group();
            }
        }
        return lvl2Filtered;
    }
}
