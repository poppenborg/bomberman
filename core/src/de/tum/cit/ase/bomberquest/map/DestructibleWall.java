package de.tum.cit.ase.bomberquest.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class DestructibleWall extends Wall{
    /**
     * Create a chest at the given position.
     *
     * @param world The Box2D world to add the chest's hitbox to.
     * @param x     The X position.
     * @param y     The Y position.
     */

    private boolean destroyed;
    private float animationTime; //to update the Animation
    private boolean playAnimation; //to start the Animation

    public DestructibleWall(World world, float x, float y) {
        super(world, x, y);
        this.destroyed = false;
        this.animationTime = 0;
        this.playAnimation = false;
    }

    //Methods

    @Override
    public TextureRegion getCurrentAppearance() {

        if (destroyed && playAnimation) {
            TextureRegion currentFrame = Animations.DESTRUCTIBLE_WALL_DESTROY.getKeyFrame(animationTime, false);
            // check if the Animation has finished and stop it
            if (Animations.DESTRUCTIBLE_WALL_DESTROY.isAnimationFinished(animationTime)) {
                playAnimation = false;
            }

            return currentFrame;
        }

        if (!destroyed) {
            return Textures.DESTRUCTIBLE_WALL;
        } else return Textures.EMPTY;
    }

    public void destroy(World world) {
        if (!destroyed) {
            destroyed = true;
            playAnimation = true; //Start the animation of the destruction
            animationTime = 0;
            world.destroyBody(getBody()); //Destroy the body and therefore the hitbox of the wall
        }
    }

    /*
    * Method update () for updating the state of the wall and the elapsed time for the animation
    * Gets called in the GameScreen class
    * */

    public void update(float deltaTime) {
        if (playAnimation) {
            animationTime += deltaTime;
        }
    }




    //Getters and Setters


    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }


}
