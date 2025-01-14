package de.tum.cit.ase.bomberquest.gameobjects;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.map.Coordinates;
import de.tum.cit.ase.bomberquest.texture.Animations;
import de.tum.cit.ase.bomberquest.texture.Textures;

import java.util.Comparator;

public class Bomb extends Coordinates {

    private float elapsedTime;


    public Bomb(float x, float y) {
        super(x, y);
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

    public float getElapsedTime() {
        return elapsedTime;
    }

}
