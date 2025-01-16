package de.tum.cit.ase.bomberquest.gameobjects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.map.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class Bomb extends Coordinates {

    private float elapsedTime;
    private float ignitingAnimationTime;
    private float explosionAnimationTime;
    private boolean isExplosionAnimationPlaying;
    private float explosionDelay;
    private boolean exploded;

    /**
     *
     * @param x x-coordinate of the Bomb
     * @param y y-coordinate of the Bomb
     * @param explosionDelay Time between the bomb has been dropped and the actual explosion
     *
     *
     */


    public Bomb(float x, float y, float explosionDelay) {
        super(x, y);
        this.elapsedTime = 0;
        this.ignitingAnimationTime = 0;
        this.explosionAnimationTime = 0;
        this.isExplosionAnimationPlaying = false;
        this.explosionDelay= explosionDelay;
        this.exploded = false;

    }

    //Methods

    /**
     * Method for the bombs texture
     * @return
     */

    public TextureRegion getCurrentAppearance () {
        if (!exploded) {
            return Animations.BOMB_IGNITING.getKeyFrame(elapsedTime, true);
        } else if (isExplosionAnimationPlaying) {
            return Animations.BOMB_Exploding.getKeyFrame(explosionAnimationTime, false);
        }
        else return null;
    }



    public void explode() {
        exploded = true;
        isExplosionAnimationPlaying = true;

    }

    /**
     * Method to update the elapsed Time, ignitingAnimationTime, explosionAnimationTime;
     * @param deltaTime time of the game;
     */

    public void update(float deltaTime) {
        if (!exploded) {
            elapsedTime += deltaTime;
            ignitingAnimationTime += deltaTime;
            if (elapsedTime >= explosionDelay) {
                explode();
            }
        } else if (isExplosionAnimationPlaying) {
            explosionAnimationTime += deltaTime;
            if (Animations.BOMB_Exploding.isAnimationFinished(explosionAnimationTime)) {
                isExplosionAnimationPlaying = false;
            }
        }
    }


    //Getters and Setter

    public float getElapsedTime() {
        return elapsedTime;
    }

    public float getExplosionDelay() {
        return explosionDelay;
    }

    public boolean isExploded() {
        return exploded;
    }
}
