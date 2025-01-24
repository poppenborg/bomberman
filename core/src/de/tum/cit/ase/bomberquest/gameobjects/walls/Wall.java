package de.tum.cit.ase.bomberquest.gameobjects.walls;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;

/**
 * A wall is a static object with a hitbox, so the player cannot walk through it, unless destroyed.
 * This is an abstract class with common behaviour of all wall classes
 */
public abstract class Wall extends GameObject {

    /**
     * Create a wall at the given position.
     *
     * @param world The Box2D world to add the wall's hitbox to.
     * @param x The X position.
     * @param y The Y position.
     */
    public Wall(World world, float x, float y) {
        super(x, y);
        this.hitbox = createHitbox(world);
    }
    
    /**
     * Create a Box2D body for the wall in form of a square.
     *
     * @param world The Box2D world to add the body to.
     * @return Created Box2D body representing the wall.
     */
    private Body createHitbox(World world) {
        Body body = createEmptyStaticBody(world, getX(), getY());
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        body.createFixture(box, 1f);
        box.dispose();
        body.setUserData(this);
        return body;
    }
}
