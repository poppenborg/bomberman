package de.tum.cit.ase.bomberquest.gameobjects.walls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.walls.Wall;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Indestructible Wall is a static object preventing dynamic objects to pass.
 * This wall can never be destroyed.
 * The blast radius of the bomb is blocked before this wall.
 */
public class IndestructibleWall extends Wall {

    /**
     * Create a IndestructibleWall at the given position.
     * This wall can not be destroyed.
     *
     * @param world The Box2D world to add the IndestructibleWall's hitbox to.
     * @param x     The X position.
     * @param y     The Y position.
     */
    public IndestructibleWall(World world, float x, float y) {
        super(world, x, y);
    }

    /**
     * Return current appearance of the wall.
     *
     * @return TextureRegion representing the appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.INDESTRUCTIBLE_WALL;
    }

}
