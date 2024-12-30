package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all texture constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Textures {
    
    public static final TextureRegion FLOWERS = SpriteSheet.BASIC_TILES.at(2, 5);

    public static final TextureRegion CHEST = SpriteSheet.BASIC_TILES.at(5, 5);

    // Added textures for Destructible and Indestructible Walls

    public static final TextureRegion DESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 5);
    public static final TextureRegion INDESTRUCTIBLE_WALL = SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 4);

    // Added textures for blank tiles (e.g. when they are destroyed)

    public static final TextureRegion EMPTY = SpriteSheet.BASIC_TILES.at(13, 1);

    // Added textures for Placeholders

    public static final TextureRegion PLACEHOLDER = SpriteSheet.ORIGINAL_BOMBERMAN.at(15, 9);

}
