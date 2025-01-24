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


public class LooseScreen extends BaseScreen{

    /**
     * Constructor for LoseScreen. Sets up the camera, viewport, stage, and UI elements.
     * 2 buttons are displayed, to return to the menu, and to exit the game
     * A music track specific to the loose screen is played
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public LooseScreen(BomberQuestGame game) {
        super(game);
        // play looser music
        setMusicTrack(MusicTrack.LOOSE);
        getMusicTrack().play();
        // Set camera zoom for a closer view
        camera.zoom = 1.5f;
        // Add a label as a title
        table.add(new Label("wasted", game.getSkin(), "title", Color.RED)).padBottom(80).row();
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
