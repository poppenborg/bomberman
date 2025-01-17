package de.tum.cit.ase.bomberquest.gameobjects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.map.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

public class Bomb extends Coordinates implements Drawable {

    private static final float IGNITING_DURATION = 3.0f;
    private static final float EXPLOSION_DURATION = 0.5f;
    private float stateTime; //time variable for the bomb
    private boolean isExploding; // current state of the bomb (igniting or exploding)
    private boolean toBeRemoved; // After the explosion, the bomb should be removed from the game


    /**
     * Creates a new Bomb object at the specified coordinates.
     *
     * @param x The x-coordinate where the bomb is placed.
     * @param y The y-coordinate where the bomb is placed.
     */


    public Bomb(float x, float y) {
        super(x, y);
        this.stateTime = 0f;
        this.isExploding = false;
        this.toBeRemoved = false;
    }

    //Methods

    /**
     * Retrieves the current appearance of the bomb based on its state.
     * If the bomb is in the "exploding" state, it returns the appropriate frame of the explosion
     * animation. Otherwise, it returns the appropriate frame of the ignition animation.
     *
     * @return The current texture region representing the bomb's appearance.
     */

    public TextureRegion getCurrentAppearance () {
        if (isExploding) {
            return Animations.BOMB_EXPLODING.getKeyFrame(stateTime, false); // Explosion Animation (single time)
        }
        return Animations.BOMB_IGNITING.getKeyFrame(stateTime, true); // Looping the Animation of the ignition
    }

    /**
     * Updates the bombs state depending o the passed time.
     */

    public void update(float deltaTime) {
        stateTime += deltaTime;

        if (isExploding) {
            if (stateTime > EXPLOSION_DURATION) {
                toBeRemoved = true;
            }
        } else {
            if (stateTime > IGNITING_DURATION) {
                explode();
            }
        }
    }

    /**
     * Triggers the explosion of this bomb.
     */

    public void explode() {
        isExploding = true;
        stateTime = 0f; // reset timer for explosion
    }






    //Getters and Setter

    public boolean isToBeRemoved() {
        return toBeRemoved;
    }
}
