package de.tum.cit.ase.bomberquest.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Drawable;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * A wall is a static object with a hitbox, so the player cannot walk through it, unless destroyed.
 */
public abstract class Wall implements Drawable {

    // We would normally get the position from the hitbox, but since we don't need to move the chest, we can store the position directly.
    private final float x;
    private final float y;
    private final Body body;

    /**
     * Create a wall at the given position.
     * @param world The Box2D world to add the wall's hitbox to.
     * @param x The X position.
     * @param y The Y position.
     */
    public Wall(World world, float x, float y) {
        this.x = x;
        this.y = y;
        // Since the hitbox never moves, and we never need to change it, we don't need to store a reference to it.
        this.body = createHitbox(world);
    }
    
    /**
     * Create a Box2D body for the wall.
     * @param world The Box2D world to add the body to.
     */
    private Body createHitbox(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.x, this.y);
        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        body.createFixture(box, 1.0f);
        box.dispose();
        body.setUserData(this);
        return body; // Return the created body for future reference
    }
    
    @Override
    public abstract TextureRegion getCurrentAppearance();

    @Override
    public float getX() {
        return x;
    }
    
    @Override
    public float getY() {
        return y;
    }

    public Body getBody() {
        return body;
    }
}
