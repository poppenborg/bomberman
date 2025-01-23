package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.MapLoader;
import games.spooky.gdx.nativefilechooser.NativeFileChooserCallback;
import games.spooky.gdx.nativefilechooser.NativeFileChooserConfiguration;


/**
 * The PauseScreen class is responsible for displaying the pause menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class PauseScreen extends BaseScreen implements Screen {
    /**
     * Constructor for PauseScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public PauseScreen(BomberQuestGame game) {
        super(game);
        // Memorize the current time
        float timeLeft = game.getMap().getTimeLeft();
        // play looser music
        setMusicTrack(MusicTrack.PAUSE);
        getMusicTrack().play();
        // Set camera zoom for a closer view
        camera.zoom = 1.5f;
        // Add a label as a title
        table.add(new Label("Pause", game.getSkin(), "title")).padBottom(80).row();
        // Create and add a button to go to the game screen
        TextButton goResumeGameButton = new TextButton("Resume Game", game.getSkin());
        table.add(goResumeGameButton).width(BUTTON_WITH).row();
        goResumeGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToGame(); // Change to the game screen when button is pressed
                game.getMap().setTimeLeft(timeLeft);
                dispose();
            }
        });
        // Create and add a button to go to select a map file
        TextButton newMapButton = new TextButton("Restart new map", game.getSkin());
        table.add(newMapButton).width(BUTTON_WITH).row();
        newMapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.openFileChooser();
            }
        });
        // Create and add a button to exit the game
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(BUTTON_WITH).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

}
