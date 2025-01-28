package de.tum.cit.ase.bomberquest.gameobjects.mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import de.tum.cit.ase.bomberquest.audio.Soundeffect;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BlastRadiusPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.bombs.Bomb;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.BombNbrPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.MovementSpeedPowerUp;
import de.tum.cit.ase.bomberquest.gameobjects.powerups.PowerUp;
import de.tum.cit.ase.bomberquest.map.GameMap;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player character in the game.
 * The player has a hitbox, so it can collide with other objects in the game.
 * The player can move, collect power-ups, and drop bombs.
 */
public class Player extends Mob implements Drawable {

    /** Time (in seconds) that have to pass until the bomb can be dropped again. */
    private static final float BOMB_COOLDOWN = 3.0f;
    /** Maximum blast radius possible. */
    private static final int BLASTCAP = 7;
    /** Maximum number of bombs. */
    private static final int NBRCAP = 7;
    /** Time elapsed since player last dropped a bomb. */
    private float timeSinceLastBomb = 3.0f;
    /** Initial blast radius */
    private int blastRadius = 1;
    /** Initial number of bombs */
    private int bombNbr = 1;
    /** Radius of the player's hitbox. */
    private float radius;
    /** List of collected power-ups. */
    private List<PowerUp> powerUps = new ArrayList<>();
    /** Flag to check if player is killed. */
    private boolean killed = false;
    /** Flag to ensure death sound is only played once. */
    private boolean soundWasPlayed = false;
    /** Initial movement speed of the player.*/
    private static final float MIN_MOVEMENTSPEED = 2.0f;
    /** Maximum movement speed of the player.*/
    private static final float MAX_MOVEMENTSPEED = 4.0f;
    /** Movement speed of the player.*/
    private float movementSpeed = MIN_MOVEMENTSPEED;


    /**
     * Constructs a Player at a given position on the game map.
     *
     * @param world The Box2D world.
     * @param x Initial X position.
     * @param y Initial Y position.
     * @param gameMap Map that holds all the game objects.
     */
    public Player(World world, float x, float y, GameMap gameMap) {
        super(world, x, y);
        this.gameMap = gameMap;
    }

    /**
     * Create a circular hitbox to detect collision.
     *
     * @param world The Box2D world to add the hitbox to.
     * @param startX The x-coordinate of the hitbox's starting position.
     * @param startY The y-coordinate of the hitbox's starting position.
     * @return The Box2D body for the hitbox.
     */
    @Override
    protected Body createHitbox(World world, float startX, float startY) {
        Body body = createEmptyDynamicBody(world, startX, startY);
        CircleShape circle = new CircleShape();
        circle.setRadius(0.3f);
        radius = circle.getRadius();
        body.createFixture(circle, 1.0f);
        circle.dispose();
        body.setUserData(this);
        return body;
    }


    /**
     * Update the player's state dependent on user input and elapsed time.
     * Handles movement, bomb dropping, and cooldown.
     *
     * @param frameTime the time since the last frame.
     */
    @Override
    public void tick(float frameTime) {

        if (killed) { //if the player killed the movement is stopped.
            getHitbox().setLinearVelocity(0,0);
            return; // If the player is killed no action is possible anymore
        }

        setElapsedTime(getElapsedTime() + frameTime);

        float xVelocity = 0;
        float yVelocity = 0;

        //Player can be controlled via the keyboard using the arrow keys OR the WASD as an alternative

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            xVelocity -= movementSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            xVelocity += movementSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            yVelocity += movementSpeed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            yVelocity -= movementSpeed;
        }

        getHitbox().setLinearVelocity(xVelocity, yVelocity);

        //Bomb cooldown

        timeSinceLastBomb += frameTime;

        //Drop Bomb

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && (timeSinceLastBomb >= BOMB_COOLDOWN || gameMap.getBombs().size() < bombNbr)) {
            // Create Sound Effect
            Soundeffect.DROP_BOMB_SOUND.play(Soundeffect.getVOLUME());
            dropBomb();
            timeSinceLastBomb = 0f;
        }
    }

    /**
     * Return current visual representation based on movement and state.
     *
     * @return A TextureRegion for the player's current appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        // if the player is dead, return the death animation
        if (killed) {
            return Animations.CHARACTER_DYING.getKeyFrame(getElapsedTime(), false);
        }

        // Get the current velocity of the player

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

    /**
     * Add the power-up to increase the player´s blast radius if the maximum wasn´t reached
     *
     * @param blastRadiusPowerUp Blast Radius power-up to add.
     */
    public void addBlastRadiusPowerUp(BlastRadiusPowerUp blastRadiusPowerUp) {
        if (powerUps.stream().filter(powerUp -> powerUp instanceof BlastRadiusPowerUp).count() < BLASTCAP) {
            powerUps.add(blastRadiusPowerUp);
            this.blastRadius += 1;
        }
    }

    /**
     * Add the power-up to increase the player´s number of simultaneously playable bombs if the maximum wasn´t reached
     *
     * @param bombNbrPowerUp Number of Bombs power-up to add.
     */
    public void addBombNbrPowerUp(BombNbrPowerUp bombNbrPowerUp) {
        if (powerUps.stream().filter(powerUp -> powerUp instanceof BombNbrPowerUp).count() < NBRCAP) {
            powerUps.add(bombNbrPowerUp);
            this.bombNbr += 1;
        }
    }

    /**
     * Add the power-up to increase the player´s movement speed.
     *
     * @param movementSpeedPowerUp Number of MovementSpeed power-ups to add.
     */
    public void addMovementSpeedPowerUp(MovementSpeedPowerUp movementSpeedPowerUp) {
        if (movementSpeed < MAX_MOVEMENTSPEED) {
            powerUps.add(movementSpeedPowerUp);
            this.increaseMovementSpeed();
        }
    }

    //Bombs

    /**
     * Drop a bomb with the player's current blast radius at the player's position.
     */
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
     * Calculate and return the remaining cool-down time before the player
     * can drop another bomb. Can not be negative.
     *
     * @return A float representing the remaining cooldown time in seconds.
     *         Returns 0 if the cooldown period has already elapsed.
     */
    public float getRemainingBombCooldown() {
        return Math.max(0, BOMB_COOLDOWN - timeSinceLastBomb);
    }


    public boolean increaseMovementSpeed() {
        if (movementSpeed >= MAX_MOVEMENTSPEED) {
            this.movementSpeed = MAX_MOVEMENTSPEED;
            return false;
        } else {
            this.movementSpeed += 0.5f;
            return true;
        }
    }


    //Getter and Setters
    public int getBlastRadius() {
        return blastRadius;
    }
    public float getMovementSpeed() {
        return movementSpeed;
    }
    public int getBombNbr() {
        return bombNbr;
    }
    public float getRadius() {
        return radius;
    }
    public boolean isKilled() {
        return killed;
    }
    public void setKilled(boolean killed) {
        this.killed = killed;
        // Play sound effect
        Soundeffect.DIE_SOUND.play(Soundeffect.getVOLUME());
    }
}
