package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import de.tum.cit.ase.bomberquest.BomberQuestGame;
import de.tum.cit.ase.bomberquest.audio.MusicTrack;
import de.tum.cit.ase.bomberquest.gameobjects.*;
import de.tum.cit.ase.bomberquest.gameobjects.bombs.Bomb;
import de.tum.cit.ase.bomberquest.gameobjects.walls.DestructibleWall;
import de.tum.cit.ase.bomberquest.map.*;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen extends BaseScreen implements Screen {

    /**
     * The size of a grid cell in pixels.
     * This allows us to think of coordinates in terms of square grid tiles
     * (e.g. x=1, y=1 is the bottom left corner of the map)
     * rather than absolute pixel coordinates.
     */
    public static final int TILE_SIZE_PX = 16;

    /**
     * The scale of the game.
     * This is used to make everything in the game look bigger or smaller.
     */
    public static final int SCALE = 4;

    private final BomberQuestGame game;
    private final SpriteBatch spriteBatch;
    private final GameMap map;
    private final Hud hud;
    /**
     * Timer of the game
     */
    private float timeLeft;
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(BomberQuestGame game) {
        super(game);
        // set the music
        setMusicTrack(MusicTrack.GAME_BACKGROUND);
        getMusicTrack().play();
        // create the game
        this.game = game;
        this.spriteBatch = game.getSpriteBatch();
        this.map = game.getMap();
        this.hud = new Hud(spriteBatch, game.getSkin().getFont("font"));
        // Create and configure the camera for the game view
        camera.setToOrtho(false);
        camera.zoom = 1f;
        this.timeLeft = map.getTimeLeft();
    }

    /**
     * The render method is called every frame to render the game.
     * @param deltaTime The time in seconds since the last render.
     */
    @Override
    public void render(float deltaTime) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToPauseScreen();
        }

        // Clear the previous frame from the screen, or else the picture smears
        ScreenUtils.clear(Color.BLACK);

        // Cap frame time to 250ms to prevent spiral of death
        float frameTime = Math.min(deltaTime, 0.250f);

        // Update the map state
        map.tick(frameTime);

        // Update the camera
        updateCamera();

        // Update the destructible walls
        for (DestructibleWall wall : map.getDestructibleWalls()) {
            wall.update(frameTime);
        }

        // Render the Bombs
        map.updateBombs(deltaTime);

        // render the Bomb-Cooldown for the hud
        float bombCooldown = map.getPlayer().getRemainingBombCooldown();
        hud.setBombCooldown(bombCooldown);

        // load the current blast radius
        int blastRadius = map.getPlayer().getBlastRadius();
        hud.setBlastRadius(blastRadius);

        // load the concurrent nbr of bombs
        int bombNbr = map.getPlayer().getBombNbr();
        hud.setBombNbr(bombNbr);

        // update the timer
        if (deltaTime < 1) {
            timeLeft -= deltaTime;
        }
        hud.setTimeLeft(timeLeft);
        map.setTimeLeft(timeLeft);

        // PowerUps
        map.updatepowerUps();

        // check the game status
        game.checkGameState();

        // Render the map on the screen
        renderMap();

        // Render the HUD on the screen
        hud.render();

    }

    /**
     * Updates the camera to match the current state of the game.
     * currently the camera follows the player.
     */
    private void updateCamera() {
        camera.setToOrtho(false);
        camera.position.x = map.getPlayer().getX() * TILE_SIZE_PX * SCALE;
        camera.position.y = map.getPlayer().getY() * TILE_SIZE_PX * SCALE;
        camera.update(); // This is necessary to apply the changes
    }

    private void renderMap() {
        // This configures the spriteBatch to use the camera's perspective when rendering
        spriteBatch.setProjectionMatrix(camera.combined);

        // Start drawing
        spriteBatch.begin();

        // Render everything in the map here, in order from lowest to highest (later things appear on top)
        for (Drawable drawable : map.allDrawablesOrdered()) {
            draw(spriteBatch, drawable);
            if (drawable instanceof Bomb) {
                Bomb bomb = (Bomb) drawable;
                for (Coordinates coord : bomb.getBlastCoordinates()) {
                    float x = coord.getX() * TILE_SIZE_PX * SCALE;
                    float y = coord.getY() * TILE_SIZE_PX * SCALE;

                    TextureRegion texture = bomb.getAppearanceForCoordinate(coord);

                    if (texture != null) {
                        spriteBatch.draw(texture, x, y, TILE_SIZE_PX * SCALE, TILE_SIZE_PX * SCALE);
                    }
                }
            }
        }

        // Finish drawing, i.e. send the drawn items to the graphics card
        spriteBatch.end();
    }

    /**
     * Draws this object on the screen.
     * The texture will be scaled by the game scale and the tile size.
     * This should only be called between spriteBatch.begin() and spriteBatch.end(), e.g. in the renderMap() method.
     * @param spriteBatch The SpriteBatch to draw with.
     */
    private static void draw(SpriteBatch spriteBatch, Drawable drawable) {
        TextureRegion texture = drawable.getCurrentAppearance();
        // Drawable coordinates are in tiles, so we need to scale them to pixels
        float x = drawable.getX() * TILE_SIZE_PX * SCALE;
        float y = drawable.getY() * TILE_SIZE_PX * SCALE;
        // Additionally scale everything by the game scale
        float width = texture.getRegionWidth() * SCALE;
        float height = texture.getRegionHeight() * SCALE;
        spriteBatch.draw(texture, x, y, width, height);
    }

    /**
     * Called when the window is resized.
     * This is where the camera is updated to match the new window size.
     * @param width The new window width.
     * @param height The new window height.
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        hud.resize(width, height);
    }

    // Unused methods from the Screen interface
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    /** Cleans up resources when the game is disposed. */
//    @Override
//    public void dispose() {
//        hud.dispose();
//        map.dispose();
//    }

}
