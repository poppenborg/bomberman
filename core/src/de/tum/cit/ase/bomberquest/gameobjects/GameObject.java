package de.tum.cit.ase.bomberquest.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Drawable;

public abstract class GameObject extends Coordinates implements Drawable {

    /** The map of the Game */
    protected GameMap gameMap;
    /** The Box2D hitbox of the GameObject, used for position and collision detection. */
    protected Body hitbox;

    /**
     * Constructor sets the coordinates of the Object
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public GameObject(float x, float y) {
        super(x, y);
    }

    /**
     * Create an empty Box2D body for the dynamic bodies .
     * @param world The Box2D world to add the body to.
     */
    protected Body createEmptyDynamicBody (World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        return body;
    }

    /**
     * Create an empty Box2D body for the static bodies .
     * @param world The Box2D world to add the body to.
     */
    protected Body createEmptyStaticBody (World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.StaticBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        return body;
    }

    @Override
    abstract public TextureRegion getCurrentAppearance();

    // getters and setters
    public GameMap getGameMap() {
        return gameMap;
    }
    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }
    public Body getHitbox() {
        return hitbox;
    }
}
