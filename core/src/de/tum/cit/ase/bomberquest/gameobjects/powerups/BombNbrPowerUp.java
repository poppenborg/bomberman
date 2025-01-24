package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Bomb Number Power-Up increases the number of bombs the player can place at a time.
 * Collected by the player when they collied.
 */
public class BombNbrPowerUp extends PowerUp {

    /**
     * Constructor for the BombNbrPowerUp
     * Object is initialized at the specified position and a hitbox is created in the game world.
     *
     * @param world The Box2D world.
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public BombNbrPowerUp(World world, float x, float y) {
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
            return Textures.BOMBNBR;
        } else {
            return Textures.EMPTY;
        }
    }

    /**
     * Check if there is a bombNbrPowerUp at the specified coordinates in the parameter. Is used to determine which item is collected
     *
     * @param coordinates the x- and y-coordinate to check
     * @return true if a bombNbrPowerUp exists at the given coordinates, false otherwise
     */
//    public boolean isBombNbrPowerUpAt(Coordinates coordinates) {
//        if (this.getX() == coordinates.getX() && this.getY() == coordinates.getY()) {
//            return true;
//        }
//        return false;
//    }
}
