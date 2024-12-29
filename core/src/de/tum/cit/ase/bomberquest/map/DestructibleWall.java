package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class DestructibleWall extends Wall{
    /**
     * Create a chest at the given position.
     *
     * @param world The Box2D world to add the chest's hitbox to.
     * @param x     The X position.
     * @param y     The Y position.
     */

    private boolean destroyed;

    public DestructibleWall(World world, float x, float y) {
        super(world, x, y);
        this.destroyed = false;
    }

    //Methods

    @Override
    public TextureRegion getCurrentAppearance() {
        if (!destroyed) {
            return Textures.DESTRUCTIBLE_WALL;
        } else return Textures.EMPTY;
    }

    public void destroy(World world) {
        if (!destroyed) {
            destroyed = true;
            world.destroyBody(getBody()); //Destroy the body and therefore the hitbox of the wall
        }
    }


    //Getters and Setters


    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
