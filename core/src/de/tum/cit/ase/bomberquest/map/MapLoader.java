package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.files.FileHandle;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.gameobjects.flowers.Flowers;
import de.tum.cit.ase.bomberquest.gameobjects.walls.IndestructibleWall;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the loaded map.
 * Loads a map file according to the file logic of this game.
 * Separates the different GameObjects into Lists and Arrays
 */
public class MapLoader {

    /** Array of MapFiles */
    private FileHandle[] mapFiles;

    /**
     * Create a new map loader
     */
    public MapLoader() {
        this.mapFiles = loadMapFiles();
    }

    /**
     * Fill up an array of files with all the existing maps.
     *
     * @return Array of all file paths for maps
     */
    public FileHandle[] loadMapFiles() {
        FileHandle directory = new FileHandle("maps");
        FileHandle[] mapFiles = new FileHandle[]{};
        if (directory.exists() && directory.isDirectory()) {
            mapFiles = directory.list();
        }
        return mapFiles;
    }

    /**
     * Praise a map.
     * For each line in the map file a new value in the HashMap is created,
     * with the Coordinates as key and an Integer - representing a specific oGameObject - as value.
     * Empty lines and lines starting with something else than a number or a space are skipped.
     *
     * @param mapFile the file to be praise
     * @return Map with keys consisting of x- and y-coordinates and values indicating the game object
     */
    public Map<Coordinates, Integer> prasingMap(FileHandle mapFile) {
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
     *
     * @param input String to be filtered
     * @param lvl1Pattern first filtered condition
     * @param lvl2Pattern second filtered condition
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

    /**
     * Load a random map of the possible ones.
     *
     * @return randomly chosen mapFile
     */
    public FileHandle loadRandomMap() {
        int nbrOfFiles = getMapFiles().length;
        return getMapFiles()[new Random().nextInt(nbrOfFiles)];
    }

    /**
     * Fill the complete Map with Flowers.
     *
     * @param prasedMap HashMap containing all the GameObjects
     * @return Multidimensional Array of flowers
     */
    public Flowers[][] loadFlowerFloor(Map<Coordinates, Integer> prasedMap) {
        // Get rectangle with
        float x = prasedMap.keySet().stream().map(Coordinates::getX).max(Float::compare).orElse(0f) + 1f;
        // Get rectangle height
        float y = prasedMap.keySet().stream().map(Coordinates::getY).max(Float::compare).orElse(0f) + 1f;
        // Create Array of Flowers
        Flowers[][] flowers = new Flowers[(int) x][(int) y];
        // Fill the Array
        for (int i = 0; i < flowers.length; i++) {
            for (int j = 0; j < flowers[i].length; j++) {
                flowers[i][j] = new Flowers(i, j);
            }
        }
        return flowers;
    }

    // Getters and Setters
    public FileHandle[] getMapFiles() {
        return mapFiles;
    }
}


