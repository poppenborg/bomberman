package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Select;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.MapLoader;

import java.util.Random;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen extends BaseScreen implements Screen {
    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(BomberQuestGame game) {
        super(game);
        // play looser music
        setMusicTrack(MusicTrack.MENU);
        getMusicTrack().play();
        // Set camera zoom for a closer view
        camera.zoom = 1.5f;
        // Add a label as a title
        table.add(new Label("Welcome to Bomberman!", game.getSkin(), "title")).padBottom(80).row();
        // Create and add a button to go to the game screen
        TextButton goToGameButton = new TextButton("Play random Map", game.getSkin());
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (game.getMap() == null || game.getMap().getMapFile() == null) {
                    game.setMap(new GameMap(game, new MapLoader().loadRandomMap())); // if no map was chosen a random one is selected
                }
                dispose();
                game.goToGame(); // Change to the game screen when button is pressed
            }
        });
        // Create and add a button to go to select a map file
        TextButton mapButton = new TextButton("Play selected Map", game.getSkin());
        table.add(mapButton).width(300).row();
        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.openFileChooser();
            }
        });
        // Create and add a button to exit the game
        TextButton exitButton = new TextButton("Exit Game", game.getSkin());
        table.add(exitButton).width(300).row();
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

}
