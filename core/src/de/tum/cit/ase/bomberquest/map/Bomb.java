package de.tum.cit.ase.bomberquest.map;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;

public class Bomb {

    private final float x;
    private final float y;
    private float elapsedTime;


    public Bomb(float x, float y) {
        this.x = x;
        this.y = y;
        this.elapsedTime = 0;
    }

    //Methods

    public TextureRegion getCurrentAppearance() {
        return null;
    }



    public void explode() {

    }

    public void update(float deltaTime) {
        elapsedTime = deltaTime;
    }


    //Getters and Setter


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

}
