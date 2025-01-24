package de.tum.cit.ase.bomberquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.audio.Soundeffect;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.MapLoader;
import de.tum.cit.ase.bomberquest.screen.*;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;
import games.spooky.gdx.nativefilechooser.NativeFileChooserIntent;

import java.io.FilenameFilter;

/**
 * The BomberQuestGame class represents the core of the Bomber Quest game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class BomberQuestGame extends Game {

    /**
     * Sprite Batch for rendering game elements.
     * This eats a lot of memory, so we only want one of these.
     */
    private SpriteBatch spriteBatch;

    /** The game's UI skin. This is used to style the game's UI elements. */
    private Skin skin;

    /**
     * The file chooser for loading map files from the user's computer.
     * This will give you access to a {@link com.badlogic.gdx.files.FileHandle} object,
     * which you can use to read the contents of the map file as a String, and then parse it into a {@link GameMap}.
     */
    private final NativeFileChooser fileChooser;

    /**
     * The map. This is where all the game objects are stored.
     * This is owned by {@link BomberQuestGame} and not by {@link GameScreen}
     * because the map should not be destroyed if we temporarily switch to another screen.
     */
    private GameMap map;

    /**
     * The File. This is the File that can be chosen with the fileChooser
     */
    private FileHandle file;

    /**
     * Constructor for BomberQuestGame.
     *
     * @param fileChooser The file chooser for the game, typically used in desktop environment.
     */
    public BomberQuestGame(NativeFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * Called when the game is created. Initializes the SpriteBatch and Skin.
     * During the class constructor, libGDX is not fully initialized yet.
     * Therefore this method serves as a second constructor for the game,
     * and we can use libGDX resources here.
     */
    @Override
    public void create() {
        this.spriteBatch = new SpriteBatch(); // Create SpriteBatch for rendering
        this.skin = new Skin(Gdx.files.internal("skin/craftacular/craftacular-ui.json")); // Load UI skin
        this.map = null; // Create an empty game map
        goToMenu(); // Navigate to the menu screen
    }

    /**
     * Switch to the menu screen.
     */
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
    }

    /**
     * Switch to the game screen.
     */
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
    }

    /**
     * Switch to the pause screen.
     */
    public void goToPauseScreen() {
        this.setScreen(new PauseScreen(this));
    }

    /**
     * Switch to the win screen.
     */
    public void goToWinScreen() {
        this.setScreen(new WinScreen(this)); // Set the current screen to GameScreen
    }

    /**
     * Switch to the loose screen.
     */
    public void goToLooseScreen() {
        this.setScreen(new LooseScreen(this)); // Set the current screen to GameScreen
    }

    /**
     * Open a folder to choose a map file from.
     * When a file was selected, the game map is initialized and the game screen is displayed.
     */
    public void openFileChooser() {
        NativeFileChooserConfiguration nativeFileChooserConfiguration = new NativeFileChooserConfiguration();
        nativeFileChooserConfiguration.directory = Gdx.files.local("maps");
        nativeFileChooserConfiguration.title = "Choose a map file";
        nativeFileChooserConfiguration.mimeFilter = "text/plain,application/octet-stream";
        nativeFileChooserConfiguration.intent = NativeFileChooserIntent.OPEN;
        NativeFileChooserCallback nativeFileChooserCallback = new NativeFileChooserCallback() {
            @Override
            public void onFileChosen(FileHandle fileHandle) {
                initializeMap(fileHandle);
                goToGame();
            }
            @Override
            public void onCancellation() {
            }
            @Override
            public void onError(Exception e) {
                System.out.println("Error: " + e);
            }
        };
        fileChooser.chooseFile(nativeFileChooserConfiguration, nativeFileChooserCallback);
    }

    /**
     * Initialize the game map with a specified file.
     *
     * @param fileHandle Map file to load.
     */
    public void initializeMap(FileHandle fileHandle) {
        this.map = new GameMap(this, fileHandle);
    }

    /**
     * Switch to the given screen and disposes of the previous screen.
     * @param screen the new screen
     */
    @Override
    public void setScreen(Screen screen) {
        Screen previousScreen = super.screen;
        super.setScreen(screen);
        if (previousScreen != null) {
            previousScreen.dispose();
        }
    }

    /**
     * Clean up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    /**
     * Check whether game is won or lost or still in progress
     * If game is won or lost the screen is changed accordingly and superfluous resources are disposed of
     */
    public void checkGameState() {
        if (map.checkLoseStatus()){
            goToLooseScreen();
            map.dispose();
        }
        if (map.checkWinStatus()) {
            goToWinScreen();
            map.dispose();
        }
    }

    // Getters and Setters
    public void setMap(GameMap map) {
        this.map = map;
    }
    public Skin getSkin() {
        return skin;
    }
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
    public GameMap getMap() {
        return map;
    }
    public NativeFileChooser getFileChooser() {
        return fileChooser;
    }
}
