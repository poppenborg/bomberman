package de.tum.cit.ase.bomberquest.gameobjects.flowers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.ase.bomberquest.gameobjects.GameObject;
import de.tum.cit.ase.bomberquest.texture.Textures;

/**
 * Flowers are a static object without any special properties.
 * They do not have a hitbox, so the player does not collide with them.
 * They are purely decorative and serve as a nice floor decoration.
 */
public class Flowers extends GameObject {

    /**
     * Constructs a Flower at the specified position
     *
     * @param x The x-coordinate of the flower.
     * @param y The y-coordinate of the flower.
     */
    public Flowers(int x, int y) {
        super(x, y);
    }

    /**
     * Return current appearance of the Flower.
     *
     * @return TextureRegion representing the appearance.
     */
    @Override
    public TextureRegion getCurrentAppearance() {
        return Textures.FLOWERS;
    }

}
