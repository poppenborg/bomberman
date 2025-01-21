package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.map.MapLoader;

/**
 * Abstract super class for common functionalities of the screens
 */
public abstract class BaseScreen implements Screen {
    protected final Stage stage;
    protected final OrthographicCamera camera;
    protected final Viewport viewport;
    protected final Table table;
    protected MusicTrack musicTrack;
    protected static final float BUTTON_WITH = 400;

    /**
     * Constructor for BaseScreen. Sets up the camera, viewport, stage, and UI elements.
     * @param game The main game class, used to access global resources and methods.
     */
    public BaseScreen(BomberQuestGame game) {
        this.camera = new OrthographicCamera();

        this.viewport = new ScreenViewport(camera); // Create a viewport with the camera
        this.stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        this.table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        this.musicTrack = null;
    }

    /**
     * The render method is called every frame to render the menu screen.
     * It clears the screen and draws the stage.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.250f); // Cap frame time to 250ms to prevent spiral of death        ScreenUtils.clear(Color.BLACK);
        ScreenUtils.clear(Color.BLACK);
        stage.act(frameTime); // Update the stage
        stage.draw(); // Draw the stage
    }

    /**
     * Resize the stage when the screen is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }

    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
        musicTrack.dispose();

    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    public void setMusicTrack(MusicTrack musicTrack) {
        this.musicTrack = musicTrack;
    }
    public MusicTrack getMusicTrack() {
        return musicTrack;
    }
}
