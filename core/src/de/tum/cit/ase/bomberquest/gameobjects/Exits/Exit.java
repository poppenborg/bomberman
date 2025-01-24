package de.tum.cit.ase.bomberquest.gameobjects.Exits;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * This class represents an exit object
 * It is a static game object that allows mobs to pass through while detecting collisions
 * If the player reaches the exit after all enemies where defeated the game is won
 */
public class Exit extends GameObject {

    /**
     * Constructor for Exit at the specified position in the game world.
     *
     * @param world The Box2D world to which this exit belongs.
     * @param x The x-coordinate of the exit.
     * @param y The y-coordinate of the exit.
     */
    public Exit(World world, float x, float y) {
        super(x, y);
        this.hitbox = createHitbox(world);
    }

    /**
     * Create a Box2D body for the PowerUp to detect collisions.
     *
     * @param world The Box2D world to add the body to.
     * @return The Box2D body representing the hitbox.
     */
    private Body createHitbox(World world) {
        Body body = createEmptyStaticBody(world, getX(), getY());
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.48f, 0.48f);
        // Create a hitbox in form of a rectangle that doesn´t prevent a mob to cross the space but still detects collision
        body.createFixture(box, 1f).setSensor(true);
        box.dispose();
        body.setUserData(this);
        return body;
    }

    /**
     * Return current appearance of the Exit.
     *
     * @return TextureRegion representing the appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.EXIT;
    }
}
