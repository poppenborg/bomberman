package de.tum.cit.ase.bomberquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.tum.cit.ase.bomberquest.map.GameMap;

/**
 * A Heads-Up Display (HUD) that displays information on the screen.
 * It uses a separate camera so that it is always fixed on the screen.
 */
public class Hud {
    
    /** The SpriteBatch used to draw the HUD. This is the same as the one used in the GameScreen. */
    private final SpriteBatch spriteBatch;
    /** The font used to draw text on the screen. */
    private final BitmapFont font;
    /** The camera used to render the HUD. */
    private final OrthographicCamera camera;
    /** The time left in the Game. */
    private float timeLeft;
    /** The Cooldown for the player to drop the next bomb*/
    private float bombCooldown;
    /** The current blast Radius*/
    private int blastRadius;
    /** The current bomb nbr*/
    private int bombNbr;

    public Hud(SpriteBatch spriteBatch, BitmapFont font) {
        this.spriteBatch = spriteBatch;
        this.font = font;
        this.camera = new OrthographicCamera();
    }
    
    /**
     * Renders the HUD on the screen.
     * This uses a different OrthographicCamera so that the HUD is always fixed on the screen.
     */
    public void render() {
        // Render from the camera's perspective
        spriteBatch.setProjectionMatrix(camera.combined);
        // Start drawing
        spriteBatch.begin();
        // Draw the HUD elements
        font.draw(spriteBatch, "Press Esc to Pause!", 10, Gdx.graphics.getHeight() - 10);
        font.draw(spriteBatch, "Time left: " + String.format("%.1f", timeLeft), 10, Gdx.graphics.getHeight() - 40);


        //Bomb-Cooldown
        if (bombCooldown <= 0) {
            if (bombNbr > 1) {
                font.draw(spriteBatch, "BOMBS READY!!!", 10, 90); // Text if ready
            } else {
                font.draw(spriteBatch, "BOMB READY!!!", 10, 90); // Text if ready
            }
        } else {
            font.draw(spriteBatch, "Bomb Cooldown: " + String.format("%.1f", bombCooldown), 10, 90);
        }

        // Max concurrent bombs
        if (bombNbr == 8) {
            font.draw(spriteBatch, "Concurrent bombs: 8 (Max)", 10, 60);
        } else {
            font.draw(spriteBatch, "Concurrent bombs: " + bombNbr, 10, 60);
        }

        //Blast Radius
        if (blastRadius == 8) {
            font.draw(spriteBatch, "Blast Radius: 8 (Max)", 10, 30); // if max is reached
        } else {
            font.draw(spriteBatch, "Blast Radius: " + blastRadius, 10, 30);
        }

        // Finish drawing
        spriteBatch.end();
    }
    
    /**
     * Resizes the HUD when the screen size changes.
     * This is called when the window is resized.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    /**
     * Update the Timer
     */
    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
    }

    /** Cleans up resources when the game is disposed. */
    public void dispose() {
        spriteBatch.dispose();
    }


    /**
     * Set the bomb cooldown
     */
    public void setBombCooldown(float bombCooldown) {
        this.bombCooldown = bombCooldown;
    }

    /**
     * Set the blastradius
     */
    public void setBlastRadius(int blastRadius) {
        this.blastRadius = blastRadius;
    }

    /**
     * Set the concurrent nbr of bombs
     */
    public void setBombNbr(int bombNbr) {
        this.bombNbr = bombNbr;
    }
}
