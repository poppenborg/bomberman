package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.audio.Soundeffect;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.MapLoader;

import java.util.Random;

/**
 * The WinScreen class is responsible for displaying a screen when the game is won.
 * It extends the BasScreen class and sets up the UI components for the menu.
 */
public class WinScreen extends BaseScreen{

    /**
     * Constructor for WinScreen. Sets up the camera, viewport, stage, and UI elements.
     * 2 buttons are displayed, to return to the menu, and to exit the game
     * A music track specific to the win screen is played
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public WinScreen(BomberQuestGame game) {
        super(game);
        // play victory sound effect
        Soundeffect.WINN_SOUND.play();
        // play winner music
        setMusicTrack(MusicTrack.WIN);
        getMusicTrack().play();
        // Set camera zoom for a closer view
        camera.zoom = 1.5f;
        // Add a label as a title
        table.add(new Label("Congratulations you won!", game.getSkin(), "title", Color.GOLD)).padBottom(80).row();
        // Create and add a button to go bac to the menu screen
        TextButton goToMenuButton = new TextButton("Go To Menu", game.getSkin());
        table.add(goToMenuButton).width(BUTTON_WITH).row();
        goToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                Soundeffect.BUTTON_SOUN.play();
                game.goToMenu();
                dispose();
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
