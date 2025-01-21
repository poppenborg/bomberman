package de.tum.cit.ase.bomberquest.gameobjects.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BlastRadiusPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.bombs.Bomb;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BombNbrPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.PowerUp;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 */
public class Player extends Mob implements Drawable {

    private static final float BOMB_COOLDOWN = 3.0f;
    private static final int BLASTCAP = 7;
    private static final int NBRCAP = 7;
    private float timeSinceLastBomb = 3.0f;
    private int blastRadius = 1;
    private int bombNbr = 1;
    private float radius;
    private List<PowerUp> powerUps;

    public Player(World world, float x, float y, GameMap gameMap) {
        super(world, x, y);
        this.gameMap = gameMap;
        powerUps = new ArrayList<>();
    }

    @Override
    protected Body createHitbox(World world, float startX, float startY) {
        // Create the body in the world using the body definition.
        Body body = createEmptyDynamicBody(world, startX, startY);
        // Now we need to give the body a shape so the physics engine knows how to collide with it.
        // We'll use a circle shape for the mob.
        CircleShape circle = new CircleShape();
//        PolygonShape rectangle = new PolygonShape();
        // Give the circle a radius of 0.3 tiles (the mob is 0.6 tiles wide).
        circle.setRadius(0.3f);
        radius = circle.getRadius();
//        rectangle.setAsBox(0.35f, 0.35f);
        // Attach the shape to the body as a fixture.
        // Bodies can have multiple fixtures, but we only need one for the plyer.
        body.createFixture(circle, 1.0f);
//        body.createFixture(rectangle, 1.0f);
        // We're done with the shape, so we should dispose of it to free up memory.
        circle.dispose();
//        rectangle.dispose();
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (timeSinceLastBomb >= BOMB_COOLDOWN || gameMap.getBombs().size() < bombNbr)) {
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

    // PowerUps

    /** Add the respective power-up if the maximum wasn´t reached */
    public void addBlastRadiusPowerUp(BlastRadiusPowerUp blastRadiusPowerUp) {
        if (powerUps.stream().filter(powerUp -> powerUp instanceof BlastRadiusPowerUp).count() < BLASTCAP) {
            powerUps.add(blastRadiusPowerUp);
            this.blastRadius += 1;
        }
    }
    public void addBombNbrPowerUp(BombNbrPowerUp bombNbrPowerUp) {
        if (powerUps.stream().filter(powerUp -> powerUp instanceof BombNbrPowerUp).count() < NBRCAP) {
            powerUps.add(bombNbrPowerUp);
            this.bombNbr += 1;
        }
    }

    //Bombs

    private void dropBomb() {
        //place bomb at position of player
        float bombX = Math.round(getX());
        float bombY = Math.round(getY());

        // Create Bomb and place it on the GameMap
        Bomb bomb = new Bomb(bombX, bombY, gameMap);
        bomb.setBlastRadius(blastRadius);
        gameMap.getBombs().add(bomb);
    }

    /**
     * Calculates and returns the remaining cooldown time before the player
     * can drop another bomb. Can not be negative.
     * @return A float representing the remaining cooldown time in seconds.
     *         Returns 0 if the cooldown period has already elapsed.
     */
    public float getRemainingBombCooldown() {
        return Math.max(0, BOMB_COOLDOWN - timeSinceLastBomb);
    }


    //Getter and Setters
    public int getBlastRadius() {
        return blastRadius;
    }
    public void setBlastRadius(int blastRadius) {
        if (blastRadius > 0 && blastRadius <= 8) {
            this.blastRadius = blastRadius;
        } else if (blastRadius < 0) {
            this.blastRadius = 1;
        } else if (blastRadius > 8) {
            this.blastRadius = 8;
        }
    }
    public int getBombNbr() {
        return bombNbr;
    }

    public float getRadius() {
        return radius;
    }
}
