package de.tum.cit.ase.bomberquest.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents the base class for all game objects.
 * Each GameObject has a position represented by x and y coordinates and a texture for rendering,
 * some also have a hitbox for collision detection.
 */
public abstract class GameObject extends Coordinates implements Drawable {

    /** The map of the Game */
    protected GameMap gameMap;
    /** The Box2D hitbox of the GameObject, used for position and collision detection. */
    protected Body hitbox;

    /**
     * Constructor sets the coordinates of the Object
     *
     * @param x x-coordinate in the GameMap
     * @param y y-coordinate in the GameMap
     */
    public GameObject(float x, float y) {
        super(x, y);
    }

    /**
     * Create an empty dynamic Box2D body for the dynamic bodies.
     *
     * @param world Box2D world to add the body to.
     * @param startX Initial x-coordinate.
     * @param startY Initial y-coordinate.
     * @return An object representing the dynamic body in the game world.
     */
    protected Body createEmptyDynamicBody (World world, float startX, float startY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY);
        Body body = world.createBody(bodyDef);
        return body;
    }

    /**
     * Create an empty static Box2D body for the dynamic bodies.
     *
     * @param world Box2D world to add the body to.
     * @param startX Initial x-coordinate.
     * @param startY Initial y-coordinate.
     * @return An object representing the static body in the game world.
     */
    protected Body createEmptyStaticBody (World world, float startX, float startY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(startX, startY);
        Body body = world.createBody(bodyDef);
        return body;
    }

    /**
     * Get the current appearance of the GameObject.
     * Each subclass must provide its own implementation of this method.
     *
     * @return An object for the current texture.
     */
    @Override
    abstract public TextureRegion getCurrentAppearance();

    // Getters and Setters
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
