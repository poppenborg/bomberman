package de.tum.cit.ase.bomberquest.gameobjects.Exits;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class Exit extends GameObject {
    public Exit(World world, float x, float y) {
        super(x, y);
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

    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.EXIT;
    }
}
