package de.tum.cit.ase.bomberquest.texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Contains all animation constants used in the game.
 * It is good practice to keep all textures and animations in constants to avoid loading them multiple times.
 * These can be referenced anywhere they are needed.
 */
public class Animations {


    //DESTRUCTIBLE WALL
    /**
     * The animation for the DestructibleWall being destroyed.
     */
    public static final Animation<TextureRegion> DESTRUCTIBLE_WALL_DESTROY = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 6),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 7),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 8),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 9),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 10),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 11)
    );

    // PLAYER
    /**
     * The animation for the character walking down.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(1, 1),
            SpriteSheet.CHARACTER.at(1, 2),
            SpriteSheet.CHARACTER.at(1, 3),
            SpriteSheet.CHARACTER.at(1, 4)
    );
    /**
     * The animation for the character walking up.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_UP = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(3, 1),
            SpriteSheet.CHARACTER.at(3, 2),
            SpriteSheet.CHARACTER.at(3, 3),
            SpriteSheet.CHARACTER.at(3, 4)
    );
    /**
     * The animation for the character walking sideways to the right.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(2, 1),
            SpriteSheet.CHARACTER.at(2, 2),
            SpriteSheet.CHARACTER.at(2, 3),
            SpriteSheet.CHARACTER.at(2, 4)
    );
    /**
     * The animation for the character walking up.
     */
    public static final Animation<TextureRegion> CHARACTER_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.CHARACTER.at(4, 1),
            SpriteSheet.CHARACTER.at(4, 2),
            SpriteSheet.CHARACTER.at(4, 3),
            SpriteSheet.CHARACTER.at(4, 4)
    );

    // ENEMY
    /**
     * The animation for the enemy walking down.
     */
    public static final Animation<TextureRegion> ENEMY_WALK_DOWN = new Animation<>(0.1f,
            SpriteSheet.ENEMY.at(1, 11),
            SpriteSheet.ENEMY.at(1, 12),
            SpriteSheet.ENEMY.at(1, 11),
            SpriteSheet.ENEMY.at(1, 10)
    );
    /**
     * The animation for the mob walking up.
     */
    public static final Animation<TextureRegion> ENEMY_WALK_UP = new Animation<>(0.1f,
            SpriteSheet.ENEMY.at(4, 11),
            SpriteSheet.ENEMY.at(4, 12),
            SpriteSheet.ENEMY.at(4, 11),
            SpriteSheet.ENEMY.at(4, 10)
    );
    /**
     * The animation for the mob walking sideways to the right.
     */
    public static final Animation<TextureRegion> ENEMY_WALK_RIGHT = new Animation<>(0.1f,
            SpriteSheet.ENEMY.at(3, 11),
            SpriteSheet.ENEMY.at(3, 12),
            SpriteSheet.ENEMY.at(3, 11),
            SpriteSheet.ENEMY.at(3, 10)
    );
    /**
     * The animation for the mob walking up.
     */
    public static final Animation<TextureRegion> ENEMY_WALK_LEFT = new Animation<>(0.1f,
            SpriteSheet.ENEMY.at(2, 11),
            SpriteSheet.ENEMY.at(2, 12),
            SpriteSheet.ENEMY.at(2, 11),
            SpriteSheet.ENEMY.at(2, 10)
    );

    // BOMB
    /**
     * The animation for the bomb igniting.
     */
    public static final Animation<TextureRegion> BOMB_IGNITING = new Animation<>(0.1f,
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 3),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 2),
            SpriteSheet.ORIGINAL_BOMBERMAN.at(4, 1)
    );

    
}
