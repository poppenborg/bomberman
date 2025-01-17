package de.tum.cit.ase.bomberquest.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.gameobjects.Bomb;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player extends Mob implements Drawable {

    private static final float BOMB_COOLDOWN = 3.0f;
    private float timeSinceLastBomb = 3.0f;
    private GameMap gameMap;


    public Player(World world, float x, float y, GameMap gameMap) {
        super(world, x, y);
        this.gameMap = gameMap;
    }

    @Override
    protected Body createHitbox(World world, float startX, float startY) {
        // BodyDef is like a blueprint for the movement properties of the body.
        BodyDef bodyDef = new BodyDef();
        // Dynamic bodies are affected by forces and collisions.
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set the initial position of the body.
        bodyDef.position.set(startX, startY);
        // Create the body in the world using the body definition.
        Body body = world.createBody(bodyDef);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the mob.
        CircleShape circle = new CircleShape();
        // Give the circle a radius of 0.3 tiles (the mob is 0.6 tiles wide).
        circle.setRadius(0.3f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the mob.
        body.createFixture(circle, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
        // Set the mob as the user data of the body so we can look up the mob from the body later.
        body.setUserData(this);
        return body;
    }


    /**
     * Move the player via the keyboard using the arrow keys OR the WASD as an alternative
     * @param frameTime the time since the last frame.
     */
    @Override
    public void tick(float frameTime) {
        setElapsedTime(getElapsedTime() + frameTime);

        float xVelocity = 0;
        float yVelocity = 0;

        //Player can be controlled via the keyboard using the arrow keys OR the WASD as an alternative

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVelocity -= 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVelocity += 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVelocity += 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVelocity -= 2;
        }

        getHitbox().setLinearVelocity(xVelocity, yVelocity);

        //Bomb cooldown

        timeSinceLastBomb += frameTime;

        //Drop Bomb

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && timeSinceLastBomb >= BOMB_COOLDOWN) {
            dropBomb();
            timeSinceLastBomb = 0f;
        }

        //Update bombs

    }

    @Override
    public TextureRegion getCurrentAppearance() {
        // Get the current velocity of the mob

        float xVelocity = getHitbox().getLinearVelocity().x;
        float yVelocity = getHitbox().getLinearVelocity().y;

        // Return animation based on the movement direction

        if (xVelocity > 0 && xVelocity > getVELOCITYTHRESHOLD()) {
            return Animations.CHARACTER_WALK_RIGHT.getKeyFrame(getElapsedTime(), true);

        } else if (xVelocity < 0 && Math.abs(xVelocity) > getVELOCITYTHRESHOLD()) {
            return Animations.CHARACTER_WALK_LEFT.getKeyFrame(getElapsedTime(), true);

        } else if (yVelocity > 0 && yVelocity > getVELOCITYTHRESHOLD()) {
            return Animations.CHARACTER_WALK_UP.getKeyFrame(getElapsedTime(), true);

        } else if (yVelocity < 0 && Math.abs(yVelocity) > getVELOCITYTHRESHOLD()) {
            return Animations.CHARACTER_WALK_DOWN.getKeyFrame(getElapsedTime(), true);

        } else {
            return Animations.CHARACTER_WALK_DOWN.getKeyFrame( 0, false);
        }
    }

    //Bombs

    private void dropBomb() {
        //place bomb at position of player
        float bombX = Math.round(getX());
        float bombY = Math.round(getY());

        // Create Bomb and place it on the GameMap
        Bomb bomb = new Bomb(bombX, bombY, gameMap);
        gameMap.getBombs().add(bomb);

        System.out.println("Bomb dropped at position: " + bombX + ", " + bombY);

    }


    //Getter and Setters


}
