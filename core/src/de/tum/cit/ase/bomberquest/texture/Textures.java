package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {

    /** Texture for flowers */
    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(2, 5);
    /** Texture for Destructible Wall */
    public static final TextureRegion DESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 5);
    /** Texture for Indestructible Wall */
    public static final TextureRegion INDESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 4);
    /** Texture for Blank Tiles */
    public static final TextureRegion EMPTY = SpriteSheet.BASIC_TILES.at(13, 1);
    /** Texture for Bomb Number power-up */
    public static final TextureRegion BOMBNBR = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 1);
    /** Texture for blast radius power-up */
    public static final TextureRegion BLASTRADIUS = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 2);
    /** Texture for Exit */
    public static final TextureRegion EXIT = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 12);

}
