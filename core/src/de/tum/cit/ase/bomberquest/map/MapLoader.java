package de.tum.cit.ase.bomberquest.map;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the loaded map.
 * Loads a map file according to the file logic of this game.
 */
public class MapLoader {
    private final String mapDirectory;

    public MapLoader(String mapDirectory) {
        this.mapDirectory = mapDirectory;
    }

    /**
     * Fills up an array of files with all the existing maps
     * @return Array of all file paths for maps
     */
    public File[] getMapFiles() {
        File directory = new File(mapDirectory);
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

    public String getMapDirectory() {
        return mapDirectory;
    }

}
