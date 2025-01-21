package de.tum.cit.ase.bomberquest.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.map.Coordinates;
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
