package de.tum.cit.ase.bomberquest.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

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

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.INDESTRUCTIBLE_WALL;
    }

}
