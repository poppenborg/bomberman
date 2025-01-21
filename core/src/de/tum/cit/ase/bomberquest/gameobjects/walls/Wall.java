package de.tum.cit.ase.bomberquest.gameobjects.walls;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;

/**
 * A wall is a static object with a hitbox, so the player cannot walk through it, unless destroyed.
 */
public abstract class Wall extends GameObject {

    /**
     * Create a wall at the given position.
     * @param world The Box2D world to add the wall's hitbox to.
     * @param x The X position.
     * @param y The Y position.
     */
    public Wall(World world, float x, float y) {
        super(x, y);
        // Since the hitbox never moves, and we never need to change it, we don't need to store a reference to it.
        this.hitbox = createHitbox(world);
    }
    
    /**
     * Create a Box2D body for the wall.
     * @param world The Box2D world to add the body to.
     */
    private Body createHitbox(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(getX(), getY());
        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        body.createFixture(box, 1f);
        box.dispose();
        body.setUserData(this);
        return body; // Return the created body for future reference
    }
}
