package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Movement Speed Power-Up increases Movement Speed of the player by 0.5f.
 * Collected by the player when they collied.
 */
public class MovementSpeedPowerUp extends PowerUp {

    /**
     * Constructor for the MovementSpeedPowerUp
     * Object is initialized at the specified position and a hitbox is created in the game world.
     *
     * @param world The Box2D world.
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public MovementSpeedPowerUp(World world, float x, float y) {
        super(world, x, y);

    }

    /**
     * Retrieve the current appearance.
     * If the power-up has been marked for removal, it returns an empty texture.
     *
     * @return Texture region representing the object's appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        if (!markedForRemoval) {
            return Textures.SPEED_POWERUP;
        } else {
            return Textures.EMPTY;
        }
    }
}
