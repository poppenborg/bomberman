package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Blast Radius Power-Up increases blast radius of the bombs by 1 field in each direction.
 * Collected by the player when they collied.
 */
public class BlastRadiusPowerUp extends PowerUp {

    /**
     * Constructor for the BlastRadiusPowerUp
     * Object is initialized at the specified position and a hitbox is created in the game world.
     *
     * @param world The Box2D world.
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public BlastRadiusPowerUp(World world, float x, float y) {
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
            return Textures.BLASTRADIUS;
        } else {
            return Textures.EMPTY;
        }
    }

    /**
     * Checks if there is a blastRadiusPowerUp at the specified coordinates in the parameter. Is used to determine which item is collected
     *
     * @param coordinates the x- and y-coordinate to check
     * @return true if a blastRadiusPowerUp exists at the given coordinates, false otherwise
     */
//    public boolean isBlastRadiusPowerUpAt(Coordinates coordinates) {
//        if (this.getX() == coordinates.getX() && this.getY() == coordinates.getY()) {
//            return true;
//        }
//        return false;
//    }
}
