package de.tum.cit.ase.bomberquest.gameobjects.powerups;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.audio.Soundeffect;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.texture.GameContactListener;

public abstract class PowerUp extends GameObject {

    /** Indicates whether the power-up should be removed */
    protected boolean markedForRemoval;
    /** Indicates whether the power-up is removed */
    protected boolean taken;

    /**
     * Constructor for the Power-up
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public PowerUp(World world, float x, float y) {
        super(x, y);
        this.markedForRemoval = false;
        this.taken = false;
        this.hitbox = createHitbox(world);
    }

    /**
     * Create a Box2D body for the PowerUp.
     * @param world The Box2D world to add the body to.
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

    public void destroy(World world) {
        if (markedForRemoval) {
            this.taken = true;
            world.destroyBody(hitbox);
        }
    }

    // getters and setters
    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }
    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
        Soundeffect.POWERUP_SOUND.play(1);
    }
    public boolean isTaken() {
        return taken;
    }
}
